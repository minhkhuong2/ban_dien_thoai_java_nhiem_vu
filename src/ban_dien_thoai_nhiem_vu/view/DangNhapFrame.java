package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DangNhapFrame extends JFrame {
    
    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JButton btnDangKy;

    public DangNhapFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Đăng Nhập Hệ Thống - PNC Store");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("PNC STORE LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 41, 54)); 
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel pnlCenter = new JPanel(new GridLayout(3, 1, 10, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // Ô Tài khoản (Để trống)
        JPanel p1 = new JPanel(new BorderLayout(5, 5));
        p1.add(new JLabel("Tài khoản:"), BorderLayout.NORTH);
        txtTaiKhoan = new JTextField(); // <-- ĐÃ XÓA "admin"
        p1.add(txtTaiKhoan, BorderLayout.CENTER);

        // Ô Mật khẩu (Để trống)
        JPanel p2 = new JPanel(new BorderLayout(5, 5));
        p2.add(new JLabel("Mật khẩu:"), BorderLayout.NORTH);
        txtMatKhau = new JPasswordField(); // <-- ĐÃ XÓA "123"
        p2.add(txtMatKhau, BorderLayout.CENTER);
        
        // Khu vực nút bấm
        JPanel p3 = new JPanel(new GridLayout(2, 1, 5, 10)); 
        
        btnDangNhap = new JButton("ĐĂNG NHẬP");
        btnDangNhap.setBackground(new Color(67, 94, 190));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 14));
        btnDangNhap.setPreferredSize(new Dimension(0, 40));
        
        // Thêm sự kiện bấm Enter là Đăng nhập luôn cho tiện
        getRootPane().setDefaultButton(btnDangNhap);
        
        btnDangKy = new JButton("Chưa có tài khoản? Đăng ký ngay");
        btnDangKy.setBorderPainted(false);
        btnDangKy.setContentAreaFilled(false);
        btnDangKy.setForeground(Color.BLUE);
        btnDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));

        p3.add(btnDangNhap);
        p3.add(btnDangKy);

        pnlCenter.add(p1);
        pnlCenter.add(p2);
        pnlCenter.add(p3);

        add(pnlCenter, BorderLayout.CENTER);
    }

    // Getters & Listeners
    public String getTaiKhoan() { return txtTaiKhoan.getText(); }
    public String getMatKhau() { return new String(txtMatKhau.getPassword()); }
    
    public void addDangNhapListener(ActionListener l) { btnDangNhap.addActionListener(l); }
    public void addChuyenSangDangKyListener(ActionListener l) { btnDangKy.addActionListener(l); }
    
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}