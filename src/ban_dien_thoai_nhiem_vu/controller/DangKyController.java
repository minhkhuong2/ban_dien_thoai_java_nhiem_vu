/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.view.DangKyFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DangKyController {
    private DangKyFrame view;

    public DangKyController(DangKyFrame view) {
        this.view = view;

        // Xử lý nút Đăng ký
        view.addDangKyListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyDangKy();
            }
        });

        // Xử lý nút Hủy
        view.addHuyListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose(); // Đóng form
            }
        });
    }

    private void xuLyDangKy() {
        // [SỬA ĐOẠN NÀY] Dùng hàm setter thay vì gọi trực tiếp
        view.setKichHoatNutDangKy(false);
        
        NhanVien nv = view.getNhanVienInput();
        String xacNhanMK = view.getXacNhanMatKhau();

        // 1. Kiểm tra dữ liệu rỗng
        if (nv.getTaiKhoan().isEmpty() || nv.getMatKhau().isEmpty()) {
            view.showMessage("Vui lòng nhập Tài khoản và Mật khẩu!");
            view.setKichHoatNutDangKy(true); // [SỬA] Bật lại nút
            return;
        }

        // 2. Kiểm tra mật khẩu trùng khớp
        if (!nv.getMatKhau().equals(xacNhanMK)) {
            view.showMessage("Mật khẩu xác nhận không khớp!");
            view.setKichHoatNutDangKy(true); // [SỬA] Bật lại nút
            return;
        }

        // 3. Lưu vào Database
        Connection conn = KetNoiCSDL.getConnection();
        if (conn == null) {
             view.showMessage("Lỗi: Không thể kết nối đến cơ sở dữ liệu!");
             view.setKichHoatNutDangKy(true);
             return;
        }

        try {
            // 3a. Kiểm tra tài khoản đã tồn tại chưa
            String checkSql = "SELECT count(*) FROM NhanVien WHERE taiKhoan = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, nv.getTaiKhoan());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                 view.showMessage("Lỗi: Tài khoản '" + nv.getTaiKhoan() + "' đã tồn tại!");
                 view.setKichHoatNutDangKy(true);
                 rs.close();
                 checkStmt.close();
                 conn.close();
                 return;
            }
            rs.close();
            checkStmt.close();

            // 3b. Tự động tạo maNV (Fix lỗi 'maNV doesn't have a default value')
            // Vì database chưa bật Auto Increment cho maNV, ta phải tự tính.
            int nextId = 1;
            Statement stGetId = conn.createStatement();
            ResultSet rsId = stGetId.executeQuery("SELECT MAX(maNV) FROM NhanVien");
            if (rsId.next()) {
                // Nếu bảng có dữ liệu, lấy max + 1. Nếu bảng rỗng, rsId.getInt trả về 0 -> nextId = 1
                nextId = rsId.getInt(1) + 1;
            }
            rsId.close();
            stGetId.close();

            // 3c. Tiến hành INSERT có maNV
            String insertSql = "INSERT INTO NhanVien (maNV, taiKhoan, matKhau, hoTen, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(insertSql);
            pst.setInt(1, nextId);
            pst.setString(2, nv.getTaiKhoan());
            pst.setString(3, nv.getMatKhau());
            pst.setString(4, nv.getHoTen());
            pst.setString(5, nv.getEmail());
            
            pst.executeUpdate();
            pst.close();
            conn.close();
            
            view.showMessage("Đăng ký thành công! Hãy quay lại đăng nhập.");
            view.dispose();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showMessage("Lỗi hệ thống: " + ex.getMessage());
            view.setKichHoatNutDangKy(true); // [SỬA] Bật lại nút khi lỗi
        }
    }
}
