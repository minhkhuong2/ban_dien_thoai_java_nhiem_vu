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

    // Màu sắc theo PNC Aesthetic
    private final Color COLOR_PRIMARY = new Color(41, 98, 255); // #2962FF Royal Blue
    private final Color COLOR_TEXT_DARK = new Color(33, 33, 33);
    private final Color COLOR_TEXT_MUTED = new Color(117, 117, 117);

    public DangNhapFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Đăng Nhập - PNC STORE");
        setSize(850, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 1. LEFT PANEL - BRANDING (Màn hình xanh phía trái)
        JPanel pnlLeft = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Vẽ nền Gradient sang trọng
                GradientPaint gp = new GradientPaint(0, 0, new Color(74, 38, 235), 0, getHeight(), COLOR_PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Vẽ họa tiết Vector trang trí (Glassmorphism effect)
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillOval(-80, -80, 250, 250);
                g2.fillOval(getWidth()-120, getHeight()-150, 250, 250);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillOval(getWidth()/2 - 100, getHeight()/2 - 100, 200, 200);
            }
        };
        pnlLeft.setPreferredSize(new Dimension(380, 0));
        pnlLeft.setLayout(new GridBagLayout());
        
        GridBagConstraints gl = new GridBagConstraints();
        gl.gridx = 0; gl.gridy = 0;
        
        JLabel lblLogoIcon = new JLabel();
        try {
            java.net.URL url = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/PNC.png");
            if (url != null) {
                // Resize for Login Screen (white on blue background, expecting PNG to have transparency)
                Image img = new ImageIcon(url).getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                lblLogoIcon.setIcon(new ImageIcon(img));
            } else {
                lblLogoIcon.setText("✦");
                lblLogoIcon.setFont(new Font("SansSerif", Font.BOLD, 70));
                lblLogoIcon.setForeground(Color.WHITE);
            }
        } catch(Exception e) {}
        lblLogoIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        gl.insets = new Insets(0, 0, 10, 0); 
        pnlLeft.add(lblLogoIcon, gl);
        
        gl.gridy++;
        JLabel lblWelcome = new JLabel("PNC STORE", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblWelcome.setForeground(Color.WHITE);
        pnlLeft.add(lblWelcome, gl);
        
        gl.gridy++;
        JLabel lblSlogan = new JLabel("Hệ Thống Quản Lý Bán Điện Thoại", SwingConstants.CENTER);
        lblSlogan.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSlogan.setForeground(new Color(255, 255, 255, 200));
        gl.insets = new Insets(15, 0, 0, 0); 
        pnlLeft.add(lblSlogan, gl);
        
        add(pnlLeft, BorderLayout.WEST);

        // 2. RIGHT PANEL - LOGIN FORM
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(new EmptyBorder(40, 60, 40, 60)); // Rộng rãi hơn
        
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.gridx = 0; g.gridy = 0;

        // --- TITLE ---
        JLabel lblTitle = new JLabel("Xin chào!");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        g.insets = new Insets(0, 0, 5, 0);
        pnlRight.add(lblTitle, g);
        
        // --- SUBTITLE ---
        g.gridy++;
        JLabel lblSubtitle = new JLabel("Vui lòng đăng nhập để tiếp tục.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(COLOR_TEXT_MUTED);
        g.insets = new Insets(0, 0, 35, 0);
        pnlRight.add(lblSubtitle, g);

        // --- EMAIL TEXTFIELD ---
        g.gridy++;
        g.insets = new Insets(0, 0, 15, 0);
        txtTaiKhoan = new JTextField();
        setupModernTextField(txtTaiKhoan, "Email / Tài khoản");
        pnlRight.add(txtTaiKhoan, g);

        // --- PASSWORD TEXTFIELD ---
        g.gridy++;
        g.insets = new Insets(0, 0, 10, 0);
        txtMatKhau = new JPasswordField();
        setupModernTextField(txtMatKhau, "Mật khẩu");
        pnlRight.add(txtMatKhau, g);

        // --- OPTIONS LAYER (REMEMBER ME & FORGOT PWD) ---
        g.gridy++;
        g.insets = new Insets(0, 0, 30, 0);
        JPanel pnlOptions = new JPanel(new BorderLayout());
        pnlOptions.setBackground(Color.WHITE);
        
        JCheckBox chkRemember = new JCheckBox("Ghi nhớ tôi");
        chkRemember.setBackground(Color.WHITE);
        chkRemember.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkRemember.setForeground(COLOR_TEXT_DARK);
        chkRemember.setFocusPainted(false);
        chkRemember.setIcon(createModernCheckIcon());
        chkRemember.setSelectedIcon(createModernCheckIcon());
        pnlOptions.add(chkRemember, BorderLayout.WEST);

        lblQuenMatKhau = new JLabel("Quên mật khẩu?");
        lblQuenMatKhau.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblQuenMatKhau.setForeground(new Color(255, 23, 68)); // #FF1744
        lblQuenMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlOptions.add(lblQuenMatKhau, BorderLayout.EAST);
        
        pnlRight.add(pnlOptions, g);

        // --- LOGIN BUTTON ---
        g.gridy++;
        g.insets = new Insets(0, 0, 25, 0);
        btnDangNhap = new JButton("ĐĂNG NHẬP") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(COLOR_PRIMARY.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(61, 118, 255));
                } else {
                    g2.setColor(COLOR_PRIMARY);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnDangNhap.setPreferredSize(new Dimension(0, 48));
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setContentAreaFilled(false);
        btnDangNhap.setBorderPainted(false);
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlRight.add(btnDangNhap, g);

        // --- SIGN UP LINK ---
        g.gridy++;
        g.insets = new Insets(0, 0, 0, 0);
        JPanel pnlSignUp = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnlSignUp.setBackground(Color.WHITE);
        
        JLabel lblAsk = new JLabel("Chưa có tài khoản?");
        lblAsk.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblAsk.setForeground(COLOR_TEXT_DARK);
        pnlSignUp.add(lblAsk);
        
        lblDangKy = new JLabel("Đăng ký ngay");
        lblDangKy.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDangKy.setForeground(new Color(0, 230, 118)); // #00E676
        lblDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlSignUp.add(lblDangKy);

        pnlRight.add(pnlSignUp, g);

        add(pnlRight, BorderLayout.CENTER);
    }

    private void setupModernTextField(JTextField field, String titleText) {
        field.setPreferredSize(new Dimension(0, 48));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        Border lineBorder = BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lineBorder, " " + titleText + " ", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.PLAIN, 12), COLOR_TEXT_MUTED);
        Border margin = new EmptyBorder(0, 10, 5, 10);
        
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
        lblDangKy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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

    private Icon createModernCheckIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = 18;
                int yOff = y + 1; // Center manually slightly
                if (b.isSelected()) {
                    g2.setColor(COLOR_PRIMARY);
                    g2.fillRoundRect(x, yOff, size, size, 6, 6);
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x + 4, yOff + 9, x + 8, yOff + 13);
                    g2.drawLine(x + 8, yOff + 13, x + 14, yOff + 5);
                } else {
                    g2.setColor(new Color(200, 200, 200));
                    g2.drawRoundRect(x, yOff, size, size, 6, 6);
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(x + 1, yOff + 1, size - 2, size - 2, 6, 6);
                }
                g2.dispose();
            }
            @Override public int getIconWidth() { return 18; }
            @Override public int getIconHeight() { return 20; }
        };
    }
}
