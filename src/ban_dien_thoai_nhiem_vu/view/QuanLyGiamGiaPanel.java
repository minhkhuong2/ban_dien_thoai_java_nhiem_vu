package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.GiamGia;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class QuanLyGiamGiaPanel extends JPanel {
    
    public interface TableActionEvent {
        void onEdit(int row);
        void onDelete(int row);
    }

    private JTable tblGiamGia;
    private DefaultTableModel model;
    
    public JTextField txtCode, txtTenCT, txtPhanTram, txtSoLuong, txtNgayKetThuc;
    private JButton btnLuu, btnLamMoi;

    // UI Constants
    // private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_SUCCESS = new Color(25, 135, 84);
    private final Color COLOR_DANGER = new Color(220, 53, 69);
    // private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    // private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public QuanLyGiamGiaPanel() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setLayout(new BorderLayout(20, 20));
        // setBackground(COLOR_BG); 
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. FORM NHẬP (TOP)
        JPanel pnlInput = new JPanel(new BorderLayout());
        pnlInput.setBackground(UIManager.getColor("window"));
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblHeader = new JLabel("Chương Trình Khuyến Mãi (Voucher)");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        // lblHeader.setForeground(COLOR_TEXT_DARK);
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        pnlInput.add(lblHeader, BorderLayout.NORTH);

        JPanel pnlFields = new JPanel(new GridLayout(2, 3, 20, 20));
        pnlFields.setOpaque(false);

        pnlFields.add(createInputPanel("Mã Voucher (*):", txtCode = new JTextField()));
        pnlFields.add(createInputPanel("Tên Chương Trình:", txtTenCT = new JTextField()));
        pnlFields.add(createInputPanel("Chiết Khấu % (1-100):", txtPhanTram = new JTextField()));
        pnlFields.add(createInputPanel("Số Lượng:", txtSoLuong = new JTextField()));
        pnlFields.add(createInputPanel("Hạn Dùng (yyyy-mm-dd):", txtNgayKetThuc = new JTextField("2025-12-31")));
        pnlFields.add(new JLabel("")); // Placeholder cho lưới

        pnlInput.add(pnlFields, BorderLayout.CENTER);
        
        // Buttons
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlBtn.setOpaque(false);
        pnlBtn.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        btnLamMoi = new JButton("Xóa Form");
        btnLamMoi.setPreferredSize(new Dimension(120, 42));
        btnLamMoi.setBackground(new Color(240, 240, 240));
        btnLamMoi.setForeground(UIManager.getColor("Label.foreground"));
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLamMoi.setBorder(null);
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLuu = new JButton("Lưu Khuyến Mãi");
        btnLuu.setPreferredSize(new Dimension(160, 42));
        btnLuu.setBackground(COLOR_SUCCESS); 
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setBorder(null);
        btnLuu.setFocusPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pnlBtn.add(btnLamMoi);
        pnlBtn.add(btnLuu);
        pnlInput.add(pnlBtn, BorderLayout.SOUTH);

        add(pnlInput, BorderLayout.NORTH);

        // 2. BẢNG DANH SÁCH (CENTER)
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(UIManager.getColor("window"));
        pnlTable.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblListTitle = new JLabel("Danh Sách Voucher Hiện Hành");
        lblListTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        // lblListTitle.setForeground(COLOR_TEXT_DARK);
        lblListTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        pnlTable.add(lblListTitle, BorderLayout.NORTH);

        String[] cols = {"Mã Voucher", "Tên Chương Trình", "% Giảm", "Số Lượng", "Hạn Dùng", "Hành Động"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 5; } // Cột Hành động có thể bấm được
        };
        tblGiamGia = new JTable(model);
        tblGiamGia.setRowHeight(45);
        tblGiamGia.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblGiamGia.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        // tblGiamGia.getTableHeader().setBackground(Color.WHITE);
        // tblGiamGia.getTableHeader().setForeground(COLOR_TEXT_MUTED);
        tblGiamGia.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_TABLE_BORDER));
        tblGiamGia.setShowGrid(false);
        tblGiamGia.setIntercellSpacing(new Dimension(0, 0));
        // tblGiamGia.setSelectionBackground(new Color(240, 244, 255));
        // tblGiamGia.setSelectionForeground(COLOR_TEXT_DARK);
        
        // Custom Render/Editor cho cột Hành Động
        tblGiamGia.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender());
        tblGiamGia.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane sc = new JScrollPane(tblGiamGia);
        // sc.getViewport().setBackground(Color.WHITE);
        sc.setBorder(null);
        
        pnlTable.add(sc, BorderLayout.CENTER);

        add(pnlTable, BorderLayout.CENTER);
    }
    
    private JPanel createInputPanel(String title, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout(5, 8));
        p.setOpaque(false);
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // lbl.setForeground(COLOR_TEXT_MUTED);
        
        txt.setPreferredSize(new Dimension(200, 42));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        p.add(lbl, BorderLayout.NORTH);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    // --- COPY CLASS NÚT BẤM (ICON) ---
    public class PanelAction extends JPanel {
        public JButton cmdEdit, cmdDelete;
        public PanelAction() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            setOpaque(true);
            cmdEdit = createBtn("edit.png", new Color(13, 202, 240)); // Info Cyan
            cmdDelete = createBtn("delete.png", COLOR_DANGER);
            add(cmdEdit); add(cmdDelete);
        }
        private JButton createBtn(String icon, Color bg) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(32, 32));
            btn.setBackground(bg);
            btn.setBorder(null);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
            action.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
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

    // Getters & Events
    public void setTableActionEvent(TableActionEvent event) {
        tblGiamGia.getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditor(event));
    }
    
    public GiamGia getGiamGiaInput() {
        if(txtCode.getText().isEmpty()) return null;
        try {
            GiamGia gg = new GiamGia();
            gg.setCode(txtCode.getText().toUpperCase());
            gg.setTenChuongTrinh(txtTenCT.getText());
            gg.setPhanTramGiam(Integer.parseInt(txtPhanTram.getText()));
            gg.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
            gg.setNgayKetThuc(java.sql.Date.valueOf(txtNgayKetThuc.getText()));
            return gg;
        } catch (Exception e) { return null; }
    }
    
    public void clearForm() {
        txtCode.setText(""); txtTenCT.setText(""); txtPhanTram.setText(""); txtSoLuong.setText("");
        txtCode.setEditable(true);
    }
    public DefaultTableModel getModel() { return model; }
    public void addLuuListener(ActionListener l) { btnLuu.addActionListener(l); }
    public void addLamMoiListener(ActionListener l) { btnLamMoi.addActionListener(l); }
}
