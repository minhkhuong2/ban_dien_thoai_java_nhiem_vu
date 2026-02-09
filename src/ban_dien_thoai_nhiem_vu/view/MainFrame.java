package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    
    // Các nút Menu
    private JButton btnTrangChu, btnBanHang, btnSanPham, btnKhachHang, btnGiamGia, btnHoaDon, btnThongKe, btnDangXuat;
    private JPanel pnlContent;
    private JPanel pnlMenu;
    
    // --- CÁC NHÃN HIỂN THỊ SỐ LIỆU (Giữ lại để tránh lỗi compile nếu Controller có gọi) ---
    public JLabel lblStatSanPham = new JLabel();
    public JLabel lblStatDonHang = new JLabel();
    public JLabel lblStatDoanhThu = new JLabel();
    public JLabel lblStatKhachHang = new JLabel();

    public MainFrame() {
        thietKeGiaoDien();
        phanQuyen();
    }

    private void thietKeGiaoDien() {
        setTitle("HỆ THỐNG QUẢN LÝ ĐIỆN THOẠI - PNC STORE");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. MENU BÊN TRÁI (SIDEBAR)
        pnlMenu = new JPanel();
        pnlMenu.setBackground(Color.WHITE); // Shopzy Style: White Sidebar
        pnlMenu.setPreferredSize(new Dimension(260, 0));
        pnlMenu.setLayout(new BorderLayout()); 
        pnlMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(240, 240, 240))); // Border right

        // Logo
        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        pnlLogo.setBackground(Color.WHITE);
        
        JLabel lblLogoText = new JLabel("PNC STORE"); 
        lblLogoText.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblLogoText.setForeground(new Color(41, 98, 255)); // Blue brand color
        
        pnlLogo.add(lblLogoText);
        pnlMenu.add(pnlLogo, BorderLayout.NORTH);

        // Menu buttons Container
        JPanel pnlMenuBtns = new JPanel();
        pnlMenuBtns.setLayout(new BoxLayout(pnlMenuBtns, BoxLayout.Y_AXIS));
        pnlMenuBtns.setBackground(Color.WHITE);
        pnlMenuBtns.setBorder(new EmptyBorder(20, 10, 20, 10));

        btnTrangChu = taoNutMenu("Dashboard", "home.png", true); // Active by default
        btnBanHang  = taoNutMenu("Bán Hàng", "cart.png", false); 
        btnSanPham  = taoNutMenu("Sản Phẩm", "phone.png", false);
        btnKhachHang = taoNutMenu("Khách Hàng", "user.png", false);
        btnGiamGia  = taoNutMenu("Voucher", "voucher.png", false);
        btnHoaDon   = taoNutMenu("Hóa Đơn", "bill.png", false);
        btnThongKe  = taoNutMenu("Thống Kê", "chart.png", false);
        btnDangXuat = taoNutMenu("Đăng Xuất", "logout.png", false);

        // Add to panel
        pnlMenuBtns.add(btnTrangChu); pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnBanHang); pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnSanPham); pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnKhachHang); pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnGiamGia); pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnHoaDon); pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnThongKe); 
        
        pnlMenu.add(pnlMenuBtns, BorderLayout.CENTER);
        
        // Bottom Logout
        JPanel pnlLogout = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLogout.setBackground(Color.WHITE);
        pnlLogout.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlLogout.add(btnDangXuat);
        pnlMenu.add(pnlLogout, BorderLayout.SOUTH);

        add(pnlMenu, BorderLayout.WEST);

        // 2. NỘI DUNG CHÍNH
        pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(new Color(245, 247, 250)); // Light Gray Background
        
        // LOAD DASHBOARD MỚI
        pnlContent.add(new TrangChuPanel(lblStatDoanhThu, lblStatDonHang, lblStatKhachHang), BorderLayout.CENTER);
        
        add(pnlContent, BorderLayout.CENTER);
    }

    private JButton taoNutMenu(String text, String iconName, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        
        if (isActive) {
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(41, 98, 255)); // Active Blue
        } else {
            btn.setForeground(new Color(120, 120, 120)); // Gray text
            btn.setBackground(Color.WHITE);
        }
        
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20)); 
        btn.setHorizontalAlignment(SwingConstants.LEFT); 
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setIconTextGap(15);
        btn.setMaximumSize(new Dimension(240, 50));
        
        if (!isActive) btn.setBorder(null); // Remove border for clean look

        try {
             URL iconURL = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/" + iconName);
             if (iconURL != null) btn.setIcon(new ImageIcon(new ImageIcon(iconURL).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {}
        
        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { 
                if (btn.getBackground().equals(Color.WHITE)) {
                    btn.setBackground(new Color(245, 247, 250));
                    btn.setForeground(new Color(41, 98, 255));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) { 
                if (!btn.getBackground().equals(new Color(41, 98, 255))) { // if not active
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(new Color(120, 120, 120));
                }
            }
        });
        return btn;
    }
    
    private void phanQuyen() {
        // Lấy thông tin người đang đăng nhập
        ban_dien_thoai_nhiem_vu.model.NhanVien nv = ban_dien_thoai_nhiem_vu.model.TaiKhoanSession.taiKhoanHienTai;
        
        if (nv != null) {
            if (!nv.getVaiTro().equalsIgnoreCase("ADMIN")) {
                btnSanPham.setVisible(false);
                btnThongKe.setVisible(false);
                btnGiamGia.setVisible(false);
            }
        }
    }

    // --- NAVIGATION HELPERS ---
    public void showPanel(JPanel panel) {
        pnlContent.removeAll();
        pnlContent.add(panel, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }
    
    public void setActiveButton(JButton activeBtn) {
        // Reset all buttons
        resetButton(btnTrangChu); resetButton(btnBanHang); resetButton(btnSanPham);
        resetButton(btnKhachHang); resetButton(btnGiamGia); resetButton(btnHoaDon);
        resetButton(btnThongKe); 
        
        // Highlight active
        activeBtn.setBackground(new Color(41, 98, 255));
        activeBtn.setForeground(Color.WHITE);
        activeBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
    }
    
    private void resetButton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(100, 100, 100)); // Darker gray for better visibility
        btn.setBorder(null);
    }

    // Events
    public void addTrangChuListener(ActionListener l) { btnTrangChu.addActionListener(l); }
    public void addBanHangListener(ActionListener l) { btnBanHang.addActionListener(l); }
    public void addSanPhamListener(ActionListener l) { btnSanPham.addActionListener(l); }
    public void addKhachHangListener(ActionListener l) { btnKhachHang.addActionListener(l); }
    public void addGiamGiaListener(ActionListener l) { btnGiamGia.addActionListener(l); }
    public void addHoaDonListener(ActionListener l) { btnHoaDon.addActionListener(l); }
    public void addThongKeListener(ActionListener l) { btnThongKe.addActionListener(l); }
    public void addDangXuatListener(ActionListener l) { btnDangXuat.addActionListener(l); }
    
    // Getters for buttons to set active state from Controller if needed (or just use listeners to call setActiveButton indirectly)
    public JButton getBtnTrangChu() { return btnTrangChu; }
    public JButton getBtnBanHang() { return btnBanHang; }
    public JButton getBtnSanPham() { return btnSanPham; }
    public JButton getBtnKhachHang() { return btnKhachHang; }
    public JButton getBtnGiamGia() { return btnGiamGia; }
    public JButton getBtnHoaDon() { return btnHoaDon; }
    public JButton getBtnThongKe() { return btnThongKe; }
}
