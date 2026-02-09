/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.NhanVien;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
public class DangNhapFrame extends JFrame {
    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap, btnDangKy;

    public DangNhapFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Đăng Nhập Hệ Thống");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("PNC STORE LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle);

        // Panel Tài khoản
        JPanel pnlUser = new JPanel();
        pnlUser.add(new JLabel("Tài khoản:"));
        txtTaiKhoan = new JTextField(15);
        pnlUser.add(txtTaiKhoan);
        add(pnlUser);

        // Panel Mật khẩu
        JPanel pnlPass = new JPanel();
        pnlPass.add(new JLabel("Mật khẩu:"));
        txtMatKhau = new JPasswordField(15);
        pnlPass.add(txtMatKhau);
        add(pnlPass);

        // Panel Nút bấm
        JPanel pnlButton = new JPanel();
        btnDangNhap = new JButton("Đăng Nhập");
        btnDangKy = new JButton("Đăng Ký");
        pnlButton.add(btnDangNhap);
        pnlButton.add(btnDangKy);
        add(pnlButton);
    }

    // --- CÁC HÀM CHO CONTROLLER GỌI ---
    
    public NhanVien getNhanVienInput() {
        String user = txtTaiKhoan.getText();
        String pass = new String(txtMatKhau.getPassword());
        return new NhanVien(user, pass, "", "");
    }

    public void addDangNhapListener(ActionListener listener) {
        btnDangNhap.addActionListener(listener);
    }

    public void addDangKyListener(ActionListener listener) {
        btnDangKy.addActionListener(listener);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
