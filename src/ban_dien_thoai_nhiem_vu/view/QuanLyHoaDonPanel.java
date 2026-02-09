package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class QuanLyHoaDonPanel extends JPanel {

    // Bảng Hóa Đơn (Master)
    private JTable tblHoaDon;
    private DefaultTableModel modelHoaDon;

    // Bảng Chi Tiết (Detail)
    private JTable tblChiTiet;
    private DefaultTableModel modelChiTiet;

    public QuanLyHoaDonPanel() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); // Main BG
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Lịch Sử Giao Dịch - Hóa Đơn");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 33, 33));
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- PHẦN 1: BẢNG HÓA ĐƠN (Ở TRÊN) ---
        JPanel pnlHoaDon = new JPanel(new BorderLayout());
        pnlHoaDon.setBackground(Color.WHITE);
        pnlHoaDon.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel lblHDHeader = new JLabel("Danh sách hóa đơn (Mới nhất)");
        lblHDHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblHDHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnlHoaDon.add(lblHDHeader, BorderLayout.NORTH);

        String[] colsHD = {"Mã HĐ", "Ngày Lập", "Nhân Viên", "Khách Hàng", "Tổng Tiền"};
        modelHoaDon = new DefaultTableModel(colsHD, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Không cho sửa
        };
        tblHoaDon = new JTable(modelHoaDon);
        styleTable(tblHoaDon);
        pnlHoaDon.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);
        
        // Gợi ý
        JLabel lblHint = new JLabel("  * Click vào hóa đơn để xem chi tiết sản phẩm bên dưới");
        lblHint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblHint.setForeground(new Color(41, 98, 255));
        lblHint.setBorder(new EmptyBorder(5, 0, 0, 0));
        pnlHoaDon.add(lblHint, BorderLayout.SOUTH);

        // --- PHẦN 2: BẢNG CHI TIẾT (Ở DƯỚI) ---
        JPanel pnlChiTiet = new JPanel(new BorderLayout());
        pnlChiTiet.setBackground(Color.WHITE);
        pnlChiTiet.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel lblCTHeader = new JLabel("Chi tiết sản phẩm");
        lblCTHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblCTHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnlChiTiet.add(lblCTHeader, BorderLayout.NORTH);

        String[] colsCT = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        modelChiTiet = new DefaultTableModel(colsCT, 0);
        tblChiTiet = new JTable(modelChiTiet);
        styleTable(tblChiTiet);
        pnlChiTiet.add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);

        // Dùng SplitPane để chia đôi màn hình (kéo lên xuống được)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlHoaDon, pnlChiTiet);
        splitPane.setDividerLocation(300); // Chiều cao bảng trên
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(null);
        splitPane.setDividerSize(10);
        splitPane.setBackground(new Color(245, 247, 250)); // Match Main BG to make divider look transparent

        add(splitPane, BorderLayout.CENTER);
    }
    
    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
    }

    // Getters
    public DefaultTableModel getModelHoaDon() { return modelHoaDon; }
    public DefaultTableModel getModelChiTiet() { return modelChiTiet; }
    public JTable getTblHoaDon() { return tblHoaDon; }
    
    // Listeners
    public void addClickHoaDonListener(MouseAdapter l) { tblHoaDon.addMouseListener(l); }
}
