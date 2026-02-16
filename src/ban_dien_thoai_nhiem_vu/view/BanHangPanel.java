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
    
    public interface ProductListener {
        void onProductSelected(SanPham sp);
    }
    private ProductListener productListener;

    public BanHangPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, taoPanelTrai(), taoPanelPhai());
        splitPane.setResizeWeight(0.65);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel taoPanelTrai() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(new Color(240, 242, 245));
        
        JPanel pnlSearch = new JPanel(new BorderLayout(10, 0));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        txtTimKiem = new JTextField();
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setBackground(new Color(67, 94, 190));
        btnTimKiem.setForeground(Color.WHITE);
        
        pnlSearch.add(txtTimKiem, BorderLayout.CENTER);
        pnlSearch.add(btnTimKiem, BorderLayout.EAST);
        
        pnlDanhSachSanPham = new JPanel(new GridLayout(0, 3, 10, 10)); 
        pnlDanhSachSanPham.setBackground(new Color(240, 242, 245));
        
        JScrollPane scroll = new JScrollPane(pnlDanhSachSanPham);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        pnl.add(pnlSearch, BorderLayout.NORTH);
        pnl.add(scroll, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel taoPanelPhai() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitle = new JLabel("Giỏ Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnl.add(lblTitle, BorderLayout.NORTH);
        
        String[] headers = {"Mã", "Tên SP", "SL", "Đơn Giá", "Thành Tiền"};
        modelGioHang = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 2; } 
        };
        tblGioHang = new JTable(modelGioHang);
        tblGioHang.setRowHeight(30);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblGioHang.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tblGioHang.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        pnl.add(new JScrollPane(tblGioHang), BorderLayout.CENTER);
        
        JPanel pnlFooter = new JPanel(new GridBagLayout());
        pnlFooter.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 0, 5, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.weightx = 1.0;
        
        btnXoaSP = new JButton("Xóa Sản Phẩm Chọn");
        btnXoaSP.setForeground(Color.RED);
        btnXoaSP.setContentAreaFilled(false);
        btnXoaSP.setBorderPainted(false);
        btnXoaSP.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlFooter.add(btnXoaSP, g);
        
        txtTenKhach = new JTextField(); 
        txtTenKhach.setBorder(BorderFactory.createTitledBorder("Số ĐT Khách Hàng (Tìm kiếm)"));
        pnlFooter.add(txtTenKhach, g);
        
        JPanel pnlVoucher = new JPanel(new BorderLayout(5, 0));
        pnlVoucher.setBackground(Color.WHITE);
        txtMaVoucher = new JTextField();
        txtMaVoucher.setBorder(BorderFactory.createTitledBorder("Mã Voucher"));
        btnApDungVoucher = new JButton("Áp Dụng");
        pnlVoucher.add(txtMaVoucher, BorderLayout.CENTER);
        pnlVoucher.add(btnApDungVoucher, BorderLayout.EAST);
        pnlFooter.add(pnlVoucher, g);
        
        pnlFooter.add(new JSeparator(), g);
        lblTongTien = createLabelTotal("Tổng tiền hàng: 0 đ", false);
        pnlFooter.add(lblTongTien, g);
        
        lblGiamGia = createLabelTotal("Giảm giá: 0 đ", false);
        lblGiamGia.setForeground(new Color(40, 167, 69));
        pnlFooter.add(lblGiamGia, g);
        
        lblThanhToan = createLabelTotal("PHẢI TRẢ: 0 VNĐ", true);
        lblThanhToan.setForeground(new Color(220, 53, 69));
        lblThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pnlFooter.add(lblThanhToan, g);
        
        btnThanhToan = new JButton("THANH TOÁN");
        btnThanhToan.setBackground(new Color(67, 94, 190));
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThanhToan.setPreferredSize(new Dimension(0, 50));
        g.insets = new Insets(15, 0, 0, 0);
        pnlFooter.add(btnThanhToan, g);
        
        pnl.add(pnlFooter, BorderLayout.SOUTH);
        return pnl;
    }
    
    private JLabel createLabelTotal(String text, boolean isBold) {
        JLabel lbl = new JLabel(text, SwingConstants.RIGHT);
        lbl.setFont(new Font("Segoe UI", isBold ? Font.BOLD : Font.PLAIN, 14));
        return lbl;
    }

    // --- HÀM LOAD ẢNH THÔNG MINH (Chữa lỗi ảnh trắng xóa) ---
    private ImageIcon loadSmartImage(String path, int w, int h) {
        if (path == null || path.trim().isEmpty()) {
            return null; // Hoặc trả về ảnh mặc định
        }
        try {
            // 1. Thử load đường dẫn tuyệt đối (File trên máy)
            File f = new File(path);
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(path);
                return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
            }
            
            // 2. Thử load đường dẫn tương đối (Resource trong Jar)
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
            JPanel pItem = new JPanel(new BorderLayout(5, 5));
            pItem.setBackground(Color.WHITE);
            pItem.setBorder(new LineBorder(new Color(230, 230, 230), 1));
            pItem.setPreferredSize(new Dimension(160, 220));
            pItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel lblImg = new JLabel();
            lblImg.setHorizontalAlignment(SwingConstants.CENTER);
            lblImg.setPreferredSize(new Dimension(160, 140));
            
            // --- GỌI HÀM LOAD ẢNH THÔNG MINH ---
            ImageIcon icon = loadSmartImage(sp.getHinhAnh(), 140, 120);
            if(icon != null) {
                lblImg.setIcon(icon);
            } else {
                lblImg.setText("NO IMAGE");
            }
            
            JPanel pInfo = new JPanel(new GridLayout(3, 1));
            pInfo.setBackground(Color.WHITE);
            pInfo.setBorder(new EmptyBorder(0, 10, 10, 10));
            
            JLabel lblName = new JLabel(sp.getTenSP());
            lblName.setFont(new Font("Segoe UI", Font.BOLD, 13));
            
            JLabel lblPrice = new JLabel(df.format(sp.getGiaBan()));
            lblPrice.setForeground(new Color(67, 94, 190));
            lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            JLabel lblStock = new JLabel("Kho: " + sp.getTonKho());
            lblStock.setForeground(Color.GRAY);
            lblStock.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            
            pInfo.add(lblName);
            pInfo.add(lblPrice);
            pInfo.add(lblStock);
            
            pItem.add(lblImg, BorderLayout.CENTER);
            pItem.add(pInfo, BorderLayout.SOUTH);
            
            pItem.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    if (productListener != null) productListener.onProductSelected(sp);
                }
                @Override public void mouseEntered(MouseEvent e) { pItem.setBorder(new LineBorder(new Color(67, 94, 190), 2)); }
                @Override public void mouseExited(MouseEvent e) { pItem.setBorder(new LineBorder(new Color(230, 230, 230), 1)); }
            });
            
            pnlDanhSachSanPham.add(pItem);
        }
        pnlDanhSachSanPham.revalidate();
        pnlDanhSachSanPham.repaint();
    }

    public String getTuKhoaTimKiem() { return txtTimKiem.getText(); }
    public void setSearchText(String text) { txtTimKiem.setText(text); }
    public JTable getTblGio() { return tblGioHang; }
    public DefaultTableModel getModelGio() { return modelGioHang; }
    public String getTenKhach() { return txtTenKhach.getText(); } 
    public String getMaGiamGia() { return txtMaVoucher.getText(); }
    public void setMaGiamGia(String code) { txtMaVoucher.setText(code); }
    public void setHienThiTien(String tong, String giam, String phaiTra) {
        lblTongTien.setText("Tổng tiền hàng: " + tong);
        lblGiamGia.setText("Giảm giá: " + giam);
        lblThanhToan.setText("PHẢI TRẢ: " + phaiTra);
    }
    public void addTimKiemListener(ActionListener l) { btnTimKiem.addActionListener(l); }
    public void setProductListener(ProductListener l) { this.productListener = l; }
    public void addXoaGioListener(ActionListener l) { btnXoaSP.addActionListener(l); }
    public void addApDungMaListener(ActionListener l) { btnApDungVoucher.addActionListener(l); }
    public void addThanhToanListener(ActionListener l) { btnThanhToan.addActionListener(l); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public String getLblThanhToanText() { return lblThanhToan.getText(); }
}