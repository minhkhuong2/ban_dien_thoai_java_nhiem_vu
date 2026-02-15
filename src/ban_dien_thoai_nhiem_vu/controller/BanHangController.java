package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.model.TaiKhoanSession;
import ban_dien_thoai_nhiem_vu.view.BanHangPanel;
import ban_dien_thoai_nhiem_vu.model.SanPham;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
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
        
        // 1. Load danh sách sản phẩm ban đầu
        if (initialSearch != null && !initialSearch.isEmpty()) {
            view.setSearchText(initialSearch);
            loadKhoHang(initialSearch);
        } else {
            loadKhoHang("");
        }

        // 2. Sự kiện Tìm kiếm sản phẩm
        view.addTimKiemListener(e -> loadKhoHang(view.getTuKhoaTimKiem()));

        // 3. Sự kiện Click sản phẩm -> Thêm vào giỏ
        view.setProductListener(sp -> themVaoGioHang(sp));

        // 4. Sự kiện Xóa món khỏi giỏ
        view.addXoaGioListener(e -> {
            int row = view.getTblGio().getSelectedRow();
            if (row >= 0) {
                view.getModelGio().removeRow(row);
                capNhatTongTien();
            } else {
                view.showMessage("Vui lòng chọn sản phẩm cần xóa!");
            }
        });

        // 5. Sự kiện Sửa số lượng trực tiếp trên bảng
        view.getModelGio().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 2 && e.getType() == TableModelEvent.UPDATE) {
                    xuLySuaSoLuong(e.getFirstRow());
                }
            }
        });

        // 6. Sự kiện Áp dụng Mã giảm giá
        view.addApDungMaListener(e -> xuLyMaGiamGia());

        // 7. Sự kiện Thanh toán (Quan trọng)
        view.addThanhToanListener(e -> xuLyThanhToan());
    }

    // --- LOAD KHO HÀNG ---
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
                sp.setHinhAnh(rs.getString("hinhAnh")); 
                list.add(sp);
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        view.hienThiDanhSachSanPham(list);
    }

    // --- THÊM VÀO GIỎ ---
    private void themVaoGioHang(SanPham sp) {
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        for(int i=0; i<view.getModelGio().getRowCount(); i++) {
            if(view.getModelGio().getValueAt(i, 0).equals(sp.getMaSP())) {
                int slCu = Integer.parseInt(view.getModelGio().getValueAt(i, 2).toString());
                int tonKho = sp.getTonKho(); // Lưu ý: Đây là tồn kho lúc load, có thể chưa real-time
                
                // Kiểm tra tồn kho sơ bộ
                if (slCu + 1 > tonKho) {
                    view.showMessage("Không đủ hàng trong kho (Tồn: " + tonKho + ")");
                    return;
                }
                
                view.getModelGio().setValueAt(slCu + 1, i, 2); 
                capNhatTongTien(); // Cập nhật lại tiền sau khi tăng số lượng
                return;
            }
        }
        
        // Nếu chưa có thì thêm mới
        view.getModelGio().addRow(new Object[]{ 
            sp.getMaSP(), sp.getTenSP(), 1, df.format(sp.getGiaBan()), df.format(sp.getGiaBan()) 
        });
        capNhatTongTien();
    }

    // --- SỬA SỐ LƯỢNG TRONG GIỎ ---
    private void xuLySuaSoLuong(int row) {
        try {
            String slMoiStr = view.getModelGio().getValueAt(row, 2).toString();
            int slMoi = Integer.parseInt(slMoiStr);
            
            if (slMoi <= 0) {
                view.showMessage("Số lượng phải lớn hơn 0!");
                view.getModelGio().setValueAt(1, row, 2); // Reset về 1
                return;
            }
            
            String donGiaStr = view.getModelGio().getValueAt(row, 3).toString().replace(",", "").replace(" đ", "");
            double donGia = Double.parseDouble(donGiaStr);
            
            // Cập nhật cột Thành tiền
            view.getModelGio().setValueAt(df.format(slMoi * donGia), row, 4);
            capNhatTongTien();
            
        } catch (NumberFormatException ex) {
            view.showMessage("Vui lòng nhập số nguyên!");
            // Có thể reset lại số lượng cũ nếu muốn
        }
    }

    // --- XỬ LÝ MÃ GIẢM GIÁ ---
    private void xuLyMaGiamGia() {
        String code = view.getMaGiamGia().toUpperCase().trim();
        if (code.isEmpty()) {
            phanTramGiam = 0; 
            view.showMessage("Đã hủy mã giảm giá."); 
            capNhatTongTien(); 
            return;
        }
        
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Kiểm tra mã còn hạn và còn số lượng không
            String sql = "SELECT * FROM GiamGia WHERE code = ? AND ngayKetThuc >= CURDATE() AND soLuong > 0";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, code);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                phanTramGiam = (double) rs.getInt("phanTramGiam") / 100;
                view.showMessage("Áp dụng mã: " + rs.getString("tenChuongTrinh") + "\nGiảm: " + rs.getInt("phanTramGiam") + "%");
            } else {
                phanTramGiam = 0; 
                view.showMessage("Mã không hợp lệ hoặc đã hết lượt dùng!");
            }
        } catch (Exception e) { 
            phanTramGiam = 0; 
            e.printStackTrace();
        }
        capNhatTongTien();
    }

    // --- TÍNH TỔNG TIỀN ---
    private void capNhatTongTien() {
        double tongHang = 0;
        for (int i = 0; i < view.getModelGio().getRowCount(); i++) {
            String tienStr = view.getModelGio().getValueAt(i, 4).toString().replace(",", "").replace(" đ", "");
            tongHang += Double.parseDouble(tienStr);
        }
        
        double tienGiam = tongHang * phanTramGiam;
        double tienPhaiTra = tongHang - tienGiam;
        
        view.setHienThiTien(
            df.format(tongHang) + " đ", 
            df.format(tienGiam) + " đ", 
            df.format(tienPhaiTra) + " VNĐ"
        );
    }
    
    // --- [HELPER] TÌM MÃ KHÁCH HÀNG QUA SĐT ---
    private Integer layMaKhachHangTheoSDT(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) return null;
        try (Connection conn = KetNoiCSDL.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT maKH FROM KhachHang WHERE sdt = ?");
            pst.setString(1, sdt.trim());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getInt("maKH");
        } catch (Exception e) { e.printStackTrace(); }
        return null; // Không tìm thấy
    }

    // --- THANH TOÁN & LƯU DB ---
    private void xuLyThanhToan() {
        if (view.getModelGio().getRowCount() == 0) { 
            view.showMessage("Giỏ hàng đang trống!"); 
            return; 
        }
        
        // 1. Lấy thông tin Nhân viên bán (Xử lý String)
        NhanVien nv = TaiKhoanSession.taiKhoanHienTai;
        String maNguoiBan = (nv != null) ? nv.getMaNV() : "NV_UNKNOWN"; // Default nếu null

        // 2. Lấy thông tin Khách hàng (Tìm ID qua SĐT)
        String inputKhach = view.getTenKhach(); // Ô này người dùng nhập SĐT hoặc Tên
        Integer maKH = layMaKhachHangTheoSDT(inputKhach); 
        String tenKhachBackup = inputKhach; // Lưu lại text họ nhập để backup

        Connection conn = null;
        try {
            conn = KetNoiCSDL.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            String maHD = "HD" + System.currentTimeMillis();
            
            // Tính lại tổng tiền lần cuối để chính xác
            double tongHang = 0;
            for (int i = 0; i < view.getModelGio().getRowCount(); i++) {
                String tienStr = view.getModelGio().getValueAt(i, 4).toString().replace(",", "").replace(" đ", "");
                tongHang += Double.parseDouble(tienStr);
            }
            double tongTienCuoiCung = tongHang * (1.0 - phanTramGiam);

            // A. INSERT HÓA ĐƠN
            // Cột: maHD, ngayLap, maNV (String), maKH (Int/Null), tenKhachHang (String), tongTien
            String sqlHD = "INSERT INTO HoaDon (maHD, ngayLap, maNV, maKH, tenKhachHang, tongTien) VALUES (?, NOW(), ?, ?, ?, ?)";
            PreparedStatement pstHD = conn.prepareStatement(sqlHD);
            pstHD.setString(1, maHD); 
            pstHD.setString(2, maNguoiBan); 
            
            if (maKH != null) pstHD.setInt(3, maKH); 
            else pstHD.setNull(3, java.sql.Types.INTEGER);
            
            pstHD.setString(4, tenKhachBackup);
            pstHD.setDouble(5, tongTienCuoiCung);
            
            pstHD.executeUpdate();

            // B. INSERT CHI TIẾT & TRỪ KHO
            String sqlCT = "INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong, donGia, thanhTien) VALUES (?, ?, ?, ?, ?)";
            String sqlKho = "UPDATE SanPham SET soLuongTon = soLuongTon - ? WHERE maSP = ?";
            
            PreparedStatement pstCT = conn.prepareStatement(sqlCT);
            PreparedStatement pstKho = conn.prepareStatement(sqlKho);

            for (int i = 0; i < view.getModelGio().getRowCount(); i++) {
                String maSP = view.getModelGio().getValueAt(i, 0).toString();
                int soLuong = Integer.parseInt(view.getModelGio().getValueAt(i, 2).toString());
                double donGia = Double.parseDouble(view.getModelGio().getValueAt(i, 3).toString().replace(",", "").replace(" đ", ""));
                double thanhTien = Double.parseDouble(view.getModelGio().getValueAt(i, 4).toString().replace(",", "").replace(" đ", ""));

                // Chi tiết
                pstCT.setString(1, maHD); pstCT.setString(2, maSP); pstCT.setInt(3, soLuong);
                pstCT.setDouble(4, donGia); pstCT.setDouble(5, thanhTien);
                pstCT.addBatch();

                // Trừ kho
                pstKho.setInt(1, soLuong); pstKho.setString(2, maSP);
                pstKho.addBatch();
            }
            pstCT.executeBatch();
            pstKho.executeBatch();

            // C. TRỪ SỐ LƯỢNG MÃ GIẢM GIÁ (Nếu có dùng)
            if (!view.getMaGiamGia().isEmpty() && phanTramGiam > 0) {
                conn.createStatement().executeUpdate("UPDATE GiamGia SET soLuong = soLuong - 1 WHERE code = '" + view.getMaGiamGia().trim() + "'");
            }

            conn.commit(); // XÁC NHẬN THÀNH CÔNG
            
            view.showMessage("Thanh toán thành công!\nMã HĐ: " + maHD);
            
            // D. RESET GIAO DIỆN
            view.getModelGio().setRowCount(0);
            phanTramGiam = 0;
            view.setMaGiamGia("");
            capNhatTongTien();
            loadKhoHang(""); // Load lại để cập nhật số lượng tồn mới
            
            // Hỏi in hóa đơn (Tính năng mở rộng nếu có)
            int confirm = JOptionPane.showConfirmDialog(view, "Bạn có muốn in hóa đơn không?", "In Hóa Đơn", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Gọi class XuatHoaDon (nếu bạn đã làm)
                // new XuatHoaDon().inHoaDon(maHD);
            }

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {} // Hoàn tác nếu lỗi
            view.showMessage("Lỗi thanh toán: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ex) {}
        }
    }
}