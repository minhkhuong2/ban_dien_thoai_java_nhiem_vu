package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DangKyFrame extends JFrame {

    private JTextField txtHoTen, txtTaiKhoan, txtSDT, txtEmail;
    private JPasswordField txtMatKhau, txtXacNhanMK;
    private JButton btnDangKy, btnQuayLai;

    public DangKyFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Đăng Ký Tài Khoản Mới - PNC STORE");
        setSize(900, 650); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2)); // Chia đôi màn hình

        // --- CỘT TRÁI: HÌNH ẢNH / BANNER ---
        JPanel pnlLeft = new JPanel();
        pnlLeft.setBackground(new Color(23, 162, 184)); // Màu xanh Cyan chủ đạo
        pnlLeft.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0; gbcLeft.gridy = 0;
        
        JLabel lblIcon = new JLabel("📝");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        pnlLeft.add(lblIcon, gbcLeft);
        
        gbcLeft.gridy++;
        JLabel lblBrand = new JLabel("PNC STORE");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblBrand.setForeground(Color.WHITE);
        pnlLeft.add(lblBrand, gbcLeft);
        
        gbcLeft.gridy++;
        JLabel lblSlogan = new JLabel("Hệ thống quản lý chuyên nghiệp");
        lblSlogan.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblSlogan.setForeground(new Color(224, 255, 255));
        pnlLeft.add(lblSlogan, gbcLeft);
        
        add(pnlLeft);

        // --- CỘT PHẢI: FORM NHẬP LIỆU (SỬA LỖI OVERLAP TẠI ĐÂY) ---
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; 
        gbc.gridx = 0; 
        gbc.gridy = 0; // Bắt đầu từ dòng 0

        // Title Form
        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(23, 162, 184));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.insets = new Insets(20, 40, 20, 40); // Cách lề
        pnlRight.add(lblTitle, gbc);

        // --- CÁC Ô NHẬP LIỆU ---
        // Gọi hàm helper đã được fix lỗi khoảng cách
        addLabelAndField(pnlRight, gbc, "Họ và Tên (*)", txtHoTen = new JTextField());
        addLabelAndField(pnlRight, gbc, "Số điện thoại (*)", txtSDT = new JTextField());
        addLabelAndField(pnlRight, gbc, "Email (*)", txtEmail = new JTextField());
        addLabelAndField(pnlRight, gbc, "Tên đăng nhập (*)", txtTaiKhoan = new JTextField());
        addLabelAndField(pnlRight, gbc, "Mật khẩu (*)", txtMatKhau = new JPasswordField());
        addLabelAndField(pnlRight, gbc, "Nhập lại mật khẩu (*)", txtXacNhanMK = new JPasswordField());

        // Buttons
        gbc.gridy++;
        gbc.insets = new Insets(20, 40, 10, 40);
        btnDangKy = new JButton("ĐĂNG KÝ NGAY");
        styleButton(btnDangKy, new Color(40, 167, 69)); // Màu xanh lá
        pnlRight.add(btnDangKy, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 40, 20, 40);
        btnQuayLai = new JButton("Quay lại Đăng nhập");
        btnQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnQuayLai.setForeground(Color.GRAY);
        btnQuayLai.setBorderPainted(false);
        btnQuayLai.setContentAreaFilled(false);
        btnQuayLai.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlRight.add(btnQuayLai, gbc);

        add(pnlRight);
    }

    // --- HÀM HELPER ĐÃ FIX LỖI OVERLAP ---
    private void addLabelAndField(JPanel pnl, GridBagConstraints gbc, String text, JTextField field) {
        // 1. Thêm Label
        gbc.gridy++; // Xuống dòng mới
        gbc.insets = new Insets(10, 40, 5, 40); // Top: 10, Bottom: 5 (Tạo khoảng cách)
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Color.DARK_GRAY);
        pnl.add(lbl, gbc);

        // 2. Thêm TextField
        gbc.gridy++; // Xuống dòng tiếp theo cho ô nhập
        gbc.insets = new Insets(0, 40, 0, 40); // Bottom: 0 (Để sát với dòng tiếp theo hơn chút)
        field.setPreferredSize(new Dimension(0, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // Bo viền cho đẹp
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
    }

    // Getters & Listeners
    public String getHoTen() { return txtHoTen.getText(); }
    public String getSDT() { return txtSDT.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getTaiKhoan() { return txtTaiKhoan.getText(); }
    public String getMatKhau() { return new String(txtMatKhau.getPassword()); }
    public String getXacNhanMK() { return new String(txtXacNhanMK.getPassword()); }
    
    public void addDangKyListener(ActionListener l) { btnDangKy.addActionListener(l); }
    public void addQuayLaiListener(ActionListener l) { btnQuayLai.addActionListener(l); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}