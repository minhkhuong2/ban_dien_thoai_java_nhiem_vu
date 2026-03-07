package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class ThongKePanel extends JPanel {

    private JLabel lblDoanhThu, lblSoDon, lblSanPhamBan;
    private BieuDoPanel pnlBieuDo;

    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_SUCCESS = new Color(25, 135, 84);
    private final Color COLOR_WARNING = new Color(255, 193, 7);

    public ThongKePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. HEADER (Title)
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("BÁO CÁO & THỐNG KÊ CHI TIẾT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. MAIN CONTENT (Split: Stats Top, Chart Bottom)
        JPanel pnlContent = new JPanel(new BorderLayout(0, 20));
        pnlContent.setOpaque(false);

        // -- Top Side: KPI Cards (Horizontal) --
        JPanel pnlKPI = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlKPI.setOpaque(false);
        pnlKPI.setPreferredSize(new Dimension(0, 140)); // Fixed height for cards

        pnlKPI.add(createModernCard("TỔNG DOANH THU", "0 đ", COLOR_SUCCESS));
        pnlKPI.add(createModernCard("TỔNG ĐƠN HÀNG", "0", COLOR_PRIMARY));
        pnlKPI.add(createModernCard("SẢN PHẨM ĐÃ BÁN", "0", COLOR_WARNING));

        // Card 1
        JPanel card1 = (JPanel) pnlKPI.getComponent(0);
        JPanel pText1 = (JPanel) ((BorderLayout)card1.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        lblDoanhThu = (JLabel) pText1.getComponent(1);
        
        // Card 2
        JPanel card2 = (JPanel) pnlKPI.getComponent(1);
        JPanel pText2 = (JPanel) ((BorderLayout)card2.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        lblSoDon = (JLabel) pText2.getComponent(1);
        
        // Card 3
        JPanel card3 = (JPanel) pnlKPI.getComponent(2);
        JPanel pText3 = (JPanel) ((BorderLayout)card3.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        lblSanPhamBan = (JLabel) pText3.getComponent(1);
        
        
        pnlContent.add(pnlKPI, BorderLayout.NORTH);

        // -- Bottom Side: Chart --
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(Color.WHITE);
        chartContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        pnlBieuDo = new BieuDoPanel("Biểu đồ Doanh Thu 7 Ngày Qua");
        chartContainer.add(pnlBieuDo, BorderLayout.CENTER);
        
        pnlContent.add(chartContainer, BorderLayout.CENTER);

        add(pnlContent, BorderLayout.CENTER);
    }

    private JPanel createModernCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Tạo thiết kế phẳng dạng phẳng (Flat Design) có đường viền Top accent (cạnh viền màu nổi phía trên)
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(6, 0, 0, 0, accentColor),
                new EmptyBorder(25, 25, 25, 25)
            )
        ));

        JPanel pText = new JPanel(new GridLayout(2, 1, 0, 8));
        pText.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(COLOR_TEXT_MUTED);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValue.setForeground(COLOR_TEXT_DARK);

        pText.add(lblTitle);
        pText.add(lblValue);

        card.add(pText, BorderLayout.CENTER);
        return card;
    }

    public void setTongDoanhThu(String text) { 
        lblDoanhThu.setText(text); 
    }
    public void setTongDonHang(String text) { 
        // Bỏ chữ " Đơn" thừa từ controller nếu có để số liệu hiển thị lớn và đẹp hơn
        String numText = text.replace(" Đơn", "").trim();
        lblSoDon.setText(numText); 
    }
    public void setTongSanPham(String text) { 
        String numText = text.replace(" Sản phẩm", "").trim();
        lblSanPhamBan.setText(numText); 
    }
    
    public void capNhatBieuDo(Map<String, Double> data) {
        pnlBieuDo.setData(data);
    }
}
