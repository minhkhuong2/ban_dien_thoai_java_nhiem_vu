/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.NhanVien;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
public class DangKyFrame extends JFrame {
    private JTextField txtTaiKhoan, txtHoTen, txtEmail;
    private JPasswordField txtMatKhau, txtXacNhanMK;
    private JButton btnXacNhan, btnHuy;

    public DangKyFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Đăng Ký Tài Khoản Mới");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form này, không tắt app
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("ĐĂNG KÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel pnlCenter = new JPanel(new GridLayout(5, 2, 10, 10));
        pnlCenter.setBorder(new EmptyBorder(10, 20, 10, 20));

        pnlCenter.add(new JLabel("Tài khoản (*):")); 
        txtTaiKhoan = new JTextField(); pnlCenter.add(txtTaiKhoan);

        pnlCenter.add(new JLabel("Mật khẩu (*):")); 
        txtMatKhau = new JPasswordField(); pnlCenter.add(txtMatKhau);
        
        pnlCenter.add(new JLabel("Nhập lại MK (*):")); 
        txtXacNhanMK = new JPasswordField(); pnlCenter.add(txtXacNhanMK);

        pnlCenter.add(new JLabel("Họ và Tên:")); 
        txtHoTen = new JTextField(); pnlCenter.add(txtHoTen);

        pnlCenter.add(new JLabel("Email:")); 
        txtEmail = new JTextField(); pnlCenter.add(txtEmail);

        add(pnlCenter, BorderLayout.CENTER);

        // Button
        JPanel pnlButton = new JPanel(new FlowLayout());
        btnXacNhan = new JButton("Đăng Ký Ngay");
        btnXacNhan.setBackground(new Color(0, 153, 76));
        btnXacNhan.setForeground(Color.WHITE);
        
        btnHuy = new JButton("Hủy Bỏ");
        btnHuy.setBackground(new Color(204, 0, 0));
        btnHuy.setForeground(Color.WHITE);

        pnlButton.add(btnXacNhan);
        pnlButton.add(btnHuy);
        add(pnlButton, BorderLayout.SOUTH);
    }

    // --- HÀM GIAO TIẾP VỚI CONTROLLER ---

    public NhanVien getNhanVienInput() {
        return new NhanVien(
            txtTaiKhoan.getText(),
            new String(txtMatKhau.getPassword()),
            txtHoTen.getText(),
            txtEmail.getText()
        );
    }
    
    // Lấy chuỗi xác nhận mật khẩu để so sánh
    public String getXacNhanMatKhau() {
        return new String(txtXacNhanMK.getPassword());
    }

    public void addDangKyListener(ActionListener listener) {
        btnXacNhan.addActionListener(listener);
    }
    
    public void addHuyListener(ActionListener listener) {
        btnHuy.addActionListener(listener);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    public void setKichHoatNutDangKy(boolean trangThai) {
    btnXacNhan.setEnabled(trangThai);
}
}
