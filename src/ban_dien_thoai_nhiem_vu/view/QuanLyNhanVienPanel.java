package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.NhanVien;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class QuanLyNhanVienPanel extends JPanel {
    
    private JTextField txtMaNV, txtHoTen, txtSDT, txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboVaiTro; // Chọn ADMIN hoặc NHANVIEN
    private JCheckBox chkTrangThai;      // Hoạt động hay Khóa
    
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private JTable tblNhanVien;
    private DefaultTableModel model;

    public QuanLyNhanVienPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("Quản lý Nhân viên & Phân quyền");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);
        
        JPanel pnlBody = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlBody.setOpaque(false);
        pnlBody.add(createFormPanel());
        pnlBody.add(createListPanel());
        
        add(pnlBody, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new TitledBorder("Thông tin tài khoản"));
        
        JPanel pInput = new JPanel(new GridBagLayout());
        pInput.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        
        // Form Fields
        g.gridx=0; g.gridy=0; pInput.add(new JLabel("Mã NV:"), g);
        txtMaNV = new JTextField(); txtMaNV.setEditable(false); g.gridx=1; pInput.add(txtMaNV, g);
        
        g.gridx=0; g.gridy=1; pInput.add(new JLabel("Họ Tên:"), g);
        txtHoTen = new JTextField(); g.gridx=1; pInput.add(txtHoTen, g);
        
        g.gridx=0; g.gridy=2; pInput.add(new JLabel("SĐT:"), g);
        txtSDT = new JTextField(); g.gridx=1; pInput.add(txtSDT, g);

        g.gridx=0; g.gridy=3; pInput.add(new JLabel("Tài khoản:"), g);
        txtTaiKhoan = new JTextField(); g.gridx=1; pInput.add(txtTaiKhoan, g);
        
        g.gridx=0; g.gridy=4; pInput.add(new JLabel("Mật khẩu:"), g);
        txtMatKhau = new JPasswordField(); g.gridx=1; pInput.add(txtMatKhau, g);

        g.gridx=0; g.gridy=5; pInput.add(new JLabel("Vai Trò (Quyền):"), g);
        cboVaiTro = new JComboBox<>(new String[]{"NHANVIEN", "ADMIN"}); 
        g.gridx=1; pInput.add(cboVaiTro, g);
        
        g.gridx=1; g.gridy=6; 
        chkTrangThai = new JCheckBox("Đang hoạt động"); 
        chkTrangThai.setSelected(true);
        pInput.add(chkTrangThai, g);

        // Buttons
        JPanel pBtn = new JPanel(new FlowLayout());
        pBtn.setBackground(Color.WHITE);
        btnThem = new JButton("Thêm"); btnThem.setBackground(new Color(40, 167, 69)); btnThem.setForeground(Color.WHITE);
        btnSua = new JButton("Cập nhật"); btnSua.setBackground(new Color(255, 193, 7));
        btnXoa = new JButton("Xóa/Khóa"); btnXoa.setBackground(new Color(220, 53, 69)); btnXoa.setForeground(Color.WHITE);
        btnLamMoi = new JButton("Làm mới");
        pBtn.add(btnThem); pBtn.add(btnSua); pBtn.add(btnXoa); pBtn.add(btnLamMoi);
        
        p.add(pInput, BorderLayout.CENTER);
        p.add(pBtn, BorderLayout.SOUTH);
        return p;
    }
    
    private JPanel createListPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new TitledBorder("Danh sách nhân viên"));
        
        String[] cols = {"Mã NV", "Họ Tên", "Tài Khoản", "Vai Trò", "Trạng Thái"};
        model = new DefaultTableModel(cols, 0);
        tblNhanVien = new JTable(model);
        tblNhanVien.setRowHeight(30);
        
        p.add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);
        return p;
    }
    
    public NhanVien getNhanVienInput() {
        try {
            NhanVien nv = new NhanVien();
            // [SỬA] Lấy trực tiếp chuỗi, không dùng Integer.parseInt
            nv.setMaNV(txtMaNV.getText()); 
            
            nv.setHoTen(txtHoTen.getText());
            nv.setSdt(txtSDT.getText());
            nv.setTaiKhoan(txtTaiKhoan.getText());
            nv.setMatKhau(new String(txtMatKhau.getPassword()));
            nv.setVaiTro(cboVaiTro.getSelectedItem().toString());
            nv.setTrangThai(chkTrangThai.isSelected() ? 1 : 0);
            return nv;
        } catch (Exception e) { return null; }
    }
    
    public void setForm(NhanVien nv) {
        txtMaNV.setText(String.valueOf(nv.getMaNV()));
        txtHoTen.setText(nv.getHoTen());
        txtSDT.setText(nv.getSdt());
        txtTaiKhoan.setText(nv.getTaiKhoan());
        txtMatKhau.setText(nv.getMatKhau()); // Hiện pass để sửa (thực tế nên ẩn)
        cboVaiTro.setSelectedItem(nv.getVaiTro());
        chkTrangThai.setSelected(nv.getTrangThai() == 1);
    }
    
    public void clearForm() {
        txtMaNV.setText(""); txtHoTen.setText(""); txtSDT.setText("");
        txtTaiKhoan.setText(""); txtMatKhau.setText(""); 
        cboVaiTro.setSelectedIndex(0); chkTrangThai.setSelected(true);
    }

    // Listeners
    public void addThemListener(ActionListener l) { btnThem.addActionListener(l); }
    public void addSuaListener(ActionListener l) { btnSua.addActionListener(l); }
    public void addXoaListener(ActionListener l) { btnXoa.addActionListener(l); }
    public void addLamMoiListener(ActionListener l) { btnLamMoi.addActionListener(l); }
    public void addTableListener(MouseAdapter l) { tblNhanVien.addMouseListener(l); }
    
    public JTable getTable() { return tblNhanVien; }
    public DefaultTableModel getModel() { return model; }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}