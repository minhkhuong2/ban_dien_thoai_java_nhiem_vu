package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DangKyFrame extends JFrame {

    private JTextField txtHoTen;
    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JPasswordField txtXacNhanMK;
    private JButton btnDangKy, btnQuayLai;

    public DangKyFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Đăng Ký Tài Khoản Mới");
        setSize(950, 600); // Rộng hơn chút để chứa form dài
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // --- LEFT PANEL: BRANDING (Có thể đổi màu gradients khác hoặc giữ nguyên) ---
        GradientPanel pnlLeft = new GradientPanel(new Color(23, 162, 184), new Color(40, 167, 69)); // Xanh lá/Cyan theme đăng ký
        pnlLeft.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0; gbcLeft.gridy = 0;
        gbcLeft.insets = new Insets(10, 10, 20, 10);
        
        JLabel lblIcon = new JLabel("📝");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        lblIcon.setForeground(Color.WHITE);
        pnlLeft.add(lblIcon, gbcLeft);
        
        gbcLeft.gridy++;
        JLabel lblTitleLeft = new JLabel("GIA NHẬP ĐỘI NGŨ");
        lblTitleLeft.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitleLeft.setForeground(Color.WHITE);
        pnlLeft.add(lblTitleLeft, gbcLeft);
        
        gbcLeft.gridy++;
        JLabel lblSub = new JLabel("Tạo tài khoản để quản lý cửa hàng ngay hôm nay");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(240, 240, 240));
        pnlLeft.add(lblSub, gbcLeft);

        add(pnlLeft);

        // --- RIGHT PANEL: REGISTER FORM ---
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 40, 5, 40); // Padding chuẩn
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Header Form
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 40, 20, 40);
        JLabel lblTitleForm = new JLabel("Đăng Ký Tài Khoản");
        lblTitleForm.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitleForm.setForeground(new Color(50, 50, 50));
        pnlRight.add(lblTitleForm, gbc);

        // Input Fields
        gbc.insets = new Insets(5, 40, 5, 40);
        
        addLabelAndField(pnlRight, gbc, "Họ và Tên", txtHoTen = new JTextField());
        addLabelAndField(pnlRight, gbc, "Tài khoản", txtTaiKhoan = new JTextField());
        addLabelAndField(pnlRight, gbc, "Mật khẩu", txtMatKhau = new JPasswordField());
        addLabelAndField(pnlRight, gbc, "Nhập lại Mật khẩu", txtXacNhanMK = new JPasswordField());

        // Buttons
        gbc.gridy++;
        gbc.insets = new Insets(20, 40, 10, 40);
        btnDangKy = new JButton("ĐĂNG KÝ NGAY");
        styleButton(btnDangKy, new Color(40, 167, 69)); // Xanh lá
        pnlRight.add(btnDangKy, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 40, 20, 40);
        btnQuayLai = new JButton("Quay lại Đăng nhập");
        btnQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnQuayLai.setForeground(new Color(100, 100, 100));
        btnQuayLai.setBorderPainted(false);
        btnQuayLai.setContentAreaFilled(false);
        btnQuayLai.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlRight.add(btnQuayLai, gbc);

        add(pnlRight);
    }

    private void addLabelAndField(JPanel pnl, GridBagConstraints gbc, String labelText, JTextField field) {
        gbc.gridy++;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(100, 100, 100));
        pnl.add(lbl, gbc);

        gbc.gridy++;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
        pnl.add(field, gbc);
    }
    
    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(0, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
    }

    // Getters & Listeners (Giữ nguyên)
    public String getHoTen() { return txtHoTen.getText(); }
    public String getTaiKhoan() { return txtTaiKhoan.getText(); }
    public String getMatKhau() { return new String(txtMatKhau.getPassword()); }
    public String getXacNhanMK() { return new String(txtXacNhanMK.getPassword()); }
    
    public void addDangKyListener(ActionListener l) { btnDangKy.addActionListener(l); }
    public void addQuayLaiListener(ActionListener l) { btnQuayLai.addActionListener(l); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}
