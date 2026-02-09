package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.model.TaiKhoanSession;
import ban_dien_thoai_nhiem_vu.view.BanHangFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class BanHangController {
    private BanHangFrame view;
    private DecimalFormat df = new DecimalFormat("#,###"); 
    private double phanTramGiam = 0; 

    public BanHangController(BanHangFrame view) {
        this.view = view;
        loadKhoHang("");

        view.addTimKiemListener(e -> loadKhoHang(view.getTuKhoaTimKiem()));

        view.addKhoHangMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) themVaoGioHang();
            }
        });

        view.addXoaGioListener(e -> {
            int row = view.getTblGio().getSelectedRow();
            if (row >= 0) {
                view.getModelGio().removeRow(row);
                capNhatTongTien();
            }
        });

        view.getModelGio().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 2 && e.getType() == TableModelEvent.UPDATE) {
                    xuLySuaSoLuong(e.getFirstRow());
                }
            }
        });

        view.addApDungMaListener(e -> xuLyMaGiamGia());

        view.addThanhToanListener(e -> xuLyThanhToan());
    }

    private void loadKhoHang(String keyword) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT * FROM SanPham WHERE soLuongTon > 0";
            if (!keyword.isEmpty()) sql += " AND tenSP LIKE '%" + keyword + "%'";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            view.getModelKho().setRowCount(0);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("maSP"));
                row.add(rs.getString("tenSP"));
                row.add(rs.getString("hangSanXuat"));
                row.add(df.format(rs.getDouble("giaBan")));
                row.add(rs.getInt("soLuongTon"));
                view.getModelKho().addRow(row);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void themVaoGioHang() {
        int row = view.getTblKho().getSelectedRow();
        if (row < 0) return;
        
        String maSP = view.getTblKho().getValueAt(row, 0).toString();
        String tenSP = view.getTblKho().getValueAt(row, 1).toString();
        double donGia = Double.parseDouble(view.getTblKho().getValueAt(row, 3).toString().replace(",", ""));
        
        for(int i=0; i<view.getModelGio().getRowCount(); i++) {
            if(view.getModelGio().getValueAt(i, 0).equals(maSP)) {
                int slCu = Integer.parseInt(view.getModelGio().getValueAt(i, 2).toString());
                view.getModelGio().setValueAt(slCu + 1, i, 2); 
                return;
            }
        }
        view.getModelGio().addRow(new Object[]{ maSP, tenSP, 1, df.format(donGia), df.format(donGia) });
        capNhatTongTien();
    }

    private void xuLySuaSoLuong(int row) {
        try {
            String slMoiStr = view.getModelGio().getValueAt(row, 2).toString();
            int slMoi = Integer.parseInt(slMoiStr);
            if (slMoi <= 0) {
                view.showMessage("Số lượng phải lớn hơn 0!");
                view.getModelGio().setValueAt(1, row, 2); 
                return;
            }
            String donGiaStr = view.getModelGio().getValueAt(row, 3).toString().replace(",", "");
            double donGia = Double.parseDouble(donGiaStr);
            view.getModelGio().setValueAt(df.format(slMoi * donGia), row, 4);
            capNhatTongTien();
        } catch (NumberFormatException ex) {
            view.showMessage("Vui lòng nhập số nguyên!");
        }
    }

    private void xuLyMaGiamGia() {
        String code = view.getMaGiamGia().toUpperCase().trim();
        if (code.isEmpty()) {
            phanTramGiam = 0; view.showMessage("Đã hủy mã giảm giá."); capNhatTongTien(); return;
        }
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT * FROM GiamGia WHERE code = ? AND ngayKetThuc >= CURDATE() AND soLuong > 0";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, code);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                phanTramGiam = (double) rs.getInt("phanTramGiam") / 100;
                view.showMessage("Áp dụng mã: " + rs.getString("tenChuongTrinh") + "\nGiảm: " + rs.getInt("phanTramGiam") + "%");
            } else {
                phanTramGiam = 0; view.showMessage("Mã không hợp lệ hoặc hết hạn!");
            }
        } catch (Exception e) { phanTramGiam = 0; }
        capNhatTongTien();
    }

    private void capNhatTongTien() {
        double tongHang = 0;
        for (int i = 0; i < view.getModelGio().getRowCount(); i++) {
            String tienStr = view.getModelGio().getValueAt(i, 4).toString().replace(",", "");
            tongHang += Double.parseDouble(tienStr);
        }
        double tienGiam = tongHang * phanTramGiam;
        view.setHienThiTien(df.format(tongHang) + " đ", df.format(tienGiam) + " đ", df.format(tongHang - tienGiam) + " VNĐ");
    }
    
    // --- SỬA LỖI TẠI ĐÂY ---
    private void xuLyThanhToan() {
        if (view.getModelGio().getRowCount() == 0) { view.showMessage("Giỏ hàng đang trống!"); return; }
        
        NhanVien nv = TaiKhoanSession.taiKhoanHienTai;
        String maNguoiBan = (nv != null) ? nv.getMaNV() : "NV_UNKNOWN";

        Connection conn = null;
        try {
            conn = KetNoiCSDL.getConnection();
            conn.setAutoCommit(false); 

            String maHD = "HD" + System.currentTimeMillis();
            String ngayLap = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            double tongTienCuoiCung = Double.parseDouble(view.getLblThanhToanText().replace("KHÁCH CẦN TRẢ: ", "").replace(" VNĐ", "").replace(",", ""));

            PreparedStatement pstHD = conn.prepareStatement("INSERT INTO HoaDon (maHD, ngayLap, maNV, tenKhachHang, tongTien) VALUES (?, ?, ?, ?, ?)");
            pstHD.setString(1, maHD); pstHD.setString(2, ngayLap); pstHD.setString(3, maNguoiBan);
            pstHD.setString(4, view.getTenKhach()); pstHD.setDouble(5, tongTienCuoiCung);
            pstHD.executeUpdate();

            PreparedStatement pstCT = conn.prepareStatement("INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong, donGia, thanhTien) VALUES (?, ?, ?, ?, ?)");
            PreparedStatement pstKho = conn.prepareStatement("UPDATE SanPham SET soLuongTon = soLuongTon - ? WHERE maSP = ?");

            for (int i = 0; i < view.getModelGio().getRowCount(); i++) {
                String maSP = view.getModelGio().getValueAt(i, 0).toString();
                int soLuong = Integer.parseInt(view.getModelGio().getValueAt(i, 2).toString());
                double donGia = Double.parseDouble(view.getModelGio().getValueAt(i, 3).toString().replace(",", ""));
                double thanhTien = Double.parseDouble(view.getModelGio().getValueAt(i, 4).toString().replace(",", ""));

                pstCT.setString(1, maHD); pstCT.setString(2, maSP); pstCT.setInt(3, soLuong);
                pstCT.setDouble(4, donGia); pstCT.setDouble(5, thanhTien);
                pstCT.executeUpdate();

                pstKho.setInt(1, soLuong); pstKho.setString(2, maSP);
                pstKho.executeUpdate();
            }

            if (!view.getMaGiamGia().isEmpty() && phanTramGiam > 0) {
                conn.prepareStatement("UPDATE GiamGia SET soLuong = soLuong - 1 WHERE code = '" + view.getMaGiamGia().trim() + "'").executeUpdate();
            }

            conn.commit(); // Lưu thành công
            
            int confirm = JOptionPane.showConfirmDialog(view, "Thanh toán thành công! In hóa đơn nhé?", "In", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) new XuatHoaDon().inHoaDon(maHD); 

            // --- ĐOẠN SỬA LỖI: CHỈ CẦN XÓA BẢNG, KHÔNG GHI ĐÈ ---
            view.getModelGio().setRowCount(0);
            // view.getTblGio().setValueAt("", 0, 0); <-- ĐÃ XÓA DÒNG GÂY LỖI NÀY
            phanTramGiam = 0;
            capNhatTongTien();
            loadKhoHang(""); 

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            view.showMessage("Lỗi thanh toán: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ex) {}
        }
    }
}