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
        
        // 1. Load sản phẩm
        if (initialSearch != null && !initialSearch.isEmpty()) {
            view.setSearchText(initialSearch);
            loadKhoHang(initialSearch);
        } else {
            loadKhoHang("");
        }

        // 2. Các sự kiện
        view.addTimKiemListener(e -> loadKhoHang(view.getTuKhoaTimKiem()));
        view.setProductListener(sp -> themVaoGioHang(sp));
        
        view.addXoaGioListener(e -> {
            int row = view.getTblGio().getSelectedRow();
            if (row >= 0) {
                view.getModelGio().removeRow(row);
                capNhatTongTien();
            } else {
                view.showMessage("Vui lòng chọn sản phẩm cần xóa!");
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
        
        // SỰ KIỆN QUAN TRỌNG NHẤT: THANH TOÁN
        view.addThanhToanListener(e -> xuLyThanhToan());
        view.addGiaoHangListener(e -> capNhatTongTien());
    }

    // --- CÁC HÀM CƠ BẢN ---
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

    private void themVaoGioHang(SanPham sp) {
        for(int i=0; i<view.getModelGio().getRowCount(); i++) {
            if(view.getModelGio().getValueAt(i, 0).equals(sp.getMaSP())) {
                int slCu = Integer.parseInt(view.getModelGio().getValueAt(i, 2).toString());
                if (slCu + 1 > sp.getTonKho()) {
                    view.showMessage("Không đủ hàng (Tồn: " + sp.getTonKho() + ")"); return;
                }
                view.getModelGio().setValueAt(slCu + 1, i, 2); 
                capNhatTongTien(); return;
            }
        }
        view.getModelGio().addRow(new Object[]{ sp.getMaSP(), sp.getTenSP(), 1, df.format(sp.getGiaBan()), df.format(sp.getGiaBan()) });
        capNhatTongTien();
    }

    private void xuLySuaSoLuong(int row) {
        try {
            int slMoi = Integer.parseInt(view.getModelGio().getValueAt(row, 2).toString());
            if (slMoi <= 0) { view.getModelGio().setValueAt(1, row, 2); return; }
            double donGia = Double.parseDouble(view.getModelGio().getValueAt(row, 3).toString().replace(",", "").replace(" đ", ""));
            view.getModelGio().setValueAt(df.format(slMoi * donGia), row, 4);
            capNhatTongTien();
        } catch (Exception ex) {}
    }

    private void xuLyMaGiamGia() {
        String code = view.getMaGiamGia().toUpperCase().trim();
        if (code.isEmpty()) { phanTramGiam = 0; view.showMessage("Đã hủy mã."); capNhatTongTien(); return; }
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT * FROM GiamGia WHERE code = ? AND ngayKetThuc >= CURDATE() AND soLuong > 0";
            PreparedStatement pst = conn.prepareStatement(sql); pst.setString(1, code);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                phanTramGiam = (double) rs.getInt("phanTramGiam") / 100;
                view.showMessage("Áp dụng mã: " + rs.getString("tenChuongTrinh") + "\nGiảm: " + rs.getInt("phanTramGiam") + "%");
            } else { phanTramGiam = 0; view.showMessage("Mã không hợp lệ!"); }
        } catch (Exception e) { phanTramGiam = 0; }
        capNhatTongTien();
    }

    private void capNhatTongTien() {
        double tongHang = 0;
        for (int i = 0; i < view.getModelGio().getRowCount(); i++) {
            tongHang += Double.parseDouble(view.getModelGio().getValueAt(i, 4).toString().replace(",", "").replace(" đ", ""));
        }
        
        double tienGiamKhach = tongHang * phanTramGiam;
        double tienVanChuyen = view.getPhiVanChuyen();
        double tienPhaiTra = tongHang - tienGiamKhach + tienVanChuyen;
        
        view.setHienThiTien(df.format(tongHang) + " đ", df.format(tienGiamKhach) + " đ", df.format(tienPhaiTra) + " VNĐ");
    }
    
    // --- HELPER LOOKUP ---
    private Integer layMaKhachHangTheoSDT(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) return null;
        try (Connection conn = KetNoiCSDL.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT maKH FROM KhachHang WHERE sdt = ?");
            pst.setString(1, sdt.trim());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getInt("maKH");
        } catch (Exception e) {}
        return null;
    }

    private String layTenKhachHangTheoSDT(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) return null;
        try (Connection conn = KetNoiCSDL.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT tenKH FROM KhachHang WHERE sdt = ?");
            pst.setString(1, sdt.trim());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getString("tenKH");
        } catch (Exception e) {}
        return null;
    }

    // --- HÀM THANH TOÁN (LOGIC CHUẨN) ---
    private void xuLyThanhToan() {
        // 1. Kiểm tra giỏ hàng
        if (view.getModelGio().getRowCount() == 0) { 
            view.showMessage("Giỏ hàng đang trống!"); 
            return; 
        }
        
        // 2. Lấy thông tin Nhân viên (String)
        NhanVien nv = TaiKhoanSession.taiKhoanHienTai;
        String maNguoiBan = (nv != null) ? nv.getMaNV() : "NV_UNKNOWN";

        // 3. Xử lý thông tin Khách hàng (QUAN TRỌNG)
        String inputKhach = view.getTenKhach(); // SĐT người dùng nhập
        Integer maKH = layMaKhachHangTheoSDT(inputKhach); // Tìm ID khách
        
        String tenKhachLuu; 
        if (maKH != null) {
            // Nếu tìm thấy khách -> Lấy tên thật (Ví dụ: "Nguyễn Văn A")
            tenKhachLuu = layTenKhachHangTheoSDT(inputKhach);
        } else {
            // Nếu không tìm thấy -> Lưu là khách lẻ
            if (inputKhach == null || inputKhach.trim().isEmpty()) {
                tenKhachLuu = "Khách vãng lai";
            } else {
                tenKhachLuu = "Khách lẻ (" + inputKhach + ")";
            }
        }

        Connection conn = null;
        try {
            conn = KetNoiCSDL.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            String maHD = "HD" + System.currentTimeMillis();
            
            // Tính tổng tiền
            double tongHang = 0;
            for (int i = 0; i < view.getModelGio().getRowCount(); i++) {
                String tienStr = view.getModelGio().getValueAt(i, 4).toString().replace(",", "").replace(" đ", "");
                tongHang += Double.parseDouble(tienStr);
            }
            double tongTienCuoiCung = tongHang * (1.0 - phanTramGiam);

            // A. INSERT HÓA ĐƠN (Mặc định 'Chờ xử lý' theo yêu cầu)
            // Tính toán trước tổng tiền khách phải trả thực sự (có trừ % và cộng ship)
            double phiVanChuyen = view.getPhiVanChuyen();
            double tongTienPhaiThu = (tongHang * (1.0 - phanTramGiam)) + phiVanChuyen;
            
            if (view.isChuyenKhoan()) {
                ban_dien_thoai_nhiem_vu.view.QRCodeDialog qr = new ban_dien_thoai_nhiem_vu.view.QRCodeDialog(
                        (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(view),
                        tongTienPhaiThu,
                        "THANHTOAN " + maHD
                );
                qr.setVisible(true);
                if (!qr.isPaid()) {
                    conn.rollback();
                    return; // Khách hủy chuyển khoản
                }
            }

            String sqlHD = "INSERT INTO HoaDon (maHD, ngayLap, maNV, maKH, tenKhachHang, tongTien, trangThai) VALUES (?, NOW(), ?, ?, ?, ?, 'Chờ xử lý')";
            PreparedStatement pstHD = conn.prepareStatement(sqlHD);
            pstHD.setString(1, maHD); 
            pstHD.setString(2, maNguoiBan); 
            
            // Xử lý maKH (Set NULL nếu không tìm thấy)
            pstHD.setObject(3, maKH, java.sql.Types.INTEGER);
            
            pstHD.setString(4, tenKhachLuu); 
            pstHD.setDouble(5, tongTienPhaiThu);
            pstHD.executeUpdate();

            // B. INSERT CHI TIẾT & TRỪ KHO (Dùng Batch cho nhanh)
            String sqlCT = "INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong, donGia, thanhTien) VALUES (?, ?, ?, ?, ?)";
            String sqlKho = "UPDATE SanPham SET soLuongTon = soLuongTon - ? WHERE maSP = ?";
            
            PreparedStatement pstCT = conn.prepareStatement(sqlCT);
            PreparedStatement pstKho = conn.prepareStatement(sqlKho);

            for (int i = 0; i < view.getModelGio().getRowCount(); i++) {
                String maSP = view.getModelGio().getValueAt(i, 0).toString();
                int soLuong = Integer.parseInt(view.getModelGio().getValueAt(i, 2).toString());
                double donGia = Double.parseDouble(view.getModelGio().getValueAt(i, 3).toString().replace(",", "").replace(" đ", ""));
                double thanhTien = Double.parseDouble(view.getModelGio().getValueAt(i, 4).toString().replace(",", "").replace(" đ", ""));

                // Batch Chi tiết
                pstCT.setString(1, maHD); pstCT.setString(2, maSP); pstCT.setInt(3, soLuong);
                pstCT.setDouble(4, donGia); pstCT.setDouble(5, thanhTien);
                pstCT.addBatch();

                // Batch Trừ Kho
                pstKho.setInt(1, soLuong); pstKho.setString(2, maSP);
                pstKho.addBatch();
            }
            
            pstCT.executeBatch();
            pstKho.executeBatch();

            // C. Trừ mã giảm giá (Nếu có)
            if (!view.getMaGiamGia().isEmpty() && phanTramGiam > 0) {
                conn.createStatement().executeUpdate("UPDATE GiamGia SET soLuong = soLuong - 1 WHERE code = '" + view.getMaGiamGia().trim() + "'");
            }

            conn.commit(); // XÁC NHẬN
            
            view.showMessage("Thanh toán thành công!\nMã HĐ: " + maHD);
            int confirm = JOptionPane.showConfirmDialog(view, "Bạn có muốn in hóa đơn (PDF) không?", "In Hóa Đơn", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                //xuất hóa đơn bằng textpdf
                new XuatHoaDonPDF().xuatHoaDon(maHD); 
            }
            // GỬI EMAIL - Làm TRƯỚC khi reset giao diện giỏ hàng
            String emailKhach = layEmailKhachHang(inputKhach);
            if (emailKhach != null && !emailKhach.isEmpty()) {
                // Tạo nội dung (cần đọc từ bảng Giỏ hàng đang hiển thị)
                String noiDungHTML = taoNoiDungHoaDonHTML(maHD, tenKhachLuu, tongTienCuoiCung);
                
                // Chạy luồng riêng (Thread) để gửi mail không làm đơ phần mềm
                new Thread(() -> {
                    GuiEmail.guiHoaDon(emailKhach, "Hóa Đơn Điện Tử PNC Store - " + maHD, noiDungHTML);
                }).start();
            }

            // Reset giao diện
            view.getModelGio().setRowCount(0);
            phanTramGiam = 0;
            view.setMaGiamGia("");
            capNhatTongTien();
            loadKhoHang(""); // Load lại kho để thấy số lượng giảm

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {} // Hoàn tác
            view.showMessage("Lỗi thanh toán: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ex) {}
        }
        
        
    }
    private String layEmailKhachHang(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) return null;
        try (Connection conn = KetNoiCSDL.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT email FROM KhachHang WHERE sdt = ?");
            pst.setString(1, sdt.trim());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getString("email");
        } catch (Exception e) {}
        return null;
    }
    
    private String taoNoiDungHoaDonHTML(String maHD, String tenKhach, double tongTien) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>Cảm ơn quý khách đã mua hàng tại PNC Store!</h3>");
        sb.append("<p>Mã hóa đơn: <b>").append(maHD).append("</b></p>");
        sb.append("<p>Khách hàng: ").append(tenKhach).append("</p>");
        sb.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
        sb.append("<tr style='background-color: #f2f2f2;'><th>Tên Sản Phẩm</th><th>SL</th><th>Đơn Giá</th><th>Thành Tiền</th></tr>");
        
        for (int i = 0; i < view.getModelGio().getRowCount(); i++) {
            String tenSP = view.getModelGio().getValueAt(i, 1).toString();
            String sl = view.getModelGio().getValueAt(i, 2).toString();
            String donGia = view.getModelGio().getValueAt(i, 3).toString();
            String thanhTien = view.getModelGio().getValueAt(i, 4).toString();
            
            sb.append("<tr>");
            sb.append("<td>").append(tenSP).append("</td>");
            sb.append("<td style='text-align: center;'>").append(sl).append("</td>");
            sb.append("<td style='text-align: right;'>").append(donGia).append("</td>");
            sb.append("<td style='text-align: right;'>").append(thanhTien).append("</td>");
            sb.append("</tr>");
        }
        
        sb.append("</table>");
        sb.append("<h3 style='text-align: right; color: red;'>TỔNG TIỀN: ").append(df.format(tongTien)).append(" VNĐ</h3>");
        sb.append("<p><i>Đây là thư tự động, vui lòng không trả lời email này.</i></p>");
        return sb.toString();
    }
}
