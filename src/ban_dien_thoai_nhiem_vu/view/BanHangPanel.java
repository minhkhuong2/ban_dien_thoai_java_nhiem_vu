package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.SanPham;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.text.DecimalFormat;

public class BanHangPanel extends JPanel {

    // --- LINH KIỆN ---
    private JTextField txtTimKiemSP;
    private JPanel pnlProductContainer; 
    private JButton btnTimKiem;

    // Bên phải
    private JTable tblGioHang;
    private DefaultTableModel modelGio;
    private JTextField txtTenKhach, txtSdtKhach, txtMaGiamGia; 
    private JLabel lblTongTien, lblGiamGia, lblThanhToan; 
    private JButton btnThanhToan, btnXoaKhoiGio, btnApDungMa; 

    public void setMaGiamGia(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Interface để Controller gọi khi click vào Product Card
    public interface ProductCardListener {
        void onProductSelected(SanPham sp);
    }
    private ProductCardListener productListener;

    public BanHangPanel() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // === PANEL TRÁI: KHO HÀNG (GRID) ===
        JPanel pnlTrai = new JPanel(new BorderLayout(10, 10));
        pnlTrai.setBackground(new Color(245, 247, 250));
        pnlTrai.setPreferredSize(new Dimension(750, 0)); // 60% approx
        
        // Tìm kiếm
        JPanel pnlTimKiem = new JPanel(new BorderLayout(5, 5));
        pnlTimKiem.setBackground(Color.WHITE);
        pnlTimKiem.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        txtTimKiemSP = new JTextField();
        txtTimKiemSP.setPreferredSize(new Dimension(0, 40));
        txtTimKiemSP.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        btnTimKiem = new JButton("Tìm Kiếm");
        btnTimKiem.setBackground(new Color(41, 98, 255));
        btnTimKiem.setForeground(Color.BLACK);
        btnTimKiem.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        
        pnlTimKiem.add(txtTimKiemSP, BorderLayout.CENTER);
        pnlTimKiem.add(btnTimKiem, BorderLayout.EAST);
        pnlTrai.add(pnlTimKiem, BorderLayout.NORTH);

        // Container Sản Phẩm (Grid 3 Cột)
        // Lưu ý: GridLayout(0, 3) nghĩa là 0 dòng (tự động) và 3 cột
        pnlProductContainer = new JPanel(new GridLayout(0, 3, 15, 15)); 
        pnlProductContainer.setBackground(new Color(245, 247, 250));
        
        JScrollPane scKho = new JScrollPane(pnlProductContainer);
        scKho.setBorder(null);
        scKho.getVerticalScrollBar().setUnitIncrement(16);
        pnlTrai.add(scKho, BorderLayout.CENTER);

        // === PANEL PHẢI: GIỎ HÀNG ===
        JPanel pnlPhai = new JPanel(new BorderLayout(10, 10));
        pnlPhai.setBackground(Color.WHITE);
        pnlPhai.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header Cart
        JLabel lblCartTitle = new JLabel("Giỏ Hàng");
        lblCartTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        pnlPhai.add(lblCartTitle, BorderLayout.NORTH);

        // Bảng giỏ hàng
        String[] colsGio = {"Mã SP", "Tên SP", "SL", "Đơn Giá", "Thành Tiền"};
        modelGio = new DefaultTableModel(colsGio, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Chỉ cho sửa cột SỐ LƯỢNG
            }
        };
        tblGioHang = new JTable(modelGio);
        tblGioHang.setRowHeight(35);
        tblGioHang.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tblGioHang.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        
        // Cấu hình chiều rộng cột
        tblGioHang.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblGioHang.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblGioHang.getColumnModel().getColumn(2).setPreferredWidth(30);

        JScrollPane scGio = new JScrollPane(tblGioHang);
        scGio.setBorder(BorderFactory.createEmptyBorder());
        scGio.getViewport().setBackground(Color.WHITE);
        pnlPhai.add(scGio, BorderLayout.CENTER);
        
        // ACTION BUTTONS (Xóa)
        btnXoaKhoiGio = new JButton("Xóa Sản Phẩm Chọn");
        btnXoaKhoiGio.setBackground(new Color(255, 235, 238));
        btnXoaKhoiGio.setForeground(Color.RED);
        btnXoaKhoiGio.setBorder(null);
        btnXoaKhoiGio.setPreferredSize(new Dimension(0, 30));
        
        // --- FORM THANH TOÁN (BOTTOM RIGHT) ---
        JPanel pnlCheckout = new JPanel();
        pnlCheckout.setLayout(new BoxLayout(pnlCheckout, BoxLayout.Y_AXIS));
        pnlCheckout.setBackground(Color.WHITE);
        pnlCheckout.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        pnlCheckout.add(btnXoaKhoiGio);
        pnlCheckout.add(Box.createVerticalStrut(15));
        
        pnlCheckout.add(createInputRow("Khách Hàng:", txtTenKhach = new JTextField()));
        pnlCheckout.add(Box.createVerticalStrut(10));
        pnlCheckout.add(createInputRow("Số ĐT:", txtSdtKhach = new JTextField()));
        pnlCheckout.add(Box.createVerticalStrut(10));
        
        // Coupon
        JPanel pMa = new JPanel(new BorderLayout(5, 0)); 
        pMa.setBackground(Color.WHITE);
        pMa.add(new JLabel("Mã Voucher: "), BorderLayout.WEST);
        txtMaGiamGia = new JTextField();
        btnApDungMa = new JButton("Áp Dụng");
        btnApDungMa.setBackground(new Color(255, 193, 7)); // Yellow
        pMa.add(txtMaGiamGia, BorderLayout.CENTER);
        pMa.add(btnApDungMa, BorderLayout.EAST);
        pnlCheckout.add(pMa);
        pnlCheckout.add(Box.createVerticalStrut(15));

        // Totals
        lblTongTien = new JLabel("Tổng: 0 đ");
        lblGiamGia = new JLabel("Giảm: 0 đ");
        lblGiamGia.setForeground(new Color(40, 167, 69));
        
        lblThanhToan = new JLabel("PHẢI TRẢ: 0 đ", SwingConstants.CENTER);
        lblThanhToan.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblThanhToan.setForeground(new Color(220, 53, 69));
        
        pnlCheckout.add(lblTongTien);
        pnlCheckout.add(lblGiamGia);
        pnlCheckout.add(Box.createVerticalStrut(10));
        pnlCheckout.add(lblThanhToan);
        pnlCheckout.add(Box.createVerticalStrut(15));

        // Checkout Button
        btnThanhToan = new JButton("THANH TOÁN");
        btnThanhToan.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnThanhToan.setBackground(new Color(41, 98, 255)); 
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnThanhToan.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnThanhToan.setFocusPainted(false);
        btnThanhToan.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        pnlCheckout.add(btnThanhToan);
        
        pnlPhai.add(pnlCheckout, BorderLayout.SOUTH);

        // Split Pane
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlTrai, pnlPhai);
        split.setDividerLocation(0.65);
        split.setResizeWeight(0.65);
        split.setBorder(null);

        add(split, BorderLayout.CENTER);
    }
    
    private JPanel createInputRow(String label, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout(5, 0));
        p.setBackground(Color.WHITE);
        p.add(new JLabel(label), BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return p;
    }
    
    // --- DISPLAY PRODUCTS AS CARDS ---
    public void hienThiDanhSachSanPham(List<SanPham> list) {
        pnlProductContainer.removeAll();
        // Calculate rows based on 3 columns
        int rows = (int) Math.ceil(list.size() / 3.0);
        if (rows == 0) rows = 1; // Prevent 0 rows issue if grid expects >0
        
        // Đảm bảo cast đúng kiểu
        LayoutManager layout = pnlProductContainer.getLayout();
        if (layout instanceof GridLayout) {
            ((GridLayout) layout).setRows(rows);
        }
        
        DecimalFormat df = new DecimalFormat("#,### đ");
        
        for (SanPham sp : list) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Image Placeholder (or real image if path exists)
            // Image Loading Logic
            JLabel lblImg = new JLabel();
            lblImg.setPreferredSize(new Dimension(100, 100)); 
            lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblImg.setHorizontalAlignment(SwingConstants.CENTER);
            lblImg.setOpaque(true);
            lblImg.setBackground(new Color(240, 245, 250));

            try {
                String imagePath = sp.getHinhAnh();
                if (imagePath != null && !imagePath.isEmpty()) {
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image img = icon.getImage();
                    // Scale image to fit 100x100 smoothly
                    Image newImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    lblImg.setIcon(new ImageIcon(newImg));
                    lblImg.setText(""); // Clear placeholder text
                } else {
                    lblImg.setText("IMG"); // Fallback text
                }
            } catch (Exception ex) {
                lblImg.setText("Err");
                ex.printStackTrace();
            }
            
            JLabel lblName = new JLabel(sp.getTenSP());
            lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
            lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel lblPrice = new JLabel(df.format(sp.getGiaBan()));
            lblPrice.setForeground(new Color(41, 98, 255));
            lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel lblStock = new JLabel("Kho: " + sp.getTonKho());
            lblStock.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblStock.setForeground(Color.GRAY);
            lblStock.setAlignmentX(Component.CENTER_ALIGNMENT);

            card.add(lblImg);
            card.add(Box.createVerticalStrut(10));
            card.add(lblName);
            card.add(lblPrice);
            card.add(lblStock);
            
            // Interaction
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                     if (productListener != null) productListener.onProductSelected(sp);
                }
                
                // Hover effect
                public void mouseEntered(MouseEvent e) {
                    card.setBackground(new Color(240, 248, 255));
                    card.setBorder(BorderFactory.createLineBorder(new Color(41, 98, 255), 1));
                }
                public void mouseExited(MouseEvent e) {
                    card.setBackground(Color.WHITE);
                    card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
                }
            });
            
            pnlProductContainer.add(card);
        }
        pnlProductContainer.revalidate();
        pnlProductContainer.repaint();
    }

    // --- GETTER & EVENTS ---
    public JTable getTblGio() { return tblGioHang; }
    public DefaultTableModel getModelGio() { return modelGio; }
    
    public String getTenKhach() { return txtTenKhach.getText(); }
    public String getSdtKhach() { return txtSdtKhach.getText(); }
    public String getMaGiamGia() { return txtMaGiamGia.getText().trim(); }
    public String getTuKhoaTimKiem() { return txtTimKiemSP.getText(); }
    public void setSearchText(String text) { txtTimKiemSP.setText(text); }
    
    public String getLblThanhToanText() {
        return lblThanhToan.getText();
    }
    
    public void setHienThiTien(String tongHang, String tienGiam, String canTra) {
        lblTongTien.setText("Tổng tiền hàng: " + tongHang);
        lblGiamGia.setText("Giảm giá: " + tienGiam);
        lblThanhToan.setText("PHẢI TRẢ: " + canTra);
    }

    public void setProductListener(ProductCardListener l) { this.productListener = l; }
    public void addTimKiemListener(ActionListener l) { btnTimKiem.addActionListener(l); }
    public void addXoaGioListener(ActionListener l) { btnXoaKhoiGio.addActionListener(l); }
    public void addThanhToanListener(ActionListener l) { btnThanhToan.addActionListener(l); }
    public void addApDungMaListener(ActionListener l) { btnApDungMa.addActionListener(l); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}
