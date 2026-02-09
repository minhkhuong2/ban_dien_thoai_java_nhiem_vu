package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.DangKyFrame;
import ban_dien_thoai_nhiem_vu.view.DangNhapFrame;
import java.sql.*;

public class DangKyController {
    private DangKyFrame view;

    public DangKyController(DangKyFrame view) {
        this.view = view;

        // 1. Sự kiện Đăng Ký
        view.addDangKyListener(e -> xuLyDangKy());

        // 2. Sự kiện Quay Lại -> Mở lại form đăng nhập
        view.addQuayLaiListener(e -> {
            view.dispose();
            DangNhapFrame loginView = new DangNhapFrame();
            new DangNhapController(loginView);
            loginView.setVisible(true);
        });
    }

    private void xuLyDangKy() {
        String hoTen = view.getHoTen();
        String tk = view.getTaiKhoan();
        String mk = view.getMatKhau();
        String xacNhan = view.getXacNhanMK();

        // Validate cơ bản
        if (hoTen.isEmpty() || tk.isEmpty() || mk.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!"); return;
        }
        if (!mk.equals(xacNhan)) {
            view.showMessage("Mật khẩu xác nhận không khớp!"); return;
        }

        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Kiểm tra trùng tài khoản
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM NhanVien WHERE taiKhoan = ?");
            checkStmt.setString(1, tk);
            if (checkStmt.executeQuery().next()) {
                view.showMessage("Tài khoản này đã tồn tại!"); return;
            }

            // Tạo mã NV tự động (Ví dụ: NV + thời gian hiện tại để không trùng)
            String maNV = "NV" + System.currentTimeMillis() % 10000; 

            // Insert vào DB
            String sql = "INSERT INTO NhanVien (maNV, hoTen, taiKhoan, matKhau, vaiTro, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, maNV);
            pst.setString(2, hoTen);
            pst.setString(3, tk);
            pst.setString(4, mk);
            pst.setString(5, "STAFF"); // Mặc định đăng ký là Nhân viên thường
            pst.setInt(6, 1); // Trạng thái hoạt động

            pst.executeUpdate();
            view.showMessage("Đăng ký thành công! Hãy đăng nhập.");
            
            // Chuyển về màn hình đăng nhập
            view.dispose();
            DangNhapFrame loginView = new DangNhapFrame();
            new DangNhapController(loginView);
            loginView.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showMessage("Lỗi: " + ex.getMessage());
        }
    }
}