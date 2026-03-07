package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.SanPham;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class BanHangPanel extends JPanel {

    // --- Components Left ---
    private JTextField txtTimKiem;
    private JButton btnTimKiem;
    private JPanel pnlDanhSachSanPham; 
    
    // --- Components Right ---
    private JTable tblGioHang;
    private DefaultTableModel modelGioHang;
    private JButton btnXoaSP;
    private JTextField txtTenKhach; 
    private JTextField txtMaVoucher;
    private JButton btnApDungVoucher;
    private JLabel lblTongTien, lblGiamGia, lblThanhToan;
    private JButton btnThanhToan;
    
    // UI Constants
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_SUCCESS = new Color(25, 135, 84);
    private final Color COLOR_DANGER = new Color(220, 53, 69);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);
    
    public interface ProductListener {
        void onProductSelected(SanPham sp);
    }
    private ProductListener productListener;

    public BanHangPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, taoPanelTrai(), taoPanelPhai());
        splitPane.setResizeWeight(0.68); // Trái danh sách lấy 68% không gian
        splitPane.setDividerSize(0);
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel taoPanelTrai() {
        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(0, 0, 0, 15)); // Cách phải 15px
        
        // Search Banner
        JPanel pnlSearch = new JPanel(new BorderLayout(15, 0));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        txtTimKiem = new JTextField();
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        btnTimKiem = createFlatButton("🔍 Tìm kiếm", COLOR_PRIMARY, Color.WHITE);
        btnTimKiem.setPreferredSize(new Dimension(130, 42));
        
        pnlSearch.add(txtTimKiem, BorderLayout.CENTER);
        pnlSearch.add(btnTimKiem, BorderLayout.EAST);
        
        // Product Grid
        pnlDanhSachSanPham = new JPanel(new GridLayout(0, 3, 15, 15)); 
        pnlDanhSachSanPham.setOpaque(false);
        
        JScrollPane scroll = new JScrollPane(pnlDanhSachSanPham);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        pnl.add(pnlSearch, BorderLayout.NORTH);
        pnl.add(scroll, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel taoPanelPhai() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblTitle = new JLabel("🛒 Đơn Hàng Mới");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        lblTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        pnl.add(lblTitle, BorderLayout.NORTH);
        
        // TABLE GIỎ HÀNG
        String[] headers = {"Mã", "Tên SP", "SL", "Đơn Giá", "Thành Tiền"};
        modelGioHang = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 2; } 
        };
        tblGioHang = new JTable(modelGioHang);
        tblGioHang.setRowHeight(40);
        tblGioHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblGioHang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblGioHang.getTableHeader().setBackground(Color.WHITE);
        tblGioHang.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_TABLE_BORDER));
        tblGioHang.setShowGrid(false);
        tblGioHang.setIntercellSpacing(new Dimension(0, 0));
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblGioHang.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tblGioHang.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        // Thu hẹp cột SL
        tblGioHang.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblGioHang.getColumnModel().getColumn(2).setPreferredWidth(40);

        JScrollPane sc = new JScrollPane(tblGioHang);
        sc.setBorder(null);
        sc.getViewport().setBackground(Color.WHITE);
        
        JPanel pTableWrap = new JPanel(new BorderLayout());
        pTableWrap.setOpaque(false);
        pTableWrap.setBorder(new EmptyBorder(15, 0, 15, 0));
        pTableWrap.add(sc, BorderLayout.CENTER);
        
        pnl.add(pTableWrap, BorderLayout.CENTER);
        
        // FOOTER TÍNH TIỀN (POS STYLE)
        JPanel pnlFooter = new JPanel(new GridBagLayout());
        pnlFooter.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 0, 8, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.weightx = 1.0;
        
        btnXoaSP = new JButton("🗑 Xóa Chọn");
        btnXoaSP.setForeground(COLOR_DANGER);
        btnXoaSP.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXoaSP.setContentAreaFilled(false);
        btnXoaSP.setBorderPainted(false);
        btnXoaSP.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlFooter.add(btnXoaSP, g);
        
        // Khách hàng & Voucher Input Form
        JPanel pnlCustomer = new JPanel(new BorderLayout(10, 5));
        pnlCustomer.setBackground(Color.WHITE);
        JLabel lblSDT = new JLabel("SĐT Khách Hàng / Quẹt thẻ thành viên:");
        lblSDT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSDT.setForeground(COLOR_TEXT_MUTED);
        txtTenKhach = new JTextField(); 
        styleInputField(txtTenKhach);
        pnlCustomer.add(lblSDT, BorderLayout.NORTH);
        pnlCustomer.add(txtTenKhach, BorderLayout.CENTER);
        pnlFooter.add(pnlCustomer, g);
        
        JPanel pnlVoucher = new JPanel(new BorderLayout(10, 5));
        pnlVoucher.setBackground(Color.WHITE);
        JLabel lblVoucher = new JLabel("Khuyến mãi (Quét mã vạch thẻ Voucher):");
        lblVoucher.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblVoucher.setForeground(COLOR_TEXT_MUTED);
        
        txtMaVoucher = new JTextField();
        styleInputField(txtMaVoucher);
        
        btnApDungVoucher = createFlatButton("Áp Dụng", Color.DARK_GRAY, Color.WHITE);
        
        JPanel pVwrap = new JPanel(new BorderLayout(10, 0));
        pVwrap.setOpaque(false);
        pVwrap.add(txtMaVoucher, BorderLayout.CENTER);
        pVwrap.add(btnApDungVoucher, BorderLayout.EAST);
        
        pnlVoucher.add(lblVoucher, BorderLayout.NORTH);
        pnlVoucher.add(pVwrap, BorderLayout.CENTER);
        pnlFooter.add(pnlVoucher, g);
        
        // Đường line gạch xéo như hóa đơn
        JSeparator st = new JSeparator();
        st.setForeground(Color.BLACK);
        g.insets = new Insets(15, 0, 10, 0);
        pnlFooter.add(st, g);
        
        // Tính tiền block
        g.insets = new Insets(5, 0, 5, 0);
        lblTongTien = createLabelTotal("TỔNG TIỀN HÀNG:", "0 đ", COLOR_TEXT_DARK, 15);
        pnlFooter.add(lblTongTien, g);
        
        lblGiamGia = createLabelTotal("CHIẾT KHẤU:", "- 0 đ", COLOR_SUCCESS, 15);
        pnlFooter.add(lblGiamGia, g);
        
        lblThanhToan = createLabelTotal("KHÁCH PHẢI TRẢ:", "0 VNĐ", COLOR_DANGER, 22);
        g.insets = new Insets(10, 0, 15, 0);
        pnlFooter.add(lblThanhToan, g);
        
        btnThanhToan = createFlatButton("THANH TOÁN (F8)", COLOR_SUCCESS, Color.WHITE);
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnThanhToan.setPreferredSize(new Dimension(0, 55));
        g.insets = new Insets(10, 0, 0, 0);
        pnlFooter.add(btnThanhToan, g);
        
        pnl.add(pnlFooter, BorderLayout.SOUTH);
        return pnl;
    }
    
    private void styleInputField(JTextField txt) {
        txt.setPreferredSize(new Dimension(0, 40));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private JLabel createLabelTotal(String title, String value, Color colorVal, int fontSize) {
        // Pseudo-Layout using HTML for left/right alignment
        JLabel lbl = new JLabel("<html><table width='350'><tr><td align='left'>" + title + "</td><td align='right'><b><font color='" + hex(colorVal) + "'>" + value + "</font></b></td></tr></table></html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        return lbl;
    }
    private String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
    
    // --- HÀM LOAD ẢNH THÔNG MINH (Chữa lỗi ảnh trắng xóa) ---
    private ImageIcon loadSmartImage(String path, int w, int h) {
        if (path == null || path.trim().isEmpty()) {
            return null; 
        }
        try {
            File f = new File(path);
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(path);
                return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
            }
            java.net.URL url = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/" + path);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {}
        return null;
    }

    public void hienThiDanhSachSanPham(List<SanPham> list) {
        pnlDanhSachSanPham.removeAll();
        
        DecimalFormat df = new DecimalFormat("#,### đ");
        for (SanPham sp : list) {
            JPanel pItem = new JPanel(new BorderLayout(0, 0));
            pItem.setBackground(Color.WHITE);
            // Border radius effect not possible with line border, but we can do rounded line borders
            pItem.setBorder(BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1, true)); 
            pItem.setPreferredSize(new Dimension(160, 220));
            pItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel lblImg = new JLabel();
            lblImg.setHorizontalAlignment(SwingConstants.CENTER);
            lblImg.setPreferredSize(new Dimension(160, 150));
            
            ImageIcon icon = loadSmartImage(sp.getHinhAnh(), 120, 120);
            if(icon != null) {
                lblImg.setIcon(icon);
            } else {
                lblImg.setText("NO IMAGE");
                lblImg.setForeground(Color.LIGHT_GRAY);
            }
            
            JPanel pInfo = new JPanel(new GridLayout(3, 1));
            pInfo.setOpaque(false);
            pInfo.setBorder(new EmptyBorder(10, 15, 10, 15));
            
            JLabel lblName = new JLabel(sp.getTenSP());
            lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblName.setForeground(COLOR_TEXT_DARK);
            
            JLabel lblPrice = new JLabel(df.format(sp.getGiaBan()));
            lblPrice.setForeground(COLOR_DANGER); // Giá màu đỏ
            lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            JLabel lblStock = new JLabel("Kho h.tại: " + sp.getTonKho());
            lblStock.setForeground(COLOR_TEXT_MUTED);
            lblStock.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            pInfo.add(lblName);
            pInfo.add(lblPrice);
            pInfo.add(lblStock);
            
            pItem.add(lblImg, BorderLayout.CENTER);
            pItem.add(pInfo, BorderLayout.SOUTH);
            
            // Hover effect
            pItem.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    if (productListener != null) productListener.onProductSelected(sp);
                }
                @Override public void mouseEntered(MouseEvent e) { pItem.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY, 2, true)); }
                @Override public void mouseExited(MouseEvent e) { pItem.setBorder(BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1, true)); }
            });
            
            pnlDanhSachSanPham.add(pItem);
        }
        
        // Pad with empty panels if list is less than 3 to keep grid size uniform
        int remainder = list.size() % 3;
        if(remainder != 0 && list.size() > 0) {
            for(int i=0; i < (3 - remainder); i++) {
                JPanel empty = new JPanel(); empty.setOpaque(false);
                pnlDanhSachSanPham.add(empty);
            }
        }

        pnlDanhSachSanPham.revalidate();
        pnlDanhSachSanPham.repaint();
    }
    
    private JButton createFlatButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public String getTuKhoaTimKiem() { return txtTimKiem.getText(); }
    public void setSearchText(String text) { txtTimKiem.setText(text); }
    public JTable getTblGio() { return tblGioHang; }
    public DefaultTableModel getModelGio() { return modelGioHang; }
    public String getTenKhach() { return txtTenKhach.getText(); } 
    public String getMaGiamGia() { return txtMaVoucher.getText(); }
    public void setMaGiamGia(String code) { txtMaVoucher.setText(code); }
    public void setHienThiTien(String tong, String giam, String phaiTra) {
        // Need to extract raw text as we modified it to be HTML previously in old logic.
        // It's safer to re-create the label fully in the modern logic.
        // Or simpler: Re-use method
        lblTongTien.setText(createLabelTotal("TỔNG TIỀN HÀNG:", tong, COLOR_TEXT_DARK, 15).getText());
        lblGiamGia.setText(createLabelTotal("CHIẾT KHẤU:", "- " + giam, COLOR_SUCCESS, 15).getText());
        lblThanhToan.setText(createLabelTotal("KHÁCH PHẢI TRẢ:", phaiTra, COLOR_DANGER, 22).getText());
    }
    public void addTimKiemListener(ActionListener l) { btnTimKiem.addActionListener(l); }
    public void setProductListener(ProductListener l) { this.productListener = l; }
    public void addXoaGioListener(ActionListener l) { btnXoaSP.addActionListener(l); }
    public void addApDungMaListener(ActionListener l) { btnApDungVoucher.addActionListener(l); }
    public void addThanhToanListener(ActionListener l) { btnThanhToan.addActionListener(l); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    // Hack to return bare text without HTML tags for the controller if it reads it directly.
    public String getLblThanhToanText() { return lblThanhToan.getText().replaceAll("<[^>]*>", ""); }
}
