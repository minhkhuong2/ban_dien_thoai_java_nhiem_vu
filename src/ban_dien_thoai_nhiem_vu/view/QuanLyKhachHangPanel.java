package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.KhachHang;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class QuanLyKhachHangPanel extends JPanel {
    
    private JTextField txtMaKH, txtTenKH, txtSDT;
    private JTextArea txtDiaChi;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    
    private JTextField txtTimKiem;
    private JButton btnTim;
    private JTable tblKhachHang;
    private DefaultTableModel model;

    // UI Constants
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_SUCCESS = new Color(25, 135, 84);
    private final Color COLOR_WARNING = new Color(255, 193, 7);
    private final Color COLOR_DANGER = new Color(220, 53, 69);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public QuanLyKhachHangPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // --- HEADER ---
        JLabel lblTitle = new JLabel("Hồ Sơ Khách Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);
        
        // --- BODY ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createFormPanel(), createListPanel());
        splitPane.setResizeWeight(0.35); // Form 35%, List 65%
        splitPane.setDividerSize(0); 
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setOpaque(false);
        pnlWrapper.setBorder(new EmptyBorder(0, 0, 0, 10)); // Khoảng cách với List
        
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblFormTitle = new JLabel("Thông Tin Liên Hệ");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFormTitle.setForeground(COLOR_TEXT_DARK);
        lblFormTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        p.add(lblFormTitle, BorderLayout.NORTH);
        
        JPanel pInput = new JPanel(new GridBagLayout());
        pInput.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 0, 10, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        
        int gridy = 0;
        txtMaKH = addInputField(pInput, "Mã Khách Hàng (Tự động):", g, gridy++, false);
        txtTenKH = addInputField(pInput, "Họ và Tên (*):", g, gridy++, true);
        txtSDT = addInputField(pInput, "Số Điện Thoại (*):", g, gridy++, true);
        
        // Địa chỉ
        g.gridy = gridy * 2;
        JLabel lblDiaChi = new JLabel("Địa Chỉ Liên Hệ:");
        lblDiaChi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDiaChi.setForeground(COLOR_TEXT_MUTED);
        pInput.add(lblDiaChi, g);
        
        g.gridy = gridy * 2 + 1; gridy++;
        txtDiaChi = new JTextArea(4, 20); 
        txtDiaChi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDiaChi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        pInput.add(new JScrollPane(txtDiaChi), g);
        
        // Buttons
        JPanel pBtn = new JPanel(new GridLayout(2, 2, 10, 10));
        pBtn.setBackground(Color.WHITE);
        pBtn.setBorder(new EmptyBorder(25, 0, 0, 0));
        
        btnThem = createFlatButton("Thêm Mới", COLOR_SUCCESS, Color.WHITE);
        btnSua = createFlatButton("Cập Nhật", COLOR_WARNING, Color.BLACK);
        btnXoa = createFlatButton("Xóa", COLOR_DANGER, Color.WHITE);
        btnLamMoi = createFlatButton("Làm Mới", new Color(240, 240, 240), COLOR_TEXT_DARK);
        
        pBtn.add(btnThem); 
        pBtn.add(btnSua); 
        pBtn.add(btnXoa); 
        pBtn.add(btnLamMoi);
        
        // Chèn vào center nhưng đẩy lên trên
        JPanel pnlCenterWrap = new JPanel(new BorderLayout());
        pnlCenterWrap.setOpaque(false);
        pnlCenterWrap.add(pInput, BorderLayout.NORTH);

        p.add(pnlCenterWrap, BorderLayout.CENTER);
        p.add(pBtn, BorderLayout.SOUTH);
        
        pnlWrapper.add(p, BorderLayout.CENTER);
        return pnlWrapper;
    }

    private JTextField addInputField(JPanel parent, String label, GridBagConstraints g, int row, boolean editable) {
        g.gridy = row * 2;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT_MUTED);
        parent.add(lbl, g);
        
        g.gridy = row * 2 + 1;
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(200, 38));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txt.setEditable(editable);
        if(!editable) txt.setBackground(new Color(245, 245, 245));
        parent.add(txt, g);
        return txt;
    }
    
    private JPanel createListPanel() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setOpaque(false);
        pnlWrapper.setBorder(new EmptyBorder(0, 10, 0, 0)); // Padding trái
        
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblListTitle = new JLabel("Danh Sách Điện Thoại & Khách Hàng");
        lblListTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblListTitle.setForeground(COLOR_TEXT_DARK);
        
        // Search
        JPanel pSearch = new JPanel(new BorderLayout(10, 0));
        pSearch.setBackground(Color.WHITE);
        pSearch.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        txtTimKiem = new JTextField("Tìm SĐT hoặc Tên khách hàng...");
        txtTimKiem.setPreferredSize(new Dimension(300, 40));
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setForeground(COLOR_TEXT_MUTED);
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtTimKiem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtTimKiem.getText().equals("Tìm SĐT hoặc Tên khách hàng...")) {
                    txtTimKiem.setText("");
                    txtTimKiem.setForeground(COLOR_TEXT_DARK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtTimKiem.getText().isEmpty()) {
                    txtTimKiem.setForeground(COLOR_TEXT_MUTED);
                    txtTimKiem.setText("Tìm SĐT hoặc Tên khách hàng...");
                }
            }
        });
        
        btnTim = createFlatButton("Tìm Kiếm", COLOR_PRIMARY, Color.WHITE);
        btnTim.setPreferredSize(new Dimension(130, 40));
        
        pSearch.add(txtTimKiem, BorderLayout.CENTER);
        pSearch.add(btnTim, BorderLayout.EAST);
        
        JPanel pnlHeaderGroup = new JPanel(new BorderLayout());
        pnlHeaderGroup.setBackground(Color.WHITE);
        pnlHeaderGroup.add(lblListTitle, BorderLayout.NORTH);
        pnlHeaderGroup.add(pSearch, BorderLayout.CENTER);

        // Table
        String[] cols = {"Mã KH", "Họ Tên", "SĐT", "Địa Chỉ", "Ngày Tham Gia"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblKhachHang = new JTable(model);
        tblKhachHang.setRowHeight(40);
        tblKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKhachHang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblKhachHang.getTableHeader().setBackground(Color.WHITE);
        tblKhachHang.getTableHeader().setForeground(COLOR_TEXT_MUTED);
        tblKhachHang.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_TABLE_BORDER));
        tblKhachHang.setShowGrid(false);
        tblKhachHang.setIntercellSpacing(new Dimension(0, 0));
        tblKhachHang.setSelectionBackground(new Color(240, 244, 255));
        tblKhachHang.setSelectionForeground(COLOR_TEXT_DARK);
        
        JScrollPane sc = new JScrollPane(tblKhachHang);
        sc.setBorder(null);
        sc.getViewport().setBackground(Color.WHITE);
        
        p.add(pnlHeaderGroup, BorderLayout.NORTH);
        p.add(sc, BorderLayout.CENTER);
        
        pnlWrapper.add(p, BorderLayout.CENTER);
        return pnlWrapper;
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
    
    // --- LẤY DỮ LIỆU TỪ FORM ---
    public KhachHang getKhachHangInput() {
        try {
            if(txtTenKH.getText().trim().isEmpty() || txtSDT.getText().trim().isEmpty()) {
                return null;
            }

            KhachHang kh = new KhachHang();
            if(!txtMaKH.getText().isEmpty()) kh.setMaKH(Integer.parseInt(txtMaKH.getText()));
            
            kh.setTenKH(txtTenKH.getText());
            kh.setSdt(txtSDT.getText());
            kh.setDiaChi(txtDiaChi.getText());
            
            return kh;
        } catch (Exception e) { return null; }
    }
    
    public void setForm(KhachHang kh) {
        txtMaKH.setText(String.valueOf(kh.getMaKH()));
        txtTenKH.setText(kh.getTenKH());
        txtSDT.setText(kh.getSdt());
        txtDiaChi.setText(kh.getDiaChi());
    }
    
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
    public String getTuKhoa() { 
        String text = txtTimKiem.getText();
        if(text.equals("Tìm SĐT hoặc Tên khách hàng...")) return "";
        return text; 
    }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public int showConfirm(String msg) { return JOptionPane.showConfirmDialog(this, msg, "Xác nhận", JOptionPane.YES_NO_OPTION); }
}
