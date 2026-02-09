package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class ThongKeFrame extends JFrame {

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

    public ThongKeFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Báo Cáo Thống Kê & Biểu Đồ");
        setSize(1200, 800); // Tăng kích thước chút cho rộng
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("DASHBOARD QUẢN TRỊ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 41, 54));
        lblTitle.setBorder(new EmptyBorder(10, 0, 0, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- CENTER: CHỨA CARDS + BIỂU ĐỒ ---
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBorder(new EmptyBorder(10, 20, 10, 20));

        // 1. CARDS (TOP)
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setPreferredSize(new Dimension(0, 120));
        pnlCards.add(taoCard("DOANH THU TỔNG", lblTongDoanhThu = new JLabel("0 VNĐ"), new Color(40, 167, 69))); 
        pnlCards.add(taoCard("TỔNG ĐƠN HÀNG", lblTongDonHang = new JLabel("0"), new Color(23, 162, 184))); 
        pnlCards.add(taoCard("SẢN PHẨM ĐÃ BÁN", lblTongSanPham = new JLabel("0"), new Color(255, 193, 7))); 
        
        pnlCenter.add(pnlCards, BorderLayout.NORTH);

        // 2. BIỂU ĐỒ (CENTER) - Đây là chỗ Controller sẽ vẽ hình vào
        pnlBieuDo = new JPanel(new BorderLayout());
        pnlBieuDo.setBackground(Color.WHITE);
        pnlBieuDo.setBorder(new TitledBorder("Biểu đồ doanh thu 7 ngày gần nhất"));
        
        pnlCenter.add(pnlBieuDo, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // --- BOTTOM: BẢNG CHI TIẾT ---
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setPreferredSize(new Dimension(0, 250)); // Chiều cao bảng
        pnlTable.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        JPanel pnlTool = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLamMoi = new JButton("🔄 Cập nhật số liệu");
        pnlTool.add(btnLamMoi);
        pnlTable.add(pnlTool, BorderLayout.NORTH);

        String[] cols = {"Ngày Tháng", "Số Đơn Hàng", "Số Sản Phẩm Bán", "Doanh Thu Ngày"};
        modelThongKe = new DefaultTableModel(cols, 0);
        tblThongKe = new JTable(modelThongKe);
        tblThongKe.setRowHeight(30);
        
        JScrollPane sc = new JScrollPane(tblThongKe);
        sc.setBorder(new TitledBorder("Chi tiết số liệu"));
        pnlTable.add(sc, BorderLayout.CENTER);

        add(pnlTable, BorderLayout.SOUTH);
    }

    private JPanel taoCard(String title, JLabel lblValue, Color bg) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bg);
        p.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel lblTieuDe = new JLabel(title);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 14));
        lblTieuDe.setForeground(new Color(255, 255, 255, 200)); 
        
        lblValue.setFont(new Font("Arial", Font.BOLD, 24));
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