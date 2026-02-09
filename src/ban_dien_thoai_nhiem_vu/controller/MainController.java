/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ban_dien_thoai_nhiem_vu.controller;
import ban_dien_thoai_nhiem_vu.view.DangNhapFrame;
import ban_dien_thoai_nhiem_vu.view.MainFrame;
import ban_dien_thoai_nhiem_vu.view.QuanLySanPhamFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MainController {
    private MainFrame mainView;

    public MainController(MainFrame view) {
        this.mainView = view;

        // 1. Xử lý nút Sản Phẩm
        this.mainView.addSanPhamListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moManHinhSanPham();
            }
        });

        // 2. Xử lý nút Đăng Xuất
        this.mainView.addDangXuatListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dangXuat();
            }
        });
        
        // 3. Các nút chưa phát triển
        this.mainView.addChucNangKhacListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainView.showMessage("Tính năng đang phát triển!");
            }
        });
    }

    private void moManHinhSanPham() {
        // Ẩn màn hình chính (không đóng hẳn để sau này quay lại được)
        mainView.setVisible(false);
        
        // Mở màn hình Sản phẩm
        QuanLySanPhamFrame spView = new QuanLySanPhamFrame();
        
        // Cần thêm nút "Quay lại" ở màn hình Sản phẩm (Nâng cao sau)
        // Hiện tại cứ mở lên đã
        spView.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE); 
        // DISPOSE để khi tắt form sản phẩm thì không tắt cả app
        
        new SanPhamController(spView);
        spView.setVisible(true);
        
        // Sự kiện khi đóng form sản phẩm thì hiện lại MainFrame
        spView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                mainView.setVisible(true);
            }
        });
    }

    private void dangXuat() {
        mainView.dispose(); // Hủy màn hình chính
        
        // Quay về đăng nhập
        DangNhapFrame loginView = new DangNhapFrame();
        new DangNhapController(loginView);
        loginView.setVisible(true);
    }
}
