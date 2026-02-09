package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFrame extends JFrame {
    
    // Các nút Menu
    private JButton btnTrangChu, btnBanHang, btnSanPham, btnKhachHang, btnGiamGia, btnHoaDon, btnThongKe, btnDangXuat;
    private JPanel pnlContent;

    // --- CÁC NHÃN HIỂN THỊ SỐ LIỆU (ĐỂ PUBLIC HOẶC CÓ GETTER ĐỂ CONTROLLER GỌI) ---
    public JLabel lblStatSanPham;
    public JLabel lblStatDonHang;
    public JLabel lblStatDoanhThu;
    public JLabel lblStatKhachHang;

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

        // 1. MENU BÊN TRÁI
        JPanel pnlMenu = new JPanel();
        pnlMenu.setBackground(new Color(33, 41, 54)); 
        pnlMenu.setPreferredSize(new Dimension(260, 0));
        pnlMenu.setLayout(new GridLayout(10, 1, 0, 0)); 

        // Logo
        JPanel pnlLogo = new JPanel(new BorderLayout());
        pnlLogo.setBackground(new Color(33, 41, 54));
        JLabel lblLogo = new JLabel("PNC STORE", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setBorder(new EmptyBorder(30, 0, 30, 0));
        pnlLogo.add(lblLogo, BorderLayout.CENTER);
        pnlMenu.add(pnlLogo);

        // Menu buttons
        btnTrangChu = taoNutMenu("Trang Chủ", "home.png");
        btnBanHang  = taoNutMenu("Bán Hàng (POS)", "cart.png");
        btnSanPham  = taoNutMenu("Quản Lý Sản Phẩm", "phone.png");
        btnKhachHang = taoNutMenu("Quản Lý Khách Hàng", "user.png");
        btnGiamGia  = taoNutMenu("Quản Lý Voucher", "voucher.png");
        btnHoaDon   = taoNutMenu("Lịch Sử Hóa Đơn", "bill.png");
        btnThongKe  = taoNutMenu("Thống Kê Doanh Thu", "chart.png");
        btnDangXuat = taoNutMenu("Đăng Xuất", "logout.png");

        pnlMenu.add(btnTrangChu); pnlMenu.add(btnBanHang); pnlMenu.add(btnSanPham);
        pnlMenu.add(btnKhachHang); pnlMenu.add(btnGiamGia); pnlMenu.add(btnHoaDon);
        pnlMenu.add(btnThongKe); pnlMenu.add(new JLabel()); pnlMenu.add(btnDangXuat);

        add(pnlMenu, BorderLayout.WEST);

        // 2. NỘI DUNG CHÍNH
        pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(new Color(240, 242, 245)); 
        pnlContent.add(taoGiaoDienTrangChu(), BorderLayout.CENTER);
        add(pnlContent, BorderLayout.CENTER);
    }

    private JPanel taoGiaoDienTrangChu() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBackground(new Color(240, 242, 245));
        pnl.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        JLabel lblHello = new JLabel("Xin chào, Admin! Hệ thống đã sẵn sàng.");
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHello.setForeground(new Color(50, 50, 50));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JLabel lblDate = new JLabel("Hôm nay: " + sdf.format(new Date()));
        lblDate.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblDate.setForeground(Color.GRAY);
        pnlHeader.add(lblHello, BorderLayout.CENTER);
        pnlHeader.add(lblDate, BorderLayout.SOUTH);
        pnl.add(pnlHeader, BorderLayout.NORTH);

        // --- KHỞI TẠO CÁC LABEL SỐ LIỆU ---
        lblStatSanPham = new JLabel("Dang tai...");
        lblStatDonHang = new JLabel("Dang tai...");
        lblStatDoanhThu = new JLabel("Dang tai...");
        lblStatKhachHang = new JLabel("Dang tai...");
        
        // Cấu hình font chung cho số liệu
        Font fontSo = new Font("Arial", Font.BOLD, 36);
        lblStatSanPham.setFont(fontSo); lblStatSanPham.setForeground(Color.WHITE);
        lblStatDonHang.setFont(fontSo); lblStatDonHang.setForeground(Color.WHITE);
        lblStatDoanhThu.setFont(fontSo); lblStatDoanhThu.setForeground(Color.WHITE);
        lblStatKhachHang.setFont(fontSo); lblStatKhachHang.setForeground(Color.WHITE);

        // Cards thống kê
        JPanel pnlStats = new JPanel(new GridLayout(1, 4, 20, 0));
        pnlStats.setOpaque(false);
        pnlStats.setPreferredSize(new Dimension(0, 180)); 

        // Truyền biến Label vào hàm tạo thẻ
        pnlStats.add(taoTheThongKe("SẢN PHẨM", lblStatSanPham, "phone.png", new Color(67, 94, 190))); 
        pnlStats.add(taoTheThongKe("ĐƠN HÀNG", lblStatDonHang, "cart.png", new Color(40, 167, 69)));  
        pnlStats.add(taoTheThongKe("DOANH THU", lblStatDoanhThu, "bill.png", new Color(255, 193, 7))); 
        pnlStats.add(taoTheThongKe("KHÁCH HÀNG", lblStatKhachHang, "user.png", new Color(23, 162, 184))); 

        pnl.add(pnlStats, BorderLayout.CENTER);

        // Footer
        JLabel lblBackground = new JLabel("HỆ THỐNG QUẢN LÝ CHUYÊN NGHIỆP", SwingConstants.CENTER);
        lblBackground.setFont(new Font("Arial", Font.BOLD, 40));
        lblBackground.setForeground(new Color(220, 220, 220)); 
        pnl.add(lblBackground, BorderLayout.SOUTH);

        return pnl;
    }

    // Sửa hàm này để nhận JLabel thay vì String value
    private JPanel taoTheThongKe(String title, JLabel lblValue, String iconName, Color bg) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bg);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(new Color(255, 255, 255, 200));
        
        JLabel lblIcon = new JLabel();
        try {
            URL url = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/" + iconName);
            if(url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                lblIcon.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {}

        JPanel pLeft = new JPanel(new GridLayout(2, 1));
        pLeft.setOpaque(false);
        pLeft.add(lblTitle);
        pLeft.add(lblValue); // Add cái Label biến toàn cục vào đây
        
        p.add(pLeft, BorderLayout.CENTER);
        p.add(lblIcon, BorderLayout.EAST);
        return p;
    }

    private JButton taoNutMenu(String text, String iconName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(new Color(200, 200, 200));
        btn.setBackground(new Color(33, 41, 54));    
        btn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 0)); 
        btn.setHorizontalAlignment(SwingConstants.LEFT); 
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setIconTextGap(15); 
        try {
            URL iconURL = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/" + iconName);
            if (iconURL != null) btn.setIcon(new ImageIcon(new ImageIcon(iconURL).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        } catch (Exception e) {}
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(55, 65, 81)); btn.setForeground(Color.WHITE); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(33, 41, 54)); btn.setForeground(new Color(200, 200, 200)); }
        });
        return btn;
    }
    private void phanQuyen() {
        // Lấy thông tin người đang đăng nhập
        ban_dien_thoai_nhiem_vu.model.NhanVien nv = ban_dien_thoai_nhiem_vu.model.TaiKhoanSession.taiKhoanHienTai;
        
        if (nv != null) {
            // Nếu KHÔNG phải Admin (tức là Nhân viên)
            if (!nv.getVaiTro().equalsIgnoreCase("ADMIN")) {
                // Ẩn các chức năng quản lý nhạy cảm
                btnSanPham.setVisible(false);  // Không cho sửa kho
                btnThongKe.setVisible(false);  // Không cho xem doanh thu
                btnGiamGia.setVisible(false);  // Không cho tạo mã giảm giá lung tung
                
                // (Tùy chọn) Ẩn luôn nút Trang chủ nếu muốn họ vào thẳng Bán hàng
                // btnTrangChu.setVisible(false);
            }
        }
    }

    // Events
    public void addBanHangListener(ActionListener l) { btnBanHang.addActionListener(l); }
    public void addSanPhamListener(ActionListener l) { btnSanPham.addActionListener(l); }
    public void addKhachHangListener(ActionListener l) { btnKhachHang.addActionListener(l); }
    public void addGiamGiaListener(ActionListener l) { btnGiamGia.addActionListener(l); }
    public void addHoaDonListener(ActionListener l) { btnHoaDon.addActionListener(l); }
    public void addThongKeListener(ActionListener l) { btnThongKe.addActionListener(l); }
    public void addDangXuatListener(ActionListener l) { btnDangXuat.addActionListener(l); }
}