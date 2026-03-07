package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class QuanLyDanhMucPanel extends JPanel {
    
    private JTextField txtTenDM;
    private JButton btnThem;
    private JTable tblDanhMuc;
    private DefaultTableModel model;

    // UI Constants
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_SUCCESS = new Color(25, 135, 84);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public QuanLyDanhMucPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitle = new JLabel("Danh Mục Sản Phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftForm(), createRightList());
        splitPane.setResizeWeight(0.35); // Trái 35%, Phải 65% giống Khách Hàng
        splitPane.setDividerSize(0); 
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createLeftForm() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setOpaque(false);
        pnlWrapper.setBorder(new EmptyBorder(0, 0, 0, 10)); // Khoảng cách với List
        
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblHeader = new JLabel("Thêm Danh Mục Mới");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeader.setForeground(COLOR_TEXT_DARK);
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JPanel pInput = new JPanel(new BorderLayout(0, 10));
        pInput.setOpaque(false);
        
        JLabel lbl = new JLabel("Tên danh mục mới:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(COLOR_TEXT_MUTED);
        
        txtTenDM = new JTextField();
        txtTenDM.setPreferredSize(new Dimension(200, 42));
        txtTenDM.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTenDM.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        pInput.add(lbl, BorderLayout.NORTH);
        pInput.add(txtTenDM, BorderLayout.CENTER);
        
        btnThem = createFlatButton("+ Lưu Danh Mục", COLOR_SUCCESS, Color.WHITE);
        
        // Căn button xuong dưới
        JPanel pCenterWrap = new JPanel(new BorderLayout());
        pCenterWrap.setOpaque(false);
        pCenterWrap.add(pInput, BorderLayout.NORTH);
        
        JPanel pBtnWrap = new JPanel(new BorderLayout());
        pBtnWrap.setOpaque(false);
        pBtnWrap.setBorder(new EmptyBorder(20, 0, 0, 0));
        pBtnWrap.add(btnThem, BorderLayout.CENTER);

        p.add(lblHeader, BorderLayout.NORTH);
        p.add(pCenterWrap, BorderLayout.CENTER);
        p.add(pBtnWrap, BorderLayout.SOUTH);
        
        pnlWrapper.add(p, BorderLayout.CENTER);
        return pnlWrapper;
    }
    
    private JPanel createRightList() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setOpaque(false);
        pnlWrapper.setBorder(new EmptyBorder(0, 10, 0, 0)); // Trái pad
        
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblHeader = new JLabel("Danh Sách Hiện Có");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeader.setForeground(COLOR_TEXT_DARK);
        
        String[] cols = {"ID", "TÊN DANH MỤC"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblDanhMuc = new JTable(model);
        tblDanhMuc.setRowHeight(45);
        tblDanhMuc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblDanhMuc.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblDanhMuc.getTableHeader().setBackground(Color.WHITE);
        tblDanhMuc.getTableHeader().setForeground(COLOR_TEXT_MUTED);
        tblDanhMuc.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_TABLE_BORDER));
        tblDanhMuc.setShowGrid(false);
        tblDanhMuc.setIntercellSpacing(new Dimension(0, 0));
        tblDanhMuc.setSelectionBackground(new Color(240, 244, 255));
        tblDanhMuc.setSelectionForeground(COLOR_TEXT_DARK);
        
        // Chỉnh độ rông cột ID
        tblDanhMuc.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblDanhMuc.getColumnModel().getColumn(0).setMaxWidth(80);

        JScrollPane sc = new JScrollPane(tblDanhMuc);
        sc.setBorder(BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1)); // Vẽ 1 line border bao quanh bảng
        sc.getViewport().setBackground(Color.WHITE);
        
        p.add(lblHeader, BorderLayout.NORTH);
        p.add(sc, BorderLayout.CENTER);
        
        pnlWrapper.add(p, BorderLayout.CENTER);
        return pnlWrapper;
    }

    private JButton createFlatButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(100, 42));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    // --- GETTERS & LISTENERS ---
    public String getTenDM() { return txtTenDM.getText().trim(); }
    public void setTenDM(String t) { txtTenDM.setText(t); }
    public DefaultTableModel getModel() { return model; }
    public void addThemListener(ActionListener l) { btnThem.addActionListener(l); }
}
