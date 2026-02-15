package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.model.TaiKhoanSession;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ThongTinTaiKhoanPanel extends JPanel {

    // Phần thông tin
    private JLabel lblMaNV, lblHoTen, lblTaiKhoan, lblVaiTro;
    
    // Phần đổi mật khẩu
    private JPasswordField txtMatKhauCu;
    private JPasswordField txtMatKhauMoi;
    private JPasswordField txtXacNhan;
    private JButton btnDoiMatKhau;
    private JButton btnHuy;

    public ThongTinTaiKhoanPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 40, 40, 40));

        // --- HEADER ---
        JLabel lblTitle = new JLabel("Thông Tin Tài Khoản & Bảo Mật");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(50, 50, 50));
        add(lblTitle, BorderLayout.NORTH);

        // --- BODY (Chia đôi: Trái Info - Phải Đổi Pass) ---
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 30, 0));
        pnlCenter.setOpaque(false);
        
        pnlCenter.add(createProfilePanel());
        pnlCenter.add(createChangePassPanel());
        
        add(pnlCenter, BorderLayout.CENTER);
        
        // Load dữ liệu lên luôn khi mở
        loadData();
    }

    private JPanel createProfilePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            " Thông tin cá nhân ", 
            TitledBorder.DEFAULT_JUSTIFICATION, 
            TitledBorder.DEFAULT_POSITION, 
            new Font("Segoe UI", Font.BOLD, 14)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 20, 10, 20);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0; 
        g.gridx = 0; g.gridy = 0;

        // Avatar (Icon đại diện)
        JLabel lblAvatar = new JLabel("👤", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        p.add(lblAvatar, g);

        // Các label thông tin
        g.gridy++; p.add(createLabelPair("Mã Nhân Viên:", lblMaNV = new JLabel("...")), g);
        g.gridy++; p.add(createLabelPair("Họ và Tên:", lblHoTen = new JLabel("...")), g);
        g.gridy++; p.add(createLabelPair("Tài khoản:", lblTaiKhoan = new JLabel("...")), g);
        g.gridy++; p.add(createLabelPair("Vai trò:", lblVaiTro = new JLabel("...")), g);
        
        // Đẩy các thành phần lên trên
        g.gridy++; g.weighty = 1.0;
        p.add(new JLabel(""), g); 

        return p;
    }

    private JPanel createLabelPair(String title, JLabel valueLabel) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(Color.GRAY);
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(new Color(33, 33, 33));
        
        p.add(lblTitle, BorderLayout.NORTH);
        p.add(valueLabel, BorderLayout.CENTER);
        p.add(new JSeparator(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel createChangePassPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            " Đổi mật khẩu ", 
            TitledBorder.DEFAULT_JUSTIFICATION, 
            TitledBorder.DEFAULT_POSITION, 
            new Font("Segoe UI", Font.BOLD, 14)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(15, 20, 5, 20);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0; 
        g.gridx = 0; g.gridy = 0;

        p.add(new JLabel("Mật khẩu hiện tại:"), g);
        g.gridy++;
        txtMatKhauCu = new JPasswordField(); styleField(txtMatKhauCu);
        p.add(txtMatKhauCu, g);

        g.gridy++;
        p.add(new JLabel("Mật khẩu mới:"), g);
        g.gridy++;
        txtMatKhauMoi = new JPasswordField(); styleField(txtMatKhauMoi);
        p.add(txtMatKhauMoi, g);

        g.gridy++;
        p.add(new JLabel("Nhập lại mật khẩu mới:"), g);
        g.gridy++;
        txtXacNhan = new JPasswordField(); styleField(txtXacNhan);
        p.add(txtXacNhan, g);

        // Buttons
        g.gridy++; g.insets = new Insets(30, 20, 10, 20);
        btnDoiMatKhau = new JButton("Xác nhận đổi");
        btnDoiMatKhau.setBackground(new Color(67, 94, 190));
        btnDoiMatKhau.setForeground(Color.WHITE);
        btnDoiMatKhau.setPreferredSize(new Dimension(0, 40));
        btnDoiMatKhau.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDoiMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));
        p.add(btnDoiMatKhau, g);

        g.gridy++; g.insets = new Insets(10, 20, 10, 20);
        btnHuy = new JButton("Xóa trắng");
        btnHuy.setBackground(new Color(240, 240, 240));
        btnHuy.setPreferredSize(new Dimension(0, 35));
        p.add(btnHuy, g);
        
        g.gridy++; g.weighty = 1.0;
        p.add(new JLabel(""), g); 

        return p;
    }
    
    private void styleField(JTextField txt) {
        txt.setPreferredSize(new Dimension(0, 35));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void loadData() {
        NhanVien nv = TaiKhoanSession.taiKhoanHienTai;
        if (nv != null) {
            lblMaNV.setText(nv.getMaNV());
            lblHoTen.setText(nv.getHoTen());
            lblTaiKhoan.setText(nv.getTaiKhoan());
            lblVaiTro.setText(nv.getVaiTro());
        }
    }

    // --- GETTERS & LISTENERS ---
    public String getMatKhauCu() { return new String(txtMatKhauCu.getPassword()); }
    public String getMatKhauMoi() { return new String(txtMatKhauMoi.getPassword()); }
    public String getXacNhan() { return new String(txtXacNhan.getPassword()); }
    
    public void clearForm() {
        txtMatKhauCu.setText("");
        txtMatKhauMoi.setText("");
        txtXacNhan.setText("");
    }

    public void addDoiMatKhauListener(ActionListener l) { btnDoiMatKhau.addActionListener(l); }
    public void addHuyListener(ActionListener l) { btnHuy.addActionListener(l); }
    
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}