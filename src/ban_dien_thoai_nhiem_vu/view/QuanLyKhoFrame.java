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
    private JButton btnNhapHang; // Nút chức năng duy nhất và quan trọng nhất
    private JTextField txtTimKiem;
    private JButton btnTim;

    public QuanLyKhoFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Quản Lý Kho Hàng");
        setSize(1200, 750);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(new Color(245, 245, 250));

        // --- 1. HEADER (Tiêu đề + Tìm kiếm + Nút Nhập) ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Kho Hàng & Tồn Kho");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(50, 50, 50));

        // Khu vực bên phải: Tìm kiếm + Nút Nhập
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlRight.setOpaque(false);

        txtTimKiem = new JTextField(20);
        txtTimKiem.setPreferredSize(new Dimension(200, 40));
        btnTim = new JButton("🔍");
        btnTim.setPreferredSize(new Dimension(50, 40));
        btnTim.setBackground(Color.WHITE);

        // Nút NHẬP HÀNG (Màu xanh lá nổi bật)
        btnNhapHang = new JButton("📦 NHẬP HÀNG");
        btnNhapHang.setBackground(new Color(40, 167, 69)); // Green
        btnNhapHang.setForeground(Color.WHITE);
        btnNhapHang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNhapHang.setPreferredSize(new Dimension(160, 40));
        btnNhapHang.setFocusPainted(false);
        btnNhapHang.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlRight.add(txtTimKiem);
        pnlRight.add(btnTim);
        pnlRight.add(btnNhapHang);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlRight, BorderLayout.EAST);

        // --- 2. TABLE HIỂN THỊ TỒN KHO ---
        // Bỏ cột "Hành động", chỉ cần hiển thị thông tin để chọn
        String[] cols = {"Mã SP", "Tên Sản Phẩm", "Hãng", "Giá Bán", "TỒN KHO"};
        modelKho = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên bảng, tránh sai sót
            }
        };

        tblKho = new JTable(modelKho);
        tblKho.setRowHeight(40);
        tblKho.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKho.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblKho.getTableHeader().setBackground(Color.WHITE);
        
        // Tô màu cột Tồn kho cho dễ nhìn
        tblKho.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                c.setForeground(new Color(220, 53, 69)); // Màu đỏ cho nổi bật số lượng
                setHorizontalAlignment(CENTER);
                return c;
            }
        });

        JScrollPane sc = new JScrollPane(tblKho);
        sc.setBorder(new EmptyBorder(0, 20, 20, 20));
        sc.getViewport().setBackground(Color.WHITE);

        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(sc, BorderLayout.CENTER);

        add(pnlMain);
    }

    // Getters & Setters
    public DefaultTableModel getModel() { return modelKho; }
    public JTable getTable() { return tblKho; }
    public String getTuKhoa() { return txtTimKiem.getText(); }
    public JPanel getContentPanePanel() { return pnlMain; } // Để MainController lấy panel nhúng vào

    // Listeners
    public void addNhapHangListener(ActionListener l) { btnNhapHang.addActionListener(l); }
    public void addTimKiemListener(ActionListener l) { btnTim.addActionListener(l); }
    
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}