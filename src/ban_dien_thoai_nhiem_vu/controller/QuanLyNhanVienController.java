package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.QuanLyNhanVienPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;

public class QuanLyNhanVienController {
    
    private QuanLyNhanVienPanel view;

    public QuanLyNhanVienController(QuanLyNhanVienPanel view) {
        this.view = view;
        loadDanhSach();
        
        view.addThemListener(e -> xuLyThem());
        view.addSuaListener(e -> xuLySua());
        view.addXoaListener(e -> xuLyXoa());
        view.addLamMoiListener(e -> { view.clearForm(); loadDanhSach(); });
        
        view.addTableListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    hienThiLenForm(view.getTable().getValueAt(row, 0).toString());
                }
            }
        });
    }

    private void loadDanhSach() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Lấy tất cả nhân viên (Lưu ý: Bạn có thể cần thêm cột sdt, email vào câu SELECT nếu bảng có)
            String sql = "SELECT * FROM NhanVien"; 
            ResultSet rs = conn.createStatement().executeQuery(sql);
            view.getModel().setRowCount(0);
            while(rs.next()) {
                Vector<Object> v = new Vector<>();
                v.add(rs.getString("maNV"));
                v.add(rs.getString("hoTen"));
                v.add(rs.getString("taiKhoan"));
                v.add(rs.getString("vaiTro"));
                // Giả sử có cột trangThai, nếu không có thì mặc định là "Hoạt động"
                try {
                    v.add(rs.getBoolean("trangThai") ? "Hoạt động" : "Đã nghỉ");
                } catch (Exception ex) { v.add("Hoạt động"); }
                
                view.getModel().addRow(v);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void hienThiLenForm(String maNV) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM NhanVien WHERE maNV=?");
            pst.setString(1, maNV);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                view.setForm(
                    rs.getString("maNV"),
                    rs.getString("hoTen"),
                    rs.getString("taiKhoan"),
                    rs.getString("sdt"), // Cần đảm bảo DB có cột này
                    rs.getString("email"), // Cần đảm bảo DB có cột này
                    rs.getString("vaiTro"),
                    true // Tạm thời để true
                );
                view.khoaMaNV();
            }
        } catch (Exception e) {}
    }

    private void xuLyThem() {
        if(view.getMaNV().isEmpty() || view.getTaiKhoan().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập Mã và Tài khoản!"); return;
        }
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Mật khẩu mặc định là 123456
            String sql = "INSERT INTO NhanVien (maNV, hoTen, taiKhoan, matKhau, vaiTro, sdt, email) VALUES (?, ?, ?, '123456', ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, view.getMaNV());
            pst.setString(2, view.getHoTen());
            pst.setString(3, view.getTaiKhoan());
            pst.setString(4, view.getVaiTro());
            pst.setString(5, view.getSDT());
            pst.setString(6, view.getEmail());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(view, "Thêm nhân viên thành công! Mật khẩu mặc định: 123456");
            view.clearForm(); loadDanhSach();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage());
        }
    }
    
    private void xuLySua() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "UPDATE NhanVien SET hoTen=?, taiKhoan=?, vaiTro=?, sdt=?, email=? WHERE maNV=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, view.getHoTen());
            pst.setString(2, view.getTaiKhoan());
            pst.setString(3, view.getVaiTro());
            pst.setString(4, view.getSDT());
            pst.setString(5, view.getEmail());
            pst.setString(6, view.getMaNV());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            view.clearForm(); loadDanhSach();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void xuLyXoa() {
        // Thực tế không nên xóa vĩnh viễn vì sẽ mất lịch sử hóa đơn
        // Chỉ nên đổi mật khẩu hoặc khóa tài khoản
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có muốn XÓA nhân viên này? (Không khuyến khích)", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = KetNoiCSDL.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("DELETE FROM NhanVien WHERE maNV=?");
                pst.setString(1, view.getMaNV());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                view.clearForm(); loadDanhSach();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Không thể xóa nhân viên này vì đã có dữ liệu bán hàng!\nHãy đổi mật khẩu hoặc khóa tài khoản.");
            }
        }
    }
}