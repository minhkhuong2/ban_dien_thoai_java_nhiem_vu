package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.SanPham;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class QuanLySanPhamPanel extends JPanel {
    
    public interface TableActionEvent {
        void onEdit(int row);
        void onDelete(int row);
    }

    private CardLayout cardLayout;
    private JPanel pnlMainContainer;
    
    // Giao diện Danh sách
    private JPanel pnlDanhSach;
    private JTable tblSanPham;
    private DefaultTableModel tableModel;
    private JButton btnMoFormThem; 
    
    // Giao diện Form nhập
    private JPanel pnlFormNhap;
    private JScrollPane scrollFormNhap;
    public JTextField txtMaSP, txtTenSP, txtHangSX, txtGiaNhap, txtGiaBan, txtTonKho;
    public JTextArea txtMoTa;
    public JLabel lblHinhAnh;
    public JButton btnChonAnh;
    public JTextField txtManHinh, txtCamSau, txtCamTruoc, txtChip, txtRam, txtRom, txtPin, txtHDH;
    private JButton btnLuu, btnHuy;

    private String duongDanAnh = "";
    
    // UI Constants
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_SUCCESS = new Color(25, 135, 84);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public QuanLySanPhamPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        
        cardLayout = new CardLayout();
        pnlMainContainer = new JPanel(cardLayout);
        pnlMainContainer.setOpaque(false);
        pnlMainContainer.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        taoGiaoDienDanhSach();
        taoGiaoDienFormNhap();
        
        pnlMainContainer.add(pnlDanhSach, "DANH_SACH");
        pnlMainContainer.add(pnlFormNhap, "FORM_NHAP");
        
        add(pnlMainContainer, BorderLayout.CENTER);
    }

    // =========================================================================
    // 1. MÀN HÌNH DANH SÁCH
    // =========================================================================
    private void taoGiaoDienDanhSach() {
        pnlDanhSach = new JPanel(new BorderLayout());
        pnlDanhSach.setOpaque(false);
        
        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitle = new JLabel("Danh Sách Sản Phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        
        btnMoFormThem = createFlatButton("+ Thêm Sản Phẩm Mới", COLOR_SUCCESS, Color.WHITE);
        btnMoFormThem.setPreferredSize(new Dimension(200, 42));
        btnMoFormThem.addActionListener(e -> {
            clearForm();
            cardLayout.show(pnlMainContainer, "FORM_NHAP");
        });

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(btnMoFormThem, BorderLayout.EAST);
        pnlDanhSach.add(pnlHeader, BorderLayout.NORTH);
        
        // Wrapper Panel for Table
        JPanel pnlTableWrapper = new JPanel(new BorderLayout());
        pnlTableWrapper.setBackground(Color.WHITE);
        pnlTableWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Table
        String[] headers = {"Mã SP", "Tên Sản Phẩm", "Thương Hiệu", "Giá Bán", "Tồn Kho", "Ảnh (Ẩn)", "Hành Động"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column == 6; }
        };
        
        tblSanPham = new JTable(tableModel);
        tblSanPham.setRowHeight(55); 
        tblSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblSanPham.getTableHeader().setBackground(Color.WHITE);
        tblSanPham.getTableHeader().setForeground(COLOR_TEXT_MUTED);
        tblSanPham.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_TABLE_BORDER));
        tblSanPham.setShowGrid(false);
        tblSanPham.setIntercellSpacing(new Dimension(0, 0));
        tblSanPham.setSelectionBackground(new Color(240, 244, 255));
        tblSanPham.setSelectionForeground(COLOR_TEXT_DARK);
        
        tblSanPham.getColumnModel().getColumn(5).setMinWidth(0);
        tblSanPham.getColumnModel().getColumn(5).setMaxWidth(0);
        tblSanPham.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        tblSanPham.getColumnModel().getColumn(6).setPreferredWidth(120); 

        JScrollPane sc = new JScrollPane(tblSanPham);
        sc.setBorder(null);
        sc.getViewport().setBackground(Color.WHITE);
        
        pnlTableWrapper.add(sc, BorderLayout.CENTER);
        pnlDanhSach.add(pnlTableWrapper, BorderLayout.CENTER);
    }
    
    // --- NÚT BẤM (HÀNH ĐỘNG TRONG BẢNG) ---
    public class PanelAction extends JPanel {
        public JButton cmdEdit, cmdDelete;
        public PanelAction() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10)); 
            setBackground(Color.WHITE);
            cmdEdit = createBtn("edit.png", new Color(13, 202, 240)); 
            cmdDelete = createBtn("delete.png", new Color(220, 53, 69));
            add(cmdEdit); add(cmdDelete);
        }
        private JButton createBtn(String icon, Color bg) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(34, 34));
            btn.setBackground(bg);
            btn.setBorder(null);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            try {
                URL url = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/" + icon);
                if(url!=null) btn.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
            } catch(Exception e){}
            return btn;
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
        private TableActionEvent event;
        public TableActionCellEditor(TableActionEvent event) { super(new JCheckBox()); this.event = event; }
        @Override public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            PanelAction action = new PanelAction();
            action.setBackground(table.getSelectionBackground());
            action.cmdEdit.addActionListener(e -> { event.onEdit(row); fireEditingStopped(); });
            action.cmdDelete.addActionListener(e -> { event.onDelete(row); fireEditingStopped(); });
            return action;
        }
    }

    public void setTableActionEvent(TableActionEvent event) {
        tblSanPham.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event));
    }

    // =========================================================================
    // 2. MÀN HÌNH FORM NHẬP
    // =========================================================================
    private void taoGiaoDienFormNhap() {
        pnlFormNhap = new JPanel(new BorderLayout());
        pnlFormNhap.setOpaque(false);
        
        // Header Form
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitle = new JLabel("Thông Tin Chi Tiết Sản Phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlFormNhap.add(pnlHeader, BorderLayout.NORTH);
        
        // CONTENT
        JPanel pnlWrapperForm = new JPanel();
        pnlWrapperForm.setLayout(new BoxLayout(pnlWrapperForm, BoxLayout.Y_AXIS));
        pnlWrapperForm.setOpaque(false);
        pnlWrapperForm.add(taoPanelThongTinChung());
        pnlWrapperForm.add(Box.createVerticalStrut(20));
        pnlWrapperForm.add(taoPanelThongSoKyThuat());
        
        scrollFormNhap = new JScrollPane(pnlWrapperForm);
        scrollFormNhap.setBorder(null);
        scrollFormNhap.setOpaque(false);
        scrollFormNhap.getViewport().setOpaque(false);
        scrollFormNhap.getVerticalScrollBar().setUnitIncrement(16);
        
        pnlFormNhap.add(scrollFormNhap, BorderLayout.CENTER);
        
        // FOOTER
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlFooter.setOpaque(false);
        pnlFooter.setBorder(new EmptyBorder(25, 0, 0, 0));
        
        btnHuy = createFlatButton("Hủy & Quay lại", new Color(240, 240, 240), COLOR_TEXT_DARK);
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
        pnlWrapper.setBackground(Color.WHITE);
        pnlWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
                " Thông tin chung ", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), COLOR_PRIMARY
            ),
            new EmptyBorder(20, 30, 20, 30)
        ));

        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 0, 15, 30); // Giãn ngang dọc
        g.fill = GridBagConstraints.HORIZONTAL;
        
        g.gridy = 0; g.gridx = 0; pnl.add(createLabel("Mã Sản Phẩm (*):"), g);
        g.gridy = 1; txtMaSP = createTextField(); pnl.add(txtMaSP, g);
        
        g.gridy = 2; pnl.add(createLabel("Thương hiệu:"), g);
        g.gridy = 3; txtHangSX = createTextField(); pnl.add(txtHangSX, g);
        
        g.gridy = 4; pnl.add(createLabel("Giá Bán:"), g);
        g.gridy = 5; txtGiaBan = createTextField(); pnl.add(txtGiaBan, g);
        
        g.gridy = 6; g.gridx = 0; g.anchor = GridBagConstraints.NORTHWEST;
        pnl.add(createLabel("Mô tả:"), g);
        g.gridy = 7; g.gridx = 0; g.ipady = 80; g.gridwidth = 2; 
        txtMoTa = new JTextArea(); 
        txtMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtMoTa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        pnl.add(new JScrollPane(txtMoTa), g);
        g.ipady = 0; g.gridwidth = 1;

        // CỘT 2
        g.gridy = 0; g.gridx = 1; g.weightx = 1.0; pnl.add(createLabel("Tên Sản Phẩm (*):"), g);
        g.gridy = 1; txtTenSP = createTextField(); pnl.add(txtTenSP, g);

        g.gridy = 2; pnl.add(createLabel("Giá Nhập:"), g);
        g.gridy = 3; txtGiaNhap = createTextField(); pnl.add(txtGiaNhap, g);

        g.gridy = 4; pnl.add(createLabel("Tồn Kho:"), g);
        g.gridy = 5; txtTonKho = createTextField(); pnl.add(txtTonKho, g);

        // KHỐI HÌNH ẢNH (Bên phải cùng)
        g.gridx = 2; g.gridy = 0; g.gridheight = 8; g.weightx = 0; g.insets = new Insets(10, 30, 10, 0);
        
        JPanel pnlAnh = new JPanel(new BorderLayout(0, 15));
        pnlAnh.setOpaque(false);
        JLabel lblAnhTitle = createLabel("Hình Ảnh Sản Phẩm");
        lblAnhTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblHinhAnh = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblHinhAnh.setPreferredSize(new Dimension(220, 220));
        lblHinhAnh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblHinhAnh.setForeground(COLOR_TEXT_MUTED);
        lblHinhAnh.setBorder(BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1));
        
        btnChonAnh = createFlatButton("Tải Ảnh Lên...", new Color(240, 240, 240), COLOR_TEXT_DARK);
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
        pnlWrapper.setBackground(Color.WHITE);
        pnlWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
                " Thông số kỹ thuật ", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), COLOR_PRIMARY
            ),
            new EmptyBorder(20, 30, 30, 30)
        ));

        JPanel pnl = new JPanel(new GridLayout(4, 2, 30, 20)); // Grid 2 columns
        pnl.setOpaque(false);
        pnl.add(taoInputThongSo("Kích thước màn hình", txtManHinh = createTextField()));
        pnl.add(taoInputThongSo("Hệ điều hành", txtHDH = createTextField()));
        pnl.add(taoInputThongSo("Camera Sau", txtCamSau = createTextField()));
        pnl.add(taoInputThongSo("Camera Trước", txtCamTruoc = createTextField()));
        pnl.add(taoInputThongSo("Chip (CPU)", txtChip = createTextField()));
        pnl.add(taoInputThongSo("RAM", txtRam = createTextField()));
        pnl.add(taoInputThongSo("Bộ nhớ trong (ROM)", txtRom = createTextField()));
        pnl.add(taoInputThongSo("Dung lượng Pin", txtPin = createTextField()));
        
        pnlWrapper.add(pnl, BorderLayout.NORTH);
        return pnlWrapper;
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT_MUTED);
        return lbl;
    }
    
    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(200, 40));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
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
        JPanel p = new JPanel(new BorderLayout(5, 8));
        p.setOpaque(false);
        p.add(createLabel(title), BorderLayout.NORTH);
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
            Image img = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
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
        if (scrollFormNhap != null) {
            SwingUtilities.invokeLater(() -> scrollFormNhap.getVerticalScrollBar().setValue(0));
        }
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
