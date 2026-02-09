package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("ĐĂNG KÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(40, 167, 69)); // Màu xanh lá
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Form
        JPanel pnlCenter = new JPanel(new GridLayout(4, 1, 10, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        pnlCenter.add(taoInput("Họ và Tên:", txtHoTen = new JTextField()));
        pnlCenter.add(taoInput("Tài khoản:", txtTaiKhoan = new JTextField()));
        pnlCenter.add(taoInput("Mật khẩu:", txtMatKhau = new JPasswordField()));
        pnlCenter.add(taoInput("Nhập lại Mật khẩu:", txtXacNhanMK = new JPasswordField()));

        add(pnlCenter, BorderLayout.CENTER);

        // Footer (Nút bấm)
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        btnDangKy = new JButton("Đăng Ký");
        btnDangKy.setBackground(new Color(40, 167, 69));
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setPreferredSize(new Dimension(120, 40));
        btnDangKy.setFont(new Font("Arial", Font.BOLD, 14));

        btnQuayLai = new JButton("Quay Lại");
        btnQuayLai.setPreferredSize(new Dimension(100, 40));

        pnlFooter.add(btnDangKy);
        pnlFooter.add(btnQuayLai);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private JPanel taoInput(String title, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    // Getters & Listeners
    public String getHoTen() { return txtHoTen.getText(); }
    public String getTaiKhoan() { return txtTaiKhoan.getText(); }
    public String getMatKhau() { return new String(txtMatKhau.getPassword()); }
    public String getXacNhanMK() { return new String(txtXacNhanMK.getPassword()); }
    
    public void addDangKyListener(ActionListener l) { btnDangKy.addActionListener(l); }
    public void addQuayLaiListener(ActionListener l) { btnQuayLai.addActionListener(l); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}
