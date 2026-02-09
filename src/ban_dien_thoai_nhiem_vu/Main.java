/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ban_dien_thoai_nhiem_vu;
import ban_dien_thoai_nhiem_vu.controller.DangNhapController;
import ban_dien_thoai_nhiem_vu.view.DangNhapFrame;
public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            // A. Tạo View Đăng Nhập
            DangNhapFrame loginView = new DangNhapFrame();
            
            // B. Gắn Controller Đăng Nhập
            new DangNhapController(loginView); 
            
            // C. Hiện lên
            loginView.setVisible(true);
        });
    }
}
