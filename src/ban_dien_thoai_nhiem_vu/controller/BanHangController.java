package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.model.TaiKhoanSession;
import ban_dien_thoai_nhiem_vu.view.BanHangPanel;
import ban_dien_thoai_nhiem_vu.model.SanPham;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class BanHangController {
    private BanHangPanel view;
    private DecimalFormat df = new DecimalFormat("#,###"); 
    private double phanTramGiam = 0; 

    public BanHangController(BanHangPanel view) {
        this(view, "");
    }

    public BanHangController(BanHangPanel view, String initialSearch) {
        this.view = view;
        if (initialSearch != null && !initialSearch.isEmpty()) {
            view.setSearchText(initialSearch);
            loadKhoHang(initialSearch);
        } else {
            loadKhoHang("");
        }

        view.addTimKiemListener(e -> loadKhoHang(view.getTuKhoaTimKiem()));

        // Thay thế listener bảng bằng listener Card
        view.setProductListener(sp -> themVaoGioHang(sp));

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
        List<SanPham> list = new ArrayList<>();
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT * FROM SanPham WHERE soLuongTon > 0";
            if (!keyword.isEmpty()) sql += " AND tenSP LIKE '%" + keyword + "%'";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("maSP"));
                sp.setTenSP(rs.getString("tenSP"));
                sp.setGiaBan(rs.getDouble("giaBan"));
                sp.setTonKho(rs.getInt("soLuongTon"));
                // Sử dụng đúng tên cột "hinhAnh" trong CSDL
                sp.setHinhAnh(rs.getString("hinhAnh")); 
                list.add(sp);
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        view.hienThiDanhSachSanPham(list);
    }

    private void themVaoGioHang(SanPham sp) {
        for(int i=0; i<view.getModelGio().getRowCount(); i++) {
            if(view.getModelGio().getValueAt(i, 0).equals(sp.getMaSP())) {
                int slCu = Integer.parseInt(view.getModelGio().getValueAt(i, 2).toString());
                view.getModelGio().setValueAt(slCu + 1, i, 2); 
                return;
            }
        }
        view.getModelGio().addRow(new Object[]{ 
            sp.getMaSP(), sp.getTenSP(), 1, df.format(sp.getGiaBan()), df.format(sp.getGiaBan()) 
        });
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
            String donGiaStr = view.getModelGio().getValueAt(row, 3).toString().replace(",", "").replace(" đ", "");
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
            String tienStr = view.getModelGio().getValueAt(i, 4).toString().replace(",", "").replace(" đ", ""); // remove currency symbol if present
            tongHang += Double.parseDouble(tienStr);
        }
        double tienGiam = tongHang * phanTramGiam;
        view.setHienThiTien(df.format(tongHang) + " đ", df.format(tienGiam) + " đ", df.format(tongHang - tienGiam) + " VNĐ");
    }
    
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
            
            // Clean string before parsing
            String tienCuoiCungStr = view.getLblThanhToanText().replace("PHẢI TRẢ: ", "").replace(" VNĐ", "").replace(",", "").replace(".", "").trim();
            // Note: Replace "." if thousands separator is ".", replace "," if thousands separator is ",". 
            // My DecimalFormat uses "," for thousands. So remove ",".
            // Let's use a safer way: calculate from cart again or parse carefully.
            // The display string is like "1,000,000 VNĐ"
            
            double tongTienCuoiCung = 0;
            // Recalculate strictly to be safe
             double tongHang = 0;
            for (int i = 0; i < view.getModelGio().getRowCount(); i++) {
                String tienStr = view.getModelGio().getValueAt(i, 4).toString().replace(",", "").replace(" đ", "");
                tongHang += Double.parseDouble(tienStr);
            }
            tongTienCuoiCung = tongHang * (1.0 - phanTramGiam);


            PreparedStatement pstHD = conn.prepareStatement("INSERT INTO HoaDon (maHD, ngayLap, maNV, tenKhachHang, tongTien) VALUES (?, ?, ?, ?, ?)");
            pstHD.setString(1, maHD); pstHD.setString(2, ngayLap); pstHD.setString(3, maNguoiBan);
            pstHD.setString(4, view.getTenKhach()); pstHD.setDouble(5, tongTienCuoiCung);
            pstHD.executeUpdate();

            PreparedStatement pstCT = conn.prepareStatement("INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong, donGia, thanhTien) VALUES (?, ?, ?, ?, ?)");
            PreparedStatement pstKho = conn.prepareStatement("UPDATE SanPham SET soLuongTon = soLuongTon - ? WHERE maSP = ?");

            for (int i = 0; i < view.getModelGio().getRowCount(); i++) {
                String maSP = view.getModelGio().getValueAt(i, 0).toString();
                int soLuong = Integer.parseInt(view.getModelGio().getValueAt(i, 2).toString());
                double donGia = Double.parseDouble(view.getModelGio().getValueAt(i, 3).toString().replace(",", "").replace(" đ", ""));
                double thanhTien = Double.parseDouble(view.getModelGio().getValueAt(i, 4).toString().replace(",", "").replace(" đ", ""));

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
            if (confirm == JOptionPane.YES_OPTION) {
                new XuatHoaDon().inHoaDon(maHD); 
            } 

            view.getModelGio().setRowCount(0);
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
