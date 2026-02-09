package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TrangChuPanel extends JPanel {

    // Labels for data binding
    private JLabel lblRevenue;
    private JLabel lblOrders;
    private JLabel lblCustomers;

    private JPanel pnlChartContainer;
    private JTable tblRecentOrders;
    private DefaultTableModel modelRecentOrders;

    public TrangChuPanel(JLabel lblRev, JLabel lblOrd, JLabel lblCust) {
        this.lblRevenue = lblRev;
        this.lblOrders = lblOrd;
        this.lblCustomers = lblCust;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); // Light Gray Background
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. Header Section
        add(createHeader(), BorderLayout.NORTH);

        // 2. Body Section
        JPanel pnlBody = new JPanel(new BorderLayout(20, 20));
        pnlBody.setOpaque(false);

        // 2.1 Stats Cards
        pnlBody.add(createStatsPanel(), BorderLayout.NORTH);

        // 2.2 Charts & Lists
        pnlBody.add(createContentPanel(), BorderLayout.CENTER);

        add(pnlBody, BorderLayout.CENTER);
    }
    
    public TrangChuPanel() {
        this(new JLabel("0 đ"), new JLabel("0"), new JLabel("0"));
    }

    private JTextField txtSearch; // Declared as field

    private JPanel createHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel lblTitle = new JLabel("Tổng Quan");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 33, 33));

        JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pRight.setOpaque(false);
        
        txtSearch = new JTextField("   Tìm kiếm...");
        txtSearch.setPreferredSize(new Dimension(200, 35));
        txtSearch.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        txtSearch.setForeground(Color.GRAY);
        
        // Placeholder logic
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().trim().equals("Tìm kiếm...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().trim().isEmpty()) {
                    txtSearch.setText("   Tìm kiếm...");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });
        
        pRight.add(txtSearch);

        // Dynamic User Name
        String userName = "Admin";
        ban_dien_thoai_nhiem_vu.model.NhanVien nv = ban_dien_thoai_nhiem_vu.model.TaiKhoanSession.taiKhoanHienTai;
        if (nv != null) {
            userName = nv.getHoTen();
        }

        JLabel lblUser = new JLabel(userName);
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 14));
        // lblUser.setIcon(...) 
        pRight.add(lblUser);

        p.add(lblTitle, BorderLayout.WEST);
        p.add(pRight, BorderLayout.EAST);
        return p;
    }

    private JPanel createStatsPanel() {
        JPanel p = new JPanel(new GridLayout(1, 4, 15, 0)); 
        p.setLayout(new GridLayout(1, 3, 25, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 140));
        
        styleLabel(lblRevenue);
        styleLabel(lblOrders);
        styleLabel(lblCustomers);

        p.add(new StatsCard("Tổng Doanh Thu", lblRevenue, "+12% so với tháng trước", new Color(41, 98, 255))); 
        p.add(new StatsCard("Tổng Đơn Hàng", lblOrders, "+5% so với tháng trước", new Color(255, 193, 7)));   
        p.add(new StatsCard("Khách Hàng Mới", lblCustomers, "+18% so với tháng trước", new Color(0, 200, 83))); 

        return p;
    }
    
    private void styleLabel(JLabel lbl) {
        lbl.setForeground(Color.BLACK);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 24)); 
    }

    private JPanel createContentPanel() {
        JPanel p = new JPanel(new GridLayout(1, 2, 20, 0));
        p.setOpaque(false);

        // Left: Chart Container
        pnlChartContainer = new JPanel(new BorderLayout());
        pnlChartContainer.setBackground(Color.WHITE);
        pnlChartContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblChartTitle = new JLabel("Biểu Đồ Doanh Thu (7 Ngày)");
        lblChartTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblChartTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnlChartContainer.add(lblChartTitle, BorderLayout.NORTH);
        
        // Label placeholder if no chart
        JLabel lblPlaceholder = new JLabel("Đang tải dữ liệu...", SwingConstants.CENTER);
        pnlChartContainer.add(lblPlaceholder, BorderLayout.CENTER);

        // Right: Recent Orders List
        JPanel pList = new JPanel(new BorderLayout());
        pList.setBackground(Color.WHITE);
        pList.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblListTitle = new JLabel("Đơn Hàng Gần Đây");
        lblListTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblListTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        pList.add(lblListTitle, BorderLayout.NORTH);

        String[] cols = {"Khách Hàng", "Tổng Tiền", "Ngày Lập"};
        modelRecentOrders = new DefaultTableModel(cols, 0);
        tblRecentOrders = new JTable(modelRecentOrders);
        tblRecentOrders.setRowHeight(40);
        tblRecentOrders.setShowGrid(false);
        tblRecentOrders.setIntercellSpacing(new Dimension(0, 0));
        tblRecentOrders.setBorder(null);
        
        // Custom Header
        tblRecentOrders.getTableHeader().setBackground(Color.WHITE);
        tblRecentOrders.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tblRecentOrders.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        JScrollPane sc = new JScrollPane(tblRecentOrders);
        sc.getViewport().setBackground(Color.WHITE);
        sc.setBorder(null);
        pList.add(sc, BorderLayout.CENTER);

        p.add(pnlChartContainer);
        p.add(pList);
        return p;
    }

    // --- Public Methods to Update Data ---
    public void setChart(org.jfree.chart.JFreeChart chart) {
        pnlChartContainer.removeAll();
        
        JLabel lblChartTitle = new JLabel("Biểu Đồ Doanh Thu (7 Ngày)");
        lblChartTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblChartTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnlChartContainer.add(lblChartTitle, BorderLayout.NORTH);
        
        org.jfree.chart.ChartPanel cp = new org.jfree.chart.ChartPanel(chart);
        cp.setPreferredSize(new Dimension(400, 300));
        pnlChartContainer.add(cp, BorderLayout.CENTER);
        
        pnlChartContainer.revalidate();
        pnlChartContainer.repaint();
    }
    
    public void addRecentOrder(Object[] row) {
        modelRecentOrders.addRow(row);
    }
    
    public void clearRecentOrders() {
        modelRecentOrders.setRowCount(0);
    }

    // --- SEARCH HELPERS ---
    public String getSearchText() {
        if (txtSearch.getText().equals("   Tìm kiếm...")) return "";
        return txtSearch.getText().trim();
    }

    public void addSearchListener(java.awt.event.ActionListener l) {
        txtSearch.addActionListener(l); 
    }

    // --- Inner Component: StatsCard ---
    class StatsCard extends JPanel {
        // Modified to accept JLabel
        public StatsCard(String title, JLabel lblValue, String sub, Color color) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(new EmptyBorder(20, 20, 20, 20));
            // In a real app, we'd use rounded borders or paintComponent for rendering

            JPanel pText = new JPanel(new GridLayout(3, 1));
            pText.setOpaque(false);
            
            JLabel lblTitle = new JLabel(title);
            lblTitle.setForeground(Color.GRAY);
            lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            // lblValue is passed from outside
            
            JLabel lblSub = new JLabel(sub);
            lblSub.setForeground(color); // Use the accent color for trend
            lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));

            pText.add(lblTitle);
            pText.add(lblValue);
            pText.add(lblSub);
            
            add(pText, BorderLayout.CENTER);
            
            // Icon (Right)
            JPanel pIcon = new JPanel();
            pIcon.setOpaque(false);
            // Circle placeholder
            JLabel lblIcon = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30)); // 30 alpha
                    g2.fillOval(0, 0, 50, 50);
                    // Draw symbol (mock)
                    g2.setColor(color);
                    g2.fillOval(20, 20, 10, 10);
                }
            };
            lblIcon.setPreferredSize(new Dimension(50, 50));
            pIcon.add(lblIcon);
            add(pIcon, BorderLayout.EAST);
        }
    }
}
