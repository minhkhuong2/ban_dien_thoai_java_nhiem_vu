package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.text.DecimalFormat;

public class BieuDoPanel extends JPanel {
    private Map<String, Double> data; 
    private String title;
    
    // Venus Colors
    private final Color COLOR_PRIMARY = new Color(74, 38, 235); // #4A26EB
    private final Color COLOR_PRIMARY_LIGHT = new Color(74, 38, 235, 30); 
    
    private final Color COLOR_TEXT_DARK = new Color(43, 54, 116);
    private final Color COLOR_TEXT_MUTED = new Color(163, 174, 208);

    public BieuDoPanel(String title) {
        this.title = title;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20)); 
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
        repaint(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) {
            g.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g.setColor(COLOR_TEXT_MUTED);
            g.drawString("Chưa có dữ liệu thống kê...", getWidth()/2 - 80, getHeight()/2);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int paddingBottom = 60;
        int paddingTop = 90;
        int paddingSide = 50;
        
        int barWidth = 45; 
        int totalSpacing = (w - 2 * paddingSide) / data.size();

        // 1. Draw Title Area
        g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2.setColor(COLOR_TEXT_MUTED);
        g2.drawString(title, 30, 40);
        
        g2.setFont(new Font("Segoe UI", Font.BOLD, 34));
        g2.setColor(COLOR_TEXT_DARK);
        
        double totalSum = 0;
        double maxVal = 0;
        for (Double val : data.values()) {
            totalSum += val;
            if (val > maxVal) maxVal = val;
        }
        if(maxVal == 0) maxVal = 1;
        
        DecimalFormat df = new DecimalFormat("#,###");
        String totalStr = "$" + df.format(totalSum);
        g2.drawString(totalStr, 30, 85);
        
        int targetY = paddingTop + (h - paddingTop - paddingBottom) / 3;
        
        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6}, 0);
        g2.setStroke(dashed);
        g2.setColor(COLOR_PRIMARY);
        g2.drawLine(paddingSide, targetY, w - paddingSide, targetY);
        
        g2.setStroke(new BasicStroke(1)); 
        
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2.drawString("Mục Tiêu Tuần", w - paddingSide - 90, targetY - 10);

        // 2. Draw Bars
        int xCenter = paddingSide + totalSpacing / 2;
        
        int currentIndex = 0;
        int highestIndex = -1;
        
        int i=0;
        for (Double val : data.values()) {
            if (val == maxVal) highestIndex = i;
            i++;
        }

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            String label = entry.getKey();
            double value = entry.getValue();
            
            int maxBarHeight = h - paddingTop - paddingBottom - 20;
            int barHeight = (int) ((value / maxVal) * maxBarHeight);
            
            int bx = xCenter - (barWidth/2);
            int by = h - paddingBottom - barHeight;

            if (currentIndex == highestIndex) {
                g2.setColor(COLOR_PRIMARY); 
            } else {
                g2.setColor(COLOR_PRIMARY_LIGHT); 
            }
            g2.fillRoundRect(bx, by, barWidth, barHeight, 20, 20); 
            
            g2.setColor(COLOR_TEXT_MUTED);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, xCenter - labelWidth/2, h - 20);

            xCenter += totalSpacing;
            currentIndex++;
        }
    }
}
