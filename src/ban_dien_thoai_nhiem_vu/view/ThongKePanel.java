package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class ThongKePanel extends JPanel {

    // 3 Ô hiển thị số liệu tổng quan
    private JLabel lblTongDoanhThu;
    private JLabel lblTongDonHang;
    private JLabel lblTongSanPham;
    
    // Khu vực chứa Biểu đồ (Controller sẽ vẽ vào đây)
    public JPanel pnlBieuDo; 
    
    // Bảng chi tiết
    private JTable tblThongKe;
    private DefaultTableModel modelThongKe;
    private JButton btnLamMoi;

    public ThongKePanel() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); // Main BG
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Báo Cáo & Thống Kê");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 33, 33));
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- CENTER: CHỨA CARDS + BIỂU ĐỒ ---
        JPanel pnlCenter = new JPanel(new BorderLayout(20, 20));
        pnlCenter.setBackground(new Color(245, 247, 250)); // Match Main BG

        // 1. CARDS (TOP)
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setBackground(new Color(245, 247, 250));
        pnlCards.setPreferredSize(new Dimension(0, 140));
        
        pnlCards.add(taoCard("DOANH THU TỔNG", lblTongDoanhThu = new JLabel("0 VNĐ"), new Color(41, 182, 246), "profit.png")); 
        pnlCards.add(taoCard("TỔNG ĐƠN HÀNG", lblTongDonHang = new JLabel("0"), new Color(102, 187, 106), "order.png")); 
        pnlCards.add(taoCard("SẢN PHẨM ĐÃ BÁN", lblTongSanPham = new JLabel("0"), new Color(255, 167, 38), "box.png")); 
        
        pnlCenter.add(pnlCards, BorderLayout.NORTH);

        // 2. BIỂU ĐỒ (CENTER) - Container
        JPanel pnlChartContainer = new JPanel(new BorderLayout());
        pnlChartContainer.setBackground(Color.WHITE);
        pnlChartContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblChartTitle = new JLabel("Biểu đồ doanh thu 7 ngày gần nhất");
        lblChartTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblChartTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        pnlChartContainer.add(lblChartTitle, BorderLayout.NORTH);
        
        pnlBieuDo = new JPanel(new BorderLayout());
        pnlBieuDo.setBackground(Color.WHITE);
        pnlChartContainer.add(pnlBieuDo, BorderLayout.CENTER);
        
        pnlCenter.add(pnlChartContainer, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // --- BOTTOM: BẢNG CHI TIẾT ---
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.setPreferredSize(new Dimension(0, 280)); 
        pnlTable.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel pnlTool = new JPanel(new BorderLayout());
        pnlTool.setBackground(Color.WHITE);
        
        JLabel lblTableTitle = new JLabel("Chi tiết số liệu theo ngày");
        lblTableTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        btnLamMoi = new JButton("🔄 Cập nhật số liệu");
        btnLamMoi.setBackground(new Color(240, 240, 240));
        btnLamMoi.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        pnlTool.add(lblTableTitle, BorderLayout.WEST);
        pnlTool.add(btnLamMoi, BorderLayout.EAST);
        pnlTool.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        pnlTable.add(pnlTool, BorderLayout.NORTH);

        String[] cols = {"Ngày Tháng", "Số Đơn Hàng", "Số Sản Phẩm Bán", "Doanh Thu Ngày"};
        modelThongKe = new DefaultTableModel(cols, 0) {
             @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblThongKe = new JTable(modelThongKe);
        tblThongKe.setRowHeight(40);
        tblThongKe.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tblThongKe.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tblThongKe.getTableHeader().setBackground(Color.WHITE);
        tblThongKe.setSelectionBackground(new Color(230, 240, 255));
        tblThongKe.setSelectionForeground(Color.BLACK);
        tblThongKe.setShowGrid(false);
        tblThongKe.setIntercellSpacing(new Dimension(0, 0));
        
        JScrollPane sc = new JScrollPane(tblThongKe);
        sc.getViewport().setBackground(Color.WHITE);
        sc.setBorder(null);
        pnlTable.add(sc, BorderLayout.CENTER);

        add(pnlTable, BorderLayout.SOUTH);
    }

    private JPanel taoCard(String title, JLabel lblValue, Color bg, String icon) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bg);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTieuDe = new JLabel(title);
        lblTieuDe.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTieuDe.setForeground(Color.WHITE);
        
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblValue.setForeground(Color.WHITE);
        
        p.add(lblTieuDe, BorderLayout.NORTH);
        p.add(lblValue, BorderLayout.CENTER);
        return p;
    }

    // Getters
    public void setTongDoanhThu(String text) { lblTongDoanhThu.setText(text); }
    public void setTongDonHang(String text) { lblTongDonHang.setText(text); }
    public void setTongSanPham(String text) { lblTongSanPham.setText(text); }
    public DefaultTableModel getModel() { return modelThongKe; }
    public void addLamMoiListener(ActionListener l) { btnLamMoi.addActionListener(l); }
}
