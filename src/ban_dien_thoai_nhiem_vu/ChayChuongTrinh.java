package ban_dien_thoai_nhiem_vu;

import ban_dien_thoai_nhiem_vu.controller.DangNhapController;
import ban_dien_thoai_nhiem_vu.view.DangNhapFrame;
import javax.swing.UIManager;

public class ChayChuongTrinh {
    public static void main(String[] args) {
        try {
            // Set giao diện đẹp (Windows style)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- KHỞI ĐỘNG TỪ MÀN HÌNH ĐĂNG NHẬP ---
        DangNhapFrame loginView = new DangNhapFrame();
        new DangNhapController(loginView);
        loginView.setVisible(true);
    }
}
