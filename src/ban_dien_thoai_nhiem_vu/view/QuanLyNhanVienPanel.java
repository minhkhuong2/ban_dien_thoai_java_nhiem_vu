package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    // UI Constants
    // private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_SUCCESS = new Color(25, 135, 84);
    private final Color COLOR_WARNING = new Color(255, 193, 7);
    private final Color COLOR_DANGER = new Color(220, 53, 69);
    // private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    // private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public QuanLyNhanVienPanel() {
        setLayout(new BorderLayout(20, 20));
        // setBackground(COLOR_BG);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // --- HEADER ---
        JLabel lblTitle = new JLabel("Danh Sách Nhân Sự");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        // lblTitle.setForeground(COLOR_TEXT_DARK);
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, taoPanelDanhSach(), taoPanelThongTin());
        splitPane.setResizeWeight(0.65); // Form 35%, List 65%
        splitPane.setDividerSize(0); 
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel taoPanelDanhSach() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setOpaque(false);
        pnlWrapper.setBorder(new EmptyBorder(0, 0, 0, 10)); // Khoảng cách với Form
        
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIManager.getColor("window"));
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblListTitle = new JLabel("Thống kê danh sách");
        lblListTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        // lblListTitle.setForeground(COLOR_TEXT_DARK);
        lblListTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        String[] cols = {"Mã NV", "Họ Tên", "Tài Khoản", "Vai Trò", "Trạng Thái"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblNhanVien = new JTable(model);
        tblNhanVien.setRowHeight(40);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        // tblNhanVien.getTableHeader().setBackground(Color.WHITE);
        // tblNhanVien.getTableHeader().setForeground(COLOR_TEXT_MUTED);
        tblNhanVien.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_TABLE_BORDER));
        tblNhanVien.setShowGrid(false);
        tblNhanVien.setIntercellSpacing(new Dimension(0, 0));
        // tblNhanVien.setSelectionBackground(new Color(240, 244, 255));
        // tblNhanVien.setSelectionForeground(COLOR_TEXT_DARK);

        JScrollPane sc = new JScrollPane(tblNhanVien);
        sc.setBorder(null);
        // sc.getViewport().setBackground(Color.WHITE);

        p.add(lblListTitle, BorderLayout.NORTH);
        p.add(sc, BorderLayout.CENTER);
        
        pnlWrapper.add(p, BorderLayout.CENTER);
        return pnlWrapper;
    }

    private JPanel taoPanelThongTin() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setOpaque(false);
        pnlWrapper.setBorder(new EmptyBorder(0, 10, 0, 0)); // Padding trái
        
        JPanel p = new JPanel(new BorderLayout(0, 15));
        p.setBackground(UIManager.getColor("window"));
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblFormTitle = new JLabel("Hồ Sơ Nhân Viên");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        // lblFormTitle.setForeground(COLOR_TEXT_DARK);
        p.add(lblFormTitle, BorderLayout.NORTH);
        
        JPanel pInput = new JPanel(new GridBagLayout());
        pInput.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 0, 10, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        
        int gridy = 0;
        txtMaNV = addInputField(pInput, "Mã Nhân Viên (*):", g, gridy++);
        txtHoTen = addInputField(pInput, "Họ và Tên (*):", g, gridy++);
        txtTaiKhoan = addInputField(pInput, "Tài Khoản (Login) (*):", g, gridy++);
        txtSDT = addInputField(pInput, "SĐT:", g, gridy++);
        txtEmail = addInputField(pInput, "Email (Để khôi phục MK):", g, gridy++);
        
        g.gridy = gridy++ * 2;
        JLabel lblVaiTro = new JLabel("Vai Trò:");
        lblVaiTro.setFont(new Font("Segoe UI", Font.BOLD, 13));
        // lblVaiTro.setForeground(COLOR_TEXT_MUTED);
        pInput.add(lblVaiTro, g);
        
        g.gridy++; 
        cboVaiTro = new JComboBox<>(new String[]{"NhanVien", "QuanLy"});
        cboVaiTro.setPreferredSize(new Dimension(200, 38));
        cboVaiTro.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cboVaiTro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(2, 5, 2, 5)
        ));
        pInput.add(cboVaiTro, g);
        
        g.gridy++; g.insets = new Insets(15, 0, 10, 0);
        chkTrangThai = new JCheckBox("Đang làm việc");
        chkTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        chkTrangThai.setForeground(COLOR_SUCCESS);
        chkTrangThai.setSelected(true);
        chkTrangThai.setOpaque(false);
        chkTrangThai.setFocusPainted(false);
        chkTrangThai.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pInput.add(chkTrangThai, g);

        // Buttons
        JPanel pBtn = new JPanel(new GridLayout(2, 2, 10, 10));
        pBtn.setOpaque(false);
        pBtn.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        btnThem = createFlatButton("Lưu Thêm", COLOR_SUCCESS, Color.WHITE);
        btnSua = createFlatButton("Cập Nhật", COLOR_WARNING, Color.BLACK);
        btnXoa = createFlatButton("Xóa NV", COLOR_DANGER, Color.WHITE);
        btnLamMoi = createFlatButton("Làm Mới Form", new Color(240, 240, 240), UIManager.getColor("Label.foreground"));
        
        pBtn.add(btnThem); pBtn.add(btnSua);
        pBtn.add(btnXoa); pBtn.add(btnLamMoi);
        
        // Push Content Upwards
        JPanel pCenterWrap = new JPanel(new BorderLayout());
        pCenterWrap.setOpaque(false);
        pCenterWrap.add(pInput, BorderLayout.NORTH);

        p.add(pCenterWrap, BorderLayout.CENTER);
        p.add(pBtn, BorderLayout.SOUTH);

        pnlWrapper.add(p, BorderLayout.CENTER);
        return pnlWrapper;
    }

    private JTextField addInputField(JPanel parent, String label, GridBagConstraints g, int row) {
        g.gridy = row * 2;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        // lbl.setForeground(COLOR_TEXT_MUTED);
        parent.add(lbl, g);
        
        g.gridy = row * 2 + 1;
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(200, 38));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        parent.add(txt, g);
        return txt;
    }

    private JButton createFlatButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(100, 42));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
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
        
        if(status) {
            chkTrangThai.setText("Đang làm việc");
            chkTrangThai.setForeground(COLOR_SUCCESS);
        } else {
            chkTrangThai.setText("Đã nghỉ việc");
            chkTrangThai.setForeground(COLOR_DANGER);
        }
    }
    
    public void clearForm() {
        txtMaNV.setText(""); txtHoTen.setText(""); txtTaiKhoan.setText("");
        txtSDT.setText(""); txtEmail.setText(""); chkTrangThai.setSelected(true);
        txtMaNV.setEditable(true);
        chkTrangThai.setText("Đang làm việc");
        chkTrangThai.setForeground(COLOR_SUCCESS);
    }
    
    public void khoaMaNV() { txtMaNV.setEditable(false); }
    
    public void addThemListener(ActionListener l) { btnThem.addActionListener(l); }
    public void addSuaListener(ActionListener l) { btnSua.addActionListener(l); }
    public void addXoaListener(ActionListener l) { btnXoa.addActionListener(l); }
    public void addLamMoiListener(ActionListener l) { btnLamMoi.addActionListener(l); }
    public void addTableListener(MouseAdapter l) { tblNhanVien.addMouseListener(l); }
}
