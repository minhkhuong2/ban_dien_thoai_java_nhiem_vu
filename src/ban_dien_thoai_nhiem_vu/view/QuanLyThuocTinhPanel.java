package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class QuanLyThuocTinhPanel extends JPanel {
    
    private JPanel pnlContainer;
    private JButton btnThemNhom;

    // Interface để Controller xử lý sự kiện
    public interface ThuocTinhListener {
        void onAddGroup();
        void onAddValue(String groupName);
        void onDeleteValue(int id);
    }

    private ThuocTinhListener listener;

    public QuanLyThuocTinhPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // --- HEADER ---
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Quản lý Thuộc tính & Biến thể");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        btnThemNhom = new JButton("+ Thêm Nhóm Mới");
        btnThemNhom.setBackground(new Color(67, 94, 190));
        btnThemNhom.setForeground(Color.WHITE);
        btnThemNhom.setPreferredSize(new Dimension(150, 40));
        btnThemNhom.setFocusPainted(false);
        
        pTop.add(lblTitle, BorderLayout.WEST);
        pTop.add(btnThemNhom, BorderLayout.EAST);
        add(pTop, BorderLayout.NORTH);
        
        // --- BODY (CONTAINER CHỨA CÁC CARD) ---
        pnlContainer = new JPanel(new GridLayout(0, 3, 20, 20)); // Grid 3 cột tự động xuống dòng
        pnlContainer.setOpaque(false);
        
        // Cho vào ScrollPane để nếu nhiều quá thì cuộn
        JScrollPane sc = new JScrollPane(pnlContainer);
        sc.setBorder(null);
        sc.getViewport().setOpaque(false);
        sc.setOpaque(false);
        sc.getVerticalScrollBar().setUnitIncrement(16);
        
        add(sc, BorderLayout.CENTER);
    }
    
    // --- HÀM VẼ GIAO DIỆN TỪ DỮ LIỆU (QUAN TRỌNG) ---
    // Controller sẽ gọi hàm này và truyền vào Map<TênNhóm, List<Object[]>>
    public void renderData(Map<String, List<Object[]>> data) {
        pnlContainer.removeAll(); // Xóa hết cái cũ
        
        for (String groupName : data.keySet()) {
            pnlContainer.add(createAttributeCard(groupName, data.get(groupName)));
        }
        
        pnlContainer.revalidate();
        pnlContainer.repaint();
    }
    
    private JPanel createAttributeCard(String groupName, List<Object[]> values) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 1. Tiêu đề nhóm (Ví dụ: RAM)
        JLabel lblTitle = new JLabel(groupName);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(33, 33, 33));
        
        // 2. Khu vực chứa các Tags (Ví dụ: 8GB, 16GB)
        JPanel pTags = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pTags.setOpaque(false);
        
        for (Object[] val : values) {
            int id = (int) val[0];
            String text = (String) val[1];
            
            // Tạo Tag màu xám
            JPanel tag = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            tag.setBackground(new Color(240, 240, 240));
            tag.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            
            JLabel lblVal = new JLabel(text);
            lblVal.setBorder(new EmptyBorder(5, 5, 5, 0));
            
            // Nút xóa nhỏ (x)
            JButton btnDel = new JButton("×");
            btnDel.setPreferredSize(new Dimension(20, 20));
            btnDel.setBorder(null);
            btnDel.setContentAreaFilled(false);
            btnDel.setForeground(Color.RED);
            btnDel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Sự kiện xóa giá trị
            btnDel.addActionListener(e -> {
                if(listener != null) listener.onDeleteValue(id);
            });
            
            tag.add(lblVal);
            tag.add(btnDel);
            pTags.add(tag);
        }
        
        // 3. Nút thêm giá trị mới vào nhóm này
        JButton btnAddVal = new JButton("+ Thêm giá trị");
        btnAddVal.setBorder(null);
        btnAddVal.setContentAreaFilled(false);
        btnAddVal.setForeground(new Color(67, 94, 190));
        btnAddVal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddVal.setHorizontalAlignment(SwingConstants.LEFT);
        
        btnAddVal.addActionListener(e -> {
            if(listener != null) listener.onAddValue(groupName);
        });

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(pTags, BorderLayout.CENTER);
        card.add(btnAddVal, BorderLayout.SOUTH);
        
        return card;
    }
    
    // Đăng ký sự kiện
    public void setListener(ThuocTinhListener listener) {
        this.listener = listener;
        btnThemNhom.addActionListener(e -> listener.onAddGroup());
    }
}