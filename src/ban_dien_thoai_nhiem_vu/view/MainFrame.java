/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
public class MainFrame extends JFrame {

    private JButton btnBanHang, btnSanPham, btnKho, btnThongKe, btnDangXuat;
    private JLabel lblXinChao;

    public MainFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Hệ Thống Quản Lý PNC Store");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Phần tiêu đề (North)
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(0, 102, 204)); // Xanh dương đậm
        pnlHeader.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitle = new JLabel("DASHBOARD QUẢN LÝ");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        lblXinChao = new JLabel("Xin chào: Admin"); // Sau này sẽ thay tên động
        lblXinChao.setFont(new Font("Arial", Font.ITALIC, 14));
        lblXinChao.setForeground(Color.WHITE);
        
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(lblXinChao, BorderLayout.EAST);
        
        add(pnlHeader, BorderLayout.NORTH);

        // 2. Phần Menu chính (Center) - Dùng GridLayout 2 hàng 3 cột
        JPanel pnlMenu = new JPanel(new GridLayout(2, 3, 20, 20));
        pnlMenu.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlMenu.setBackground(new Color(240, 240, 240));

        // Tạo các nút chức năng to đẹp
        btnBanHang = taoNutMenu("BÁN HÀNG", new Color(0, 153, 76));
        btnSanPham = taoNutMenu("SẢN PHẨM", new Color(255, 153, 51));
        btnKho = taoNutMenu("KHO HÀNG", new Color(102, 0, 153));
        btnThongKe = taoNutMenu("THỐNG KÊ", new Color(0, 102, 204));
        btnDangXuat = taoNutMenu("ĐĂNG XUẤT", new Color(204, 0, 0));

        pnlMenu.add(btnBanHang);
        pnlMenu.add(btnSanPham);
        pnlMenu.add(btnKho);
        pnlMenu.add(btnThongKe);
        pnlMenu.add(btnDangXuat);

        add(pnlMenu, BorderLayout.CENTER);
        
        // 3. Footer (South)
        JLabel lblFooter = new JLabel("Phiên bản 1.0 - Nhóm 3 (2026)", SwingConstants.CENTER);
        lblFooter.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblFooter, BorderLayout.SOUTH);
    }

    // Hàm phụ trợ để tạo nút cho nhanh
    private JButton taoNutMenu(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    // --- CÁC HÀM ĐỂ CONTROLLER GỌI ---
    
    public void addSanPhamListener(ActionListener listener) {
        btnSanPham.addActionListener(listener);
    }

    public void addDangXuatListener(ActionListener listener) {
        btnDangXuat.addActionListener(listener);
    }
    
    // Các nút chưa làm chức năng thì tạm thời hiện thông báo
    public void addChucNangKhacListener(ActionListener listener) {
        btnBanHang.addActionListener(listener);
        btnKho.addActionListener(listener);
        btnThongKe.addActionListener(listener);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
