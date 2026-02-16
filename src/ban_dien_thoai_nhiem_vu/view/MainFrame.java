package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class MainFrame extends JFrame {
    
    // --- CÁC NÚT MENU (SIDEBAR) ---
    private JButton btnTrangChu;
    private JButton btnBanHang;
    private JButton btnSanPham; // Nút này sẽ hiện Popup Menu
    private JButton btnKho; 
    private JButton btnKhachHang;
    private JButton btnGiamGia;
    private JButton btnHoaDon;
    private JButton btnThongKe;
    private JButton btnHeThong; // Nút Hệ thống
    private JButton btnDangXuat;

    // --- POPUP MENU CHO SẢN PHẨM ---
    private JPopupMenu popupSanPham;
    private JMenuItem menuQuanLySP;
    private JMenuItem menuDanhMuc;
    private JMenuItem menuThuongHieu;
    private JMenuItem menuThuocTinh;

    // --- POPUP MENU CHO HỆ THỐNG ---
    private JPopupMenu popupHeThong;
    private JMenuItem menuNhanVien;
    private JMenuItem menuTaiKhoan;

    // --- PANEL CHỨA NỘI DUNG ---
    private JPanel pnlContent;
    private JPanel pnlMenu;
    
    // --- LABEL HIỂN THỊ THÔNG TIN USER ---
    private JLabel lblUserInfo; 

    // --- CÁC NHÃN HIỂN THỊ SỐ LIỆU (Dashboard) ---
    public JLabel lblStatSanPham = new JLabel("0");
    public JLabel lblStatDonHang = new JLabel("0");
    public JLabel lblStatDoanhThu = new JLabel("0 đ");
    public JLabel lblStatKhachHang = new JLabel("0");

    public MainFrame() {
        thietKeGiaoDien();
        taoMenuSanPhamDropdown(); // Tạo menu con cho nút Sản Phẩm
        taoMenuHeThongDropdown(); // Tạo menu con cho nút Hệ thống
    }

    private void thietKeGiaoDien() {
        setTitle("HỆ THỐNG QUẢN LÝ ĐIỆN THOẠI - PNC STORE");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. MENU BÊN TRÁI (SIDEBAR)
        pnlMenu = new JPanel();
        pnlMenu.setBackground(Color.WHITE); 
        pnlMenu.setPreferredSize(new Dimension(260, 0));
        pnlMenu.setLayout(new BorderLayout()); 
        pnlMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(240, 240, 240))); 

        // --- Logo Area ---
        JPanel pnlLogo = new JPanel(new BorderLayout());
        pnlLogo.setBackground(Color.WHITE);
        pnlLogo.setBorder(new EmptyBorder(30, 0, 30, 0));
        
        JLabel lblLogoText = new JLabel("PNC STORE", SwingConstants.CENTER); 
        lblLogoText.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblLogoText.setForeground(new Color(41, 98, 255)); 
        
        // Label hiển thị tên user (Controller sẽ set text vào đây)
        lblUserInfo = new JLabel("Xin chào, ...", SwingConstants.CENTER);
        lblUserInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblUserInfo.setForeground(Color.GRAY);
        
        pnlLogo.add(lblLogoText, BorderLayout.CENTER);
        pnlLogo.add(lblUserInfo, BorderLayout.SOUTH);
        
        pnlMenu.add(pnlLogo, BorderLayout.NORTH);

        // --- Menu Buttons Container ---
        JPanel pnlMenuBtns = new JPanel();
        pnlMenuBtns.setLayout(new BoxLayout(pnlMenuBtns, BoxLayout.Y_AXIS));
        pnlMenuBtns.setBackground(Color.WHITE);
        pnlMenuBtns.setBorder(new EmptyBorder(10, 10, 20, 10));

        // Khởi tạo các nút
        btnTrangChu  = taoNutMenu("Dashboard", "home.png", true); 
        btnBanHang   = taoNutMenu("Bán Hàng", "cart.png", false); 
        btnSanPham   = taoNutMenu("Sản Phẩm ▼", "phone.png", false);
        btnKho       = taoNutMenu("Quản Lý Kho", "warehouse.png", false); 
        btnKhachHang = taoNutMenu("Khách Hàng", "user.png", false);
        btnGiamGia   = taoNutMenu("Voucher", "voucher.png", false);
        btnHoaDon    = taoNutMenu("Hóa Đơn", "bill.png", false);
        btnThongKe   = taoNutMenu("Thống Kê", "chart.png", false);
        btnHeThong   = taoNutMenu("Hệ thống ▼", "setting.png", false);
        btnDangXuat  = taoNutMenu("Đăng Xuất", "logout.png", false);

        // Thêm vào Panel (Có khoảng cách)
        addButtonToPanel(pnlMenuBtns, btnTrangChu);
        addButtonToPanel(pnlMenuBtns, btnBanHang);
        addButtonToPanel(pnlMenuBtns, btnSanPham);
        addButtonToPanel(pnlMenuBtns, btnKho);
        addButtonToPanel(pnlMenuBtns, btnKhachHang);
        addButtonToPanel(pnlMenuBtns, btnGiamGia);
        addButtonToPanel(pnlMenuBtns, btnHoaDon);
        addButtonToPanel(pnlMenuBtns, btnThongKe);
        addButtonToPanel(pnlMenuBtns, btnHeThong);
        
        pnlMenu.add(pnlMenuBtns, BorderLayout.CENTER);
        
        // Logout Area
        JPanel pnlLogout = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLogout.setBackground(Color.WHITE);
        pnlLogout.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlLogout.add(btnDangXuat);
        pnlMenu.add(pnlLogout, BorderLayout.SOUTH);

        add(pnlMenu, BorderLayout.WEST);

        // 2. NỘI DUNG CHÍNH (CONTENT AREA)
        pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(new Color(245, 247, 250)); 
        add(pnlContent, BorderLayout.CENTER);
    }
    
    // Hàm phụ để add nút và khoảng cách cho gọn code
    private void addButtonToPanel(JPanel pnl, JButton btn) {
        pnl.add(btn);
        pnl.add(Box.createVerticalStrut(10));
    }

    private void taoMenuHeThongDropdown() {
        popupHeThong = new JPopupMenu();
        popupHeThong.setBackground(Color.WHITE);
        popupHeThong.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        menuNhanVien = new JMenuItem("   Quản lý Nhân sự   ");
        menuTaiKhoan = new JMenuItem("   Thông tin Tài khoản   ");
        
        Font fontMenu = new Font("Segoe UI", Font.PLAIN, 14);
        styleMenuItem(menuNhanVien, fontMenu);
        styleMenuItem(menuTaiKhoan, fontMenu);
        
        popupHeThong.add(menuNhanVien);
        popupHeThong.add(menuTaiKhoan);
        
        btnHeThong.addActionListener(e -> popupHeThong.show(btnHeThong, 0, btnHeThong.getHeight()));
    }

    private void taoMenuSanPhamDropdown() {
        popupSanPham = new JPopupMenu();
        popupSanPham.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        popupSanPham.setBackground(Color.WHITE);

        // Tạo các Item con
        menuQuanLySP = new JMenuItem("   Danh sách Sản phẩm   ");
        menuDanhMuc = new JMenuItem("   Quản lý Danh mục   ");
        menuThuongHieu = new JMenuItem("   Quản lý Thương hiệu   ");
        menuThuocTinh = new JMenuItem("   Quản lý Thuộc tính   ");

        // Style
        Font fontMenu = new Font("Segoe UI", Font.PLAIN, 14);
        styleMenuItem(menuQuanLySP, fontMenu);
        styleMenuItem(menuDanhMuc, fontMenu);
        styleMenuItem(menuThuongHieu, fontMenu);
        styleMenuItem(menuThuocTinh, fontMenu);

        // Thêm vào Popup
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
        item.setPreferredSize(new Dimension(220, 35));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JButton taoNutMenu(String text, String iconName, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        
        if (isActive) {
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(41, 98, 255)); 
        } else {
            btn.setForeground(new Color(120, 120, 120)); 
            btn.setBackground(Color.WHITE);
        }
        
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20)); 
        btn.setHorizontalAlignment(SwingConstants.LEFT); 
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setIconTextGap(15);
        btn.setMaximumSize(new Dimension(240, 50));
        
        if (!isActive) btn.setBorder(null);

        // Load Icon an toàn
        try {
             // Đường dẫn icon (Đảm bảo folder icons nằm đúng chỗ)
             URL iconURL = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/" + iconName);
             if (iconURL != null) {
                 ImageIcon icon = new ImageIcon(iconURL);
                 Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                 btn.setIcon(new ImageIcon(img));
             }
        } catch (Exception e) {}
        
        // Hover Effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { 
                // Chỉ đổi màu khi nút đó KHÔNG phải là nút đang Active (Màu xanh)
                if (!btn.getBackground().equals(new Color(41, 98, 255))) { 
                    btn.setBackground(new Color(245, 247, 250));
                    btn.setForeground(new Color(41, 98, 255));
                }
            }
            @Override
            public void mouseExited(MouseEvent evt) { 
                // Khi chuột rời đi, nếu không phải nút Active thì trả về màu trắng
                if (!btn.getBackground().equals(new Color(41, 98, 255))) { 
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(new Color(120, 120, 120));
                }
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
        resetButton(btnTrangChu); 
        resetButton(btnBanHang); 
        resetButton(btnSanPham);
        resetButton(btnKho); 
        resetButton(btnKhachHang); 
        resetButton(btnGiamGia); 
        resetButton(btnHoaDon);
        resetButton(btnThongKe); 
        resetButton(btnHeThong); // Đừng quên nút Hệ thống
        
        if (activeBtn != null) {
            activeBtn.setBackground(new Color(41, 98, 255));
            activeBtn.setForeground(Color.WHITE);
        }
    }
    
    private void resetButton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(100, 100, 100)); 
        btn.setBorder(null);
    }
    
    // --- GETTERS CHO CONTROLLER (BẮT BUỘC PHẢI CÓ) ---
    
    // 1. Buttons
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
    
    // Lưu ý: Controller có thể hỏi btnNhanVien, nhưng ta để nó trong menu con.
    // Ta trả về null để Controller không bị lỗi biên dịch, nhưng Controller sẽ dùng getMenuNhanVien()
    public JButton getBtnNhanVien() { return null; } 

    // 2. Menu Items
    public JMenuItem getMenuQuanLySP() { return menuQuanLySP; }
    public JMenuItem getMenuDanhMuc() { return menuDanhMuc; }
    public JMenuItem getMenuThuongHieu() { return menuThuongHieu; }
    public JMenuItem getMenuThuocTinh() { return menuThuocTinh; }
    public JMenuItem getMenuNhanVien() { return menuNhanVien; }
    public JMenuItem getMenuTaiKhoan() { return menuTaiKhoan; }
    
    // 3. User Info
    public void setLblUserInfo(String text) {
        if (lblUserInfo != null) lblUserInfo.setText(text);
    }
}