package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.KhachHang;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class QuanLyKhachHangPanel extends JPanel {
    
    // Interface xử lý sự kiện bảng
    public interface TableActionEvent {
        void onEdit(int row);
        void onDelete(int row);
    }

    private JTable tblKhachHang;
    private DefaultTableModel model;
    
    // Form nhập liệu
    public JTextField txtTenKH, txtSDT, txtEmail;
    public JTextArea txtDiaChi;
    private JButton btnLuu, btnLamMoi;

    public QuanLyKhachHangPanel() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); // Main BG
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. FORM NHẬP LIỆU (TOP)
        JPanel pnlInput = new JPanel(new BorderLayout());
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblHeader = new JLabel("Quản Lý Khách Hàng");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblHeader.setForeground(new Color(33, 33, 33));
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        pnlInput.add(lblHeader, BorderLayout.NORTH);

        JPanel pnlFields = new JPanel(new GridLayout(2, 4, 15, 15));
        pnlFields.setBackground(Color.WHITE);

        pnlFields.add(createInputPanel("Họ tên KH (*):", txtTenKH = new JTextField()));
        pnlFields.add(createInputPanel("Số điện thoại (*):", txtSDT = new JTextField()));
        pnlFields.add(createInputPanel("Email:", txtEmail = new JTextField()));
        
        txtDiaChi = new JTextArea(2, 20); 
        txtDiaChi.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        txtDiaChi.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPanel pnlDiaChi = new JPanel(new BorderLayout(5, 5));
        pnlDiaChi.setBackground(Color.WHITE);
        JLabel lblDC = new JLabel("Địa chỉ:");
        lblDC.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblDC.setForeground(Color.GRAY);
        pnlDiaChi.add(lblDC, BorderLayout.NORTH);
        pnlDiaChi.add(new JScrollPane(txtDiaChi), BorderLayout.CENTER);
        
        pnlFields.add(pnlDiaChi);

        pnlInput.add(pnlFields, BorderLayout.CENTER);
        
        // Buttons
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.setBackground(Color.WHITE);
        pnlBtn.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setPreferredSize(new Dimension(100, 40));
        btnLamMoi.setBackground(new Color(240, 240, 240));
        btnLamMoi.setBorder(null);
        
        btnLuu = new JButton("Lưu Khách Hàng");
        btnLuu.setPreferredSize(new Dimension(150, 40));
        btnLuu.setBackground(new Color(0, 200, 83)); // Green
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLuu.setBorder(null);
        
        pnlBtn.add(btnLamMoi);
        pnlBtn.add(btnLuu);
        pnlInput.add(pnlBtn, BorderLayout.SOUTH);

        add(pnlInput, BorderLayout.NORTH);

        // 2. BẢNG DANH SÁCH (CENTER)
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        pnlTable.add(new JLabel("Danh sách khách hàng"), BorderLayout.NORTH);
        ((JLabel)pnlTable.getComponent(0)).setFont(new Font("SansSerif", Font.BOLD, 18));
        ((JLabel)pnlTable.getComponent(0)).setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] cols = {"Mã KH", "Họ Tên", "SĐT", "Email", "Địa Chỉ", "Ngày Tham Gia", "Hành Động"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Chỉ cột hành động sửa được
            }
        };
        tblKhachHang = new JTable(model);
        tblKhachHang.setRowHeight(45);
        tblKhachHang.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tblKhachHang.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tblKhachHang.getTableHeader().setBackground(Color.WHITE);
        tblKhachHang.setSelectionBackground(new Color(230, 240, 255));
        tblKhachHang.setSelectionForeground(Color.BLACK);
        tblKhachHang.setShowGrid(false);
        tblKhachHang.setIntercellSpacing(new Dimension(0, 0));
        
        // Cấu hình cột hành động
        tblKhachHang.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        tblKhachHang.getColumnModel().getColumn(6).setPreferredWidth(120);
        
        JScrollPane sc = new JScrollPane(tblKhachHang);
        sc.getViewport().setBackground(Color.WHITE);
        sc.setBorder(null);
        
        pnlTable.add(sc, BorderLayout.CENTER);

        add(pnlTable, BorderLayout.CENTER);
    }
    
    private JPanel createInputPanel(String title, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(Color.GRAY);
        
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        txt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        p.add(lbl, BorderLayout.NORTH);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    // --- CÁC CLASS XỬ LÝ NÚT BẤM TRONG BẢNG ---
    public class PanelAction extends JPanel {
        public JButton cmdEdit, cmdDelete;
        public PanelAction() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            setBackground(Color.WHITE);
            cmdEdit = createBtn("edit.png", new Color(23, 162, 184));
            cmdDelete = createBtn("delete.png", new Color(220, 53, 69));
            add(cmdEdit); add(cmdDelete);
        }
        private JButton createBtn(String icon, Color bg) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(30, 30));
            btn.setBackground(bg);
            btn.setBorder(null);
            try {
                URL url = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/" + icon);
                if(url!=null) btn.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH)));
            } catch(Exception e){}
            return btn;
        }
    }
    public class TableActionCellRender extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            PanelAction action = new PanelAction();
            action.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return action;
        }
    }
    public class TableActionCellEditor extends DefaultCellEditor {
        private TableActionEvent event;
        public TableActionCellEditor(TableActionEvent event) { super(new JCheckBox()); this.event = event; }
        @Override public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            PanelAction action = new PanelAction();
            action.setBackground(table.getSelectionBackground());
            action.cmdEdit.addActionListener(e -> { event.onEdit(row); fireEditingStopped(); });
            action.cmdDelete.addActionListener(e -> { event.onDelete(row); fireEditingStopped(); });
            return action;
        }
    }

    // --- GETTER & EVENTS ---
    public void setTableActionEvent(TableActionEvent event) {
        tblKhachHang.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event));
    }
    
    public KhachHang getKhachHangInput() {
        if(txtTenKH.getText().isEmpty() || txtSDT.getText().isEmpty()) return null;
        KhachHang kh = new KhachHang();
        kh.setTenKH(txtTenKH.getText());
        kh.setSdt(txtSDT.getText());
        kh.setEmail(txtEmail.getText());
        kh.setDiaChi(txtDiaChi.getText());
        return kh;
    }
    
    public void clearForm() {
        txtTenKH.setText(""); txtSDT.setText(""); txtEmail.setText(""); txtDiaChi.setText("");
        tblKhachHang.clearSelection();
    }
    
    public DefaultTableModel getModel() { return model; }
    public void addLuuListener(ActionListener l) { btnLuu.addActionListener(l); }
    public void addLamMoiListener(ActionListener l) { btnLamMoi.addActionListener(l); }
}
