package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.QuanLyDanhMucPanel;
import javax.swing.JOptionPane;
import java.sql.*;

public class DanhMucController {
    
    private QuanLyDanhMucPanel view;

    public DanhMucController(QuanLyDanhMucPanel view) {
        this.view = view;
        loadData();
        
        // Sự kiện thêm
        view.addThemListener(e -> themDanhMuc());
    }

    private void loadData() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM DanhMuc");
            view.getModel().setRowCount(0);
            while(rs.next()) {
                view.getModel().addRow(new Object[]{rs.getInt("maDM"), rs.getString("tenDM")});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void themDanhMuc() {
        String ten = view.getTenDM();
        if(ten.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập tên danh mục!");
            return;
        }
        
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Kiểm tra trùng tên
            PreparedStatement check = conn.prepareStatement("SELECT * FROM DanhMuc WHERE tenDM = ?");
            check.setString(1, ten);
            if(check.executeQuery().next()) {
                JOptionPane.showMessageDialog(view, "Danh mục này đã tồn tại!");
                return;
            }

            PreparedStatement pst = conn.prepareStatement("INSERT INTO DanhMuc(tenDM) VALUES(?)");
            pst.setString(1, ten);
            pst.executeUpdate();
            
            view.setTenDM("");
            loadData();
            JOptionPane.showMessageDialog(view, "Thêm danh mục thành công!");
        } catch (Exception e) { 
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage());
        }
    }
}