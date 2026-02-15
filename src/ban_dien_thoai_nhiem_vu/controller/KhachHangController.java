package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.KhachHang;
import ban_dien_thoai_nhiem_vu.view.QuanLyKhachHangPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;

public class KhachHangController {
    
    private QuanLyKhachHangPanel view;

    public KhachHangController(QuanLyKhachHangPanel view) {
        this.view = view;
        loadDanhSach(""); // Load all
        
        // 1. Click bảng -> Đổ lên form
        view.addTableClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    KhachHang kh = new KhachHang();
                    kh.setMaKH(Integer.parseInt(view.getModel().getValueAt(row, 0).toString()));
                    kh.setTenKH(view.getModel().getValueAt(row, 1).toString());
                    kh.setSdt(view.getModel().getValueAt(row, 2).toString());
                    kh.setDiaChi(view.getModel().getValueAt(row, 3).toString());
                    view.setForm(kh);
                }
            }
        });
        
        // 2. Thêm
        view.addThemListener(e -> {
            KhachHang kh = view.getKhachHangInput();
            if(kh == null || kh.getTenKH().isEmpty() || kh.getSdt().isEmpty()) {
                view.showMessage("Vui lòng nhập Tên và SĐT!"); return;
            }
            try (Connection conn = KetNoiCSDL.getConnection()) {
                // Check trùng SĐT
                PreparedStatement check = conn.prepareStatement("SELECT * FROM KhachHang WHERE sdt=?");
                check.setString(1, kh.getSdt());
                if(check.executeQuery().next()) {
                    view.showMessage("Số điện thoại này đã tồn tại!"); return;
                }
                
                String sql = "INSERT INTO KhachHang(tenKH, sdt, diaChi) VALUES(?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, kh.getTenKH());
                pst.setString(2, kh.getSdt());
                pst.setString(3, kh.getDiaChi());
                pst.executeUpdate();
                view.showMessage("Thêm khách hàng thành công!");
                loadDanhSach("");
                view.clearForm();
            } catch (Exception ex) { ex.printStackTrace(); }
        });
        
        // 3. Sửa
        view.addSuaListener(e -> {
            KhachHang kh = view.getKhachHangInput();
            if(kh.getMaKH() == 0) { view.showMessage("Vui lòng chọn khách hàng để sửa!"); return; }
            
            try (Connection conn = KetNoiCSDL.getConnection()) {
                String sql = "UPDATE KhachHang SET tenKH=?, sdt=?, diaChi=? WHERE maKH=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, kh.getTenKH());
                pst.setString(2, kh.getSdt());
                pst.setString(3, kh.getDiaChi());
                pst.setInt(4, kh.getMaKH());
                pst.executeUpdate();
                view.showMessage("Cập nhật thành công!");
                loadDanhSach("");
            } catch (Exception ex) { ex.printStackTrace(); }
        });
        
        // 4. Xóa
        view.addXoaListener(e -> {
             KhachHang kh = view.getKhachHangInput();
             if(kh.getMaKH() == 0) return;
             if(view.showConfirm("Bạn có chắc muốn xóa khách hàng này?\nLưu ý: Nếu xóa, lịch sử mua hàng của họ có thể bị ảnh hưởng.") == JOptionPane.YES_OPTION) {
                 try (Connection conn = KetNoiCSDL.getConnection()) {
                     PreparedStatement pst = conn.prepareStatement("DELETE FROM KhachHang WHERE maKH=?");
                     pst.setInt(1, kh.getMaKH());
                     pst.executeUpdate();
                     view.showMessage("Đã xóa!");
                     loadDanhSach("");
                     view.clearForm();
                 } catch (Exception ex) { view.showMessage("Không thể xóa (Khách hàng này đã có hóa đơn)."); }
             }
        });
        
        // 5. Tìm kiếm & Làm mới
        view.addTimKiemListener(e -> loadDanhSach(view.getTuKhoa()));
        view.addLamMoiListener(e -> { view.clearForm(); loadDanhSach(""); });
    }

    private void loadDanhSach(String keyword) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT * FROM KhachHang";
            if(!keyword.isEmpty()) sql += " WHERE tenKH LIKE ? OR sdt LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            if(!keyword.isEmpty()) {
                pst.setString(1, "%" + keyword + "%");
                pst.setString(2, "%" + keyword + "%");
            }
            ResultSet rs = pst.executeQuery();
            view.getModel().setRowCount(0);
            while(rs.next()) {
                view.getModel().addRow(new Object[]{
                    rs.getInt("maKH"), rs.getString("tenKH"), rs.getString("sdt"), 
                    rs.getString("diaChi"), rs.getDate("ngayThamGia")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}