package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.QuanLyThuocTinhPanel;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThuocTinhController {
    
    private QuanLyThuocTinhPanel view;

    public ThuocTinhController(QuanLyThuocTinhPanel view) {
        this.view = view;
        loadData();
        
        // Cài đặt xử lý sự kiện cho View
        view.setListener(new QuanLyThuocTinhPanel.ThuocTinhListener() {
            @Override
            public void onAddGroup() {
                xuLyThemNhom();
            }

            @Override
            public void onAddValue(String groupName) {
                xuLyThemGiaTri(groupName);
            }

            @Override
            public void onDeleteValue(int id) {
                xuLyXoaGiaTri(id);
            }
        });
    }

    // --- 1. LOAD DATA & GOM NHÓM ---
    private void loadData() {
        // Map: Key = Tên nhóm (RAM), Value = List các {ID, Giá trị}
        Map<String, List<Object[]>> dataMap = new HashMap<>();
        
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT * FROM ThuocTinh ORDER BY tenThuocTinh, giaTri";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String group = rs.getString("tenThuocTinh");
                String val = rs.getString("giaTri");
                
                // Nếu chưa có nhóm này trong Map thì tạo mới
                dataMap.putIfAbsent(group, new ArrayList<>());
                
                // Thêm giá trị vào list của nhóm đó
                dataMap.get(group).add(new Object[]{id, val});
            }
            
            // Đẩy dữ liệu đã xử lý sang View để vẽ
            view.renderData(dataMap);
            
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- 2. THÊM NHÓM MỚI ---
    private void xuLyThemNhom() {
        String tenNhom = JOptionPane.showInputDialog(view, "Nhập tên nhóm thuộc tính mới (VD: Chất liệu):");
        if (tenNhom != null && !tenNhom.trim().isEmpty()) {
            // Khi tạo nhóm mới, ta cần tạo ít nhất 1 giá trị đầu tiên (hoặc tạo dummy)
            // Ở đây ta hỏi luôn giá trị đầu tiên
            String giaTriDau = JOptionPane.showInputDialog(view, "Nhập giá trị đầu tiên cho " + tenNhom + " (VD: Nhôm):");
            if (giaTriDau != null && !giaTriDau.trim().isEmpty()) {
                insertDB(tenNhom, giaTriDau);
            }
        }
    }

    // --- 3. THÊM GIÁ TRỊ VÀO NHÓM CÓ SẴN ---
    private void xuLyThemGiaTri(String groupName) {
        String giaTri = JOptionPane.showInputDialog(view, "Thêm giá trị mới cho nhóm '" + groupName + "':");
        if (giaTri != null && !giaTri.trim().isEmpty()) {
            insertDB(groupName, giaTri);
        }
    }
    
    private void insertDB(String ten, String giaTri) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO ThuocTinh(tenThuocTinh, giaTri) VALUES(?, ?)");
            pst.setString(1, ten);
            pst.setString(2, giaTri);
            pst.executeUpdate();
            loadData(); // Vẽ lại giao diện
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- 4. XÓA GIÁ TRỊ ---
    private void xuLyXoaGiaTri(int id) {
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn muốn xóa thuộc tính này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = KetNoiCSDL.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("DELETE FROM ThuocTinh WHERE id=?");
                pst.setInt(1, id);
                pst.executeUpdate();
                loadData(); // Vẽ lại giao diện
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}