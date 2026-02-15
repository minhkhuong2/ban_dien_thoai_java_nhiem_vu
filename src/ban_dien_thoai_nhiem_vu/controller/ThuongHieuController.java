package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.QuanLyThuongHieuPanel;
import javax.swing.*;
import java.sql.*;

public class ThuongHieuController {
    
    private QuanLyThuongHieuPanel view;

    public ThuongHieuController(QuanLyThuongHieuPanel view) {
        this.view = view;
        loadData();
        
        view.addThemListener(e -> themThuongHieu());
        view.addXoaListener(e -> xoaThuongHieu());
    }

    private void loadData() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM ThuongHieu");
            view.getModel().setRowCount(0);
            while(rs.next()) {
                // Logo tạm thời để text, nâng cao thì dùng Icon renderer
                view.getModel().addRow(new Object[]{rs.getInt("maTH"), "(Logo)", rs.getString("tenTH")});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void themThuongHieu() {
        String ten = JOptionPane.showInputDialog(view, "Nhập tên thương hiệu mới:");
        if (ten != null && !ten.trim().isEmpty()) {
            try (Connection conn = KetNoiCSDL.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("INSERT INTO ThuongHieu(tenTH) VALUES(?)");
                pst.setString(1, ten);
                pst.executeUpdate();
                loadData();
                JOptionPane.showMessageDialog(view, "Thêm thành công!");
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    
    private void xoaThuongHieu() {
        int row = view.getTable().getSelectedRow();
        if(row < 0) {
            JOptionPane.showMessageDialog(view, "Chọn thương hiệu cần xóa!");
            return;
        }
        
        String id = view.getModel().getValueAt(row, 0).toString();
        if(JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = KetNoiCSDL.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("DELETE FROM ThuongHieu WHERE maTH = ?");
                pst.setString(1, id);
                pst.executeUpdate();
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Không thể xóa (Thương hiệu đang được sử dụng trong SP).");
            }
        }
    }
}