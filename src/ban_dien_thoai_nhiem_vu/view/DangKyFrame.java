package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DangKyFrame extends JFrame {

    private JTextField txtHoTen, txtTaiKhoan, txtSDT, txtEmail;
    private JPasswordField txtMatKhau, txtXacNhanMK;
    private JButton btnDangKy;
    private JLabel lblQuayLai;

    // Màu sắc theo PNC Aesthetic
    private final Color COLOR_PRIMARY = new Color(41, 98, 255); // #2962FF
    private final Color COLOR_TEXT_DARK = new Color(33, 33, 33);
    private final Color COLOR_TEXT_MUTED = new Color(117, 117, 117);

    public DangKyFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Đăng Ký Tài Khoản - PNC STORE");
        setSize(1000, 650); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
                
                // Vẽ họa tiết Vector trang trí
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
        JLabel lblSlogan = new JLabel("Bắt Đầu Hành Trình Cùng Chúng Tôi", SwingConstants.CENTER);
        lblSlogan.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSlogan.setForeground(new Color(255, 255, 255, 200));
        gl.insets = new Insets(15, 0, 0, 0); 
        pnlLeft.add(lblSlogan, gl);
        
        add(pnlLeft, BorderLayout.WEST);

        // 2. RIGHT PANEL - REGISTER FORM
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(new EmptyBorder(30, 50, 30, 50));
        
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0; 
        g.gridx = 0; g.gridy = 0;

        // --- TITLE ---
        JLabel lblTitle = new JLabel("Đăng ký mới");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        g.gridwidth = 2; // Span 2 columns
        g.insets = new Insets(0, 0, 5, 0);
        pnlRight.add(lblTitle, g);
        
        // --- SUBTITLE ---
        g.gridy++;
        JLabel lblSubtitle = new JLabel("Thiết lập tài khoản quản lý của hệ thống.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(COLOR_TEXT_MUTED);
        g.insets = new Insets(0, 0, 25, 0);
        pnlRight.add(lblSubtitle, g);

        // Reset grid width
        g.gridwidth = 1;

        // --- ROW 1: HO TEN & SDT ---
        g.gridy++;
        g.insets = new Insets(0, 0, 15, 10); 
        txtHoTen = new JTextField();
        setupModernTextField(txtHoTen, "Họ và tên");
        pnlRight.add(txtHoTen, g);

        g.gridx = 1;
        g.insets = new Insets(0, 10, 15, 0);
        txtSDT = new JTextField();
        setupModernTextField(txtSDT, "Số điện thoại");
        pnlRight.add(txtSDT, g);

        // --- ROW 2: TAI KHOAN & EMAIL ---
        g.gridy++; g.gridx = 0;
        g.insets = new Insets(0, 0, 15, 10);
        txtTaiKhoan = new JTextField();
        setupModernTextField(txtTaiKhoan, "Tên đăng nhập");
        pnlRight.add(txtTaiKhoan, g);

        g.gridx = 1;
        g.insets = new Insets(0, 10, 15, 0);
        txtEmail = new JTextField();
        setupModernTextField(txtEmail, "Địa chỉ Email");
        pnlRight.add(txtEmail, g);

        // Reset to full width for Password fields
        g.gridwidth = 2;

        // --- ROW 3: PASSWORD ---
        g.gridy++; g.gridx = 0;
        g.insets = new Insets(0, 0, 15, 0);
        txtMatKhau = new JPasswordField();
        setupModernTextField(txtMatKhau, "Mật khẩu");
        pnlRight.add(txtMatKhau, g);

        // --- ROW 4: CONFIRM PWD ---
        g.gridy++;
        g.insets = new Insets(0, 0, 20, 0);
        txtXacNhanMK = new JPasswordField();
        setupModernTextField(txtXacNhanMK, "Xác nhận mật khẩu");
        pnlRight.add(txtXacNhanMK, g);

        // --- TERMS ---
        g.gridy++;
        JCheckBox chkTerms = new JCheckBox("Tôi đồng ý với các Điều khoản dịch vụ & Chính sách bảo mật");
        chkTerms.setBackground(Color.WHITE);
        chkTerms.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkTerms.setForeground(COLOR_TEXT_DARK);
        chkTerms.setFocusPainted(false);
        chkTerms.setIcon(createModernCheckIcon());
        chkTerms.setSelectedIcon(createModernCheckIcon());
        g.insets = new Insets(0, 0, 25, 0);
        pnlRight.add(chkTerms, g);

        // --- SUBMIT BUTTON ---
        g.gridy++;
        g.insets = new Insets(0, 0, 20, 0);
        btnDangKy = new JButton("TẠO TÀI KHOẢN") {
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
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnDangKy.setPreferredSize(new Dimension(0, 48));
        btnDangKy.setFocusPainted(false);
        btnDangKy.setContentAreaFilled(false);
        btnDangKy.setBorderPainted(false);
        btnDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlRight.add(btnDangKy, g);

        // --- LOGIN LINK ---
        g.gridy++;
        g.insets = new Insets(0, 0, 0, 0);
        JPanel pnlLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnlLogin.setBackground(Color.WHITE);
        
        JLabel lblAsk = new JLabel("Đã có tài khoản?");
        lblAsk.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblAsk.setForeground(COLOR_TEXT_DARK);
        pnlLogin.add(lblAsk);
        
        lblQuayLai = new JLabel("Đăng nhập");
        lblQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblQuayLai.setForeground(new Color(255, 23, 68)); // #FF1744 
        lblQuayLai.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlLogin.add(lblQuayLai);

        pnlRight.add(pnlLogin, g);

        add(pnlRight, BorderLayout.CENTER);
    }

    private void setupModernTextField(JTextField field, String titleText) {
        field.setPreferredSize(new Dimension(0, 48));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        Border lineBorder = BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lineBorder, " " + titleText + " ", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.PLAIN, 12), COLOR_TEXT_MUTED);
        Border margin = new EmptyBorder(0, 10, 5, 10); // Internal padding
        
        field.setBorder(BorderFactory.createCompoundBorder(titledBorder, margin));
    }

    // Getters
    public String getHoTen() { return txtHoTen.getText(); }
    public String getSDT() { return txtSDT.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getTaiKhoan() { return txtTaiKhoan.getText(); }
    public String getMatKhau() { return new String(txtMatKhau.getPassword()); }
    public String getXacNhanMK() { return new String(txtXacNhanMK.getPassword()); }
    
    // Listeners
    public void addDangKyListener(ActionListener l) { btnDangKy.addActionListener(l); }
    public void addQuayLaiListener(ActionListener l) {
        lblQuayLai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                l.actionPerformed(new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, "QUAY_LAI"));
            }
        });
    }
    
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }

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
