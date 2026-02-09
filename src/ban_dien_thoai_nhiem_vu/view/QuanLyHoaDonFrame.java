package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class QuanLyHoaDonFrame extends JFrame {

    // Bảng Hóa Đơn (Master)
    private JTable tblHoaDon;
    private DefaultTableModel modelHoaDon;

    // Bảng Chi Tiết (Detail)
    private JTable tblChiTiet;
    private DefaultTableModel modelChiTiet;

    public QuanLyHoaDonFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Lịch Sử Giao Dịch & Hóa Đơn");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ HÓA ĐƠN BÁN HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(33, 41, 54));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- PHẦN 1: BẢNG HÓA ĐƠN (Ở TRÊN) ---
        JPanel pnlHoaDon = new JPanel(new BorderLayout());
        pnlHoaDon.setBorder(new TitledBorder("Danh sách Hóa đơn"));
        
        String[] colsHD = {"Mã HĐ", "Ngày Lập", "Nhân Viên", "Khách Hàng", "Tổng Tiền"};
        modelHoaDon = new DefaultTableModel(colsHD, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Không cho sửa
        };
        tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setRowHeight(30);
        pnlHoaDon.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);
        
        // Gợi ý
        JLabel lblHint = new JLabel("  * Click vào hóa đơn để xem chi tiết sản phẩm bên dưới");
        lblHint.setForeground(Color.BLUE);
        lblHint.setPreferredSize(new Dimension(0, 25));
        pnlHoaDon.add(lblHint, BorderLayout.SOUTH);

        // --- PHẦN 2: BẢNG CHI TIẾT (Ở DƯỚI) ---
        JPanel pnlChiTiet = new JPanel(new BorderLayout());
        pnlChiTiet.setBorder(new TitledBorder("Chi tiết sản phẩm trong hóa đơn"));

        String[] colsCT = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        modelChiTiet = new DefaultTableModel(colsCT, 0);
        tblChiTiet = new JTable(modelChiTiet);
        tblChiTiet.setRowHeight(30);
        pnlChiTiet.add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);

        // Dùng SplitPane để chia đôi màn hình (kéo lên xuống được)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlHoaDon, pnlChiTiet);
        splitPane.setDividerLocation(350); // Chiều cao bảng trên
        splitPane.setResizeWeight(0.5);

        add(splitPane, BorderLayout.CENTER);
    }

    // Getters
    public DefaultTableModel getModelHoaDon() { return modelHoaDon; }
    public DefaultTableModel getModelChiTiet() { return modelChiTiet; }
    public JTable getTblHoaDon() { return tblHoaDon; }
    
    // Listeners
    public void addClickHoaDonListener(MouseAdapter l) { tblHoaDon.addMouseListener(l); }
}