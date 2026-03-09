package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class QuanLyKhoFrame extends JFrame {

    private JPanel pnlMain;
    private JTable tblKho;
    private DefaultTableModel modelKho;
    private JButton btnNhapHang; 
    private JTextField txtTimKiem;
    private JButton btnTim;
    
    // UI Constants
    // private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    // private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    // private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public QuanLyKhoFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Quản Lý Kho Hàng");
        setSize(1200, 750);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pnlMain = new JPanel(new BorderLayout(20, 20));
        // pnlMain.setBackground(COLOR_BG);
        pnlMain.setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- 1. HEADER (Tiêu đề + Tìm kiếm + Nút Nhập) ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblTitle = new JLabel("Kho Hàng & Tồn Kho");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        // lblTitle.setForeground(COLOR_TEXT_DARK);

        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlRight.setOpaque(false);

        txtTimKiem = new JTextField(20);
        txtTimKiem.setPreferredSize(new Dimension(250, 42));
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));

        btnTim = new JButton("Tìm Kiếm");
        btnTim.setPreferredSize(new Dimension(100, 42));
        btnTim.setOpaque(false);
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // btnTim.setForeground(COLOR_TEXT_DARK);
        btnTim.setBorder(BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1));
        btnTim.setFocusPainted(false);
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnNhapHang = new JButton("+ NHẬP HÀNG MỚI");
        btnNhapHang.setBackground(new Color(25, 135, 84)); // Success Green
        btnNhapHang.setForeground(Color.WHITE);
        btnNhapHang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNhapHang.setPreferredSize(new Dimension(180, 42));
        btnNhapHang.setBorder(null);
        btnNhapHang.setFocusPainted(false);
        btnNhapHang.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlRight.add(txtTimKiem);
        pnlRight.add(btnTim);
        pnlRight.add(btnNhapHang);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlRight, BorderLayout.EAST);

        // --- 2. TABLE HIỂN THỊ TỒN KHO ---
        JPanel pnlTableContainer = new JPanel(new BorderLayout());
        pnlTableContainer.setBackground(UIManager.getColor("window"));
        pnlTableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));

        String[] cols = {"Mã SP", "Tên Sản Phẩm", "Hãng", "Giá Bán", "TỒN KHO"};
        modelKho = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblKho = new JTable(modelKho);
        tblKho.setRowHeight(45);
        tblKho.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblKho.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        // tblKho.getTableHeader().setBackground(Color.WHITE);
        // tblKho.getTableHeader().setForeground(COLOR_TEXT_MUTED);
        tblKho.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_TABLE_BORDER));
        tblKho.setShowGrid(false);
        tblKho.setIntercellSpacing(new Dimension(0, 0));
        // tblKho.setSelectionBackground(new Color(240, 244, 255));
        // tblKho.setSelectionForeground(COLOR_TEXT_DARK);
        
        tblKho.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 15));
                c.setForeground(new Color(220, 53, 69)); // Text Danger
                setHorizontalAlignment(CENTER);
                return c;
            }
        });

        JScrollPane sc = new JScrollPane(tblKho);
        sc.setBorder(null);
        // sc.getViewport().setBackground(Color.WHITE);
        
        pnlTableContainer.add(sc, BorderLayout.CENTER);

        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(pnlTableContainer, BorderLayout.CENTER);

        add(pnlMain);
    }

    public DefaultTableModel getModel() { return modelKho; }
    public JTable getTable() { return tblKho; }
    public String getTuKhoa() { return txtTimKiem.getText(); }
    public JPanel getContentPanePanel() { return pnlMain; } 

    public void addNhapHangListener(ActionListener l) { btnNhapHang.addActionListener(l); }
    public void addTimKiemListener(ActionListener l) { btnTim.addActionListener(l); }
    
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}
