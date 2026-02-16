package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.NhanVien;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ThongTinTaiKhoanPanel extends JPanel {

    private JLabel lblAvatar;
    private JButton btnDoiAvatar, btnLuuThongTin, btnDoiMatKhau;
    private JTextField txtMaNV, txtHoTen, txtSDT, txtEmail, txtNgaySinh, txtTaiKhoan;
    private JPasswordField txtMatKhauCu, txtMatKhauMoi, txtXacNhan;
    private String duongDanAnhMoi = null; 

    public ThongTinTaiKhoanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- HEADER ---
        JLabel lblTitle = new JLabel("HỒ SƠ CÁ NHÂN & BẢO MẬT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(67, 94, 190));
        add(lblTitle, BorderLayout.NORTH);

        // --- CONTENT ---
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlCenter.setBackground(Color.WHITE);

        // 1. CỘT TRÁI: THÔNG TIN
        JPanel pnlInfo = new JPanel(new GridBagLayout());
        pnlInfo.setBorder(new TitledBorder("Thông tin cá nhân"));
        pnlInfo.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5); g.fill = GridBagConstraints.HORIZONTAL;

        // Avatar
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        lblAvatar = new JLabel();
        lblAvatar.setPreferredSize(new Dimension(100, 100));
        lblAvatar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        // Icon mặc định (Tránh lỗi nếu không có ảnh)
        lblAvatar.setIcon(UIManager.getIcon("FileView.computerIcon")); 
        
        JPanel pAvt = new JPanel(); pAvt.setBackground(Color.WHITE); pAvt.add(lblAvatar);
        pnlInfo.add(pAvt, g);

        g.gridy++; 
        btnDoiAvatar = new JButton("Chọn ảnh...");
        JPanel pBtnA = new JPanel(); pBtnA.setBackground(Color.WHITE); pBtnA.add(btnDoiAvatar);
        pnlInfo.add(pBtnA, g);

        // Fields
        g.gridwidth = 1;
        addField(pnlInfo, g, 2, "Mã NV:", txtMaNV = new JTextField()); txtMaNV.setEditable(false);
        addField(pnlInfo, g, 3, "Họ Tên:", txtHoTen = new JTextField());
        addField(pnlInfo, g, 4, "Ngày Sinh:", txtNgaySinh = new JTextField());
        addField(pnlInfo, g, 5, "SĐT:", txtSDT = new JTextField());
        addField(pnlInfo, g, 6, "Email:", txtEmail = new JTextField());
        addField(pnlInfo, g, 7, "Vai Trò:", txtTaiKhoan = new JTextField()); txtTaiKhoan.setEditable(false);

        g.gridx = 0; g.gridy = 8; g.gridwidth = 2; g.insets = new Insets(20, 5, 5, 5);
        btnLuuThongTin = new JButton("Cập nhật Hồ sơ");
        btnLuuThongTin.setBackground(new Color(40, 167, 69)); btnLuuThongTin.setForeground(Color.WHITE);
        pnlInfo.add(btnLuuThongTin, g);

        pnlCenter.add(pnlInfo);

        // 2. CỘT PHẢI: ĐỔI MẬT KHẨU
        JPanel pnlPass = new JPanel(new GridBagLayout());
        pnlPass.setBorder(new TitledBorder("Đổi mật khẩu"));
        pnlPass.setBackground(Color.WHITE);
        
        GridBagConstraints gp = new GridBagConstraints();
        gp.insets = new Insets(10, 10, 10, 10); gp.fill = GridBagConstraints.HORIZONTAL; gp.gridx = 0; gp.gridy = 0;

        pnlPass.add(new JLabel("Mật khẩu hiện tại:"), gp);
        gp.gridy++; pnlPass.add(txtMatKhauCu = new JPasswordField(20), gp);
        
        gp.gridy++; pnlPass.add(new JLabel("Mật khẩu mới:"), gp);
        gp.gridy++; pnlPass.add(txtMatKhauMoi = new JPasswordField(20), gp);
        
        gp.gridy++; pnlPass.add(new JLabel("Nhập lại mật khẩu mới:"), gp);
        gp.gridy++; pnlPass.add(txtXacNhan = new JPasswordField(20), gp);

        gp.gridy++; gp.insets = new Insets(20, 10, 10, 10);
        btnDoiMatKhau = new JButton("Lưu Mật Khẩu");
        btnDoiMatKhau.setBackground(new Color(67, 94, 190)); btnDoiMatKhau.setForeground(Color.WHITE);
        pnlPass.add(btnDoiMatKhau, gp);
        
        gp.gridy++; gp.weighty = 1.0; pnlPass.add(new JLabel(), gp); // Đẩy lên trên

        pnlCenter.add(pnlPass);
        add(pnlCenter, BorderLayout.CENTER);

        // Event chọn ảnh
        btnDoiAvatar.addActionListener(e -> chonAnh());
    }

    private void addField(JPanel p, GridBagConstraints g, int y, String lbl, JTextField txt) {
        g.gridx = 0; g.gridy = y; g.weightx = 0.3; p.add(new JLabel(lbl), g);
        g.gridx = 1; g.weightx = 0.7; p.add(txt, g);
    }

    private void chonAnh() {
        JFileChooser ch = new JFileChooser();
        ch.setFileFilter(new FileNameExtensionFilter("Ảnh JPG/PNG", "jpg", "png"));
        if(ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            duongDanAnhMoi = f.getAbsolutePath();
            ImageIcon icon = new ImageIcon(duongDanAnhMoi);
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblAvatar.setIcon(new ImageIcon(img));
        }
    }

    // --- GETTERS & SETTERS ---
    public void setThongTin(NhanVien nv) {
        txtMaNV.setText(nv.getMaNV());
        txtHoTen.setText(nv.getHoTen());
        txtSDT.setText(nv.getSdt());
        txtEmail.setText(nv.getEmail());
        txtNgaySinh.setText(nv.getNgaySinh() != null ? nv.getNgaySinh().toString() : "");
        txtTaiKhoan.setText(nv.getVaiTro());
        
        if (nv.getHinhAnh() != null && !nv.getHinhAnh().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(nv.getHinhAnh());
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblAvatar.setIcon(new ImageIcon(img));
            } catch (Exception e) {}
        }
    }

    public String getHoTen() { return txtHoTen.getText(); }
    public String getSDT() { return txtSDT.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getNgaySinh() { return txtNgaySinh.getText(); }
    public String getDuongDanAnhMoi() { return duongDanAnhMoi; }
    
    public String getMatKhauCu() { return new String(txtMatKhauCu.getPassword()); }
    public String getMatKhauMoi() { return new String(txtMatKhauMoi.getPassword()); }
    public String getXacNhan() { return new String(txtXacNhan.getPassword()); }

    // --- [MỚI] HÀM THIẾU MÀ CONTROLLER CẦN ---
    public void showMessage(String msg) { 
        JOptionPane.showMessageDialog(this, msg); 
    }
    
    public void clearPassFields() {
        txtMatKhauCu.setText("");
        txtMatKhauMoi.setText("");
        txtXacNhan.setText("");
    }

    public void addLuuThongTinListener(ActionListener l) { btnLuuThongTin.addActionListener(l); }
    public void addDoiMatKhauListener(ActionListener l) { btnDoiMatKhau.addActionListener(l); }
}