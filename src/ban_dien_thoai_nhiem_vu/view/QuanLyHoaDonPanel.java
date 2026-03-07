package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class QuanLyHoaDonPanel extends JPanel {

    private JTextField txtTimKiem;
    private JButton btnTim, btnLamMoi;
    private JTable tblHoaDon, tblChiTiet;
    private DefaultTableModel modelHoaDon, modelChiTiet;
    private JButton btnIn, btnCapNhatTrangThai;
    private JComboBox<String> cboTrangThai;
    
    private JLabel lblMaHD, lblNgayLap, lblNguoiBan, lblKhachHang, lblTongTien;

    // UI Constants
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public QuanLyHoaDonPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, taoPanelDanhSach(), taoPanelChiTiet());
        splitPane.setResizeWeight(0.65); 
        splitPane.setDividerSize(0); // Clean no-visible divider
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        
        // Add some padding between the splits using rigid area trick or we just rely on the panel borders
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel taoPanelDanhSach() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setOpaque(false);
        pnlWrapper.setBorder(new EmptyBorder(0, 0, 0, 10)); // Right padding to separate from details

        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Header Text
        JLabel lblListTitle = new JLabel("Lịch Sử Hóa Đơn");
        lblListTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblListTitle.setForeground(COLOR_TEXT_DARK);

        // Search Block
        JPanel pSearch = new JPanel(new BorderLayout(15, 0));
        pSearch.setBackground(Color.WHITE);
        pSearch.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        txtTimKiem = new JTextField("Nhập mã HĐ hoặc tên khách...");
        txtTimKiem.setPreferredSize(new Dimension(200, 40));
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setForeground(COLOR_TEXT_MUTED);
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtTimKiem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtTimKiem.getText().equals("Nhập mã HĐ hoặc tên khách...")) {
                    txtTimKiem.setText("");
                    txtTimKiem.setForeground(COLOR_TEXT_DARK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtTimKiem.getText().isEmpty()) {
                    txtTimKiem.setForeground(COLOR_TEXT_MUTED);
                    txtTimKiem.setText("Nhập mã HĐ hoặc tên khách...");
                }
            }
        });
        
        btnTim = createFlatButton("Tìm Kiếm", COLOR_PRIMARY, Color.WHITE);
        btnLamMoi = createFlatButton("Tải Lại", new Color(240, 240, 240), COLOR_TEXT_DARK);
        btnIn = createFlatButton("In Báo Cáo", new Color(25, 135, 84), Color.WHITE);

        
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pBtn.setBackground(Color.WHITE);
        pBtn.add(btnTim); 
        pBtn.add(btnLamMoi);
        pBtn.add(btnIn);
        
        pSearch.add(txtTimKiem, BorderLayout.CENTER);
        pSearch.add(pBtn, BorderLayout.EAST);

        JPanel pnlHeaderGroup = new JPanel(new BorderLayout());
        pnlHeaderGroup.setBackground(Color.WHITE);
        pnlHeaderGroup.add(lblListTitle, BorderLayout.NORTH);
        pnlHeaderGroup.add(pSearch, BorderLayout.CENTER);

        // Table
        String[] cols = {"Mã HĐ", "Ngày Lập", "Nhân Viên", "Khách Hàng", "Tổng Tiền", "Trạng Thái"};
        modelHoaDon = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblHoaDon = new JTable(modelHoaDon);
        styleModernTable(tblHoaDon);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblHoaDon.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblHoaDon.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        JScrollPane sc = new JScrollPane(tblHoaDon);
        sc.setBorder(null);
        sc.getViewport().setBackground(Color.WHITE);

        pnl.add(pnlHeaderGroup, BorderLayout.NORTH);
        pnl.add(sc, BorderLayout.CENTER);
        
        pnlWrapper.add(pnl, BorderLayout.CENTER);
        return pnlWrapper;
    }

    private JPanel taoPanelChiTiet() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setOpaque(false);
        pnlWrapper.setBorder(new EmptyBorder(0, 10, 0, 0)); // Left padding to separate from list

        JPanel pnl = new JPanel(new BorderLayout(0, 20));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        // Info Panel - Invoice Detail Header Like a Receipt
        JPanel pInfo = new JPanel(new GridLayout(6, 1, 0, 10)); // Thu hẹp gap một chút để đủ chỗ
        pInfo.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("Chi Tiết Hóa Đơn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        pInfo.add(lblTitle);
        pInfo.add(lblMaHD = new JLabel("Mã HĐ: Chưa chọn")); lblMaHD.setFont(new Font("Segoe UI", Font.BOLD, 15)); lblMaHD.setForeground(COLOR_PRIMARY);
        pInfo.add(lblNgayLap = createDetailLabel("Ngày lập: --"));
        pInfo.add(lblNguoiBan = createDetailLabel("Người bán: --"));
        pInfo.add(lblKhachHang = createDetailLabel("Khách hàng: --"));
        
        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        pnlTotal.setBackground(Color.WHITE);
        pnlTotal.add(lblTongTien = new JLabel("TỔNG TIẾN: 0 đ")); 
        lblTongTien.setForeground(new Color(220, 53, 69)); 
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pInfo.add(pnlTotal);
        
        // --- STATUS UPDATE BLOCK ---
        JPanel pnlStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlStatus.setBackground(Color.WHITE);
        pnlStatus.setBorder(new EmptyBorder(5, 0, 10, 0));
        
        JLabel lblStatusTag = new JLabel("Trạng thái đơn hàng:");
        lblStatusTag.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatusTag.setForeground(COLOR_TEXT_DARK);
        
        String[] trangThaiList = {"Chờ xử lý", "Đang giao", "Hoàn thành", "Đã hủy"};
        cboTrangThai = new JComboBox<>(trangThaiList);
        cboTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboTrangThai.setBackground(Color.WHITE);
        cboTrangThai.setPreferredSize(new Dimension(140, 35));
        
        btnCapNhatTrangThai = createFlatButton("Cập Nhật", new Color(13, 202, 240), Color.BLACK);
        btnCapNhatTrangThai.setPreferredSize(new Dimension(100, 35));
        
        pnlStatus.add(lblStatusTag);
        pnlStatus.add(cboTrangThai);
        pnlStatus.add(btnCapNhatTrangThai);

        // Table Details
        String[] cols = {"Tên SP", "SL", "Đơn Giá", "Thành Tiền"};
        modelChiTiet = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblChiTiet = new JTable(modelChiTiet);
        styleModernTable(tblChiTiet);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblChiTiet.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tblChiTiet.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        JScrollPane sc = new JScrollPane(tblChiTiet);
        sc.setBorder(BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1));
        sc.getViewport().setBackground(Color.WHITE);

        // Add 3 main blocks: Info (Top), Status Action (Middle), Table (Bottom)
        JPanel pnlTopBlocks = new JPanel(new BorderLayout());
        pnlTopBlocks.setBackground(Color.WHITE);
        pnlTopBlocks.add(pInfo, BorderLayout.NORTH);
        pnlTopBlocks.add(pnlStatus, BorderLayout.SOUTH);

        pnl.add(pnlTopBlocks, BorderLayout.NORTH);
        pnl.add(sc, BorderLayout.CENTER);
        
        pnlWrapper.add(pnl, BorderLayout.CENTER);
        return pnlWrapper;
    }
    
    // --- UI Helpers ---
    private JLabel createDetailLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(COLOR_TEXT_DARK);
        return lbl;
    }

    private JButton createFlatButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void styleModernTable(JTable tbl) {
        tbl.setRowHeight(40);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tbl.getTableHeader().setBackground(Color.WHITE);
        tbl.getTableHeader().setForeground(COLOR_TEXT_MUTED);
        tbl.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_TABLE_BORDER));
        tbl.setShowGrid(false);
        tbl.setIntercellSpacing(new Dimension(0, 0));
        tbl.setSelectionBackground(new Color(240, 244, 255));
        tbl.setSelectionForeground(COLOR_TEXT_DARK);
    }
    
    // Getters & Listeners
    public DefaultTableModel getModelHoaDon() { return modelHoaDon; }
    public DefaultTableModel getModelChiTiet() { return modelChiTiet; }
    public JTable getTblHoaDon() { return tblHoaDon; }
    public String getTuKhoa() { 
        String text = txtTimKiem.getText();
        if(text.equals("Nhập mã HĐ hoặc tên khách...")) return "";
        return text; 
    }
    
    public void setThongTinChiTiet(String ma, String ngay, String nv, String kh, String tong) {
        lblMaHD.setText("Mã HĐ: " + ma);
        lblNgayLap.setText("Ngày lập: " + ngay);
        lblNguoiBan.setText("Người bán: " + nv);
        lblKhachHang.setText("Khách hàng: " + kh);
        lblTongTien.setText("TỔNG TIẾN: " + tong);
    }
    
    public void addTimKiemListener(ActionListener l) { btnTim.addActionListener(l); }
    public void addLamMoiListener(ActionListener l) { btnLamMoi.addActionListener(l); }
    public void addBangListener(MouseAdapter l) { tblHoaDon.addMouseListener(l); }
    public JButton getBtnIn() { return btnIn; }
    public void addInListener(ActionListener l) { btnIn.addActionListener(l); }
    
    public JComboBox<String> getCboTrangThai() { return cboTrangThai; }
    public void addCapNhatStatusListener(ActionListener l) { btnCapNhatTrangThai.addActionListener(l); }
}
