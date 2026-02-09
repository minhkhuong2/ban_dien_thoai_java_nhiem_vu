package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.SanPham;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class QuanLySanPhamFrame extends JFrame {
    
    // Interface giao tiếp
    public interface TableActionEvent {
        void onEdit(int row);
        void onDelete(int row);
    }

    // --- LINH KIỆN ---
    private CardLayout cardLayout;
    private JPanel pnlMainContainer;
    
    // Giao diện Danh sách
    private JPanel pnlDanhSach;
    private JTable tblSanPham;
    private DefaultTableModel tableModel;
    private JButton btnMoFormThem; 
    
    // Giao diện Form nhập
    private JPanel pnlFormNhap;
    private JTabbedPane tabForm;
    public JTextField txtMaSP, txtTenSP, txtHangSX, txtGiaNhap, txtGiaBan, txtTonKho;
    public JTextArea txtMoTa;
    public JLabel lblHinhAnh;
    public JButton btnChonAnh;
    public JTextField txtManHinh, txtCamSau, txtCamTruoc, txtChip, txtRam, txtRom, txtPin, txtHDH;
    private JButton btnLuu, btnHuy;

    private String duongDanAnh = "";

    public QuanLySanPhamFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Quản Lý Sản Phẩm - PNC Store Admin");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        pnlMainContainer = new JPanel(cardLayout);
        
        taoGiaoDienDanhSach();
        taoGiaoDienFormNhap();
        
        pnlMainContainer.add(pnlDanhSach, "DANH_SACH");
        pnlMainContainer.add(pnlFormNhap, "FORM_NHAP");
        
        add(pnlMainContainer);
    }

    // =========================================================================
    // 1. MÀN HÌNH DANH SÁCH
    // =========================================================================
    private void taoGiaoDienDanhSach() {
        pnlDanhSach = new JPanel(new BorderLayout());
        pnlDanhSach.setBackground(new Color(245, 245, 250)); 
        
        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("Danh sách sản phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(50, 50, 50));
        
        btnMoFormThem = new JButton("+ Thêm Sản Phẩm Mới");
        btnMoFormThem.setBackground(new Color(67, 94, 190)); 
        btnMoFormThem.setForeground(Color.WHITE);
        btnMoFormThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMoFormThem.setPreferredSize(new Dimension(200, 40));
        btnMoFormThem.setFocusPainted(false);
        
        btnMoFormThem.addActionListener(e -> {
            clearForm();
            cardLayout.show(pnlMainContainer, "FORM_NHAP");
        });

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(btnMoFormThem, BorderLayout.EAST);
        pnlDanhSach.add(pnlHeader, BorderLayout.NORTH);
        
        // Table
        String[] headers = {"Mã SP", "Tên Sản Phẩm", "Thương Hiệu", "Giá Bán", "Tồn Kho", "Ảnh (Ẩn)", "Hành Động"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override 
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Chỉ cột Hành động mới click được
            }
        };
        
        tblSanPham = new JTable(tableModel);
        tblSanPham.setRowHeight(50); 
        tblSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblSanPham.getTableHeader().setBackground(Color.WHITE);
        
        // Ẩn cột ảnh
        tblSanPham.getColumnModel().getColumn(5).setMinWidth(0);
        tblSanPham.getColumnModel().getColumn(5).setMaxWidth(0);

        // Cấu hình cột Hành Động
        tblSanPham.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        tblSanPham.getColumnModel().getColumn(6).setPreferredWidth(120); // Mở rộng cột này ra tí cho nút đỡ bị chật

        JScrollPane sc = new JScrollPane(tblSanPham);
        sc.setBorder(new EmptyBorder(0, 20, 20, 20));
        sc.getViewport().setBackground(Color.WHITE);
        pnlDanhSach.add(sc, BorderLayout.CENTER);
    }
    
    // --- CLASS VẼ NÚT BẤM (ĐÃ NÂNG CẤP DÙNG ICON) ---
    public class PanelAction extends JPanel {
        public JButton cmdEdit;
        public JButton cmdDelete;

        public PanelAction() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 8)); // Canh giữa
            setBackground(Color.WHITE);

            // Nút Sửa
            cmdEdit = new JButton();
            setupButton(cmdEdit, "edit.png", new Color(23, 162, 184), "Sửa");

            // Nút Xóa
            cmdDelete = new JButton();
            setupButton(cmdDelete, "delete.png", new Color(220, 53, 69), "Xóa");

            add(cmdEdit);
            add(cmdDelete);
        }

        // Hàm hỗ trợ nạp icon, nếu không có icon thì hiện chữ
        private void setupButton(JButton btn, String iconName, Color bg, String fallbackText) {
            btn.setPreferredSize(new Dimension(32, 32)); // Kích thước nút vuông
            btn.setBackground(bg);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder()); // Bỏ viền lồi
            
            try {
                // Thử tìm ảnh trong thư mục icons
                URL url = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/" + iconName);
                if (url != null) {
                    ImageIcon icon = new ImageIcon(url);
                    // Scale ảnh nhỏ lại vừa nút (16x16)
                    Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(img));
                } else {
                    // Không có ảnh -> Hiện chữ (Sửa/Xóa)
                    btn.setText(fallbackText);
                    btn.setForeground(Color.WHITE);
                    btn.setFont(new Font("Arial", Font.BOLD, 10));
                    btn.setMargin(new Insets(0,0,0,0));
                }
            } catch (Exception e) {
                btn.setText(fallbackText);
            }
        }
    }

    // --- RENDERER ---
    public class TableActionCellRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            PanelAction action = new PanelAction();
            if (isSelected) action.setBackground(table.getSelectionBackground());
            else action.setBackground(Color.WHITE);
            return action;
        }
    }

    // --- EDITOR ---
    public class TableActionCellEditor extends DefaultCellEditor {
        private TableActionEvent event;
        public TableActionCellEditor(TableActionEvent event) {
            super(new JCheckBox());
            this.event = event;
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            PanelAction action = new PanelAction();
            action.setBackground(table.getSelectionBackground());
            
            action.cmdEdit.addActionListener(e -> {
                event.onEdit(row);
                fireEditingStopped();
            });
            action.cmdDelete.addActionListener(e -> {
                event.onDelete(row);
                fireEditingStopped();
            });
            return action;
        }
    }

    public void setTableActionEvent(TableActionEvent event) {
        tblSanPham.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event));
    }

    // =========================================================================
    // 2. MÀN HÌNH FORM NHẬP (GIỮ NGUYÊN)
    // =========================================================================
    private void taoGiaoDienFormNhap() {
        pnlFormNhap = new JPanel(new BorderLayout());
        pnlFormNhap.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("  Thông tin sản phẩm", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setPreferredSize(new Dimension(0, 60));
        lblTitle.setBorder(new EmptyBorder(0, 20, 0, 0));
        pnlFormNhap.add(lblTitle, BorderLayout.NORTH);
        
        tabForm = new JTabbedPane();
        tabForm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabForm.addTab("1. Thông tin chung", taoTabThongTinChung());
        tabForm.addTab("2. Thông số kỹ thuật", taoTabThongSoKyThuat());
        
        pnlFormNhap.add(tabForm, BorderLayout.CENTER);
        
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        pnlFooter.setBackground(new Color(245, 245, 250));
        
        btnHuy = new JButton("Quay lại danh sách");
        btnHuy.setPreferredSize(new Dimension(150, 40));
        btnHuy.addActionListener(e -> cardLayout.show(pnlMainContainer, "DANH_SACH"));
        
        btnLuu = new JButton("Lưu & Hoàn Tất");
        btnLuu.setBackground(new Color(67, 94, 190));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setPreferredSize(new Dimension(160, 40));
        
        pnlFooter.add(btnHuy);
        pnlFooter.add(btnLuu);
        pnlFormNhap.add(pnlFooter, BorderLayout.SOUTH);
    }
    
    private JPanel taoTabThongTinChung() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        
        g.gridx = 0; g.gridy = 0; pnl.add(new JLabel("Mã Sản Phẩm (*):"), g);
        g.gridx = 1; g.weightx = 1.0; txtMaSP = new JTextField(); pnl.add(txtMaSP, g);
        
        g.gridx = 0; g.gridy = 1; g.weightx = 0; pnl.add(new JLabel("Tên Sản Phẩm (*):"), g);
        g.gridx = 1; txtTenSP = new JTextField(); pnl.add(txtTenSP, g);

        g.gridx = 0; g.gridy = 3; pnl.add(new JLabel("Thương hiệu:"), g);
        g.gridx = 1; txtHangSX = new JTextField(); pnl.add(txtHangSX, g);
        
        g.gridx = 0; g.gridy = 4; pnl.add(new JLabel("Giá Nhập:"), g);
        g.gridx = 1; txtGiaNhap = new JTextField(); pnl.add(txtGiaNhap, g);
        
        g.gridx = 0; g.gridy = 5; pnl.add(new JLabel("Giá Bán:"), g);
        g.gridx = 1; txtGiaBan = new JTextField(); pnl.add(txtGiaBan, g);

        g.gridx = 0; g.gridy = 6; pnl.add(new JLabel("Tồn Kho:"), g);
        g.gridx = 1; txtTonKho = new JTextField(); pnl.add(txtTonKho, g);
        
        g.gridx = 0; g.gridy = 7; g.anchor = GridBagConstraints.NORTHWEST;
        pnl.add(new JLabel("Mô tả:"), g);
        g.gridx = 1; g.ipady = 60; 
        txtMoTa = new JTextArea(); txtMoTa.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnl.add(new JScrollPane(txtMoTa), g);
        g.ipady = 0;

        g.gridx = 2; g.gridy = 0; g.gridheight = 6; g.weightx = 0.5;
        JPanel pnlAnh = new JPanel(new BorderLayout());
        pnlAnh.setBackground(Color.WHITE);
        pnlAnh.setBorder(new TitledBorder("Hình Ảnh"));
        lblHinhAnh = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblHinhAnh.setPreferredSize(new Dimension(180, 180));
        lblHinhAnh.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btnChonAnh = new JButton("Chọn Ảnh...");
        pnlAnh.add(lblHinhAnh, BorderLayout.CENTER);
        pnlAnh.add(btnChonAnh, BorderLayout.SOUTH);
        pnl.add(pnlAnh, g);

        return pnl;
    }
    
    private JPanel taoTabThongSoKyThuat() {
        JPanel pnl = new JPanel(new GridLayout(4, 2, 20, 20));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnl.add(taoInputThongSo("Kích thước màn hình", txtManHinh = new JTextField()));
        pnl.add(taoInputThongSo("Hệ điều hành", txtHDH = new JTextField()));
        pnl.add(taoInputThongSo("Camera Sau", txtCamSau = new JTextField()));
        pnl.add(taoInputThongSo("Camera Trước", txtCamTruoc = new JTextField()));
        pnl.add(taoInputThongSo("Chip (CPU)", txtChip = new JTextField()));
        pnl.add(taoInputThongSo("RAM", txtRam = new JTextField()));
        pnl.add(taoInputThongSo("Bộ nhớ trong (ROM)", txtRom = new JTextField()));
        pnl.add(taoInputThongSo("Dung lượng Pin", txtPin = new JTextField()));
        return pnl;
    }
    
    private JPanel taoInputThongSo(String title, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(title);
        lbl.setForeground(Color.GRAY);
        p.add(lbl, BorderLayout.NORTH);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    public SanPham getSanPhamInput() {
        try {
            SanPham sp = new SanPham();
            sp.setMaSP(txtMaSP.getText());
            sp.setTenSP(txtTenSP.getText());
            sp.setHangSanXuat(txtHangSX.getText());
            sp.setGiaNhap(Double.parseDouble(txtGiaNhap.getText()));
            sp.setGiaBan(Double.parseDouble(txtGiaBan.getText()));
            sp.setTonKho(Integer.parseInt(txtTonKho.getText()));
            sp.setHinhAnh(duongDanAnh);
            sp.setMoTa(txtMoTa.getText());
            sp.setManHinh(txtManHinh.getText());
            sp.setHeDieuHanh(txtHDH.getText());
            sp.setCameraSau(txtCamSau.getText());
            sp.setCameraTruoc(txtCamTruoc.getText());
            sp.setChip(txtChip.getText());
            sp.setRam(txtRam.getText());
            sp.setRom(txtRom.getText());
            sp.setPin(txtPin.getText());
            return sp;
        } catch (Exception e) { return null; }
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
        txtMoTa.setText(""); txtManHinh.setText(""); txtChip.setText(""); txtRam.setText(""); txtRom.setText(""); txtPin.setText(""); txtCamSau.setText(""); txtCamTruoc.setText(""); txtHDH.setText("");
        hienThiAnh(null);
        tabForm.setSelectedIndex(0);
        txtMaSP.setEditable(true);
    }

    public void showFormNhap() { cardLayout.show(pnlMainContainer, "FORM_NHAP"); }
    public void showDanhSach() { cardLayout.show(pnlMainContainer, "DANH_SACH"); }
    public void addLuuListener(ActionListener l) { btnLuu.addActionListener(l); }
    public void addChonAnhListener(ActionListener l) { btnChonAnh.addActionListener(l); }
    public DefaultTableModel getTableModel() { return tableModel; }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public int showConfirm(String msg) { return JOptionPane.showConfirmDialog(this, msg, "Xác nhận", JOptionPane.YES_NO_OPTION); }
}