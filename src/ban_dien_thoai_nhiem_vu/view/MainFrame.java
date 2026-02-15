package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.model.TaiKhoanSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class MainFrame extends JFrame {
    
    // --- CÁC NÚT MENU ---
    private JButton btnTrangChu;
    private JButton btnBanHang;
    private JButton btnSanPham; // Nút này sẽ hiện Popup Menu
    private JButton btnKho; 
    private JButton btnKhachHang;
    private JButton btnGiamGia;
    private JButton btnHoaDon;
    private JButton btnThongKe;
    private JButton btnDangXuat;

    // --- POPUP MENU CHO SẢN PHẨM ---
    private JPopupMenu popupSanPham;
    private JMenuItem menuQuanLySP;
    private JMenuItem menuDanhMuc;
    private JMenuItem menuThuongHieu;
    private JMenuItem menuThuocTinh;

    private JPanel pnlContent;
    private JPanel pnlMenu;
    
    // --- CÁC NHÃN HIỂN THỊ SỐ LIỆU (Để Controller cập nhật) ---
    public JLabel lblStatSanPham = new JLabel("0");
    public JLabel lblStatDonHang = new JLabel("0");
    public JLabel lblStatDoanhThu = new JLabel("0 đ");
    public JLabel lblStatKhachHang = new JLabel("0");
    private JButton btnHeThong; // Nút Hệ thống (Admin Only)
private JPopupMenu popupHeThong;
private JMenuItem menuNhanVien, menuTaiKhoan;

    public MainFrame() {
        thietKeGiaoDien();
        taoMenuSanPhamDropdown(); // Tạo menu con cho nút Sản Phẩm
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
        pnlMenu.setBackground(Color.WHITE); 
        pnlMenu.setPreferredSize(new Dimension(260, 0));
        pnlMenu.setLayout(new BorderLayout()); 
        pnlMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(240, 240, 240))); 

        // Logo Area
        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        pnlLogo.setBackground(Color.WHITE);
        
        JLabel lblLogoText = new JLabel("PNC STORE"); 
        lblLogoText.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblLogoText.setForeground(new Color(41, 98, 255)); 
        
        pnlLogo.add(lblLogoText);
        pnlMenu.add(pnlLogo, BorderLayout.NORTH);

        // Menu Buttons Container
        JPanel pnlMenuBtns = new JPanel();
        pnlMenuBtns.setLayout(new BoxLayout(pnlMenuBtns, BoxLayout.Y_AXIS));
        pnlMenuBtns.setBackground(Color.WHITE);
        pnlMenuBtns.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Khởi tạo các nút
        btnTrangChu  = taoNutMenu("Dashboard", "home.png", true); 
        btnBanHang   = taoNutMenu("Bán Hàng", "cart.png", false); 
        btnSanPham   = taoNutMenu("Sản Phẩm ▼", "phone.png", false); // Thêm mũi tên
        btnKho       = taoNutMenu("Quản Lý Kho", "warehouse.png", false); 
        btnKhachHang = taoNutMenu("Khách Hàng", "user.png", false);
        btnGiamGia   = taoNutMenu("Voucher", "voucher.png", false);
        btnHoaDon    = taoNutMenu("Hóa Đơn", "bill.png", false);
        btnThongKe   = taoNutMenu("Thống Kê", "chart.png", false);
        btnDangXuat  = taoNutMenu("Đăng Xuất", "logout.png", false);

        // Thêm vào Panel (Có khoảng cách)
        pnlMenuBtns.add(btnTrangChu);  pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnBanHang);   pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnSanPham);   pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnKho);       pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnKhachHang); pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnGiamGia);   pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnHoaDon);    pnlMenuBtns.add(Box.createVerticalStrut(10));
        pnlMenuBtns.add(btnThongKe); 
        
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
        
        // Mặc định load Dashboard ngay khi mở
        TrangChuPanel homePanel = new TrangChuPanel(lblStatDoanhThu, lblStatDonHang, lblStatKhachHang);
        pnlContent.add(homePanel, BorderLayout.CENTER);
        
        add(pnlContent, BorderLayout.CENTER);
        
        btnHeThong = taoNutMenu("Hệ thống ▼", "setting.png", false);
        pnlMenuBtns.add(btnHeThong);
        taoMenuHeThongDropdown();
    }
    
    
    private void taoMenuHeThongDropdown() {
    popupHeThong = new JPopupMenu();
    popupHeThong.setBackground(Color.WHITE);
    
    menuNhanVien = new JMenuItem("   Quản lý Nhân sự   ");
    menuTaiKhoan = new JMenuItem("   Thông tin Tài khoản   ");
    
    Font fontMenu = new Font("Segoe UI", Font.PLAIN, 14);
    menuNhanVien.setFont(fontMenu); menuNhanVien.setBackground(Color.WHITE);
    menuTaiKhoan.setFont(fontMenu); menuTaiKhoan.setBackground(Color.WHITE);
    
    popupHeThong.add(menuNhanVien);
    popupHeThong.add(menuTaiKhoan);
    
    btnHeThong.addActionListener(e -> popupHeThong.show(btnHeThong, 0, btnHeThong.getHeight()));
}

    // --- HÀM TẠO MENU DROPDOWN CHO SẢN PHẨM ---
    private void taoMenuSanPhamDropdown() {
        popupSanPham = new JPopupMenu();
        popupSanPham.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        popupSanPham.setBackground(Color.WHITE);

        // Tạo các Item con
        menuQuanLySP = new JMenuItem("   Danh sách Sản phẩm   ");
        menuDanhMuc = new JMenuItem("   Quản lý Danh mục   ");
        menuThuongHieu = new JMenuItem("   Quản lý Thương hiệu   ");
        menuThuocTinh = new JMenuItem("   Quản lý Thuộc tính   ");

        // Style cho đẹp
        Font fontMenu = new Font("Segoe UI", Font.PLAIN, 14);
        styleMenuItem(menuQuanLySP, fontMenu);
        styleMenuItem(menuDanhMuc, fontMenu);
        styleMenuItem(menuThuongHieu, fontMenu);
        styleMenuItem(menuThuocTinh, fontMenu);

        // Thêm vào Popup
        popupSanPham.add(menuQuanLySP);
        popupSanPham.addSeparator(); // Đường kẻ ngang
        popupSanPham.add(menuDanhMuc);
        popupSanPham.add(menuThuongHieu);
        popupSanPham.add(menuThuocTinh);

        // Sự kiện: Bấm nút Sản Phẩm -> Hiện Popup ngay bên dưới
        btnSanPham.addActionListener(e -> {
            popupSanPham.show(btnSanPham, 0, btnSanPham.getHeight());
        });
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

        try {
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
                if (btn.getBackground().equals(Color.WHITE)) { 
                    btn.setBackground(new Color(245, 247, 250));
                    btn.setForeground(new Color(41, 98, 255));
                }
            }
            @Override
            public void mouseExited(MouseEvent evt) { 
                if (!btn.getBackground().equals(new Color(41, 98, 255))) { 
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(new Color(120, 120, 120));
                }
            }
        });
        return btn;
    }
    
    private void phanQuyen() {
    NhanVien nv = TaiKhoanSession.taiKhoanHienTai;
    if (nv != null) {
        if (!nv.getVaiTro().equalsIgnoreCase("ADMIN")) {
            // NẾU LÀ NHÂN VIÊN:
            btnSanPham.setVisible(false); // Không được sửa sản phẩm
            btnKho.setVisible(false);     // Không được nhập kho
            btnThongKe.setVisible(false); // Không xem doanh thu
            
            // Ẩn chức năng quản lý nhân sự trong menu Hệ thống
            menuNhanVien.setVisible(false); 
            // Hoặc ẩn luôn nút Hệ thống nếu muốn, nhưng ta để lại để họ đổi mật khẩu
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
        resetButton(btnTrangChu); 
        resetButton(btnBanHang); 
        resetButton(btnSanPham);
        resetButton(btnKho); 
        resetButton(btnKhachHang); 
        resetButton(btnGiamGia); 
        resetButton(btnHoaDon);
        resetButton(btnThongKe); 
        
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

    // --- LISTENERS CHO MAIN CONTROLLER ---
    // Lưu ý: Nút Sản Phẩm giờ dùng Dropdown nên không addListener trực tiếp
    public void addTrangChuListener(ActionListener l) { btnTrangChu.addActionListener(l); }
    public void addBanHangListener(ActionListener l) { btnBanHang.addActionListener(l); }
    
    // Nút Kho
    public void addKhoListener(ActionListener l) { btnKho.addActionListener(l); }
    
    // Các nút khác
    public void addKhachHangListener(ActionListener l) { btnKhachHang.addActionListener(l); }
    public void addGiamGiaListener(ActionListener l) { btnGiamGia.addActionListener(l); }
    public void addHoaDonListener(ActionListener l) { btnHoaDon.addActionListener(l); }
    public void addThongKeListener(ActionListener l) { btnThongKe.addActionListener(l); }
    public void addDangXuatListener(ActionListener l) { btnDangXuat.addActionListener(l); }
    
    // --- GETTERS CHO CONTROLLER ---
    public JButton getBtnTrangChu() { return btnTrangChu; }
    public JButton getBtnBanHang() { return btnBanHang; }
    public JButton getBtnSanPham() { return btnSanPham; }
    public JButton getBtnKho() { return btnKho; } 
    public JButton getBtnKhachHang() { return btnKhachHang; }
    public JButton getBtnGiamGia() { return btnGiamGia; }
    public JButton getBtnHoaDon() { return btnHoaDon; }
    public JButton getBtnThongKe() { return btnThongKe; }
    
    // --- GETTERS CHO MENU DROPDOWN (QUAN TRỌNG) ---
    public JMenuItem getMenuQuanLySP() { return menuQuanLySP; }
    public JMenuItem getMenuDanhMuc() { return menuDanhMuc; }
    public JMenuItem getMenuThuongHieu() { return menuThuongHieu; }
    public JMenuItem getMenuThuocTinh() { return menuThuocTinh; }
    public JMenuItem getMenuNhanVien() { return menuNhanVien; }
    public JMenuItem getMenuTaiKhoan() { return menuTaiKhoan; }
    public JButton getBtnHeThong() { return btnHeThong; }
}