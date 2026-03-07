package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.model.TaiKhoanSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TrangChuPanel extends JPanel {

    private JLabel lblRevenue;
    private JLabel lblOrders;
    private JLabel lblCustomers;

    private JPanel pnlChartContainer;
    private JTable tblRecentOrders;
    private DefaultTableModel modelRecentOrders;
    private JTextField txtSearch; 

    private final Color COLOR_BG = new Color(244, 247, 254);
    private final Color COLOR_PRIMARY = new Color(74, 38, 235);
    private final Color COLOR_TEXT_DARK = new Color(43, 54, 116);
    private final Color COLOR_TEXT_MUTED = new Color(163, 174, 208);

    public TrangChuPanel(JLabel lblRev, JLabel lblOrd, JLabel lblCust) {
        this.lblRevenue = lblRev;
        this.lblOrders = lblOrd;
        this.lblCustomers = lblCust;
        
        setLayout(new BorderLayout());
        setBackground(COLOR_BG); 
        setBorder(new EmptyBorder(30, 30, 30, 30));

        add(createHeader(), BorderLayout.NORTH);

        JPanel pnlBody = new JPanel();
        pnlBody.setLayout(new BoxLayout(pnlBody, BoxLayout.Y_AXIS));
        pnlBody.setOpaque(false);

        JPanel pnlSmallWidgets = createSmallWidgets();
        pnlBody.add(pnlSmallWidgets);
        pnlBody.add(Box.createVerticalStrut(25)); 

        JPanel pnlGrid = createWidgetGrid();
        pnlBody.add(pnlGrid);

        JScrollPane scroll = new JScrollPane(pnlBody);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COLOR_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }
    
    public TrangChuPanel() {
        this(new JLabel("0"), new JLabel("0"), new JLabel("0"));
    }

    private JPanel createHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 30, 0));

        JPanel pnlTitles = new JPanel(new GridLayout(2, 1));
        pnlTitles.setOpaque(false);
        
        String userName = "Admin";
        NhanVien nv = TaiKhoanSession.taiKhoanHienTai;
        if (nv != null) userName = nv.getHoTen();

        JLabel lblGreeting = new JLabel("Xin chào " + userName + ",");
        lblGreeting.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblGreeting.setForeground(COLOR_TEXT_MUTED);

        JLabel lblTitle = new JLabel("Chào mừng đến PNC Store!");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(COLOR_TEXT_DARK);

        pnlTitles.add(lblGreeting);
        pnlTitles.add(lblTitle);
        p.add(pnlTitles, BorderLayout.WEST);

        JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        pRight.setOpaque(false);
        
        JPanel pnlSearch = new RoundedPanel(25, Color.WHITE);
        pnlSearch.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 8));
        pnlSearch.setPreferredSize(new Dimension(300, 45));
        
        JPanel searchIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_TEXT_MUTED);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(2, 2, 10, 10);
                g2.drawLine(10, 10, 16, 16);
            }
        };
        searchIcon.setOpaque(false);
        searchIcon.setPreferredSize(new Dimension(20, 20));
        
        txtSearch = new JTextField("Tìm kiếm");
        txtSearch.setPreferredSize(new Dimension(220, 30));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(COLOR_TEXT_MUTED);
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (txtSearch.getText().trim().equals("Tìm kiếm")) {
                    txtSearch.setText(""); txtSearch.setForeground(COLOR_TEXT_DARK);
                }
            }
            public void focusLost(FocusEvent evt) {
                if (txtSearch.getText().trim().isEmpty()) {
                    txtSearch.setText("Tìm kiếm"); txtSearch.setForeground(COLOR_TEXT_MUTED);
                }
            }
        });
        
        pnlSearch.add(searchIcon);
        pnlSearch.add(txtSearch);
        pRight.add(pnlSearch);

        p.add(pRight, BorderLayout.EAST);
        return p;
    }

    private JPanel createSmallWidgets() {
        JPanel p = new JPanel(new GridLayout(1, 4, 20, 0)); 
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        styleStatLabel(lblRevenue);
        styleStatLabel(lblCustomers);
        styleStatLabel(lblOrders);

        p.add(createWidgetCard("Doanh Thu", lblRevenue, 1, false));
        p.add(createWidgetCard("Khách Hàng Mới", lblCustomers, 2, false));
        p.add(createWidgetCard("Đơn Hàng", lblOrders, 3, false));
        
        // --- BIND CLICK LISTENERS FOR TESTING ---
        addClickToIncrementListener(lblRevenue, true);
        addClickToIncrementListener(lblCustomers, false);
        addClickToIncrementListener(lblOrders, false);
        
        JLabel lblActive = new JLabel("Hệ thống Online");
        lblActive.setForeground(Color.WHITE);
        lblActive.setFont(new Font("Segoe UI", Font.BOLD, 20));
        p.add(createWidgetCard("Hoạt Động", lblActive, 4, true));

        return p;
    }

    private void styleStatLabel(JLabel lbl) {
        lbl.setForeground(COLOR_TEXT_DARK);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
    }

    private JPanel createWidgetCard(String title, JLabel valueLabel, int iconType, boolean isActive) {
        RoundedPanel card = new RoundedPanel(20, isActive ? COLOR_PRIMARY : Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel pText = new JPanel(new GridLayout(2, 1, 0, 5));
        pText.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(isActive ? new Color(255,255,255,200) : COLOR_TEXT_MUTED);
        
        pText.add(lblTitle);
        
        if(!isActive) {
            String txt = valueLabel.getText();
            txt = txt.replace("VNĐ", "").replace(" đ", "").replace(" VNĐ", "")
                     .replace(" Sản phẩm", "").replace(" Đơn", "").trim();
            valueLabel.setText(txt);
        }
        pText.add(valueLabel);

        JPanel pIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(isActive ? new Color(255,255,255,50) : new Color(244, 247, 254));
                g2.fillOval(0, 0, 50, 50);
                
                g2.setColor(isActive ? Color.WHITE : COLOR_PRIMARY);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int cx = 25, cy = 25;
                if (iconType == 1) { 
                    g2.drawLine(cx - 6, cy + 8, cx - 6, cy - 2);
                    g2.drawLine(cx, cy + 8, cx, cy - 8);
                    g2.drawLine(cx + 6, cy + 8, cx + 6, cy + 2);
                    g2.drawLine(cx - 10, cy + 8, cx + 10, cy + 8);
                } else if (iconType == 2) { 
                    g2.drawOval(cx - 5, cy - 8, 10, 10);
                    g2.drawArc(cx - 10, cy + 6, 20, 12, 0, 180);
                } else if (iconType == 3) { 
                    g2.drawRect(cx - 8, cy - 6, 16, 12);
                    g2.drawLine(cx - 8, cy - 2, cx + 8, cy - 2);
                    g2.drawOval(cx - 5, cy + 8, 2, 2);
                    g2.drawOval(cx + 3, cy + 8, 2, 2);
                } else if (iconType == 4) { 
                    g2.fillPolygon(new int[]{cx+2, cx-6, cx+2, cx-2, cx+6, cx-2}, 
                                   new int[]{cy-10, cy+2, cy+2, cy+12, cy, cy}, 6);
                }
            }
        };
        pIcon.setOpaque(false);
        pIcon.setPreferredSize(new Dimension(50, 50));

        card.add(pText, BorderLayout.WEST);
        card.add(pIcon, BorderLayout.EAST);

        return card;
    }

    private void addClickToIncrementListener(JLabel lbl, boolean isMoney) {
        lbl.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    String text = lbl.getText().replaceAll("[^0-9]", ""); // Lấy ra số
                    if(text.isEmpty()) text = "0";
                    long value = Long.parseLong(text);
                    
                    if(isMoney) {
                        value += 750000;
                        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
                        lbl.setText(df.format(value) + " đ");
                    } else {
                        value += 1;
                        lbl.setText(String.valueOf(value));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private JPanel createWidgetGrid() {
        JPanel p = new JPanel(new BorderLayout(20, 20));
        p.setOpaque(false);

        JPanel pTopRow = new JPanel(new BorderLayout(20, 0));
        pTopRow.setOpaque(false);

        RoundedPanel cardChart = new RoundedPanel(20, Color.WHITE);
        cardChart.setLayout(new BorderLayout());
        cardChart.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JLabel lblChartTitle = new JLabel("Tổng Doanh Thu");
        lblChartTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblChartTitle.setForeground(COLOR_TEXT_DARK);
        cardChart.add(lblChartTitle, BorderLayout.NORTH);

        pnlChartContainer = new JPanel(new BorderLayout());
        pnlChartContainer.setOpaque(false);
        
        JLabel lblPlaceholder = new JLabel("Đang tải biểu đồ...");
        lblPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPlaceholder.setForeground(COLOR_TEXT_MUTED);
        lblPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        
        pnlChartContainer.add(lblPlaceholder, BorderLayout.CENTER);
        cardChart.add(pnlChartContainer, BorderLayout.CENTER);

        RoundedPanel cardProfile = new RoundedPanel(20, Color.WHITE);
        cardProfile.setLayout(new BorderLayout());
        cardProfile.setPreferredSize(new Dimension(300, 0)); 
        
        JPanel pProfContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                g2.setColor(new Color(244, 247, 254));
                g2.fillOval(w/2 - 40, 10, 80, 80);
                
                g2.setColor(COLOR_PRIMARY);
                g2.fillOval(w/2 - 15, 25, 30, 30);
                g2.fillArc(w/2 - 30, 60, 60, 40, 0, 180);
            }
        };
        pProfContent.setOpaque(false);
        pProfContent.setPreferredSize(new Dimension(100, 100));

        cardProfile.add(pProfContent, BorderLayout.NORTH);
        
        JLabel lblProfName = new JLabel("PNC Store HCM", SwingConstants.CENTER);
        lblProfName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblProfName.setForeground(COLOR_TEXT_DARK);
        cardProfile.add(lblProfName, BorderLayout.CENTER);

        JPanel pnlProfStats = new JPanel(new GridLayout(1, 3));
        pnlProfStats.setOpaque(false);
        pnlProfStats.setBorder(new EmptyBorder(0, 0, 20, 0));
        pnlProfStats.add(createProfStat("Dự Án", "28"));
        pnlProfStats.add(createProfStat("Theo Dõi", "643"));
        pnlProfStats.add(createProfStat("Đang Theo", "76"));
        cardProfile.add(pnlProfStats, BorderLayout.SOUTH);

        pTopRow.add(cardChart, BorderLayout.CENTER);
        pTopRow.add(cardProfile, BorderLayout.EAST);

        p.add(pTopRow, BorderLayout.NORTH);
        
        JPanel pBotRow = new JPanel(new GridLayout(1, 2, 20, 0));
        pBotRow.setOpaque(false);
        pBotRow.setPreferredSize(new Dimension(100, 350));

        RoundedPanel cardTrans = new RoundedPanel(20, Color.WHITE);
        cardTrans.setLayout(new BorderLayout());
        cardTrans.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblTransTitle = new JLabel("Giao Dịch Gần Đây");
        lblTransTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTransTitle.setForeground(COLOR_TEXT_DARK);
        lblTransTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        cardTrans.add(lblTransTitle, BorderLayout.NORTH);

        String[] cols = {"Khách Hàng", "Tổng Tiền", "Trạng Thái"};
        modelRecentOrders = new DefaultTableModel(cols, 0);
        tblRecentOrders = new JTable(modelRecentOrders);
        tblRecentOrders.setRowHeight(40);
        tblRecentOrders.setShowGrid(false);
        tblRecentOrders.setIntercellSpacing(new Dimension(0, 0));
        tblRecentOrders.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblRecentOrders.setForeground(COLOR_TEXT_DARK);
        
        tblRecentOrders.getTableHeader().setOpaque(false);
        tblRecentOrders.getTableHeader().setBackground(Color.WHITE);
        tblRecentOrders.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblRecentOrders.getTableHeader().setForeground(COLOR_TEXT_MUTED);
        tblRecentOrders.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230,230,230)));
        
        JScrollPane sc = new JScrollPane(tblRecentOrders);
        sc.getViewport().setBackground(Color.WHITE);
        sc.setBorder(null);
        cardTrans.add(sc, BorderLayout.CENTER);

        RoundedPanel cardExtra = new RoundedPanel(20, Color.WHITE);
        cardExtra.setLayout(new BorderLayout());
        cardExtra.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JLabel lblExtraTitle = new JLabel("Lịch Trình Hôm Nay");
        lblExtraTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblExtraTitle.setForeground(COLOR_TEXT_DARK);
        cardExtra.add(lblExtraTitle, BorderLayout.NORTH);
        
        JPanel pnlTasks = new JPanel(new GridLayout(3, 1, 0, 10));
        pnlTasks.setOpaque(false);
        pnlTasks.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        pnlTasks.add(createTask("Giao Hàng", "01:00 PM - 02:00 PM"));
        pnlTasks.add(createTask("Kiểm Kho", "02:00 PM - 03:00 PM"));
        pnlTasks.add(createTask("Họp Nhân Viên", "03:00 PM - 04:00 PM"));
        
        cardExtra.add(pnlTasks, BorderLayout.CENTER);

        pBotRow.add(cardTrans);
        pBotRow.add(cardExtra);

        p.add(pBotRow, BorderLayout.CENTER);

        return p;
    }

    private JPanel createProfStat(String title, String val) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setOpaque(false);
        JLabel lTitle = new JLabel(title, SwingConstants.CENTER);
        lTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lTitle.setForeground(COLOR_TEXT_MUTED);
        JLabel lVal = new JLabel(val, SwingConstants.CENTER);
        lVal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lVal.setForeground(COLOR_TEXT_DARK);
        p.add(lTitle); p.add(lVal);
        return p;
    }

    private JPanel createTask(String title, String time) {
        JPanel p = new JPanel(new BorderLayout(15, 0));
        p.setOpaque(false);
        
        JPanel pnlBar = new JPanel();
        pnlBar.setBackground(COLOR_PRIMARY);
        pnlBar.setPreferredSize(new Dimension(4, 0));
        p.add(pnlBar, BorderLayout.WEST);
        
        JPanel pText = new JPanel(new GridLayout(2, 1));
        pText.setOpaque(false);
        JLabel lblTask = new JLabel(title);
        lblTask.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTask.setForeground(COLOR_TEXT_DARK);
        
        JLabel lblTime = new JLabel(time);
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTime.setForeground(COLOR_TEXT_MUTED);
        
        pText.add(lblTask);
        pText.add(lblTime);
        p.add(pText, BorderLayout.CENTER);
        
        return p;
    }

    public void setChart(JPanel chart) {
        pnlChartContainer.removeAll();
        if (chart != null) {
            chart.setPreferredSize(new Dimension(800, 300));
            pnlChartContainer.add(chart, BorderLayout.CENTER);
        }
        pnlChartContainer.validate();
        pnlChartContainer.repaint();
    }
    
    public void setRevenue(String text) { lblRevenue.setText(text); }
    public void setOrders(String text) { lblOrders.setText(text); }
    public void setCustomers(String text) { lblCustomers.setText(text); }
    
    public void addRecentOrder(Object[] row) {
        modelRecentOrders.addRow(row);
    }
    public void clearRecentOrders() {
        modelRecentOrders.setRowCount(0);
    }

    public String getSearchText() {
        String txt = txtSearch.getText();
        if (txt.equals("Tìm kiếm")) return "";
        return txt.trim();
    }
    public void addSearchListener(ActionListener l) {
        txtSearch.addActionListener(l); 
    }

    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bg) {
            this.cornerRadius = radius;
            this.bgColor = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            super.paintComponent(g);
        }
    }
}
