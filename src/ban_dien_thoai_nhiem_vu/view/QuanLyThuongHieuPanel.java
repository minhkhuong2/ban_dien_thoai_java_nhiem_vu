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
    private JButton btnSua;
    private JButton btnXoa; 

    // UI Constants
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_DANGER = new Color(220, 53, 69);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public QuanLyThuongHieuPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setOpaque(false);
        pHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitle = new JLabel("Thương Hiệu & Hãng Sản Xuất");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        
        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pButtons.setOpaque(false);
        
        btnThem = createFlatButton("+ Thêm Thương Hiệu", COLOR_PRIMARY, Color.WHITE);
        btnThem.setPreferredSize(new Dimension(180, 42));
        
        btnSua = createFlatButton("Sửa Thương Hiệu", new Color(255, 193, 7), Color.BLACK);
        btnSua.setPreferredSize(new Dimension(160, 42));
        
        btnXoa = createFlatButton("Xóa đã chọn", COLOR_DANGER, Color.WHITE);
        btnXoa.setPreferredSize(new Dimension(140, 42));

        pButtons.add(btnThem);
        pButtons.add(btnSua);
        pButtons.add(btnXoa);
        
        pHeader.add(lblTitle, BorderLayout.WEST);
        pHeader.add(pButtons, BorderLayout.EAST);
        add(pHeader, BorderLayout.NORTH);
        
        // Table Wrapper
        JPanel pTable = new JPanel(new BorderLayout());
        pTable.setBackground(Color.WHITE);
        pTable.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        String[] cols = {"ID", "LOGO", "TÊN THƯƠNG HIỆU"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
            @Override public Class<?> getColumnClass(int column) {
                if (column == 1) return Icon.class; // Xử lý render ảnh
                return Object.class;
            }
        };
        tblThuongHieu = new JTable(model);
        tblThuongHieu.setRowHeight(80); // Tăng chiều cao để hiện được Logo to
        tblThuongHieu.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblThuongHieu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblThuongHieu.getTableHeader().setBackground(Color.WHITE);
        tblThuongHieu.getTableHeader().setForeground(COLOR_TEXT_MUTED);
        tblThuongHieu.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_TABLE_BORDER));
        tblThuongHieu.setShowGrid(false);
        tblThuongHieu.setIntercellSpacing(new Dimension(0, 0));
        tblThuongHieu.setSelectionBackground(new Color(240, 244, 255));
        tblThuongHieu.setSelectionForeground(COLOR_TEXT_DARK);
        
        // Resize ID Cột
        tblThuongHieu.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblThuongHieu.getColumnModel().getColumn(0).setMaxWidth(80);
        
        JScrollPane sc = new JScrollPane(tblThuongHieu);
        sc.setBorder(null);
        sc.getViewport().setBackground(Color.WHITE);
        
        pTable.add(sc, BorderLayout.CENTER);
        add(pTable, BorderLayout.CENTER);
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

    // Getters & Listeners
    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return tblThuongHieu; }
    
    public void addThemListener(ActionListener l) { btnThem.addActionListener(l); }
    public void addSuaListener(ActionListener l) { btnSua.addActionListener(l); }
    public void addXoaListener(ActionListener l) { btnXoa.addActionListener(l); }
}
