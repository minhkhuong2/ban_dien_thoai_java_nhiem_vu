package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DangNhapFrame extends JFrame {
    
    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JLabel lblDangKy;
    private JLabel lblQuenMatKhau;

    public DangNhapFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Đăng Nhập - PNC STORE");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Main Panel with Padding
        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(40, 50, 40, 50));
        
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.gridx = 0; g.gridy = 0;
        
        // --- LOGO ---
        JLabel lblLogo = new JLabel("PNC STORE");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(new Color(41, 98, 255)); // #2962FF
        g.insets = new Insets(0, 0, 30, 0);
        pnlMain.add(lblLogo, g);

        // --- TITLE ---
        g.gridy++;
        JLabel lblTitle = new JLabel("Đăng nhập");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(new Color(33, 33, 33)); // #212121
        g.insets = new Insets(0, 0, 5, 0);
        pnlMain.add(lblTitle, g);
        
        // --- SUBTITLE ---
        g.gridy++;
        JLabel lblSubtitle = new JLabel("Đăng nhập để vào hệ thống PNC Store của bạn");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(117, 117, 117)); // #757575
        g.insets = new Insets(0, 0, 30, 0);
        pnlMain.add(lblSubtitle, g);

        // --- EMAIL TEXTFIELD ---
        g.gridy++;
        g.insets = new Insets(0, 0, 15, 0);
        txtTaiKhoan = new JTextField();
        setupModernTextField(txtTaiKhoan, "Email / Tài khoản");
        pnlMain.add(txtTaiKhoan, g);

        // --- PASSWORD TEXTFIELD ---
        g.gridy++;
        g.insets = new Insets(0, 0, 15, 0);
        txtMatKhau = new JPasswordField();
        setupModernTextField(txtMatKhau, "Mật khẩu");
        pnlMain.add(txtMatKhau, g);

        // --- OPTIONS LAYER (REMEMBER ME & FORGOT PWD) ---
        g.gridy++;
        g.insets = new Insets(0, 0, 25, 0);
        JPanel pnlOptions = new JPanel(new BorderLayout());
        pnlOptions.setBackground(Color.WHITE);
        
        JCheckBox chkRemember = new JCheckBox("Ghi nhớ tôi");
        chkRemember.setBackground(Color.WHITE);
        chkRemember.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkRemember.setForeground(new Color(33, 33, 33));
        chkRemember.setFocusPainted(false);
        pnlOptions.add(chkRemember, BorderLayout.WEST);

        lblQuenMatKhau = new JLabel("Quên mật khẩu?");
        lblQuenMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblQuenMatKhau.setForeground(new Color(255, 107, 107)); // Nhẹ nhàng xíu
        lblQuenMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlOptions.add(lblQuenMatKhau, BorderLayout.EAST);
        
        pnlMain.add(pnlOptions, g);

        // --- LOGIN BUTTON ---
        g.gridy++;
        g.insets = new Insets(0, 0, 20, 0);
        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setBackground(new Color(41, 98, 255)); // #2962FF
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangNhap.setPreferredSize(new Dimension(0, 45));
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setBorderPainted(false);
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlMain.add(btnDangNhap, g);

        // --- SIGN UP LINK ---
        g.gridy++;
        g.insets = new Insets(0, 0, 0, 0);
        JPanel pnlSignUp = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnlSignUp.setBackground(Color.WHITE);
        
        JLabel lblAsk = new JLabel("Chưa có tài khoản?");
        lblAsk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAsk.setForeground(new Color(33, 33, 33));
        pnlSignUp.add(lblAsk);
        
        lblDangKy = new JLabel("Đăng ký");
        lblDangKy.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDangKy.setForeground(new Color(255, 107, 107)); 
        lblDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlSignUp.add(lblDangKy);

        pnlMain.add(pnlSignUp, g);

        add(pnlMain, BorderLayout.CENTER);
    }

    private void setupModernTextField(JTextField field, String titleText) {
        field.setPreferredSize(new Dimension(0, 45));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        Border lineBorder = BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lineBorder, " " + titleText + " ", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.PLAIN, 11), new Color(117, 117, 117));
        Border margin = new EmptyBorder(0, 10, 5, 10); // Internal padding
        
        field.setBorder(BorderFactory.createCompoundBorder(titledBorder, margin));
    }

    // Getters
    public String getTaiKhoan() { return txtTaiKhoan.getText(); }
    public String getMatKhau() { return new String(txtMatKhau.getPassword()); }
    
    // Listeners Map Sang JLabel thay vì JButton như mã cũ cho mượt
    public void addLoginListener(ActionListener l) {
        btnDangNhap.addActionListener(l);
    }
    
    public void addDangKyListener(ActionListener l) {
        // Áp dụng mouse click event cho label để đóng vai trò như button
        lblDangKy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Call back dummy action event
                l.actionPerformed(new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, "DANG_KY"));
            }
        });
    }
    
    public void addQuenMatKhauListener(ActionListener l) {
        lblQuenMatKhau.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                l.actionPerformed(new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, "QUEN_MAT_KHAU"));
            }
        });
    }
    
    public void showMessage(String msg) { 
        JOptionPane.showMessageDialog(this, msg); 
    }
}
