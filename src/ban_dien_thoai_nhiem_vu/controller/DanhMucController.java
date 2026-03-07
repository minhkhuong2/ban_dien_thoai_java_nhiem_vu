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
        view.addSuaListener(e -> suaDanhMuc());
        view.addXoaListener(e -> xoaDanhMuc());
        view.addTableClickListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                xuLyChonBang();
            }
        });
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

    private void xuLyChonBang() {
        int row = view.getTable().getSelectedRow();
        if(row >= 0) {
            int id = (int) view.getModel().getValueAt(row, 0);
            String ten = (String) view.getModel().getValueAt(row, 1);
            view.setSelectedId(id);
            view.setTenDM(ten);
        }
    }

    private void suaDanhMuc() {
        int id = view.getSelectedId();
        if(id == -1) {
            view.showMessage("Vui lòng chọn danh mục để sửa!");
            return;
        }
        String ten = view.getTenDM();
        if(ten.isEmpty()) {
            view.showMessage("Tên danh mục không được để trống!");
            return;
        }
        
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Check trùng
            PreparedStatement check = conn.prepareStatement("SELECT * FROM DanhMuc WHERE tenDM = ? AND maDM != ?");
            check.setString(1, ten);
            check.setInt(2, id);
            if(check.executeQuery().next()) {
                view.showMessage("Danh mục này đã tồn tại!");
                return;
            }

            PreparedStatement pst = conn.prepareStatement("UPDATE DanhMuc SET tenDM = ? WHERE maDM = ?");
            pst.setString(1, ten);
            pst.setInt(2, id);
            pst.executeUpdate();
            
            view.setTenDM("");
            view.setSelectedId(-1);
            loadData();
            view.showMessage("Cập nhật thành công!");
        } catch (Exception e) { 
            view.showMessage("Lỗi: " + e.getMessage());
        }
    }

    private void xoaDanhMuc() {
        int id = view.getSelectedId();
        if(id == -1) {
            view.showMessage("Vui lòng chọn danh mục để xóa!");
            return;
        }
        
        int chon = view.showConfirm("Bạn có chắc chắn muốn xóa danh mục này? Các sản phẩm thuộc danh mục này cũng có thể bị ảnh hưởng!");
        if(chon == JOptionPane.YES_OPTION) {
            try (Connection conn = KetNoiCSDL.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("DELETE FROM DanhMuc WHERE maDM = ?");
                pst.setInt(1, id);
                pst.executeUpdate();
                
                view.setTenDM("");
                view.setSelectedId(-1);
                loadData();
                view.showMessage("Xóa danh mục thành công!");
            } catch (Exception e) { 
                view.showMessage("Không thể xóa danh mục này (có thể do đang chứa sản phẩm)!");
            }
        }
    }
}
