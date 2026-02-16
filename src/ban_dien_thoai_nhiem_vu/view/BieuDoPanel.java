package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.text.DecimalFormat;

// Class này dùng để tự vẽ biểu đồ mà không cần thư viện ngoài
public class BieuDoPanel extends JPanel {
    private Map<String, Double> data; // Dữ liệu: Ngày -> Doanh thu
    private String title;

    public BieuDoPanel(String title) {
        this.title = title;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
        repaint(); // Vẽ lại khi có dữ liệu mới
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) {
            g.drawString("Chưa có dữ liệu thống kê...", getWidth()/2 - 50, getHeight()/2);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int padding = 50;
        int barWidth = (w - 2 * padding) / data.size() - 20; // Độ rộng cột
        
        // Tìm giá trị lớn nhất để chia tỉ lệ
        double maxVal = 0;
        for (Double val : data.values()) if (val > maxVal) maxVal = val;
        if(maxVal == 0) maxVal = 1; // Tránh chia cho 0

        // Vẽ tiêu đề
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.drawString(title, w/2 - g2.getFontMetrics().stringWidth(title)/2, 30);

        // Vẽ các cột
        int x = padding;
        DecimalFormat df = new DecimalFormat("#,###");
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            String label = entry.getKey();
            double value = entry.getValue();
            
            // Tính chiều cao cột
            int barHeight = (int) ((value / maxVal) * (h - 2 * padding - 40));

            // Vẽ cột (Màu xanh dương)
            g2.setColor(new Color(67, 94, 190));
            g2.fillRect(x, h - padding - barHeight, barWidth, barHeight);
            
            // Viền cột
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(x, h - padding - barHeight, barWidth, barHeight);

            // Vẽ số tiền trên đỉnh cột
            g2.setColor(Color.BLACK);
            String valStr = df.format(value);
            g2.drawString(valStr, x + (barWidth - g2.getFontMetrics().stringWidth(valStr))/2, h - padding - barHeight - 5);

            // Vẽ ngày dưới chân cột
            g2.drawString(label, x + (barWidth - g2.getFontMetrics().stringWidth(label))/2, h - padding + 20);

            x += barWidth + 20; // Dịch sang cột tiếp theo
        }
        
        // Vẽ trục X
        g2.setColor(Color.GRAY);
        g2.drawLine(padding - 10, h - padding, w - padding, h - padding);
    }
}