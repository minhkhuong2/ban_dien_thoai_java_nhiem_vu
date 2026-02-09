package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class BanHangFrame extends JFrame {

    // --- LINH KIỆN ---
    private JTextField txtTimKiemSP;
    private JTable tblKhoHang;
    private DefaultTableModel modelKho;

    // Bên phải
    private JTable tblGioHang;
    private DefaultTableModel modelGio;
    private JTextField txtTenKhach, txtSdtKhach, txtMaGiamGia; 
    private JLabel lblTongTien, lblGiamGia, lblThanhToan; 
    private JButton btnThanhToan, btnXoaKhoiGio, btnTimKiem, btnApDungMa; 

    public BanHangFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Hệ thống Bán Hàng - POS (Có Sửa Số Lượng & Giảm Giá)");
        setSize(1250, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2, 10, 10));

        // === PANEL TRÁI: KHO HÀNG ===
        JPanel pnlTrai = new JPanel(new BorderLayout(5, 5));
        pnlTrai.setBorder(new EmptyBorder(10, 10, 10, 5));
        
        // Tìm kiếm
        JPanel pnlTimKiem = new JPanel(new BorderLayout(5, 5));
        pnlTimKiem.setBorder(new TitledBorder("Tìm kiếm sản phẩm"));
        txtTimKiemSP = new JTextField();
        btnTimKiem = new JButton("Tìm");
        pnlTimKiem.add(txtTimKiemSP, BorderLayout.CENTER);
        pnlTimKiem.add(btnTimKiem, BorderLayout.EAST);
        pnlTrai.add(pnlTimKiem, BorderLayout.NORTH);

        // Bảng kho
        String[] colsKho = {"Mã SP", "Tên Sản Phẩm", "Hãng", "Giá Bán", "Tồn"};
        modelKho = new DefaultTableModel(colsKho, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblKhoHang = new JTable(modelKho);
        tblKhoHang.setRowHeight(30);
        pnlTrai.add(new JScrollPane(tblKhoHang), BorderLayout.CENTER);
        pnlTrai.add(new JLabel("💡 Click đúp vào sản phẩm để thêm vào giỏ"), BorderLayout.SOUTH);

        // === PANEL PHẢI: GIỎ HÀNG ===
        JPanel pnlPhai = new JPanel(new BorderLayout(5, 5));
        pnlPhai.setBorder(new EmptyBorder(10, 5, 10, 10));

        // Bảng giỏ hàng
        JPanel pnlGioHang = new JPanel(new BorderLayout());
        pnlGioHang.setBorder(new TitledBorder("Giỏ hàng (Sửa số lượng trực tiếp tại bảng)"));
        
        String[] colsGio = {"Mã SP", "Tên SP", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        modelGio = new DefaultTableModel(colsGio, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Chỉ cho sửa cột SỐ LƯỢNG
            }
        };
        tblGioHang = new JTable(modelGio);
        tblGioHang.setRowHeight(30);
        pnlGioHang.add(new JScrollPane(tblGioHang), BorderLayout.CENTER);
        
        btnXoaKhoiGio = new JButton("Xóa dòng chọn");
        btnXoaKhoiGio.setBackground(new Color(220, 53, 69));
        btnXoaKhoiGio.setForeground(Color.WHITE);
        pnlGioHang.add(btnXoaKhoiGio, BorderLayout.SOUTH);
        
        pnlPhai.add(pnlGioHang, BorderLayout.CENTER);

        // --- FORM THANH TOÁN (ĐÃ SỬA LỖI MẤT NÚT) ---
        // Tăng số dòng lên 7 để chứa đủ nút
        JPanel pnlThanhToan = new JPanel(new GridLayout(7, 1, 5, 5)); 
        pnlThanhToan.setBorder(new TitledBorder("Thông tin thanh toán"));
        pnlThanhToan.setPreferredSize(new Dimension(0, 350)); // Tăng chiều cao lên xíu cho thoải mái

        JPanel p1 = new JPanel(new BorderLayout()); p1.add(new JLabel("Tên Khách: "), BorderLayout.WEST); txtTenKhach = new JTextField(); p1.add(txtTenKhach, BorderLayout.CENTER);
        JPanel p2 = new JPanel(new BorderLayout()); p2.add(new JLabel("SĐT:          "), BorderLayout.WEST); txtSdtKhach = new JTextField(); p2.add(txtSdtKhach, BorderLayout.CENTER);
        
        // Ô Mã giảm giá
        JPanel pMa = new JPanel(new BorderLayout()); 
        pMa.add(new JLabel("Mã Giảm Giá: "), BorderLayout.WEST);
        txtMaGiamGia = new JTextField();
        btnApDungMa = new JButton("Áp Dụng");
        JPanel pInputMa = new JPanel(new BorderLayout());
        pInputMa.add(txtMaGiamGia, BorderLayout.CENTER);
        pInputMa.add(btnApDungMa, BorderLayout.EAST);
        pMa.add(pInputMa, BorderLayout.CENTER);

        // Các nhãn tiền
        lblTongTien = new JLabel("Tổng tiền hàng: 0 đ");
        lblGiamGia = new JLabel("Giảm giá: 0 đ");
        lblGiamGia.setForeground(new Color(0, 128, 0));
        
        lblThanhToan = new JLabel("KHÁCH CẦN TRẢ: 0 VNĐ", SwingConstants.CENTER);
        lblThanhToan.setFont(new Font("Arial", Font.BOLD, 22));
        lblThanhToan.setForeground(Color.RED);

        // Nút Thanh Toán (To đùng)
        btnThanhToan = new JButton("THANH TOÁN & IN HÓA ĐƠN");
        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 16));
        btnThanhToan.setBackground(new Color(40, 167, 69)); 
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Thêm lần lượt vào Panel
        pnlThanhToan.add(p1);
        pnlThanhToan.add(p2);
        pnlThanhToan.add(pMa);
        pnlThanhToan.add(lblTongTien);
        pnlThanhToan.add(lblGiamGia);
        pnlThanhToan.add(lblThanhToan);
        pnlThanhToan.add(btnThanhToan); // <-- ĐÃ BỔ SUNG DÒNG NÀY

        pnlPhai.add(pnlThanhToan, BorderLayout.SOUTH);

        add(pnlTrai);
        add(pnlPhai);
    }

    // --- GETTER & EVENTS ---
    public JTable getTblKho() { return tblKhoHang; }
    public JTable getTblGio() { return tblGioHang; }
    public DefaultTableModel getModelKho() { return modelKho; }
    public DefaultTableModel getModelGio() { return modelGio; }
    
    public String getTenKhach() { return txtTenKhach.getText(); }
    public String getSdtKhach() { return txtSdtKhach.getText(); }
    public String getMaGiamGia() { return txtMaGiamGia.getText().trim(); }
    
    public String getLblThanhToanText() {
        return lblThanhToan.getText();
    }
    
    public void setHienThiTien(String tongHang, String tienGiam, String canTra) {
        lblTongTien.setText("Tổng tiền hàng: " + tongHang);
        lblGiamGia.setText("Giảm giá: " + tienGiam);
        lblThanhToan.setText("KHÁCH CẦN TRẢ: " + canTra);
    }

    public void addTimKiemListener(ActionListener l) { btnTimKiem.addActionListener(l); }
    public void addKhoHangMouseListener(MouseAdapter l) { tblKhoHang.addMouseListener(l); }
    public void addXoaGioListener(ActionListener l) { btnXoaKhoiGio.addActionListener(l); }
    public void addThanhToanListener(ActionListener l) { btnThanhToan.addActionListener(l); }
    public void addApDungMaListener(ActionListener l) { btnApDungMa.addActionListener(l); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public String getTuKhoaTimKiem() { return txtTimKiemSP.getText(); }
}