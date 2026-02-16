package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class ThongKePanel extends JPanel {

    // 3 Thẻ bài tóm tắt
    private JLabel lblDoanhThu, lblSoDon, lblSanPhamBan;
    private BieuDoPanel pnlBieuDo;

    public ThongKePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(240, 242, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. HEADER (3 Thẻ bài)
        JPanel pnlHeader = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlHeader.setOpaque(false);
        pnlHeader.setPreferredSize(new Dimension(0, 120));

        pnlHeader.add(createCard("DOANH THU (Tháng này)", "0 VNĐ", new Color(40, 167, 69), "💰"));
        pnlHeader.add(createCard("TỔNG ĐƠN HÀNG", "0", new Color(23, 162, 184), "🛒"));
        pnlHeader.add(createCard("SẢN PHẨM ĐÃ BÁN", "0", new Color(255, 193, 7), "📦"));
        
        // Lưu tham chiếu để Controller set text
        lblDoanhThu = (JLabel) ((JPanel) pnlHeader.getComponent(0)).getComponent(1);
        lblSoDon = (JLabel) ((JPanel) pnlHeader.getComponent(1)).getComponent(1);
        lblSanPhamBan = (JLabel) ((JPanel) pnlHeader.getComponent(2)).getComponent(1);

        add(pnlHeader, BorderLayout.NORTH);

        // 2. BODY (Biểu đồ)
        pnlBieuDo = new BieuDoPanel("BIỂU ĐỒ DOANH THU 7 NGÀY GẦN NHẤT");
        add(pnlBieuDo, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value, Color color, String icon) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(color);
        p.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Title
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(new Color(255, 255, 255, 200));
        
        // Value
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(Color.WHITE);
        
        // Icon
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcon.setForeground(new Color(255, 255, 255, 100));

        JPanel pText = new JPanel(new GridLayout(2, 1));
        pText.setOpaque(false);
        pText.add(lblTitle);
        pText.add(lblValue);
        
        p.add(pText, BorderLayout.CENTER);
        p.add(lblIcon, BorderLayout.EAST);
        
        return p;
    }

    // --- SETTERS ---
    public void setTongDoanhThu(String text) { lblDoanhThu.setText(text); }
    public void setTongDonHang(String text) { lblSoDon.setText(text); }
    public void setTongSanPham(String text) { lblSanPhamBan.setText(text); }
    
    public void capNhatBieuDo(Map<String, Double> data) {
        pnlBieuDo.setData(data);
    }
}