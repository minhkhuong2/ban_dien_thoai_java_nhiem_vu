package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.model.TaiKhoanSession;
import ban_dien_thoai_nhiem_vu.view.DangNhapFrame;
import ban_dien_thoai_nhiem_vu.view.MainFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DangNhapController {
    private DangNhapFrame view;

    public DangNhapController(DangNhapFrame view) {
        this.view = view;
        view.addLoginListener(e -> xuLyDangNhap());
    }

    private void xuLyDangNhap() {
        String tk = view.getTaiKhoan();
        String mk = view.getMatKhau();

        if (tk.isEmpty() || mk.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT * FROM NhanVien WHERE taiKhoan = ? AND matKhau = ? AND trangThai = 1";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, tk);
            pst.setString(2, mk);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV")); 
                nv.setHoTen(rs.getString("hoTen"));
                nv.setNgaySinh(rs.getDate("ngaySinh"));
                nv.setSdt(rs.getString("sdt"));
                nv.setEmail(rs.getString("email"));
                nv.setTaiKhoan(rs.getString("taiKhoan"));
                nv.setVaiTro(rs.getString("vaiTro"));
                nv.setTrangThai(rs.getInt("trangThai"));
                
                TaiKhoanSession.taiKhoanHienTai = nv;

                view.showMessage("Xin chào, " + nv.getHoTen());
                
                // Mở màn hình chính
                MainFrame mainFrame = new MainFrame();
                new MainController(mainFrame); // Khởi tạo controller
                mainFrame.setVisible(true);    // HIỂN THỊ MÀN HÌNH CHÍNH

                // Đóng màn hình đăng nhập
                view.dispose();

            } else {
                view.showMessage("Sai tài khoản hoặc mật khẩu!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Lỗi kết nối CSDL!");
        }
    }
}
