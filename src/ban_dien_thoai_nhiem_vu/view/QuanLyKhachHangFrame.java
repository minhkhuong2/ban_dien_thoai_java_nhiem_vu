package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.model.KhachHang;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class QuanLyKhachHangFrame extends JFrame {
    
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

    public QuanLyKhachHangFrame() {
        thietKeGiaoDien();
    }

    private void thietKeGiaoDien() {
        setTitle("Quản Lý Khách Hàng - PNC Store");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. FORM NHẬP LIỆU (TOP)
        JPanel pnlInput = new JPanel(new BorderLayout());
        pnlInput.setBorder(new TitledBorder("Thông tin khách hàng"));
        pnlInput.setPreferredSize(new Dimension(0, 180));

        JPanel pnlFields = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlFields.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnlFields.add(new JLabel("Họ tên KH (*):")); 
        txtTenKH = new JTextField(); pnlFields.add(txtTenKH);
        
        pnlFields.add(new JLabel("Số điện thoại (*):")); 
        txtSDT = new JTextField(); pnlFields.add(txtSDT);
        
        pnlFields.add(new JLabel("Email:")); 
        txtEmail = new JTextField(); pnlFields.add(txtEmail);
        
        pnlFields.add(new JLabel("Địa chỉ:")); 
        txtDiaChi = new JTextArea(2, 20); 
        txtDiaChi.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnlFields.add(new JScrollPane(txtDiaChi));

        pnlInput.add(pnlFields, BorderLayout.CENTER);

        // Nút bấm
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLamMoi = new JButton("Làm mới");
        btnLuu = new JButton("Lưu Khách Hàng");
        btnLuu.setBackground(new Color(40, 167, 69));
        btnLuu.setForeground(Color.WHITE);
        
        pnlBtn.add(btnLamMoi);
        pnlBtn.add(btnLuu);
        pnlInput.add(pnlBtn, BorderLayout.SOUTH);

        add(pnlInput, BorderLayout.NORTH);

        // 2. BẢNG DANH SÁCH (CENTER)
        String[] cols = {"Mã KH", "Họ Tên", "SĐT", "Email", "Địa Chỉ", "Ngày Tham Gia", "Hành Động"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Chỉ cột hành động sửa được
            }
        };
        tblKhachHang = new JTable(model);
        tblKhachHang.setRowHeight(40);
        
        // Cấu hình cột hành động (Giống bên Sản phẩm)
        tblKhachHang.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        tblKhachHang.getColumnModel().getColumn(6).setPreferredWidth(120);

        add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);
    }

    // --- CÁC CLASS XỬ LÝ NÚT BẤM TRONG BẢNG (COPY TỪ SẢN PHẨM SANG) ---
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