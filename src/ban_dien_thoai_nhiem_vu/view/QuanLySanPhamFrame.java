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
import java.net.URL;
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
    private JScrollPane scrollFormNhap;
    private JTable tblSanPham;
    private DefaultTableModel tableModel;

    // UI Constants
    // private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_SUCCESS = new Color(25, 135, 84);
    private final Color COLOR_WARNING = new Color(255, 193, 7);
    private final Color COLOR_DANGER = new Color(220, 53, 69);
    // private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    // private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public interface TableActionEvent {
        void onEdit(int row);
        void onDelete(int row);
    }

    public QuanLySanPhamFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Quản Lý Sản Phẩm - PNC Store");
        setSize(1350, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // getContentPane().setBackground(COLOR_BG);
        
        cardLayout = new CardLayout();
        pnlMainContainer = new JPanel(cardLayout);
        pnlMainContainer.setOpaque(false);
        pnlMainContainer.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        taoGiaoDienDanhSach();
        taoGiaoDienFormNhap();
        
        pnlMainContainer.add(pnlDanhSach, "DANH_SACH");
        pnlMainContainer.add(pnlFormNhap, "FORM_NHAP");
        add(pnlMainContainer);
    }

    // --- 1. MÀN HÌNH DANH SÁCH ---
    private void taoGiaoDienDanhSach() {
        pnlDanhSach = new JPanel(new BorderLayout());
        pnlDanhSach.setOpaque(false);
        
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel lblTitle = new JLabel("Danh Sách Sản Phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        // lblTitle.setForeground(COLOR_TEXT_DARK);
        
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlButtons.setOpaque(false);

        btnNhapHang = createFlatButton("NHẬP HÀNG", COLOR_PRIMARY, Color.WHITE);
        btnNhapHang.setPreferredSize(new Dimension(160, 42));
        
        btnMoFormThem = createFlatButton("+ Thêm Sản Phẩm Mới", COLOR_SUCCESS, Color.WHITE);
        btnMoFormThem.setPreferredSize(new Dimension(200, 42));
        
        pnlButtons.add(btnNhapHang);
        pnlButtons.add(btnMoFormThem);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlButtons, BorderLayout.EAST);
        pnlDanhSach.add(pnlHeader, BorderLayout.NORTH);
        
        // Bảng
        JPanel pnlTableWrapper = new JPanel(new BorderLayout());
        pnlTableWrapper.setBackground(UIManager.getColor("window"));
        pnlTableWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        String[] headers = {"Mã SP", "Tên Sản Phẩm", "Thương Hiệu", "Giá Bán", "Tồn Kho", "Ảnh (Ẩn)", "Hành Động"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column == 6; }
        };
        
        tblSanPham = new JTable(tableModel);
        tblSanPham.setRowHeight(55); 
        tblSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        // tblSanPham.getTableHeader().setBackground(Color.WHITE);
        // tblSanPham.getTableHeader().setForeground(COLOR_TEXT_MUTED);
        tblSanPham.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_TABLE_BORDER));
        tblSanPham.setShowGrid(false);
        tblSanPham.setIntercellSpacing(new Dimension(0, 0));
        // tblSanPham.setSelectionBackground(new Color(240, 244, 255));
        // tblSanPham.setSelectionForeground(COLOR_TEXT_DARK);
        
        tblSanPham.getColumnModel().getColumn(5).setMinWidth(0);
        tblSanPham.getColumnModel().getColumn(5).setMaxWidth(0);
        tblSanPham.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        tblSanPham.getColumnModel().getColumn(6).setPreferredWidth(120);

        JScrollPane sc = new JScrollPane(tblSanPham);
        sc.setBorder(null);
        // sc.getViewport().setBackground(Color.WHITE);
        pnlTableWrapper.add(sc, BorderLayout.CENTER);

        pnlDanhSach.add(pnlTableWrapper, BorderLayout.CENTER);
    }
    
    // --- 2. FORM NHẬP LIỆU GỘP CHUNG (KHÔNG DÙNG TAB) ---
    private void taoGiaoDienFormNhap() {
        pnlFormNhap = new JPanel(new BorderLayout());
        pnlFormNhap.setOpaque(false);
        
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitle = new JLabel("Thông Tin Chi Tiết Sản Phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        // lblTitle.setForeground(COLOR_TEXT_DARK);
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlFormNhap.add(pnlHeader, BorderLayout.NORTH);
        
        // --- NỘI DUNG CUỘN DỌC ---
        JPanel pnlWrapperForm = new JPanel();
        pnlWrapperForm.setLayout(new BoxLayout(pnlWrapperForm, BoxLayout.Y_AXIS));
        pnlWrapperForm.setOpaque(false);
        pnlWrapperForm.add(taoPanelThongTinChung());
        pnlWrapperForm.add(Box.createVerticalStrut(25));
        pnlWrapperForm.add(taoPanelThongSoKyThuat());
        
        scrollFormNhap = new JScrollPane(pnlWrapperForm);
        scrollFormNhap.setBorder(null);
        scrollFormNhap.setOpaque(false);
        scrollFormNhap.getViewport().setOpaque(false);
        scrollFormNhap.getVerticalScrollBar().setUnitIncrement(16);
        
        pnlFormNhap.add(scrollFormNhap, BorderLayout.CENTER);
        
        // --- NÚT BẤM (FOOTER) ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlFooter.setOpaque(false);
        pnlFooter.setBorder(new EmptyBorder(25, 0, 0, 0));
        
        btnHuy = createFlatButton("Hủy & Quay lại", new Color(240, 240, 240), UIManager.getColor("Label.foreground"));
        btnHuy.setPreferredSize(new Dimension(150, 45));
        btnHuy.addActionListener(e -> cardLayout.show(pnlMainContainer, "DANH_SACH"));
        
        btnLuu = createFlatButton("Lưu Thông Tin", COLOR_PRIMARY, Color.WHITE);
        btnLuu.setPreferredSize(new Dimension(160, 45));
        
        pnlFooter.add(btnHuy);
        pnlFooter.add(btnLuu);
        pnlFormNhap.add(pnlFooter, BorderLayout.SOUTH);
    }
    
    private JPanel taoPanelThongTinChung() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setBackground(UIManager.getColor("window"));
        pnlWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
                " 1. Thông tin cơ bản ", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 18), COLOR_PRIMARY
            ),
            new EmptyBorder(20, 30, 30, 30)
        ));

        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 0, 15, 30);
        g.fill = GridBagConstraints.HORIZONTAL;
        
        g.gridx=0; g.gridy=0; pnl.add(createLabel("Mã Sản Phẩm (*):"), g);
        g.gridx=1; g.weightx=1.0; txtMaSP = createTextField(); pnl.add(txtMaSP, g);
        
        g.gridx=0; g.gridy=1; g.weightx=0; pnl.add(createLabel("Danh Mục:"), g);
        g.gridx=1; cboDanhMuc = new JComboBox<>(); cboDanhMuc.setPreferredSize(new Dimension(0, 40)); pnl.add(cboDanhMuc, g);
        
        g.gridx=0; g.gridy=2; pnl.add(createLabel("Giá Nhập:"), g);
        g.gridx=1; txtGiaNhap = createTextField(); pnl.add(txtGiaNhap, g);

        // Cột 2
        g.gridx=2; g.gridy=0; g.weightx=0; g.insets = new Insets(8, 30, 15, 10); pnl.add(createLabel("Tên Sản Phẩm (*):"), g);
        g.gridx=3; g.weightx=1.0; g.insets = new Insets(8, 0, 15, 0); txtTenSP = createTextField(); pnl.add(txtTenSP, g);
        
        g.gridx=2; g.gridy=1; g.weightx=0; g.insets = new Insets(8, 30, 15, 10); pnl.add(createLabel("Thương Hiệu:"), g);
        g.gridx=3; g.weightx=1.0; g.insets = new Insets(8, 0, 15, 0); cboThuongHieu = new JComboBox<>(); cboThuongHieu.setPreferredSize(new Dimension(0, 40)); pnl.add(cboThuongHieu, g);
        
        g.gridx=2; g.gridy=2; g.weightx=0; g.insets = new Insets(8, 30, 15, 10); pnl.add(createLabel("Giá Bán:"), g);
        g.gridx=3; g.weightx=1.0; g.insets = new Insets(8, 0, 15, 0); txtGiaBan = createTextField(); pnl.add(txtGiaBan, g);

        // Tồn Kho & Ảnh
        g.gridx=0; g.gridy=3; g.weightx=0; g.insets = new Insets(8, 0, 15, 30); pnl.add(createLabel("Tồn Kho (Auto):"), g);
        g.gridx=1; g.weightx=1.0; g.insets = new Insets(8, 0, 15, 0); txtTonKho = createTextField(); txtTonKho.setEditable(false); pnl.add(txtTonKho, g);
        
        g.gridx=0; g.gridy=4; g.weightx=0; g.anchor = GridBagConstraints.NORTHWEST; g.insets = new Insets(8, 0, 15, 30);
        pnl.add(createLabel("Mô tả / Bài viết:"), g);
        g.gridx=1; g.ipady=80; g.gridwidth=3; 
        txtMoTa = new JTextArea(); 
        txtMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtMoTa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        pnl.add(new JScrollPane(txtMoTa), g);
        g.ipady=0; g.gridwidth=1;
        
        // HÌNH ẢNH
        g.gridx=4; g.gridy=0; g.gridheight=5; g.weightx=0; g.insets = new Insets(8, 30, 15, 0);
        JPanel pnlAnh = new JPanel(new BorderLayout(0, 15));
        pnlAnh.setOpaque(false);
        JLabel lblAnhTitle = createLabel("Hình Ảnh");
        lblAnhTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblHinhAnh = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblHinhAnh.setPreferredSize(new Dimension(200, 200));
        lblHinhAnh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // lblHinhAnh.setForeground(COLOR_TEXT_MUTED);
        lblHinhAnh.setBorder(BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1));
        
        btnChonAnh = createFlatButton("Tải Ảnh Lên...", new Color(240, 240, 240), UIManager.getColor("Label.foreground"));
        btnChonAnh.setPreferredSize(new Dimension(0, 40));
        
        pnlAnh.add(lblAnhTitle, BorderLayout.NORTH);
        pnlAnh.add(lblHinhAnh, BorderLayout.CENTER);
        pnlAnh.add(btnChonAnh, BorderLayout.SOUTH);
        pnl.add(pnlAnh, g);

        pnlWrapper.add(pnl, BorderLayout.NORTH);
        return pnlWrapper;
    }
    
    private JPanel taoPanelThongSoKyThuat() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setBackground(UIManager.getColor("window"));
        pnlWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
                " 2. Thông số kỹ thuật & Biến thể ", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 18), COLOR_PRIMARY
            ),
            new EmptyBorder(20, 30, 30, 30)
        ));

        // A. Cấu hình cứng
        JPanel pnlCung = new JPanel(new GridLayout(3, 2, 40, 20)); // Grid 2 columns
        pnlCung.setOpaque(false);
        pnlCung.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        pnlCung.add(taoInputThongSo("Kích thước màn hình:", txtManHinh = createTextField()));
        pnlCung.add(taoInputThongSo("Hệ điều hành:", txtHDH = createTextField()));
        pnlCung.add(taoInputThongSo("Camera Sau:", txtCamSau = createTextField()));
        pnlCung.add(taoInputThongSo("Camera Trước:", txtCamTruoc = createTextField()));
        pnlCung.add(taoInputThongSo("Chip xử lý (CPU):", txtChip = createTextField()));
        pnlCung.add(taoInputThongSo("Dung lượng Pin:", txtPin = createTextField()));

        // B. Biến thể
        JPanel pnlDongWrapper = new JPanel(new BorderLayout());
        pnlDongWrapper.setOpaque(false);
        pnlDongWrapper.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_TABLE_BORDER),
            " Cấu hình tùy biến (Ví dụ: RAM, Màu sắc...) ", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), UIManager.getColor("Label.foreground")
        ));
        
        pnlThuocTinhDong = new JPanel(new GridLayout(0, 3, 20, 20));
        pnlThuocTinhDong.setBackground(UIManager.getColor("window"));
        pnlThuocTinhDong.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        pnlDongWrapper.add(pnlThuocTinhDong, BorderLayout.CENTER);

        pnlWrapper.add(pnlCung, BorderLayout.NORTH);
        pnlWrapper.add(pnlDongWrapper, BorderLayout.CENTER);
        return pnlWrapper;
    }
    
    // --- UI HELPERS ---
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        // lbl.setForeground(COLOR_TEXT_MUTED);
        return lbl;
    }
    
    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(200, 42));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 12, 5, 12)
        ));
        return txt;
    }
    
    private JButton createFlatButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private JPanel taoInputThongSo(String title, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        p.add(createLabel(title), BorderLayout.NORTH);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    // --- LOGIC METHODS ---
    public void addDynamicAttribute(String groupName, DefaultComboBoxModel<ThuocTinh> model) {
        JLabel lbl = createLabel(groupName + ":");
        JComboBox<ThuocTinh> cbo = new JComboBox<>(model);
        cbo.setPreferredSize(new Dimension(150, 40));
        cbo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JPanel pItem = new JPanel(new BorderLayout(0, 8));
        pItem.setOpaque(false);
        pItem.add(lbl, BorderLayout.NORTH); 
        pItem.add(cbo, BorderLayout.CENTER);
        
        pnlThuocTinhDong.add(pItem);
        dynamicCombos.put(groupName, cbo);
    }
    
    public void clearDynamicAttributes() {
        pnlThuocTinhDong.removeAll(); 
        dynamicCombos.clear();
        pnlThuocTinhDong.revalidate(); 
        pnlThuocTinhDong.repaint();
    }
    
    public void hienThiAnh(String path) {
        if (path == null || path.isEmpty()) { 
            lblHinhAnh.setIcon(null); 
            lblHinhAnh.setText("Chưa có ảnh"); 
            return; 
        }
        try {
            // Nếu là đường dẫn tuyệt đối (preview ngay sau khi chọn) → dùng trực tiếp
            // Nếu chỉ là tên file (lấy từ DB) → tìm trong thư mục images/
            java.io.File filAnh = new java.io.File(path);
            if (!filAnh.isAbsolute()) {
                filAnh = new java.io.File("images", path);
            }
            
            if (filAnh.exists()) {
                ImageIcon icon = new ImageIcon(filAnh.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                lblHinhAnh.setIcon(new ImageIcon(img)); 
                lblHinhAnh.setText("");
            } else {
                lblHinhAnh.setIcon(null);
                lblHinhAnh.setText("Không tìm thấy ảnh");
            }
        } catch (Exception e) { 
            lblHinhAnh.setText("Lỗi Ảnh"); 
        }
    }
    
    public void clearForm() {
        txtMaSP.setText(""); txtTenSP.setText(""); txtGiaNhap.setText(""); txtGiaBan.setText(""); txtTonKho.setText("");
        txtMoTa.setText(""); hienThiAnh(null);
        txtChip.setText(""); txtManHinh.setText(""); txtCamSau.setText(""); txtCamTruoc.setText(""); txtPin.setText(""); txtHDH.setText("");
        if(cboDanhMuc.getItemCount()>0) cboDanhMuc.setSelectedIndex(0);
        if(cboThuongHieu.getItemCount()>0) cboThuongHieu.setSelectedIndex(0);
        clearDynamicAttributes();
        txtMaSP.setEditable(true);
        if (scrollFormNhap != null) {
            SwingUtilities.invokeLater(() -> scrollFormNhap.getVerticalScrollBar().setValue(0));
        }
    }
    
    // --- BẢNG: EDIT / DELETE ICONS ---
    public class PanelAction extends JPanel {
        public JButton cmdEdit, cmdDelete;
        public PanelAction() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10)); 
            setOpaque(false);
            cmdEdit = new JButton();
            cmdEdit.setPreferredSize(new Dimension(34, 34));
            cmdEdit.setBackground(new Color(13, 202, 240));
            cmdEdit.setBorder(null);
            cmdEdit.setFocusPainted(false);
            cmdEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            cmdDelete = new JButton();
            cmdDelete.setPreferredSize(new Dimension(34, 34));
            cmdDelete.setBackground(COLOR_DANGER);
            cmdDelete.setBorder(null);
            cmdDelete.setFocusPainted(false);
            cmdDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));

            try {
                URL editUrl = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/edit.png");
                if(editUrl!=null) cmdEdit.setIcon(new ImageIcon(new ImageIcon(editUrl).getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
                URL delUrl = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/delete.png");
                if(delUrl!=null) cmdDelete.setIcon(new ImageIcon(new ImageIcon(delUrl).getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
            } catch(Exception e){}

            add(cmdEdit); add(cmdDelete);
        }
    }
    public class TableActionCellRender extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            PanelAction action = new PanelAction();
            if (isSelected) action.setBackground(table.getSelectionBackground());
            return action;
        }
    }
    public class TableActionCellEditor extends DefaultCellEditor {
        TableActionEvent event;
        public TableActionCellEditor(TableActionEvent event) { super(new JCheckBox()); this.event = event; }
        @Override public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            PanelAction action = new PanelAction();
            action.setBackground(table.getSelectionBackground());
            action.cmdEdit.addActionListener(e -> { event.onEdit(row); fireEditingStopped(); });
            action.cmdDelete.addActionListener(e -> { event.onDelete(row); fireEditingStopped(); });
            return action;
        }
    }
    
    // --- Getters & Listers ---
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
