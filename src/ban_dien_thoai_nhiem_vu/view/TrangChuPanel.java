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
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class TrangChuPanel extends JPanel {

    // Labels for data binding (Nhận từ MainFrame)
    private JLabel lblRevenue;
    private JLabel lblOrders;
    private JLabel lblCustomers;

    private JPanel pnlChartContainer;
    private JTable tblRecentOrders;
    private DefaultTableModel modelRecentOrders;
    private JTextField txtSearch; 

    // Constructor chính
    public TrangChuPanel(JLabel lblRev, JLabel lblOrd, JLabel lblCust) {
        this.lblRevenue = lblRev;
        this.lblOrders = lblOrd;
        this.lblCustomers = lblCust;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); // Màu nền xám nhẹ
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. Header Section
        add(createHeader(), BorderLayout.NORTH);

        // 2. Body Section
        JPanel pnlBody = new JPanel(new BorderLayout(20, 20));
        pnlBody.setOpaque(false);

        // 2.1 Stats Cards (3 ô số liệu)
        pnlBody.add(createStatsPanel(), BorderLayout.NORTH);

        // 2.2 Charts & Lists (Biểu đồ + Bảng)
        pnlBody.add(createContentPanel(), BorderLayout.CENTER);

        add(pnlBody, BorderLayout.CENTER);
    }
    
    // Constructor mặc định (để tránh lỗi nếu gọi không tham số)
    public TrangChuPanel() {
        this(new JLabel("0 đ"), new JLabel("0"), new JLabel("0"));
    }

    private JPanel createHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel lblTitle = new JLabel("Tổng Quan Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 33, 33));

        JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pRight.setOpaque(false);
        
        txtSearch = new JTextField(" Tìm kiếm...");
        txtSearch.setPreferredSize(new Dimension(250, 40));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(Color.GRAY);
        
        // Placeholder logic
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (txtSearch.getText().trim().equals("Tìm kiếm...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent evt) {
                if (txtSearch.getText().trim().isEmpty()) {
                    txtSearch.setText(" Tìm kiếm...");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });
        
        pRight.add(txtSearch);

        // Lấy tên người dùng đang đăng nhập
        String userName = "Admin";
        NhanVien nv = TaiKhoanSession.taiKhoanHienTai;
        if (nv != null) {
            userName = nv.getHoTen();
        }

        JLabel lblUser = new JLabel("Xin chào, " + userName);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblUser.setForeground(new Color(50, 50, 50));
        pRight.add(lblUser);

        p.add(lblTitle, BorderLayout.WEST);
        p.add(pRight, BorderLayout.EAST);
        return p;
    }

    private JPanel createStatsPanel() {
        JPanel p = new JPanel(new GridLayout(1, 3, 25, 0)); // Grid 3 cột
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 140));
        
        styleLabel(lblRevenue);
        styleLabel(lblOrders);
        styleLabel(lblCustomers);

        // Tạo 3 thẻ Card màu sắc
        p.add(new StatsCard("Tổng Doanh Thu", lblRevenue, "Tháng này", new Color(41, 98, 255))); 
        p.add(new StatsCard("Tổng Đơn Hàng", lblOrders, "Tháng này", new Color(255, 193, 7)));    
        p.add(new StatsCard("Khách Hàng", lblCustomers, "Tổng số", new Color(40, 167, 69))); 

        return p;
    }
    
    private void styleLabel(JLabel lbl) {
        lbl.setForeground(new Color(50, 50, 50));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 26)); 
    }

    private JPanel createContentPanel() {
        JPanel p = new JPanel(new GridLayout(1, 2, 20, 0));
        p.setOpaque(false);

        // LEFT: Chart Container (Biểu đồ)
        pnlChartContainer = new JPanel(new BorderLayout());
        pnlChartContainer.setBackground(Color.WHITE);
        pnlChartContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblChartTitle = new JLabel("Biểu Đồ Doanh Thu (7 Ngày Gần Nhất)");
        lblChartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblChartTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnlChartContainer.add(lblChartTitle, BorderLayout.NORTH);
        
        JLabel lblPlaceholder = new JLabel("Đang tải dữ liệu...", SwingConstants.CENTER);
        pnlChartContainer.add(lblPlaceholder, BorderLayout.CENTER);

        // RIGHT: Recent Orders List (Bảng đơn hàng)
        JPanel pList = new JPanel(new BorderLayout());
        pList.setBackground(Color.WHITE);
        pList.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblListTitle = new JLabel("Đơn Hàng Mới Nhất");
        lblListTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblListTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        pList.add(lblListTitle, BorderLayout.NORTH);

        String[] cols = {"Khách Hàng", "Tổng Tiền", "Ngày Lập"};
        modelRecentOrders = new DefaultTableModel(cols, 0);
        tblRecentOrders = new JTable(modelRecentOrders);
        tblRecentOrders.setRowHeight(35);
        tblRecentOrders.setShowGrid(false);
        tblRecentOrders.setIntercellSpacing(new Dimension(0, 0));
        tblRecentOrders.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Custom Header Bảng
        tblRecentOrders.getTableHeader().setBackground(new Color(248, 249, 250));
        tblRecentOrders.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblRecentOrders.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        JScrollPane sc = new JScrollPane(tblRecentOrders);
        sc.getViewport().setBackground(Color.WHITE);
        sc.setBorder(null);
        pList.add(sc, BorderLayout.CENTER);

        p.add(pnlChartContainer);
        p.add(pList);
        return p;
    }

    // --- CÁC HÀM PUBLIC ĐỂ CONTROLLER GỌI ---
    
    public void setChart(JFreeChart chart) {
        pnlChartContainer.removeAll();
        
        JLabel lblChartTitle = new JLabel("Biểu Đồ Doanh Thu");
        lblChartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblChartTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnlChartContainer.add(lblChartTitle, BorderLayout.NORTH);
        
        if (chart != null) {
            ChartPanel cp = new ChartPanel(chart);
            cp.setPreferredSize(new Dimension(400, 300));
            pnlChartContainer.add(cp, BorderLayout.CENTER);
        }
        
        pnlChartContainer.validate();
        pnlChartContainer.repaint();
    }
    
    public void addRecentOrder(Object[] row) {
        modelRecentOrders.addRow(row);
    }
    
    public void clearRecentOrders() {
        modelRecentOrders.setRowCount(0);
    }

    public String getSearchText() {
        String txt = txtSearch.getText();
        if (txt.equals(" Tìm kiếm...") || txt.equals("Tìm kiếm...")) return "";
        return txt.trim();
    }

    public void addSearchListener(ActionListener l) {
        txtSearch.addActionListener(l); 
    }

    // --- CLASS CON: THẺ THỐNG KÊ (INNER CLASS) ---
    class StatsCard extends JPanel {
        public StatsCard(String title, JLabel lblValue, String sub, Color color) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            // Vẽ viền màu dưới đáy cho đẹp
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, color),
                new EmptyBorder(20, 20, 20, 20)
            ));

            JPanel pText = new JPanel(new GridLayout(3, 1));
            pText.setOpaque(false);
            
            JLabel lblTitle = new JLabel(title);
            lblTitle.setForeground(Color.GRAY);
            lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            // lblValue được truyền từ ngoài vào (Binding)
            
            JLabel lblSub = new JLabel(sub);
            lblSub.setForeground(color); 
            lblSub.setFont(new Font("Segoe UI", Font.BOLD, 12));

            pText.add(lblTitle);
            pText.add(lblValue);
            pText.add(lblSub);
            
            add(pText, BorderLayout.CENTER);
            
            // Icon tròn bên phải
            JPanel pIcon = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30)); // Độ trong suốt 30
                    g2.fillOval(0, 0, 50, 50);
                    
                    g2.setColor(color);
                    g2.fillOval(20, 20, 10, 10); // Chấm tròn nhỏ ở giữa
                }
            };
            pIcon.setOpaque(false);
            pIcon.setPreferredSize(new Dimension(50, 50));
            add(pIcon, BorderLayout.EAST);
        }
    }
}