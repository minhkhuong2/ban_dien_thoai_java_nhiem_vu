package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class QuanLyThuocTinhPanel extends JPanel {
    
    private JPanel pnlContainer;
    private JButton btnThemNhom;

    public interface ThuocTinhListener {
        void onAddGroup();
        void onAddValue(String groupName);
        void onDeleteValue(int id);
    }

    private ThuocTinhListener listener;

    // UI Constants
    // private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    // private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_TABLE_BORDER = new Color(222, 226, 230);

    public QuanLyThuocTinhPanel() {
        setLayout(new BorderLayout(20, 20));
        // setBackground(COLOR_BG);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // --- HEADER ---
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setOpaque(false);
        pTop.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitle = new JLabel("Thuộc Tính & Biến Thể");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        // lblTitle.setForeground(COLOR_TEXT_DARK);
        
        btnThemNhom = new JButton("+ Thêm Nhóm Mới");
        btnThemNhom.setBackground(COLOR_PRIMARY);
        btnThemNhom.setForeground(Color.WHITE);
        btnThemNhom.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThemNhom.setPreferredSize(new Dimension(170, 42));
        btnThemNhom.setBorder(null);
        btnThemNhom.setFocusPainted(false);
        btnThemNhom.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pTop.add(lblTitle, BorderLayout.WEST);
        pTop.add(btnThemNhom, BorderLayout.EAST);
        add(pTop, BorderLayout.NORTH);
        
        // --- BODY (CONTAINER CHỨA CÁC CARD) ---
        pnlContainer = new JPanel(new GridLayout(0, 3, 25, 25)); // Grid 3 cột
        pnlContainer.setOpaque(false);
        
        JScrollPane sc = new JScrollPane(pnlContainer);
        sc.setBorder(null);
        sc.getViewport().setOpaque(false);
        sc.setOpaque(false);
        sc.getVerticalScrollBar().setUnitIncrement(16);
        
        add(sc, BorderLayout.CENTER);
    }
    
    public void renderData(Map<String, List<Object[]>> data) {
        pnlContainer.removeAll(); 
        
        for (String groupName : data.keySet()) {
            pnlContainer.add(createAttributeCard(groupName, data.get(groupName)));
        }
        
        pnlContainer.revalidate();
        pnlContainer.repaint();
    }
    
    private JPanel createAttributeCard(String groupName, List<Object[]> values) {
        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout(0, 20));
        card.setBackground(UIManager.getColor("window"));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        // 1. Tiêu đề nhóm
        JLabel lblTitle = new JLabel(groupName);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        // lblTitle.setForeground(COLOR_TEXT_DARK);
        lblTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        
        // 2. Khu vực chứa Tags
        JPanel pTags = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pTags.setOpaque(false);
        
        for (Object[] val : values) {
            int id = (int) val[0];
            String text = (String) val[1];
            
            // Tag xám nhạt hiện đại
            JPanel tag = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            // tag.setBackground(new Color(248, 249, 250)); // Light BG
            tag.setOpaque(false);
            tag.setBorder(BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1));
            
            JLabel lblVal = new JLabel(text);
            lblVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            // lblVal.setForeground(COLOR_TEXT_DARK);
            
            JButton btnDel = new JButton("×");
            btnDel.setFont(new Font("Arial", Font.BOLD, 14));
            btnDel.setPreferredSize(new Dimension(20, 20));
            btnDel.setBorder(null);
            btnDel.setContentAreaFilled(false);
            btnDel.setForeground(new Color(220, 53, 69)); // Lighter red
            btnDel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDel.addActionListener(e -> {
                if(listener != null) listener.onDeleteValue(id);
            });
            
            // Hover effect cho dấu X
            btnDel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnDel.setForeground(Color.RED);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnDel.setForeground(new Color(220, 53, 69));
                }
            });
            
            tag.add(lblVal);
            tag.add(btnDel);
            pTags.add(tag);
        }
        
        // 3. Nút thêm giá trị
        JButton btnAddVal = new JButton("+ Thêm giá trị");
        btnAddVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddVal.setBorder(null);
        btnAddVal.setContentAreaFilled(false);
        btnAddVal.setForeground(COLOR_PRIMARY);
        btnAddVal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddVal.setHorizontalAlignment(SwingConstants.LEFT);
        
        btnAddVal.addActionListener(e -> {
            if(listener != null) listener.onAddValue(groupName);
        });

        // Group Content together so it aligns North inside the card
        JPanel pContent = new JPanel(new BorderLayout(0, 15));
        pContent.setOpaque(false);
        pContent.add(lblTitle, BorderLayout.NORTH);
        pContent.add(pTags, BorderLayout.CENTER);
        
        card.add(pContent, BorderLayout.NORTH);
        card.add(btnAddVal, BorderLayout.SOUTH);
        
        // Gói lại để card ko bung ra hết ô nếu nội dung ít
        cardWrapper.add(card, BorderLayout.NORTH);
        return cardWrapper;
    }
    
    public void setListener(ThuocTinhListener listener) {
        this.listener = listener;
        btnThemNhom.addActionListener(e -> listener.onAddGroup());
    }
}
