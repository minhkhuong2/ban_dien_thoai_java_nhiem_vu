package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.view.QuanLyNhanVienPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.JOptionPane;

public class NhanVienController {
    private QuanLyNhanVienPanel view;

    public NhanVienController(QuanLyNhanVienPanel view) {
        this.view = view;
        loadData();
        
        view.addTableListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if(row >= 0) {
                    // [SỬA] Lấy ID dạng String từ bảng
                    String id = view.getModel().getValueAt(row, 0).toString();
                    loadNhanVienLenForm(id);
                }
            }
        });
        
        view.addThemListener(e -> xuLyThem());
        view.addSuaListener(e -> xuLySua());
        view.addXoaListener(e -> xuLyXoa());
        view.addLamMoiListener(e -> { view.clearForm(); loadData(); });
    }

    private void loadData() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM NhanVien");
            view.getModel().setRowCount(0);
            while(rs.next()) {
                view.getModel().addRow(new Object[]{
                    rs.getString("maNV"), // [SỬA] getInt -> getString
                    rs.getString("hoTen"), 
                    rs.getString("taiKhoan"),
                    rs.getString("vaiTro"), 
                    rs.getInt("trangThai") == 1 ? "Hoạt động" : "Khóa"
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    // [SỬA] Tham số id là String
    private void loadNhanVienLenForm(String id) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM NhanVien WHERE maNV=?");
            pst.setString(1, id); // [SỬA] setString
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV")); // [SỬA]
                nv.setHoTen(rs.getString("hoTen"));
                nv.setSdt(rs.getString("sdt"));
                nv.setTaiKhoan(rs.getString("taiKhoan"));
                nv.setMatKhau(rs.getString("matKhau"));
                nv.setVaiTro(rs.getString("vaiTro"));
                nv.setTrangThai(rs.getInt("trangThai"));
                view.setForm(nv);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void xuLyThem() {
        NhanVien nv = view.getNhanVienInput();
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // [MỚI] Tự động sinh mã NV vì Database là Varchar (không tự tăng)
            String newMaNV = "NV" + System.currentTimeMillis() % 100000;
            
            // [SỬA] Thêm maNV vào câu Insert
            String sql = "INSERT INTO NhanVien(maNV, hoTen, sdt, taiKhoan, matKhau, vaiTro, trangThai) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, newMaNV);
            pst.setString(2, nv.getHoTen()); 
            pst.setString(3, nv.getSdt());
            pst.setString(4, nv.getTaiKhoan()); 
            pst.setString(5, nv.getMatKhau());
            pst.setString(6, nv.getVaiTro()); 
            pst.setInt(7, nv.getTrangThai());
            
            pst.executeUpdate();
            view.showMessage("Thêm thành công! Mã NV mới: " + newMaNV);
            loadData(); view.clearForm();
        } catch (Exception e) { view.showMessage("Lỗi: " + e.getMessage()); }
    }
    
    private void xuLySua() {
        NhanVien nv = view.getNhanVienInput(); // Lưu ý: hàm này trong View phải xóa Integer.parseInt đi nhé (xem lưu ý dưới)
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "UPDATE NhanVien SET hoTen=?, sdt=?, matKhau=?, vaiTro=?, trangThai=? WHERE maNV=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nv.getHoTen()); 
            pst.setString(2, nv.getSdt());
            pst.setString(3, nv.getMatKhau()); 
            pst.setString(4, nv.getVaiTro());
            pst.setInt(5, nv.getTrangThai()); 
            pst.setString(6, nv.getMaNV()); // [SỬA] setString cho điều kiện WHERE
            
            pst.executeUpdate();
            view.showMessage("Cập nhật thành công!");
            loadData();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void xuLyXoa() {
        NhanVien nv = view.getNhanVienInput();
        if(JOptionPane.showConfirmDialog(view, "Bạn muốn khóa tài khoản " + nv.getMaNV() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION)==0){
            try (Connection conn = KetNoiCSDL.getConnection()) {
                // [SỬA] Dùng PreparedStatement cho an toàn với String
                PreparedStatement pst = conn.prepareStatement("UPDATE NhanVien SET trangThai=0 WHERE maNV=?");
                pst.setString(1, nv.getMaNV());
                pst.executeUpdate();
                view.showMessage("Đã khóa tài khoản!");
                loadData();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}