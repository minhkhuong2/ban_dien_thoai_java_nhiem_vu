package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.ThuongHieu;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class ThuongHieuDialog extends JDialog {
    
    private JTextField txtTen;
    private JLabel lblLogo;
    private JButton btnChonAnh, btnLuu, btnHuy;
    private String hinhAnhPath = "";
    private ThuongHieu result = null;

    // UI Constants
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public ThuongHieuDialog(Window parent, String title, ThuongHieu th) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel pnlMain = new JPanel(new BorderLayout(20, 20));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JLabel lblHeader = new JLabel(title);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(COLOR_TEXT_DARK);
        pnlMain.add(lblHeader, BorderLayout.NORTH);
        
        JPanel pnlContent = new JPanel(new GridBagLayout());
        pnlContent.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 0, 10, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        
        // Tên Thương Hiệu
        g.gridy = 0;
        JLabel lblTen = new JLabel("Tên Thương Hiệu (*):");
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlContent.add(lblTen, g);
        
        g.gridy = 1;
        txtTen = new JTextField();
        txtTen.setPreferredSize(new Dimension(200, 40));
        txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTen.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        pnlContent.add(txtTen, g);
        
        // Logo
        g.gridy = 2;
        JLabel lblLogoTitle = new JLabel("Logo Thương Hiệu:");
        lblLogoTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlContent.add(lblLogoTitle, g);
        
        g.gridy = 3;
        JPanel pnlLogoWrap = new JPanel(new BorderLayout(15, 0));
        pnlLogoWrap.setOpaque(false);
        
        lblLogo = new JLabel("Chưa có Logo", SwingConstants.CENTER);
        lblLogo.setPreferredSize(new Dimension(80, 80));
        lblLogo.setBorder(BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1));
        
        btnChonAnh = new JButton("Tải Ảnh Lên...");
        btnChonAnh.setFocusPainted(false);
        btnChonAnh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonAnh.addActionListener(e -> chonAnh());
        
        JPanel pBtnWrap = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pBtnWrap.setOpaque(false);
        pBtnWrap.add(btnChonAnh);
        
        pnlLogoWrap.add(lblLogo, BorderLayout.WEST);
        pnlLogoWrap.add(pBtnWrap, BorderLayout.CENTER);
        
        pnlContent.add(pnlLogoWrap, g);
        
        pnlMain.add(pnlContent, BorderLayout.CENTER);
        
        // Nút Lưu/Hủy
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlFooter.setOpaque(false);
        
        btnHuy = new JButton("Hủy Bỏ");
        btnHuy.setPreferredSize(new Dimension(100, 40));
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHuy.setFocusPainted(false);
        btnHuy.addActionListener(e -> dispose());
        
        btnLuu = new JButton("Lưu Lại");
        btnLuu.setPreferredSize(new Dimension(120, 40));
        btnLuu.setBackground(COLOR_PRIMARY);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setBorder(null);
        btnLuu.setFocusPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLuu.addActionListener(e -> luuDuLieu());
        
        pnlFooter.add(btnHuy);
        pnlFooter.add(btnLuu);
        
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);
        
        setContentPane(pnlMain);
        
        // Nếu là edit thì điền dữ liệu
        if (th != null) {
            txtTen.setText(th.getTenTH());
            if (th.getLogo() != null && !th.getLogo().isEmpty()) {
                hinhAnhPath = th.getLogo();
                hienThiAnh(hinhAnhPath);
            }
        }
    }
    
    private void chonAnh() {
        JFileChooser ch = new JFileChooser();
        ch.setFileFilter(new FileNameExtensionFilter("Ảnh JPG/PNG", "jpg", "png", "jpeg"));
        if(ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            hinhAnhPath = f.getAbsolutePath();
            hienThiAnh(hinhAnhPath);
        }
    }
    
    private void hienThiAnh(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
            lblLogo.setText("");
        } catch (Exception e) {
            lblLogo.setIcon(null);
            lblLogo.setText("Lỗi Ảnh");
        }
    }
    
    private void luuDuLieu() {
        if (txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên thương hiệu!");
            return;
        }
        result = new ThuongHieu();
        result.setTenTH(txtTen.getText().trim());
        result.setLogo(hinhAnhPath);
        dispose();
    }
    
    public ThuongHieu getResult() {
        return result;
    }
}
