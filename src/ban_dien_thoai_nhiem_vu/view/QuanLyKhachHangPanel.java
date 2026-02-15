package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.KhachHang;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class QuanLyKhachHangPanel extends JPanel {
    
    // Components Form
    private JTextField txtMaKH, txtTenKH, txtSDT;
    private JTextArea txtDiaChi; // Không có txtEmail
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    
    // Components List
    private JTextField txtTimKiem;
    private JButton btnTim;
    private JTable tblKhachHang;
    private DefaultTableModel model;

    public QuanLyKhachHangPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // --- HEADER ---
        JLabel lblTitle = new JLabel("Quản lý Khách Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);
        
        // --- BODY ---
        JPanel pnlBody = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlBody.setOpaque(false);
        pnlBody.add(createFormPanel());
        pnlBody.add(createListPanel());
        
        add(pnlBody, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new TitledBorder("Thông tin khách hàng"));
        
        JPanel pInput = new JPanel(new GridBagLayout());
        pInput.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        
        // Mã KH
        g.gridx=0; g.gridy=0; pInput.add(new JLabel("Mã KH:"), g);
        txtMaKH = new JTextField(); txtMaKH.setEditable(false); 
        g.gridx=1; pInput.add(txtMaKH, g);
        
        // Tên KH
        g.gridx=0; g.gridy=1; pInput.add(new JLabel("Họ và Tên (*):"), g);
        txtTenKH = new JTextField();
        g.gridx=1; pInput.add(txtTenKH, g);
        
        // SĐT
        g.gridx=0; g.gridy=2; pInput.add(new JLabel("Số Điện Thoại (*):"), g);
        txtSDT = new JTextField();
        g.gridx=1; pInput.add(txtSDT, g);
        
        // Địa chỉ
        g.gridx=0; g.gridy=3; pInput.add(new JLabel("Địa Chỉ:"), g);
        txtDiaChi = new JTextArea(3, 20); 
        txtDiaChi.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        g.gridx=1; pInput.add(new JScrollPane(txtDiaChi), g);
        
        // Buttons
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pBtn.setBackground(Color.WHITE);
        
        btnThem = new JButton("Thêm"); btnThem.setBackground(new Color(40, 167, 69)); btnThem.setForeground(Color.WHITE);
        btnSua = new JButton("Sửa"); btnSua.setBackground(new Color(255, 193, 7));
        btnXoa = new JButton("Xóa"); btnXoa.setBackground(new Color(220, 53, 69)); btnXoa.setForeground(Color.WHITE);
        btnLamMoi = new JButton("Làm mới");
        
        pBtn.add(btnThem); pBtn.add(btnSua); pBtn.add(btnXoa); pBtn.add(btnLamMoi);
        
        p.add(pInput, BorderLayout.CENTER);
        p.add(pBtn, BorderLayout.SOUTH);
        return p;
    }
    
    private JPanel createListPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new TitledBorder("Danh sách & Tìm kiếm"));
        
        // Search
        JPanel pSearch = new JPanel(new BorderLayout(10, 0));
        pSearch.setOpaque(false);
        pSearch.setBorder(new EmptyBorder(10, 10, 10, 10));
        txtTimKiem = new JTextField();
        btnTim = new JButton("Tìm SĐT / Tên");
        pSearch.add(txtTimKiem, BorderLayout.CENTER);
        pSearch.add(btnTim, BorderLayout.EAST);
        
        // Table
        String[] cols = {"Mã KH", "Họ Tên", "SĐT", "Địa Chỉ", "Ngày Tham Gia"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblKhachHang = new JTable(model);
        tblKhachHang.setRowHeight(30);
        
        p.add(pSearch, BorderLayout.NORTH);
        p.add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);
        return p;
    }
    
    // --- LẤY DỮ LIỆU TỪ FORM (ĐÃ SỬA: KHÔNG CÓ EMAIL) ---
    public KhachHang getKhachHangInput() {
        try {
            // Kiểm tra rỗng
            if(txtTenKH.getText().trim().isEmpty() || txtSDT.getText().trim().isEmpty()) {
                return null;
            }

            KhachHang kh = new KhachHang();
            if(!txtMaKH.getText().isEmpty()) kh.setMaKH(Integer.parseInt(txtMaKH.getText()));
            
            kh.setTenKH(txtTenKH.getText());
            kh.setSdt(txtSDT.getText());
            kh.setDiaChi(txtDiaChi.getText());
            // Đã xóa dòng setEmail gây lỗi
            
            return kh;
        } catch (Exception e) { return null; }
    }
    
    // Đổ dữ liệu lên Form
    public void setForm(KhachHang kh) {
        txtMaKH.setText(String.valueOf(kh.getMaKH()));
        txtTenKH.setText(kh.getTenKH());
        txtSDT.setText(kh.getSdt());
        txtDiaChi.setText(kh.getDiaChi());
    }
    
    // Làm mới Form (ĐÃ SỬA: KHÔNG CÓ EMAIL)
    public void clearForm() {
        txtMaKH.setText(""); 
        txtTenKH.setText(""); 
        txtSDT.setText(""); 
        txtDiaChi.setText("");
        txtTenKH.requestFocus();
    }
    
    // --- LISTENERS ---
    public void addThemListener(ActionListener l) { btnThem.addActionListener(l); }
    public void addSuaListener(ActionListener l) { btnSua.addActionListener(l); }
    public void addXoaListener(ActionListener l) { btnXoa.addActionListener(l); }
    public void addLamMoiListener(ActionListener l) { btnLamMoi.addActionListener(l); }
    public void addTimKiemListener(ActionListener l) { btnTim.addActionListener(l); }
    public void addTableClickListener(MouseAdapter l) { tblKhachHang.addMouseListener(l); }
    
    // Getters
    public JTable getTable() { return tblKhachHang; }
    public DefaultTableModel getModel() { return model; }
    public String getTuKhoa() { return txtTimKiem.getText(); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public int showConfirm(String msg) { return JOptionPane.showConfirmDialog(this, msg, "Xác nhận", JOptionPane.YES_NO_OPTION); }
}