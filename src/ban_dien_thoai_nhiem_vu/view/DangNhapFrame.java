package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class DangNhapFrame extends JFrame {
    
    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JButton btnDangKy;
    private JButton btnQuenMatKhau; // [NÚT MỚI]

    public DangNhapFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Đăng Nhập - PNC STORE");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- TRÁI: LOGO ---
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        pnlLeft.setBackground(new Color(67, 94, 190));
        pnlLeft.setPreferredSize(new Dimension(400, 0));
        JLabel lblLogo = new JLabel("PNC STORE");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblLogo.setForeground(Color.WHITE);
        pnlLeft.add(lblLogo);
        add(pnlLeft, BorderLayout.WEST);

        // --- PHẢI: FORM ---
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.gridy = 0;

        // Title
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(67, 94, 190));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlRight.add(lblTitle, g);

        // Inputs
        g.gridy++; pnlRight.add(new JLabel("Tài khoản:"), g);
        g.gridy++; txtTaiKhoan = new JTextField(20); txtTaiKhoan.setPreferredSize(new Dimension(0, 40)); pnlRight.add(txtTaiKhoan, g);

        g.gridy++; pnlRight.add(new JLabel("Mật khẩu:"), g);
        g.gridy++; txtMatKhau = new JPasswordField(20); txtMatKhau.setPreferredSize(new Dimension(0, 40)); pnlRight.add(txtMatKhau, g);

        // Nút Đăng Nhập
        g.gridy++; g.insets = new Insets(20, 10, 5, 10);
        btnDangNhap = new JButton("ĐĂNG NHẬP");
        btnDangNhap.setBackground(new Color(67, 94, 190));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangNhap.setPreferredSize(new Dimension(0, 45));
        btnDangNhap.setFocusPainted(false);
        pnlRight.add(btnDangNhap, g);

        // --- [VÍ DỤ CỤ THỂ] THÊM 2 NÚT LINK Ở DƯỚI ---
        g.gridy++;
        g.insets = new Insets(5, 10, 10, 10);
        
        JPanel pnlLinks = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pnlLinks.setBackground(Color.WHITE);

        btnDangKy = new JButton("Đăng ký mới");
        styleLinkButton(btnDangKy, new Color(67, 94, 190)); // Màu xanh
        
        btnQuenMatKhau = new JButton("Quên mật khẩu?");
        styleLinkButton(btnQuenMatKhau, Color.RED); // Màu đỏ cho nổi bật

        pnlLinks.add(btnDangKy);
        pnlLinks.add(new JLabel("|")); // Dấu gạch ngăn cách
        pnlLinks.add(btnQuenMatKhau);
        
        pnlRight.add(pnlLinks, g);

        add(pnlRight, BorderLayout.CENTER);
    }

    private void styleLinkButton(JButton btn, Color color) {
        btn.setForeground(color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    // Getters & Listeners
    public String getTaiKhoan() { return txtTaiKhoan.getText(); }
    public String getMatKhau() { return new String(txtMatKhau.getPassword()); }
    
    public void addLoginListener(ActionListener l) { btnDangNhap.addActionListener(l); }
    public void addDangKyListener(ActionListener l) { btnDangKy.addActionListener(l); }
    
    // [QUAN TRỌNG] Getter cho nút Quên MK
    public void addQuenMatKhauListener(ActionListener l) { btnQuenMatKhau.addActionListener(l); }
    
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}