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

    public DangKyFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Đăng Ký Tài Khoản - PNC STORE");
        setSize(550, 750); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Form Panel
        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0; 
        g.gridx = 0; g.gridy = 0;

        // --- LOGO ---
        JLabel lblLogo = new JLabel("PNC STORE");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(new Color(41, 98, 255)); // #2962FF
        g.insets = new Insets(0, 0, 25, 0);
        g.gridwidth = 2; // Span across columns
        pnlMain.add(lblLogo, g);

        // --- TITLE ---
        g.gridy++;
        JLabel lblTitle = new JLabel("Đăng ký");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 33, 33));
        g.insets = new Insets(0, 0, 5, 0);
        pnlMain.add(lblTitle, g);
        
        // --- SUBTITLE ---
        g.gridy++;
        JLabel lblSubtitle = new JLabel("Thiết lập tài khoản cá nhân của bạn.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(117, 117, 117));
        g.insets = new Insets(0, 0, 20, 0);
        pnlMain.add(lblSubtitle, g);

        // Reset grid width to split standard fields into two columns where needed
        g.gridwidth = 1;

        // --- ROW 1: HO TEN & SDT ---
        g.gridy++;
        g.insets = new Insets(0, 0, 15, 5); // Right margin for left item
        txtHoTen = new JTextField();
        setupModernTextField(txtHoTen, "Họ và tên");
        pnlMain.add(txtHoTen, g);

        g.gridx = 1;
        g.insets = new Insets(0, 5, 15, 0); // Left margin for right item
        txtSDT = new JTextField();
        setupModernTextField(txtSDT, "Số điện thoại");
        pnlMain.add(txtSDT, g);

        // --- ROW 2: TAI KHOAN & EMAIL ---
        g.gridy++; g.gridx = 0;
        g.insets = new Insets(0, 0, 15, 5);
        txtTaiKhoan = new JTextField();
        setupModernTextField(txtTaiKhoan, "Tên đăng nhập / Account");
        pnlMain.add(txtTaiKhoan, g);

        g.gridx = 1;
        g.insets = new Insets(0, 5, 15, 0);
        txtEmail = new JTextField();
        setupModernTextField(txtEmail, "Email");
        pnlMain.add(txtEmail, g);

        // Reset to full width for Password fields
        g.gridwidth = 2;

        // --- ROW 3: PASSWORD ---
        g.gridy++; g.gridx = 0;
        g.insets = new Insets(0, 0, 15, 0);
        txtMatKhau = new JPasswordField();
        setupModernTextField(txtMatKhau, "Mật khẩu");
        pnlMain.add(txtMatKhau, g);

        // --- ROW 4: CONFIRM PWD ---
        g.gridy++;
        g.insets = new Insets(0, 0, 15, 0);
        txtXacNhanMK = new JPasswordField();
        setupModernTextField(txtXacNhanMK, "Xác nhận mật khẩu");
        pnlMain.add(txtXacNhanMK, g);

        // --- TERMS ---
        g.gridy++;
        JCheckBox chkTerms = new JCheckBox("  Tôi đồng ý với các Điều khoản & Chính sách bảo mật");
        chkTerms.setBackground(Color.WHITE);
        chkTerms.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkTerms.setForeground(new Color(33, 33, 33));
        chkTerms.setFocusPainted(false);
        g.insets = new Insets(5, 0, 20, 0);
        pnlMain.add(chkTerms, g);

        // --- SUBMIT BUTTON ---
        g.gridy++;
        g.insets = new Insets(0, 0, 15, 0);
        btnDangKy = new JButton("Tạo tài khoản");
        btnDangKy.setBackground(new Color(41, 98, 255)); // #2962FF
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangKy.setPreferredSize(new Dimension(0, 45));
        btnDangKy.setFocusPainted(false);
        btnDangKy.setBorderPainted(false);
        btnDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlMain.add(btnDangKy, g);

        // --- LOGIN LINK ---
        g.gridy++;
        g.insets = new Insets(0, 0, 0, 0);
        JPanel pnlLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnlLogin.setBackground(Color.WHITE);
        
        JLabel lblAsk = new JLabel("Đã có tài khoản?");
        lblAsk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAsk.setForeground(new Color(33, 33, 33));
        pnlLogin.add(lblAsk);
        
        lblQuayLai = new JLabel("Đăng nhập");
        lblQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblQuayLai.setForeground(new Color(255, 107, 107));
        lblQuayLai.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlLogin.add(lblQuayLai);

        pnlMain.add(pnlLogin, g);

        // Wrap to center vertically via North/Center/South, or just straight Center
        JScrollPane scrollPane = new JScrollPane(pnlMain);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
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
}
