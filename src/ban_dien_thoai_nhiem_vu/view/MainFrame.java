package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class MainFrame extends JFrame {
    
    // --- SIDEBAR MENU BUTTONS ---
    private JButton btnTrangChu;
    private JButton btnBanHang;
    private JButton btnSanPham; // Submenu
    private JButton btnKho; 
    private JButton btnKhachHang;
    private JButton btnGiamGia;
    private JButton btnHoaDon;
    private JButton btnThongKe;
    private JButton btnHeThong; // Submenu
    private JButton btnDangXuat;

    // --- POPUP MENU ---
    private JPopupMenu popupSanPham;
    private JMenuItem menuQuanLySP;
    private JMenuItem menuDanhMuc;
    private JMenuItem menuThuongHieu;
    private JMenuItem menuThuocTinh;

    private JPopupMenu popupHeThong;
    private JMenuItem menuNhanVien;
    private JMenuItem menuTaiKhoan;

    // --- PANELS ---
    private JPanel pnlContent;
    private JPanel pnlMenu;
    
    // --- USER INFO ---
    private JLabel lblUserInfo; 

    // --- DASHBOARD LABELS (Passed to TrangChuPanel) ---
    public JLabel lblStatSanPham = new JLabel("0");
    public JLabel lblStatDonHang = new JLabel("0");
    public JLabel lblStatDoanhThu = new JLabel("0 đ");
    public JLabel lblStatKhachHang = new JLabel("0");

    // COLORS MATCHING VENUS MOCKUP
    private final Color COLOR_PRIMARY = new Color(74, 38, 235); // #4A26EB Indigo Blue
    private final Color COLOR_BG = new Color(244, 247, 254); // #F4F7FE Light body background
    private final Color COLOR_TEXT_MUTED = new Color(160, 174, 192); // Gray textual items

    public MainFrame() {
        thietKeGiaoDien();
        taoMenuSanPhamDropdown();
        taoMenuHeThongDropdown();
    }

    private void thietKeGiaoDien() {
        setTitle("PNC Dashboard");
        setSize(1450, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. SIDEBAR (MENU BÊN TRÁI)
        pnlMenu = new JPanel();
        pnlMenu.setBackground(Color.WHITE); 
        pnlMenu.setPreferredSize(new Dimension(280, 0));
        pnlMenu.setLayout(new BorderLayout()); 
        pnlMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230))); 

        // LOGO AREA
        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 40));
        pnlLogo.setBackground(Color.WHITE);
        
        // Vẽ logo tải từ tài nguyên
        JLabel lblIcon = new JLabel();
        try {
            java.net.URL url = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/PNC.png");
            if (url != null) {
                ImageIcon originalIcon = new ImageIcon(url);
                Image scaledImg = originalIcon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
                lblIcon.setIcon(new ImageIcon(scaledImg));
            } else {
                lblIcon.setText("✦");
            }
        } catch (Exception e) {}
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcon.setPreferredSize(new Dimension(50, 50));

        JLabel lblLogoText = new JLabel("PNC STORE"); 
        lblLogoText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogoText.setForeground(new Color(43, 54, 116)); 
        
        pnlLogo.add(lblIcon);
        pnlLogo.add(lblLogoText);
        
        pnlMenu.add(pnlLogo, BorderLayout.NORTH);

        // MENU BUTTONS CONTAINER
        JPanel pnlMenuBtns = new JPanel();
        pnlMenuBtns.setLayout(new BoxLayout(pnlMenuBtns, BoxLayout.Y_AXIS));
        pnlMenuBtns.setBackground(Color.WHITE);
        pnlMenuBtns.setBorder(new EmptyBorder(10, 20, 20, 20)); // Padding cho các nút

        btnTrangChu  = taoNutMenu("Tổng Quan", true); 
        btnBanHang   = taoNutMenu("Bán Hàng", false); 
        btnSanPham   = taoNutMenu("Sản Phẩm", false);
        btnKho       = taoNutMenu("Quản Lý Kho", false); 
        btnKhachHang = taoNutMenu("Khách Hàng", false);
        btnGiamGia   = taoNutMenu("Khuyến Mãi", false);
        btnHoaDon    = taoNutMenu("Hóa Đơn", false);
        // btnThongKe   = taoNutMenu("Thống Kê", false); // Bỏ comment ra nếu muốn dùng lại
        btnHeThong   = taoNutMenu("Hệ Thống", false);
        
        addButtonToPanel(pnlMenuBtns, btnTrangChu);
        addButtonToPanel(pnlMenuBtns, btnBanHang);
        addButtonToPanel(pnlMenuBtns, btnSanPham);
        addButtonToPanel(pnlMenuBtns, btnKho);
        addButtonToPanel(pnlMenuBtns, btnKhachHang);
        addButtonToPanel(pnlMenuBtns, btnGiamGia);
        addButtonToPanel(pnlMenuBtns, btnHoaDon);
        // addButtonToPanel(pnlMenuBtns, btnThongKe); // Đã comment nút Thống Kê
        addButtonToPanel(pnlMenuBtns, btnHeThong);
        
        pnlMenu.add(pnlMenuBtns, BorderLayout.CENTER);
        
        // LOGOUT AREA
        JPanel pnlLogout = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        pnlLogout.setBackground(Color.WHITE);
        btnDangXuat = taoNutMenu("Đăng Xuất", false);
        pnlLogout.add(btnDangXuat);
        pnlMenu.add(pnlLogout, BorderLayout.SOUTH);

        add(pnlMenu, BorderLayout.WEST);

        // 2. MAIN CONTENT AREA
        pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(COLOR_BG); 
        add(pnlContent, BorderLayout.CENTER);
    }
    
    private void addButtonToPanel(JPanel pnl, JButton btn) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(btn, BorderLayout.CENTER);
        wrapper.setMaximumSize(new Dimension(280, 50));
        
        pnl.add(wrapper);
        pnl.add(Box.createVerticalStrut(10)); // Khoảng cách giữa các nút
    }

    private void taoMenuHeThongDropdown() {
        popupHeThong = new JPopupMenu();
        popupHeThong.setBackground(Color.WHITE);
        popupHeThong.setBorder(BorderFactory.createLineBorder(new Color(230,230,230)));
        
        menuNhanVien = new JMenuItem("   Quản Lý Nhân Sự   ");
        menuTaiKhoan = new JMenuItem("   Thông Tin Tài Khoản   ");
        
        Font fontMenu = new Font("Segoe UI", Font.PLAIN, 14);
        styleMenuItem(menuNhanVien, fontMenu);
        styleMenuItem(menuTaiKhoan, fontMenu);
        
        popupHeThong.add(menuNhanVien);
        popupHeThong.add(menuTaiKhoan);
        
        btnHeThong.addActionListener(e -> popupHeThong.show(btnHeThong, 0, btnHeThong.getHeight()));
    }

    private void taoMenuSanPhamDropdown() {
        popupSanPham = new JPopupMenu();
        popupSanPham.setBorder(BorderFactory.createLineBorder(new Color(230,230,230)));
        popupSanPham.setBackground(Color.WHITE);

        menuQuanLySP = new JMenuItem("   Danh Sách Sản Phẩm   ");
        menuDanhMuc = new JMenuItem("   Danh Mục   ");
        menuThuongHieu = new JMenuItem("   Thương Hiệu   ");
        menuThuocTinh = new JMenuItem("   Thuộc Tính   ");

        Font fontMenu = new Font("Segoe UI", Font.PLAIN, 14);
        styleMenuItem(menuQuanLySP, fontMenu);
        styleMenuItem(menuDanhMuc, fontMenu);
        styleMenuItem(menuThuongHieu, fontMenu);
        styleMenuItem(menuThuocTinh, fontMenu);

        popupSanPham.add(menuQuanLySP);
        popupSanPham.addSeparator(); 
        popupSanPham.add(menuDanhMuc);
        popupSanPham.add(menuThuongHieu);
        popupSanPham.add(menuThuocTinh);

        btnSanPham.addActionListener(e -> popupSanPham.show(btnSanPham, 0, btnSanPham.getHeight()));
    }

    private void styleMenuItem(JMenuItem item, Font font) {
        item.setFont(font);
        item.setBackground(Color.WHITE);
        item.setForeground(new Color(113, 128, 150));
        item.setPreferredSize(new Dimension(220, 40));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Custom Button Class to draw rounded background
    class SidebarButton extends JButton {
        private boolean isActive = false;
        
        public SidebarButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }
        
        public void setActive(boolean a) {
            this.isActive = a;
            repaint();
        }
        
        public boolean isActive() { return isActive; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (isActive) {
                g2.setColor(COLOR_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(245, 247, 250));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
            super.paintComponent(g);
        }
    }

    private SidebarButton taoNutMenu(String text, boolean isActive) {
        SidebarButton btn = new SidebarButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT); 
        btn.setBorder(new EmptyBorder(12, 20, 12, 20)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setActive(isActive);
        
        if (isActive) {
            btn.setForeground(Color.WHITE);
        } else {
            btn.setForeground(COLOR_TEXT_MUTED);
        }
        
        // Add fake icon indicator for styling (Venus style)
        btn.setIconTextGap(15);
        JLabel iconSubstitute = new JLabel("✦");
        iconSubstitute.setForeground(isActive ? Color.WHITE : COLOR_TEXT_MUTED);
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { 
                if (!btn.isActive()) btn.setForeground(COLOR_PRIMARY);
            }
            @Override
            public void mouseExited(MouseEvent evt) { 
                if (!btn.isActive()) btn.setForeground(COLOR_TEXT_MUTED);
            }
        });
        
        return btn;
    }

    // --- NAVIGATION HELPERS ---
    public void showPanel(JPanel panel) {
        pnlContent.removeAll();
        pnlContent.add(panel, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }
    
    public void setActiveButton(JButton activeBtn) {
        resetButton((SidebarButton)btnTrangChu); 
        resetButton((SidebarButton)btnBanHang); 
        resetButton((SidebarButton)btnSanPham);
        resetButton((SidebarButton)btnKho); 
        resetButton((SidebarButton)btnKhachHang); 
        resetButton((SidebarButton)btnGiamGia); 
        resetButton((SidebarButton)btnHoaDon);
        // resetButton((SidebarButton)btnThongKe); 
        resetButton((SidebarButton)btnHeThong); 
        
        if (activeBtn != null && activeBtn instanceof SidebarButton) {
            SidebarButton sb = (SidebarButton) activeBtn;
            sb.setActive(true);
            sb.setForeground(Color.WHITE);
        }
    }
    
    private void resetButton(SidebarButton btn) {
        btn.setActive(false);
        btn.setForeground(COLOR_TEXT_MUTED); 
    }
    
    // --- GETTERS CHO CONTROLLER ---
    public JButton getBtnTrangChu() { return btnTrangChu; }
    public JButton getBtnBanHang() { return btnBanHang; }
    public JButton getBtnSanPham() { return btnSanPham; }
    public JButton getBtnKho() { return btnKho; } 
    public JButton getBtnKhachHang() { return btnKhachHang; }
    public JButton getBtnGiamGia() { return btnGiamGia; }
    public JButton getBtnHoaDon() { return btnHoaDon; }
    public JButton getBtnThongKe() { return btnThongKe; }
    public JButton getBtnHeThong() { return btnHeThong; }
    public JButton getBtnDangXuat() { return btnDangXuat; }
    public JButton getBtnNhanVien() { return null; } 

    public JMenuItem getMenuQuanLySP() { return menuQuanLySP; }
    public JMenuItem getMenuDanhMuc() { return menuDanhMuc; }
    public JMenuItem getMenuThuongHieu() { return menuThuongHieu; }
    public JMenuItem getMenuThuocTinh() { return menuThuocTinh; }
    public JMenuItem getMenuNhanVien() { return menuNhanVien; }
    public JMenuItem getMenuTaiKhoan() { return menuTaiKhoan; }
    
    public void setLblUserInfo(String text) {
        // Ignored in new mockup as we use "Welcome to Venus!" style header,
        // but keeping it safe if controller calls it.
    }
}
