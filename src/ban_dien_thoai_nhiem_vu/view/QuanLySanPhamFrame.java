package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.SanPham;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class QuanLySanPhamFrame extends JFrame {
    
    // --- KHAI BÁO LINH KIỆN ---
    private CardLayout cardLayout;
    private JPanel pnlMainContainer; // Panel chính chứa CardLayout
    
    // >> GIAO DIỆN 1: DANH SÁCH SẢN PHẨM
    private JPanel pnlDanhSach;
    private JTable tblSanPham;
    private DefaultTableModel tableModel;
    private JButton btnMoFormThem; // Nút "Thêm Sản Phẩm Mới" (+ New)
    
    // >> GIAO DIỆN 2: FORM THÊM/SỬA (TAB)
    private JPanel pnlFormNhap;
    private JTabbedPane tabForm;
    
    // Tab 1: Thông tin chung
    public JTextField txtMaSP, txtTenSP, txtHangSX, txtGiaNhap, txtGiaBan, txtTonKho;
    public JTextArea txtMoTa;
    public JLabel lblHinhAnh;
    public JButton btnChonAnh;
    
    // Tab 2: Thông số kỹ thuật (Mới thêm)
    public JTextField txtManHinh, txtCamSau, txtCamTruoc, txtChip, txtRam, txtRom, txtPin, txtHDH;
    
    // Nút hành động form
    private JButton btnLuu, btnHuy;

    // Biến lưu đường dẫn ảnh
    private String duongDanAnh = "";

    public QuanLySanPhamFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Quản Lý Sản Phẩm - PNC Store Admin");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Dùng CardLayout để chuyển đổi giữa "Xem DS" và "Thêm Mới"
        cardLayout = new CardLayout();
        pnlMainContainer = new JPanel(cardLayout);
        
        // 1. Tạo giao diện Danh sách (Giống ảnh 1)
        taoGiaoDienDanhSach();
        
        // 2. Tạo giao diện Form nhập liệu (Giống ảnh 2, 3)
        taoGiaoDienFormNhap();
        
        // Thêm 2 màn hình vào Card
        pnlMainContainer.add(pnlDanhSach, "DANH_SACH");
        pnlMainContainer.add(pnlFormNhap, "FORM_NHAP");
        
        add(pnlMainContainer);
    }

    // =========================================================================
    // PHẦN 1: THIẾT KẾ MÀN HÌNH DANH SÁCH (TABLE)
    // =========================================================================
    private void taoGiaoDienDanhSach() {
        pnlDanhSach = new JPanel(new BorderLayout());
        pnlDanhSach.setBackground(Color.WHITE);
        
        // --- Header: Tiêu đề + Nút Thêm Mới ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("Quản lý Sản phẩm");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(50, 50, 50));
        
        // Nút Thêm Mới màu tím giống ảnh
        btnMoFormThem = new JButton("+ Thêm Sản Phẩm Mới");
        btnMoFormThem.setBackground(new Color(102, 102, 255)); // Màu tím xanh
        btnMoFormThem.setForeground(Color.WHITE);
        btnMoFormThem.setFont(new Font("Arial", Font.BOLD, 14));
        btnMoFormThem.setPreferredSize(new Dimension(200, 40));
        btnMoFormThem.setFocusPainted(false);
        
        // Sự kiện: Bấm nút này -> Chuyển sang Card FORM_NHAP
        btnMoFormThem.addActionListener(e -> {
            clearForm(); // Xóa trắng form trước khi thêm
            cardLayout.show(pnlMainContainer, "FORM_NHAP");
        });

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(btnMoFormThem, BorderLayout.EAST);
        
        pnlDanhSach.add(pnlHeader, BorderLayout.NORTH);
        
        // --- Table Danh sách ---
        String[] headers = {"Mã SP", "Tên Sản Phẩm", "Thương Hiệu", "Giá Bán", "Tồn Kho", "Ảnh (Ẩn)"};
        tableModel = new DefaultTableModel(headers, 0);
        tblSanPham = new JTable(tableModel);
        tblSanPham.setRowHeight(40); // Hàng cao thoáng
        tblSanPham.setFont(new Font("Arial", Font.PLAIN, 14));
        tblSanPham.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblSanPham.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Ẩn cột ảnh
        tblSanPham.getColumnModel().getColumn(5).setMinWidth(0);
        tblSanPham.getColumnModel().getColumn(5).setMaxWidth(0);
        
        JScrollPane sc = new JScrollPane(tblSanPham);
        sc.setBorder(new EmptyBorder(0, 20, 20, 20)); // Cách lề
        pnlDanhSach.add(sc, BorderLayout.CENTER);
    }

    // =========================================================================
    // PHẦN 2: THIẾT KẾ MÀN HÌNH FORM NHẬP (TABBED PANE)
    // =========================================================================
    private void taoGiaoDienFormNhap() {
        pnlFormNhap = new JPanel(new BorderLayout());
        
        // Header Form
        JLabel lblTitle = new JLabel("  Thêm Sản phẩm mới", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setPreferredSize(new Dimension(0, 60));
        lblTitle.setBorder(new EmptyBorder(0, 20, 0, 0));
        pnlFormNhap.add(lblTitle, BorderLayout.NORTH);
        
        // --- TẠO TAB ---
        tabForm = new JTabbedPane();
        tabForm.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Tab 1: Thông tin chung (Giống ảnh 2)
        JPanel pnlTab1 = taoTabThongTinChung();
        tabForm.addTab("1. Thông tin chung", pnlTab1);
        
        // Tab 2: Thông số kỹ thuật (Giống ảnh 3)
        JPanel pnlTab2 = taoTabThongSoKyThuat();
        tabForm.addTab("2. Thông số kỹ thuật", pnlTab2);
        
        pnlFormNhap.add(tabForm, BorderLayout.CENTER);
        
        // --- Footer: Nút Lưu / Hủy ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnlFooter.setPreferredSize(new Dimension(0, 60));
        
        btnHuy = new JButton("Hủy Bỏ");
        btnHuy.setPreferredSize(new Dimension(100, 40));
        btnHuy.addActionListener(e -> cardLayout.show(pnlMainContainer, "DANH_SACH")); // Quay về
        
        btnLuu = new JButton("Lưu & Hoàn Tất");
        btnLuu.setBackground(new Color(102, 102, 255));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 14));
        btnLuu.setPreferredSize(new Dimension(160, 40));
        
        pnlFooter.add(btnHuy);
        pnlFooter.add(btnLuu);
        pnlFormNhap.add(pnlFooter, BorderLayout.SOUTH);
    }
    
    // --- Thiết kế Tab 1: Thông tin cơ bản ---
    private JPanel taoTabThongTinChung() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        
        // Cột 1: Các ô nhập
        g.gridx = 0; g.gridy = 0; pnl.add(new JLabel("Mã Sản Phẩm (*):"), g);
        g.gridx = 1; g.weightx = 1.0; txtMaSP = new JTextField(); pnl.add(txtMaSP, g);
        
        g.gridx = 0; g.gridy = 1; g.weightx = 0; pnl.add(new JLabel("Tên Sản Phẩm (*):"), g);
        g.gridx = 1; pnl.add(new JLabel("(Ví dụ: Samsung Galaxy S24 Ultra)"), g); // Label gợi ý
        g.gridx = 1; g.gridy = 2; txtTenSP = new JTextField(); pnl.add(txtTenSP, g);

        g.gridx = 0; g.gridy = 3; pnl.add(new JLabel("Thương hiệu:"), g);
        g.gridx = 1; txtHangSX = new JTextField(); pnl.add(txtHangSX, g);
        
        g.gridx = 0; g.gridy = 4; pnl.add(new JLabel("Giá Nhập:"), g);
        g.gridx = 1; txtGiaNhap = new JTextField(); pnl.add(txtGiaNhap, g);
        
        g.gridx = 0; g.gridy = 5; pnl.add(new JLabel("Giá Bán:"), g);
        g.gridx = 1; txtGiaBan = new JTextField(); pnl.add(txtGiaBan, g);

        g.gridx = 0; g.gridy = 6; pnl.add(new JLabel("Tồn Kho:"), g);
        g.gridx = 1; txtTonKho = new JTextField(); pnl.add(txtTonKho, g);
        
        // Mô tả sản phẩm (TextArea to)
        g.gridx = 0; g.gridy = 7; g.anchor = GridBagConstraints.NORTHWEST;
        pnl.add(new JLabel("Mô tả sản phẩm:"), g);
        
        g.gridx = 1; g.ipady = 60; // Tăng chiều cao
        txtMoTa = new JTextArea(); 
        txtMoTa.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnl.add(new JScrollPane(txtMoTa), g);
        g.ipady = 0; // Reset

        // Cột 2: Ảnh (Bên phải)
        g.gridx = 2; g.gridy = 0; g.gridheight = 6; g.weightx = 0.5;
        JPanel pnlAnh = new JPanel(new BorderLayout());
        pnlAnh.setBorder(new TitledBorder("Hình Ảnh"));
        lblHinhAnh = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblHinhAnh.setPreferredSize(new Dimension(200, 200));
        lblHinhAnh.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        btnChonAnh = new JButton("Chọn Ảnh...");
        pnlAnh.add(lblHinhAnh, BorderLayout.CENTER);
        pnlAnh.add(btnChonAnh, BorderLayout.SOUTH);
        
        pnl.add(pnlAnh, g);

        return pnl;
    }
    
    // --- Thiết kế Tab 2: Thông số kỹ thuật ---
    private JPanel taoTabThongSoKyThuat() {
        JPanel pnl = new JPanel(new GridLayout(4, 2, 20, 20)); // Grid 2 cột
        pnl.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        pnl.add(taoInputThongSo("Kích thước màn hình", txtManHinh = new JTextField()));
        pnl.add(taoInputThongSo("Hệ điều hành", txtHDH = new JTextField()));
        pnl.add(taoInputThongSo("Camera Sau", txtCamSau = new JTextField()));
        pnl.add(taoInputThongSo("Camera Trước", txtCamTruoc = new JTextField()));
        pnl.add(taoInputThongSo("Chip (CPU)", txtChip = new JTextField()));
        pnl.add(taoInputThongSo("RAM (Dung lượng)", txtRam = new JTextField()));
        pnl.add(taoInputThongSo("Bộ nhớ trong (ROM)", txtRom = new JTextField()));
        pnl.add(taoInputThongSo("Dung lượng Pin", txtPin = new JTextField()));
        
        return pnl;
    }
    
    // Hàm phụ trợ tạo 1 ô nhập liệu có tiêu đề bên trên (giống ảnh 3)
    private JPanel taoInputThongSo(String title, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(Color.GRAY);
        p.add(lbl, BorderLayout.NORTH);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    // =========================================================================
    // CÁC HÀM GETTER / SETTER VÀ SỰ KIỆN CHO CONTROLLER
    // =========================================================================
    
    // Hàm lấy dữ liệu từ Form (Gồm cả Tab 1 và Tab 2)
    public SanPham getSanPhamInput() {
        try {
            SanPham sp = new SanPham();
            // Tab 1
            sp.setMaSP(txtMaSP.getText());
            sp.setTenSP(txtTenSP.getText());
            sp.setHangSanXuat(txtHangSX.getText());
            sp.setGiaNhap(Double.parseDouble(txtGiaNhap.getText()));
            sp.setGiaBan(Double.parseDouble(txtGiaBan.getText()));
            sp.setTonKho(Integer.parseInt(txtTonKho.getText()));
            sp.setHinhAnh(duongDanAnh);
            sp.setMoTa(txtMoTa.getText());
            
            // Tab 2: Thông số kỹ thuật (Đã bỏ comment)
            sp.setManHinh(txtManHinh.getText());
            sp.setHeDieuHanh(txtHDH.getText());
            sp.setCameraSau(txtCamSau.getText());
            sp.setCameraTruoc(txtCamTruoc.getText());
            sp.setChip(txtChip.getText());
            sp.setRam(txtRam.getText());
            sp.setRom(txtRom.getText());
            sp.setPin(txtPin.getText());
            
            return sp;
        } catch (Exception e) {
            return null;
        }
    }
    
    public void hienThiAnh(String path) {
        if (path == null || path.isEmpty()) {
            lblHinhAnh.setIcon(null); lblHinhAnh.setText("Chưa có ảnh"); return;
        }
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            lblHinhAnh.setIcon(new ImageIcon(img));
            lblHinhAnh.setText("");
            duongDanAnh = path;
        } catch (Exception e) { lblHinhAnh.setText("Lỗi ảnh"); }
    }
    
    public void clearForm() {
        txtMaSP.setText(""); txtTenSP.setText(""); txtHangSX.setText("");
        txtGiaNhap.setText(""); txtGiaBan.setText(""); txtTonKho.setText("");
        txtManHinh.setText(""); txtChip.setText(""); // ... xóa các ô khác
        hienThiAnh(null);
        tabForm.setSelectedIndex(0); // Về tab 1
    }

    // Gắn sự kiện
    public void addLuuListener(ActionListener l) { btnLuu.addActionListener(l); }
    public void addChonAnhListener(ActionListener l) { btnChonAnh.addActionListener(l); }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getTable() { return tblSanPham; }
    public void addTableMouseListener(MouseListener l) { tblSanPham.addMouseListener(l); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    
    // Hàm chuyển về danh sách (dùng cho Controller sau khi lưu xong)
    public void showDanhSach() { cardLayout.show(pnlMainContainer, "DANH_SACH"); }
}