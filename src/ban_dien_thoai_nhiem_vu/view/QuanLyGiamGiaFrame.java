package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.GiamGia;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class QuanLyGiamGiaFrame extends JFrame {
    
    public interface TableActionEvent {
        void onEdit(int row);
        void onDelete(int row);
    }

    private JTable tblGiamGia;
    private DefaultTableModel model;
    
    // Form nhập liệu
    public JTextField txtCode, txtTenCT, txtPhanTram, txtSoLuong, txtNgayKetThuc;
    private JButton btnLuu, btnLamMoi;

    public QuanLyGiamGiaFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Quản Lý Mã Giảm Giá - Voucher");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. FORM NHẬP (TOP)
        JPanel pnlInput = new JPanel(new BorderLayout());
        pnlInput.setBorder(new TitledBorder("Thông tin Voucher"));
        pnlInput.setPreferredSize(new Dimension(0, 160));

        JPanel pnlFields = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlFields.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnlFields.add(new JLabel("Mã Code (*):")); 
        txtCode = new JTextField(); pnlFields.add(txtCode);
        
        pnlFields.add(new JLabel("Tên Chương Trình:")); 
        txtTenCT = new JTextField(); pnlFields.add(txtTenCT);
        
        pnlFields.add(new JLabel("% Giảm (1-100):")); 
        txtPhanTram = new JTextField(); pnlFields.add(txtPhanTram);
        
        pnlFields.add(new JLabel("Số Lượng:")); 
        txtSoLuong = new JTextField(); pnlFields.add(txtSoLuong);

        pnlFields.add(new JLabel("Hạn Dùng (yyyy-mm-dd):")); 
        txtNgayKetThuc = new JTextField("2025-12-31"); pnlFields.add(txtNgayKetThuc);

        pnlInput.add(pnlFields, BorderLayout.CENTER);

        // Nút bấm
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLamMoi = new JButton("Làm mới");
        btnLuu = new JButton("Lưu Voucher");
        btnLuu.setBackground(new Color(40, 167, 69));
        btnLuu.setForeground(Color.WHITE);
        
        pnlBtn.add(btnLamMoi);
        pnlBtn.add(btnLuu);
        pnlInput.add(pnlBtn, BorderLayout.SOUTH);

        add(pnlInput, BorderLayout.NORTH);

        // 2. BẢNG DANH SÁCH (CENTER)
        String[] cols = {"Mã Code", "Tên Chương Trình", "% Giảm", "Số Lượng", "Hạn Dùng", "Hành Động"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 5; }
        };
        tblGiamGia = new JTable(model);
        tblGiamGia.setRowHeight(40);
        
        // Cấu hình cột hành động
        tblGiamGia.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender());
        tblGiamGia.getColumnModel().getColumn(5).setPreferredWidth(100);

        add(new JScrollPane(tblGiamGia), BorderLayout.CENTER);
    }

    // --- COPY CLASS NÚT BẤM (ICON) ---
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