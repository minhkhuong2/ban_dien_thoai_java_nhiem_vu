package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class QuanLyThuongHieuPanel extends JPanel {
    
    private JTable tblThuongHieu;
    private DefaultTableModel model;
    private JButton btnThem;
    private JButton btnXoa; // Thêm nút xóa

    public QuanLyThuongHieuPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Quản lý Thương hiệu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pButtons.setOpaque(false);
        
        btnThem = new JButton("+ Thêm Thương hiệu");
        btnThem.setBackground(new Color(67, 94, 190));
        btnThem.setForeground(Color.WHITE);
        btnThem.setPreferredSize(new Dimension(160, 40));
        
        btnXoa = new JButton("Xóa đã chọn");
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setPreferredSize(new Dimension(120, 40));

        pButtons.add(btnThem);
        pButtons.add(btnXoa);
        
        pHeader.add(lblTitle, BorderLayout.WEST);
        pHeader.add(pButtons, BorderLayout.EAST);
        add(pHeader, BorderLayout.NORTH);
        
        // Table
        JPanel pTable = new JPanel(new BorderLayout());
        pTable.setBackground(Color.WHITE);
        pTable.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        String[] cols = {"ID", "LOGO", "TÊN THƯƠNG HIỆU"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblThuongHieu = new JTable(model);
        tblThuongHieu.setRowHeight(50);
        tblThuongHieu.setShowGrid(false);
        tblThuongHieu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        pTable.add(new JScrollPane(tblThuongHieu), BorderLayout.CENTER);
        add(pTable, BorderLayout.CENTER);
    }

    // Getters & Listeners
    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return tblThuongHieu; }
    
    public void addThemListener(ActionListener l) { btnThem.addActionListener(l); }
    public void addXoaListener(ActionListener l) { btnXoa.addActionListener(l); }
}