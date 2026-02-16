package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.DangKyFrame;
import ban_dien_thoai_nhiem_vu.view.DangNhapFrame;
import java.sql.*;

public class DangKyController {
    private DangKyFrame view;

    public DangKyController(DangKyFrame view) {
        this.view = view;

        view.addDangKyListener(e -> xuLyDangKy());
        
        view.addQuayLaiListener(e -> {
            view.dispose();
            DangNhapFrame login = new DangNhapFrame();
            new DangNhapController(login);
            login.setVisible(true);
        });
    }

    private void xuLyDangKy() {
        String hoTen = view.getHoTen();
        String sdt = view.getSDT();      // Mới
        String email = view.getEmail();  // Mới
        String tk = view.getTaiKhoan();
        String mk = view.getMatKhau();
        String xacNhan = view.getXacNhanMK();

        // Validate
        if (hoTen.isEmpty() || sdt.isEmpty() || email.isEmpty() || tk.isEmpty() || mk.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!"); return;
        }
        if (!mk.equals(xacNhan)) {
            view.showMessage("Mật khẩu xác nhận không khớp!"); return;
        }

        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Check trùng
            PreparedStatement check = conn.prepareStatement("SELECT * FROM NhanVien WHERE taiKhoan = ?");
            check.setString(1, tk);
            if (check.executeQuery().next()) {
                view.showMessage("Tài khoản đã tồn tại!"); return;
            }

            // Insert (Có thêm sdt, email)
            String maNV = "NV" + System.currentTimeMillis() % 10000;
            String sql = "INSERT INTO NhanVien (maNV, hoTen, sdt, email, taiKhoan, matKhau, vaiTro, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, maNV);
            pst.setString(2, hoTen);
            pst.setString(3, sdt);
            pst.setString(4, email);
            pst.setString(5, tk);
            pst.setString(6, mk);
            pst.setString(7, "NhanVien"); // Mặc định là NV thường
            
            pst.executeUpdate();
            
            view.showMessage("Đăng ký thành công! Vui lòng đăng nhập.");
            
            // Chuyển màn hình
            view.dispose();
            DangNhapFrame login = new DangNhapFrame();
            new DangNhapController(login);
            login.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showMessage("Lỗi: " + ex.getMessage());
        }
    }
}