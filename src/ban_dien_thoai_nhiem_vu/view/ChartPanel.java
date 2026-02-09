package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

public class ChartPanel extends JPanel {
    private int[] values;
    private Color barColor = new Color(41, 98, 255); // Blue primary

    public ChartPanel() {
        // Mock data
        values = new int[12];
        Random rand = new Random();
        for (int i = 0; i < 12; i++) {
            values[i] = rand.nextInt(100) + 20;
        }
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 250));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int pad = 40;
        
        // Draw axis lines
        g2.setColor(new Color(230, 230, 230));
        g2.drawLine(pad, h - pad, w - pad, h - pad); // X axis
        
        // Draw bars
        int barWidth = (w - 2 * pad) / values.length - 10;
        int maxVal = 150; // Scale
        
        for (int i = 0; i < values.length; i++) {
            int barHeight = (int) ((double) values[i] / maxVal * (h - 2 * pad));
            int x = pad + i * (barWidth + 10) + 5;
            int y = h - pad - barHeight;
            
            // Draw shadow
            // g2.setColor(new Color(240, 240, 250));
            // g2.fill(new RoundRectangle2D.Double(x, pad, barWidth, h - 2 * pad, 10, 10));

            // Draw bar
            if (i % 2 == 0) g2.setColor(barColor);
            else g2.setColor(new Color(255, 193, 7)); // Accent color
            
            g2.fill(new RoundRectangle2D.Double(x, y, barWidth, barHeight, 8, 8));
            
            // Label
            // String[] months = {"J","F","M","A","M","J","J","A","S","O","N","D"};
            // g2.setColor(Color.GRAY);
            // g2.drawString(months[i], x + barWidth/2 - 5, h - pad + 15);
        }
    }
}
