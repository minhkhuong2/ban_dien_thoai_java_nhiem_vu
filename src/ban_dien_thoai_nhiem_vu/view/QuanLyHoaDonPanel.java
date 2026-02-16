package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class QuanLyHoaDonPanel extends JPanel {

    // Components
    private JTextField txtTimKiem;
    private JButton btnTim, btnLamMoi;
    private JTable tblHoaDon, tblChiTiet;
    private DefaultTableModel modelHoaDon, modelChiTiet;
    private JButton btnIn;
    
    // Label tổng hợp
    private JLabel lblMaHD, lblNgayLap, lblNguoiBan, lblKhachHang, lblTongTien;

    public QuanLyHoaDonPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // CHIA ĐÔI MÀN HÌNH: TRÁI (LIST) - PHẢI (DETAIL)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, taoPanelDanhSach(), taoPanelChiTiet());
        splitPane.setResizeWeight(0.6); // Bên trái chiếm 60%
        splitPane.setDividerSize(5);
        
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel taoPanelDanhSach() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(new TitledBorder("Danh sách Hóa Đơn"));
        

        // Search
        JPanel pSearch = new JPanel(new BorderLayout(5, 0));
        pSearch.setBackground(Color.WHITE);
        pSearch.setBorder(new EmptyBorder(5, 5, 5, 5));
        txtTimKiem = new JTextField();
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập mã HĐ hoặc tên khách...");
        btnTim = new JButton("Tìm");
        btnLamMoi = new JButton("Tải lại");
        btnIn = new JButton("🖨 In lại");
        
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pBtn.setBackground(Color.WHITE);
        pBtn.add(btnTim); 
        pBtn.add(btnLamMoi);
        pBtn.add(btnIn);
        
        pSearch.add(txtTimKiem, BorderLayout.CENTER);
        pSearch.add(pBtn, BorderLayout.EAST);

        // Table Invoice
        String[] cols = {"Mã HĐ", "Ngày Lập", "Nhân Viên", "Khách Hàng", "Tổng Tiền"};
        modelHoaDon = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setRowHeight(28);
        
        // Căn phải cột tiền
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblHoaDon.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        pnl.add(pSearch, BorderLayout.NORTH);
        pnl.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);
        return pnl;
    }

    private JPanel taoPanelChiTiet() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(new TitledBorder("Chi tiết Hóa Đơn"));
        
        // Info Panel
        JPanel pInfo = new JPanel(new GridLayout(5, 1, 5, 5));
        pInfo.setBackground(Color.WHITE);
        pInfo.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        pInfo.add(lblMaHD = new JLabel("Mã HĐ: ...")); lblMaHD.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pInfo.add(lblNgayLap = new JLabel("Ngày lập: ..."));
        pInfo.add(lblNguoiBan = new JLabel("Người bán: ..."));
        pInfo.add(lblKhachHang = new JLabel("Khách hàng: ..."));
        pInfo.add(lblTongTien = new JLabel("TỔNG TIỀN: ...")); 
        lblTongTien.setForeground(Color.RED); lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Table Detail
        String[] cols = {"Tên SP", "SL", "Đơn Giá", "Thành Tiền"};
        modelChiTiet = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblChiTiet = new JTable(modelChiTiet);
        tblChiTiet.setRowHeight(28);
        
        // Căn phải
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblChiTiet.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tblChiTiet.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        pnl.add(pInfo, BorderLayout.NORTH);
        pnl.add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);
        return pnl;
    }
    
    // Getters & Listeners
    public DefaultTableModel getModelHoaDon() { return modelHoaDon; }
    public DefaultTableModel getModelChiTiet() { return modelChiTiet; }
    public JTable getTblHoaDon() { return tblHoaDon; }
    public String getTuKhoa() { return txtTimKiem.getText(); }
    
    public void setThongTinChiTiet(String ma, String ngay, String nv, String kh, String tong) {
        lblMaHD.setText("Mã HĐ: " + ma);
        lblNgayLap.setText("Ngày lập: " + ngay);
        lblNguoiBan.setText("Người bán: " + nv);
        lblKhachHang.setText("Khách hàng: " + kh);
        lblTongTien.setText("TỔNG TIỀN: " + tong);
    }
    
    public void addTimKiemListener(ActionListener l) { btnTim.addActionListener(l); }
    public void addLamMoiListener(ActionListener l) { btnLamMoi.addActionListener(l); }
    public void addBangListener(MouseAdapter l) { tblHoaDon.addMouseListener(l); }
    public JButton getBtnIn() { return btnIn; }
    public void addInListener(ActionListener l) { btnIn.addActionListener(l); }
}