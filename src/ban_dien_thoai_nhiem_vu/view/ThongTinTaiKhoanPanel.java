package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.NhanVien;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ThongTinTaiKhoanPanel extends JPanel {

    private JLabel lblAvatar;
    private JButton btnDoiAvatar, btnLuuThongTin, btnDoiMatKhau;
    private JTextField txtMaNV, txtHoTen, txtSDT, txtEmail, txtNgaySinh, txtTaiKhoan;
    private JPasswordField txtMatKhauCu, txtMatKhauMoi, txtXacNhan;
    private String duongDanAnhMoi = null; 

    // UI Constants
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_SUCCESS = new Color(25, 135, 84);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public ThongTinTaiKhoanPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- HEADER ---
        JLabel lblTitle = new JLabel("Hồ Sơ Cá Nhân & Bảo Mật");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- CONTENT ---
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 30, 0)); // Trái phải cách nhau 30px
        pnlCenter.setOpaque(false);

        pnlCenter.add(taoPanelThongTin());
        pnlCenter.add(taoPanelDoiMatKhau());
        
        add(pnlCenter, BorderLayout.CENTER);

        // Event chọn ảnh
        btnDoiAvatar.addActionListener(e -> chonAnh());
    }
    
    // =========================================================
    // 1. PANEL THÔNG TIN
    // =========================================================
    private JPanel taoPanelThongTin() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel lblHeader = new JLabel("Thông Tin Cá Nhân");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeader.setForeground(COLOR_TEXT_DARK);
        p.add(lblHeader, BorderLayout.NORTH);
        
        JPanel pContent = new JPanel(new GridBagLayout());
        pContent.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 0, 8, 0); g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        
        // Avatar Block
        g.gridx = 0; g.gridy = 0;
        JPanel pAvtWrap = new JPanel(new BorderLayout(0, 5));
        pAvtWrap.setOpaque(false);
        
        lblAvatar = new JLabel();
        lblAvatar.setPreferredSize(new Dimension(100, 100)); // Thu nhỏ avatar lại một chút để thông tin ở dưới đẩy lên
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        setCircularImage(lblAvatar, null); // Set default
        
        JPanel pCenterAvt = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pCenterAvt.setOpaque(false);
        pCenterAvt.add(lblAvatar);
        
        btnDoiAvatar = createFlatButton("Đổi Ảnh Đại Diện", new Color(240, 240, 240), COLOR_TEXT_DARK);
        btnDoiAvatar.setPreferredSize(new Dimension(160, 38));
        JPanel pCenterBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pCenterBtn.setOpaque(false);
        pCenterBtn.add(btnDoiAvatar);
        
        pAvtWrap.add(pCenterAvt, BorderLayout.CENTER);
        pAvtWrap.add(pCenterBtn, BorderLayout.SOUTH);
        pContent.add(pAvtWrap, g);

        // Fields
        g.gridy++; txtMaNV = addInputField(pContent, "Mã MV (Login ID):", g, false);
        g.gridy++; txtTaiKhoan = addInputField(pContent, "Vai Trò Hiện Tại:", g, false);
        g.gridy++; txtHoTen = addInputField(pContent, "Họ và Tên:", g, true);
        g.gridy++; txtNgaySinh = addInputField(pContent, "Ngày Sinh:", g, true);
        g.gridy++; txtSDT = addInputField(pContent, "Số Điện Thoại:", g, true);
        g.gridy++; txtEmail = addInputField(pContent, "Email Liên Hệ:", g, true);

        // Nút Lưu
        g.gridy++; g.insets = new Insets(15, 0, 0, 0); // Giảm insets
        btnLuuThongTin = createFlatButton("Cập Nhật Hồ Sơ", COLOR_SUCCESS, Color.WHITE);
        btnLuuThongTin.setPreferredSize(new Dimension(0, 40)); // Giảm chiều cao nút
        
        JPanel pCenterWrap = new JPanel(new BorderLayout());
        pCenterWrap.setOpaque(false);
        pCenterWrap.add(pContent, BorderLayout.NORTH);

        // Put the form in a JScrollPane to prevent any cutoff on smaller screens
        JScrollPane sc = new JScrollPane(pCenterWrap);
        sc.setBorder(null);
        sc.setOpaque(false);
        sc.getViewport().setOpaque(false);
        sc.getVerticalScrollBar().setUnitIncrement(16);

        p.add(sc, BorderLayout.CENTER);
        p.add(btnLuuThongTin, BorderLayout.SOUTH);
        
        return p;
    }
    
    // =========================================================
    // 2. PANEL ĐỔI MẬT KHẨU
    // =========================================================
    private JPanel taoPanelDoiMatKhau() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel lblHeader = new JLabel("Bảo Mật & Đăng Nhập");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeader.setForeground(COLOR_TEXT_DARK);
        p.add(lblHeader, BorderLayout.NORTH);
        
        JPanel pContent = new JPanel(new GridBagLayout());
        pContent.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 0, 10, 0); g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        
        g.gridy = 0; txtMatKhauCu = addPasswordField(pContent, "Mật Khẩu Hiện Tại:", g);
        g.gridy++; txtMatKhauMoi = addPasswordField(pContent, "Mật Khẩu Mới:", g);
        g.gridy++; txtXacNhan = addPasswordField(pContent, "Xác Nhận Mật Khẩu Mới:", g);

        // Information banner
        g.gridy++; g.insets = new Insets(30, 0, 10, 0);
        JPanel pBanner = new JPanel(new BorderLayout(10, 10));
        pBanner.setBackground(new Color(232, 244, 253)); // Light blue alert box
        pBanner.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel lblInfoIcon = new JLabel("ℹ");
        lblInfoIcon.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblInfoIcon.setForeground(COLOR_PRIMARY);
        JLabel lblInfoText = new JLabel("<html>Nên sử dụng mật khẩu mạnh chứa ít nhất 8 ký tự, <br>bao gồm chữ, số và ký tự đặc biệt để tài khoản an toàn hơn.</html>");
        lblInfoText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfoText.setForeground(new Color(10, 88, 202));
        pBanner.add(lblInfoIcon, BorderLayout.WEST);
        pBanner.add(lblInfoText, BorderLayout.CENTER);
        pContent.add(pBanner, g);

        // Nút Lưu Mật khẩu
        g.gridy++; g.insets = new Insets(25, 0, 0, 0);
        btnDoiMatKhau = createFlatButton("Đổi Mật Khẩu", COLOR_PRIMARY, Color.WHITE);
        btnDoiMatKhau.setPreferredSize(new Dimension(0, 45));

        JPanel pCenterWrap = new JPanel(new BorderLayout());
        pCenterWrap.setOpaque(false);
        pCenterWrap.add(pContent, BorderLayout.NORTH);

        p.add(pCenterWrap, BorderLayout.CENTER);
        p.add(btnDoiMatKhau, BorderLayout.SOUTH);
        
        return p;
    }

    // =========================================================
    // UTILS 
    // =========================================================
    
    private void setCircularImage(JLabel label, String path) {
        try {
            int diameter = 100; // Update diameter to match the smaller layout
            Image img;
            if (path == null || path.isEmpty()) {
                img = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            } else {
                img = new ImageIcon(path).getImage();
            }
            BufferedImage bi = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bi.createGraphics();
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
            g2.drawImage(img, 0, 0, diameter, diameter, null);
            g2.setClip(null); // Reset
            
            // Draw a subtle border
            g2.setColor(COLOR_TABLE_BORDER);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(1, 1, diameter-2, diameter-2);
            g2.dispose();
            
            label.setIcon(new ImageIcon(bi));
        } catch (Exception e) {
            label.setIcon(UIManager.getIcon("FileView.computerIcon"));
        }
    }

    private JTextField addInputField(JPanel parent, String label, GridBagConstraints g, boolean isEditable) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT_MUTED);
        p.add(lbl, BorderLayout.NORTH);
        
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(0, 36)); // Thu gọn chiều cao Input
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(3, 10, 3, 10)
        ));
        
        if(!isEditable) {
            txt.setEditable(false);
            txt.setBackground(new Color(248, 249, 250));
            txt.setForeground(Color.GRAY);
        }
        
        p.add(txt, BorderLayout.CENTER);
        parent.add(p, g);
        return txt;
    }
    
    private JPasswordField addPasswordField(JPanel parent, String label, GridBagConstraints g) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT_MUTED);
        p.add(lbl, BorderLayout.NORTH);
        
        JPasswordField txt = new JPasswordField();
        txt.setPreferredSize(new Dimension(0, 42));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 12, 5, 12)
        ));
        
        p.add(txt, BorderLayout.CENTER);
        parent.add(p, g);
        return txt;
    }

    private JButton createFlatButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void chonAnh() {
        JFileChooser ch = new JFileChooser();
        ch.setFileFilter(new FileNameExtensionFilter("Ảnh JPG/PNG", "jpg", "png"));
        if(ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            duongDanAnhMoi = f.getAbsolutePath();
            setCircularImage(lblAvatar, duongDanAnhMoi);
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
            setCircularImage(lblAvatar, nv.getHinhAnh());
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
