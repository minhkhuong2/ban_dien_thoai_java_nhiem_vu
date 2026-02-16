package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class QuanLyNhanVienPanel extends JPanel {

    private JTextField txtMaNV, txtHoTen, txtTaiKhoan, txtSDT, txtEmail;
    private JComboBox<String> cboVaiTro;
    private JCheckBox chkTrangThai; // Hoạt động / Đã nghỉ
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private JTable tblNhanVien;
    private DefaultTableModel model;

    public QuanLyNhanVienPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- LEFT: DANH SÁCH ---
        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setBorder(new TitledBorder("Danh sách nhân viên"));
        
        String[] cols = {"Mã NV", "Họ Tên", "Tài Khoản", "Vai Trò", "Trạng Thái"};
        model = new DefaultTableModel(cols, 0);
        tblNhanVien = new JTable(model);
        tblNhanVien.setRowHeight(25);
        pnlLeft.add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

        // --- RIGHT: FORM NHẬP ---
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBorder(new TitledBorder("Thông tin chi tiết"));
        pnlRight.setPreferredSize(new Dimension(300, 0));
        pnlRight.setBackground(Color.WHITE);
        
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.gridy = 0;

        pnlRight.add(new JLabel("Mã Nhân Viên:"), g);
        g.gridy++; pnlRight.add(txtMaNV = new JTextField(), g);

        g.gridy++; pnlRight.add(new JLabel("Họ và Tên:"), g);
        g.gridy++; pnlRight.add(txtHoTen = new JTextField(), g);

        g.gridy++; pnlRight.add(new JLabel("Tài Khoản (Login):"), g);
        g.gridy++; pnlRight.add(txtTaiKhoan = new JTextField(), g);
        
        g.gridy++; pnlRight.add(new JLabel("SĐT:"), g);
        g.gridy++; pnlRight.add(txtSDT = new JTextField(), g);
        
        g.gridy++; pnlRight.add(new JLabel("Email (Để khôi phục MK):"), g);
        g.gridy++; pnlRight.add(txtEmail = new JTextField(), g);

        g.gridy++; pnlRight.add(new JLabel("Vai Trò:"), g);
        g.gridy++; 
        cboVaiTro = new JComboBox<>(new String[]{"NhanVien", "QuanLy"});
        pnlRight.add(cboVaiTro, g);
        
        g.gridy++; 
        chkTrangThai = new JCheckBox("Đang làm việc");
        chkTrangThai.setSelected(true);
        pnlRight.add(chkTrangThai, g);

        // Buttons
        JPanel pBtn = new JPanel(new GridLayout(2, 2, 5, 5));
        btnThem = new JButton("Thêm"); btnThem.setBackground(new Color(40, 167, 69)); btnThem.setForeground(Color.WHITE);
        btnSua = new JButton("Sửa"); btnSua.setBackground(new Color(255, 193, 7));
        btnXoa = new JButton("Xóa"); btnXoa.setBackground(new Color(220, 53, 69)); btnXoa.setForeground(Color.WHITE);
        btnLamMoi = new JButton("Làm mới");
        
        pBtn.add(btnThem); pBtn.add(btnSua);
        pBtn.add(btnXoa); pBtn.add(btnLamMoi);
        
        g.gridy++; g.insets = new Insets(20, 5, 5, 5);
        pnlRight.add(pBtn, g);
        
        // Đẩy lên trên cùng
        g.gridy++; g.weighty = 1.0; pnlRight.add(new JLabel(), g);

        add(pnlLeft, BorderLayout.CENTER);
        add(pnlRight, BorderLayout.EAST);
    }
    
    // Getters & Setters cơ bản
    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return tblNhanVien; }
    
    public String getMaNV() { return txtMaNV.getText(); }
    public String getHoTen() { return txtHoTen.getText(); }
    public String getTaiKhoan() { return txtTaiKhoan.getText(); }
    public String getSDT() { return txtSDT.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getVaiTro() { return cboVaiTro.getSelectedItem().toString(); }
    public boolean getTrangThai() { return chkTrangThai.isSelected(); }
    
    public void setForm(String ma, String ten, String tk, String sdt, String mail, String role, boolean status) {
        txtMaNV.setText(ma); txtHoTen.setText(ten); txtTaiKhoan.setText(tk);
        txtSDT.setText(sdt); txtEmail.setText(mail); cboVaiTro.setSelectedItem(role);
        chkTrangThai.setSelected(status);
    }
    
    public void clearForm() {
        txtMaNV.setText(""); txtHoTen.setText(""); txtTaiKhoan.setText("");
        txtSDT.setText(""); txtEmail.setText(""); chkTrangThai.setSelected(true);
        txtMaNV.setEditable(true);
    }
    
    public void khoaMaNV() { txtMaNV.setEditable(false); }
    
    public void addThemListener(ActionListener l) { btnThem.addActionListener(l); }
    public void addSuaListener(ActionListener l) { btnSua.addActionListener(l); }
    public void addXoaListener(ActionListener l) { btnXoa.addActionListener(l); }
    public void addLamMoiListener(ActionListener l) { btnLamMoi.addActionListener(l); }
    public void addTableListener(MouseAdapter l) { tblNhanVien.addMouseListener(l); }
}