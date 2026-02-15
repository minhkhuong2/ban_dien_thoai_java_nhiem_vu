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

    public QuanLyDanhMucPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("Quản lý Danh mục Sản phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);
        
        JPanel pnlContent = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlContent.setOpaque(false);
        pnlContent.add(createLeftForm());
        pnlContent.add(createRightList());
        add(pnlContent, BorderLayout.CENTER);
    }
    
    private JPanel createLeftForm() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblHeader = new JLabel("Thêm Danh Mục Mới");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JPanel pInput = new JPanel(new GridLayout(4, 1, 5, 5));
        pInput.setOpaque(false);
        pInput.add(new JLabel("Tên danh mục:"));
        txtTenDM = new JTextField();
        txtTenDM.setPreferredSize(new Dimension(0, 40));
        pInput.add(txtTenDM);
        
        btnThem = new JButton("+ Thêm ngay");
        btnThem.setBackground(new Color(67, 94, 190));
        btnThem.setForeground(Color.WHITE);
        btnThem.setPreferredSize(new Dimension(0, 45));
        
        p.add(lblHeader, BorderLayout.NORTH);
        p.add(pInput, BorderLayout.CENTER);
        p.add(btnThem, BorderLayout.SOUTH);
        return p;
    }
    
    private JPanel createRightList() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblHeader = new JLabel("Danh sách hiện có");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        String[] cols = {"ID", "TÊN DANH MỤC"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblDanhMuc = new JTable(model);
        tblDanhMuc.setRowHeight(40);
        tblDanhMuc.setShowGrid(false);
        
        p.add(lblHeader, BorderLayout.NORTH);
        p.add(new JScrollPane(tblDanhMuc), BorderLayout.CENTER);
        return p;
    }
    
    // --- GETTERS & LISTENERS CHO CONTROLLER ---
    public String getTenDM() { return txtTenDM.getText().trim(); }
    public void setTenDM(String t) { txtTenDM.setText(t); }
    public DefaultTableModel getModel() { return model; }
    public void addThemListener(ActionListener l) { btnThem.addActionListener(l); }
}