package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// GradientPaint is in java.awt, which is already imported by java.awt.*

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
        setSize(900, 550); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2)); // Chia đôi màn hình

        // --- LEFT PANEL: GRADIENT & BRANDING ---
        GradientPanel pnlLeft = new GradientPanel(new Color(67, 94, 190), new Color(41, 128, 185));
        pnlLeft.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0; gbcLeft.gridy = 0;
        gbcLeft.insets = new Insets(10, 10, 20, 10);
        
        // Logo
        JLabel lblIcon = new JLabel("📱");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        lblIcon.setForeground(Color.WHITE);
        pnlLeft.add(lblIcon, gbcLeft);
        
        // Tên App
        gbcLeft.gridy++;
        JLabel lblApp = new JLabel("PNC MOBILE STORE");
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblApp.setForeground(Color.WHITE);
        pnlLeft.add(lblApp, gbcLeft);
        
        // Features
        gbcLeft.gridy++;
        String[] features = {"✔ Quản lý Sản Phẩm", "✔ Thống kê Doanh Thu", "✔ Hỗ trợ Online 24/7"};
        for (String f : features) {
            JLabel lblF = new JLabel(f);
            lblF.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblF.setForeground(new Color(240, 240, 240));
            gbcLeft.insets = new Insets(5, 10, 5, 10);
            pnlLeft.add(lblF, gbcLeft);
            gbcLeft.gridy++;
        }

        add(pnlLeft);

        // --- RIGHT PANEL: LOGIN FORM ---
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.weightx = 1.0;

        // Title
        gbc.gridy = 0;
        JLabel lblWelcome = new JLabel("Welcome Admin!");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblWelcome.setForeground(new Color(50, 50, 50));
        pnlRight.add(lblWelcome, gbc);

        gbc.gridy++;
        JLabel lblSub = new JLabel("Vui lòng đăng nhập để tiếp tục");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);
        gbc.insets = new Insets(0, 40, 30, 40);
        pnlRight.add(lblSub, gbc);

        // Input: Tài khoản
        gbc.gridy++;
        gbc.insets = new Insets(10, 40, 5, 40);
        JLabel lblUser = new JLabel("Tài khoản");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(100, 100, 100));
        pnlRight.add(lblUser, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 40, 15, 40);
        txtTaiKhoan = new JTextField();
        styleTextField(txtTaiKhoan);
        pnlRight.add(txtTaiKhoan, gbc);

        // Input: Mật khẩu
        gbc.gridy++;
        gbc.insets = new Insets(5, 40, 5, 40);
        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(100, 100, 100));
        pnlRight.add(lblPass, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 40, 20, 40);
        txtMatKhau = new JPasswordField();
        styleTextField(txtMatKhau);
        pnlRight.add(txtMatKhau, gbc);

        // Button: Đăng nhập
        gbc.gridy++;
        gbc.insets = new Insets(10, 40, 10, 40);
        btnDangNhap = new JButton("ĐĂNG NHẬP");
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangNhap.setBackground(new Color(67, 94, 190));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setBorderPainted(false);
        btnDangNhap.setPreferredSize(new Dimension(0, 45));
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnDangNhap.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnDangNhap.setBackground(new Color(45, 75, 160)); }
            public void mouseExited(MouseEvent e) { btnDangNhap.setBackground(new Color(67, 94, 190)); }
        });
        
        pnlRight.add(btnDangNhap, gbc);
        getRootPane().setDefaultButton(btnDangNhap);

        add(pnlRight);
    }
    
    private void styleTextField(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(0, 40));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
    }

    // --- INNER CLASS: GRADIENT PANEL (ĐỂ VẼ MÀU NỀN ĐẸP) ---
    // Đây là phần bạn bị thiếu trước đó gây lỗi
    class GradientPanel extends JPanel {
        private Color color1;
        private Color color2;

        public GradientPanel(Color color1, Color color2) {
            this.color1 = color1;
            this.color2 = color2;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth();
            int h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    // --- GETTERS & LISTENERS ---
    
    public String getTaiKhoan() { return txtTaiKhoan.getText().trim(); }
    public String getMatKhau() { return new String(txtMatKhau.getPassword()); }
    
    // Đổi tên thành addLoginListener để Controller cũ nhận diện được
    public void addLoginListener(ActionListener l) { 
        btnDangNhap.addActionListener(l); 
    }
    
    // Nếu Controller nào dùng addDangNhapListener thì vẫn gọi được cái này
    public void addDangNhapListener(ActionListener l) {
        btnDangNhap.addActionListener(l);
    }
    
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}
