package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.DanhMuc;
import ban_dien_thoai_nhiem_vu.model.ThuongHieu;
import ban_dien_thoai_nhiem_vu.model.ThuocTinh;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class QuanLySanPhamFrame extends JFrame {
    
    // --- [QUAN TRỌNG] ĐỂ PUBLIC ĐỂ CONTROLLER GỌI ĐƯỢC ---
    public JTextField txtMaSP, txtTenSP, txtGiaNhap, txtGiaBan, txtTonKho;
    public JTextArea txtMoTa;
    public JComboBox<DanhMuc> cboDanhMuc;     
    public JComboBox<ThuongHieu> cboThuongHieu; 
    public JLabel lblHinhAnh;
    public JButton btnChonAnh;
    
    // Thông số kỹ thuật
    public JTextField txtManHinh, txtCamSau, txtCamTruoc, txtChip, txtPin, txtHDH;
    public JPanel pnlThuocTinhDong; 
    public Map<String, JComboBox<ThuocTinh>> dynamicCombos = new HashMap<>();

    // Buttons
    private JButton btnLuu, btnHuy, btnMoFormThem; 
    public JButton btnNhapHang; 

    // Layout
    private CardLayout cardLayout;
    private JPanel pnlMainContainer;
    private JPanel pnlDanhSach, pnlFormNhap;
    private JTable tblSanPham;
    private DefaultTableModel tableModel;
    private JTabbedPane tabForm;

    public interface TableActionEvent {
        void onEdit(int row);
        void onDelete(int row);
    }

    public QuanLySanPhamFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Quản Lý Sản Phẩm - PNC Store");
        setSize(1250, 800);
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

    // --- 1. MÀN HÌNH DANH SÁCH ---
    private void taoGiaoDienDanhSach() {
        pnlDanhSach = new JPanel(new BorderLayout());
        pnlDanhSach.setBackground(new Color(245, 245, 250)); 
        
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("Danh sách sản phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);

        btnNhapHang = new JButton("📦 NHẬP HÀNG");
        btnNhapHang.setBackground(new Color(40, 167, 69)); 
        btnNhapHang.setForeground(Color.WHITE);
        btnNhapHang.setPreferredSize(new Dimension(150, 40));
        
        btnMoFormThem = new JButton("+ Thêm SP Mới");
        btnMoFormThem.setBackground(new Color(67, 94, 190)); 
        btnMoFormThem.setForeground(Color.WHITE);
        btnMoFormThem.setPreferredSize(new Dimension(180, 40));
        
        pnlButtons.add(btnNhapHang);
        pnlButtons.add(btnMoFormThem);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlButtons, BorderLayout.EAST);
        pnlDanhSach.add(pnlHeader, BorderLayout.NORTH);
        
        String[] headers = {"Mã SP", "Tên Sản Phẩm", "Thương Hiệu", "Giá Bán", "Tồn Kho", "Ảnh (Ẩn)", "Hành Động"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column == 6; }
        };
        
        tblSanPham = new JTable(tableModel);
        tblSanPham.setRowHeight(50); 
        
        tblSanPham.getColumnModel().getColumn(5).setMinWidth(0);
        tblSanPham.getColumnModel().getColumn(5).setMaxWidth(0);
        tblSanPham.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        tblSanPham.getColumnModel().getColumn(6).setPreferredWidth(120);

        pnlDanhSach.add(new JScrollPane(tblSanPham), BorderLayout.CENTER);
    }
    
    // --- 2. FORM NHẬP LIỆU ---
    private void taoGiaoDienFormNhap() {
        pnlFormNhap = new JPanel(new BorderLayout());
        pnlFormNhap.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("  Thông tin sản phẩm", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setPreferredSize(new Dimension(0, 60));
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
        
        g.gridx=0; g.gridy=0; pnl.add(new JLabel("Mã SP (*):"), g);
        g.gridx=1; g.weightx=1.0; txtMaSP = new JTextField(); pnl.add(txtMaSP, g);
        
        g.gridx=0; g.gridy=1; g.weightx=0; pnl.add(new JLabel("Tên SP (*):"), g);
        g.gridx=1; txtTenSP = new JTextField(); pnl.add(txtTenSP, g);

        g.gridx=0; g.gridy=2; pnl.add(new JLabel("Danh Mục:"), g);
        g.gridx=1; cboDanhMuc = new JComboBox<>(); pnl.add(cboDanhMuc, g);

        g.gridx=0; g.gridy=3; pnl.add(new JLabel("Thương Hiệu:"), g);
        g.gridx=1; cboThuongHieu = new JComboBox<>(); pnl.add(cboThuongHieu, g);
        
        g.gridx=0; g.gridy=4; pnl.add(new JLabel("Giá Nhập:"), g);
        g.gridx=1; txtGiaNhap = new JTextField(); pnl.add(txtGiaNhap, g);
        
        g.gridx=0; g.gridy=5; pnl.add(new JLabel("Giá Bán:"), g);
        g.gridx=1; txtGiaBan = new JTextField(); pnl.add(txtGiaBan, g);

        g.gridx=0; g.gridy=6; pnl.add(new JLabel("Tồn Kho:"), g);
        g.gridx=1; txtTonKho = new JTextField(); pnl.add(txtTonKho, g);
        
        g.gridx=0; g.gridy=7; g.anchor = GridBagConstraints.NORTHWEST;
        pnl.add(new JLabel("Mô tả:"), g);
        g.gridx=1; g.ipady=60; 
        txtMoTa = new JTextArea(); txtMoTa.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnl.add(new JScrollPane(txtMoTa), g);
        g.ipady=0;

        g.gridx=2; g.gridy=0; g.gridheight=6; g.weightx=0.5;
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
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));

        // A. Cấu hình cứng
        JPanel pnlCung = new JPanel(new GridLayout(3, 2, 20, 15));
        pnlCung.setBackground(Color.WHITE);
        pnlCung.setBorder(new TitledBorder("A. Cấu hình phần cứng (Nhập tay)"));
        pnlCung.add(createInput("Chip xử lý (CPU):", txtChip = new JTextField()));
        pnlCung.add(createInput("Màn hình:", txtManHinh = new JTextField()));
        pnlCung.add(createInput("Camera Sau:", txtCamSau = new JTextField()));
        pnlCung.add(createInput("Camera Trước:", txtCamTruoc = new JTextField()));
        pnlCung.add(createInput("Dung lượng Pin:", txtPin = new JTextField()));
        pnlCung.add(createInput("Hệ điều hành:", txtHDH = new JTextField()));

        // B. Biến thể
        JPanel pnlDongWrapper = new JPanel(new BorderLayout());
        pnlDongWrapper.setBackground(Color.WHITE);
        pnlDongWrapper.setBorder(new TitledBorder("B. Biến thể sản phẩm (Tự động tải từ 'Quản lý Thuộc tính')"));
        pnlThuocTinhDong = new JPanel(new GridLayout(0, 3, 15, 15));
        pnlThuocTinhDong.setBackground(Color.WHITE);
        pnlDongWrapper.add(pnlThuocTinhDong, BorderLayout.CENTER);

        pnl.add(pnlCung, BorderLayout.NORTH);
        pnl.add(new JScrollPane(pnlDongWrapper), BorderLayout.CENTER);
        return pnl;
    }
    
    private JPanel createInput(String title, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(title); lbl.setForeground(Color.GRAY);
        p.add(lbl, BorderLayout.NORTH); p.add(txt, BorderLayout.CENTER);
        return p;
    }

    // --- Helpers ---
    public void addDynamicAttribute(String groupName, DefaultComboBoxModel<ThuocTinh> model) {
        JLabel lbl = new JLabel(groupName + ":");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JComboBox<ThuocTinh> cbo = new JComboBox<>(model);
        cbo.setPreferredSize(new Dimension(150, 35));
        JPanel pItem = new JPanel(new BorderLayout(0, 5));
        pItem.setBackground(Color.WHITE);
        pItem.add(lbl, BorderLayout.NORTH); pItem.add(cbo, BorderLayout.CENTER);
        pnlThuocTinhDong.add(pItem);
        dynamicCombos.put(groupName, cbo);
    }
    
    public void clearDynamicAttributes() {
        pnlThuocTinhDong.removeAll(); dynamicCombos.clear();
        pnlThuocTinhDong.revalidate(); pnlThuocTinhDong.repaint();
    }
    
    public void hienThiAnh(String path) {
        if (path == null || path.isEmpty()) { lblHinhAnh.setIcon(null); lblHinhAnh.setText("No Image"); return; }
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            lblHinhAnh.setIcon(new ImageIcon(img)); lblHinhAnh.setText("");
        } catch (Exception e) { lblHinhAnh.setText("Err"); }
    }
    
    public void clearForm() {
        txtMaSP.setText(""); txtTenSP.setText(""); txtGiaNhap.setText(""); txtGiaBan.setText(""); txtTonKho.setText("");
        txtMoTa.setText(""); hienThiAnh(null);
        txtChip.setText(""); txtManHinh.setText(""); txtCamSau.setText(""); txtCamTruoc.setText(""); txtPin.setText(""); txtHDH.setText("");
        if(cboDanhMuc.getItemCount()>0) cboDanhMuc.setSelectedIndex(0);
        if(cboThuongHieu.getItemCount()>0) cboThuongHieu.setSelectedIndex(0);
        clearDynamicAttributes();
        tabForm.setSelectedIndex(0);
        txtMaSP.setEditable(true);
    }
    
    public class PanelAction extends JPanel {
        public JButton cmdEdit, cmdDelete;
        public PanelAction() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 8)); setBackground(Color.WHITE);
            cmdEdit = new JButton("Sửa"); cmdEdit.setBackground(new Color(23, 162, 184)); cmdEdit.setForeground(Color.WHITE);
            cmdDelete = new JButton("Xóa"); cmdDelete.setBackground(new Color(220, 53, 69)); cmdDelete.setForeground(Color.WHITE);
            add(cmdEdit); add(cmdDelete);
        }
    }
    public class TableActionCellRender extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            PanelAction action = new PanelAction();
            if (isSelected) action.setBackground(table.getSelectionBackground()); else action.setBackground(Color.WHITE);
            return action;
        }
    }
    public class TableActionCellEditor extends DefaultCellEditor {
        TableActionEvent event;
        public TableActionCellEditor(TableActionEvent event) { super(new JCheckBox()); this.event = event; }
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            PanelAction action = new PanelAction();
            action.setBackground(table.getSelectionBackground());
            action.cmdEdit.addActionListener(e -> { event.onEdit(row); fireEditingStopped(); });
            action.cmdDelete.addActionListener(e -> { event.onDelete(row); fireEditingStopped(); });
            return action;
        }
    }
    
    // Getters
    public JButton getBtnMoFormThem() { return btnMoFormThem; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public void setTableActionEvent(TableActionEvent event) { tblSanPham.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event)); }
    public void addLuuListener(ActionListener l) { btnLuu.addActionListener(l); }
    public void addChonAnhListener(ActionListener l) { btnChonAnh.addActionListener(l); }
    public void addNhapHangListener(ActionListener l) { btnNhapHang.addActionListener(l); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public int showConfirm(String msg) { return JOptionPane.showConfirmDialog(this, msg, "Xác nhận", JOptionPane.YES_NO_OPTION); }
    public void showFormNhap() { cardLayout.show(pnlMainContainer, "FORM_NHAP"); }
    public void showDanhSach() { cardLayout.show(pnlMainContainer, "DANH_SACH"); }
}