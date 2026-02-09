/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.view.DangNhapFrame;
import ban_dien_thoai_nhiem_vu.view.MainFrame;
// Import thêm 2 cái này
import ban_dien_thoai_nhiem_vu.view.DangKyFrame;
import ban_dien_thoai_nhiem_vu.controller.DangKyController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class DangNhapController {
    private DangNhapFrame view;

    public DangNhapController(DangNhapFrame view) {
        this.view = view;

        this.view.addDangNhapListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyDangNhap();
            }
        });

        // Sự kiện nút Đăng Ký -> Mở Form Đăng Ký
        this.view.addDangKyListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moFormDangKy();
            }
        });
    }

    private void xuLyDangNhap() {
        NhanVien nv = view.getNhanVienInput();
        try {
            Connection conn = KetNoiCSDL.getConnection();
            String sql = "SELECT * FROM NhanVien WHERE taiKhoan = ? AND matKhau = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nv.getTaiKhoan());
            pst.setString(2, nv.getMatKhau());
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                view.showMessage("Đăng nhập thành công!");
                view.dispose(); // Tắt form đăng nhập đi
                
                // === [SỬA ĐOẠN NÀY] ===
                // Thay vì mở QuanLySanPhamFrame, ta mở MainFrame (Dashboard)
                
                MainFrame mainView = new MainFrame(); // Tạo màn hình chính
                new MainController(mainView);         // Kích hoạt điều khiển màn hình chính
                mainView.setVisible(true);            // Hiện lên
                
                // ======================
                
            } else {
                view.showMessage("Sai tài khoản hoặc mật khẩu!");
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showMessage("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    private void xuLyDangKy() {
        NhanVien nv = view.getNhanVienInput();
        try {
            Connection conn = KetNoiCSDL.getConnection();
            String sql = "SELECT * FROM NhanVien WHERE taiKhoan = ? AND matKhau = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nv.getTaiKhoan());
            pst.setString(2, nv.getMatKhau());
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                view.showMessage("Đăng nhập thành công!");
                view.dispose();
                
                // Mở Dashboard
                MainFrame mainView = new MainFrame(); 
                new MainController(mainView);         
                mainView.setVisible(true);            
                
            } else {
                view.showMessage("Sai tài khoản hoặc mật khẩu!");
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void moFormDangKy() {
        DangKyFrame dkView = new DangKyFrame(); // Tạo View
        new DangKyController(dkView);           // Gắn Controller
        dkView.setVisible(true);                // Hiện lên
    }
}
