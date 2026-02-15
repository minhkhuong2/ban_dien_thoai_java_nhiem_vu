package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.model.TaiKhoanSession;
import ban_dien_thoai_nhiem_vu.view.ThongTinTaiKhoanPanel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ThongTinTaiKhoanController {
    
    private ThongTinTaiKhoanPanel view;

    public ThongTinTaiKhoanController(ThongTinTaiKhoanPanel view) {
        this.view = view;
        
        view.addDoiMatKhauListener(e -> xuLyDoiMatKhau());
        view.addHuyListener(e -> view.clearForm());
    }

    private void xuLyDoiMatKhau() {
        String mkCu = view.getMatKhauCu();
        String mkMoi = view.getMatKhauMoi();
        String xacNhan = view.getXacNhan();
        
        NhanVien nv = TaiKhoanSession.taiKhoanHienTai;
        
        // 1. Kiểm tra rỗng
        if (mkCu.isEmpty() || mkMoi.isEmpty() || xacNhan.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        // 2. Kiểm tra xác nhận mật khẩu
        if (!mkMoi.equals(xacNhan)) {
            view.showMessage("Mật khẩu mới và Xác nhận không khớp!");
            return;
        }
        
        // 3. Kiểm tra mật khẩu cũ & Cập nhật
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Check pass cũ trong DB cho chắc ăn (tránh trường hợp Session bị cũ)
            String sqlCheck = "SELECT * FROM NhanVien WHERE maNV = ? AND matKhau = ?";
            PreparedStatement pstCheck = conn.prepareStatement(sqlCheck);
            pstCheck.setString(1, nv.getMaNV());
            pstCheck.setString(2, mkCu);
            
            ResultSet rs = pstCheck.executeQuery();
            if (!rs.next()) {
                view.showMessage("Mật khẩu cũ không đúng!");
                return;
            }
            
            // Cập nhật pass mới
            String sqlUpdate = "UPDATE NhanVien SET matKhau = ? WHERE maNV = ?";
            PreparedStatement pstUp = conn.prepareStatement(sqlUpdate);
            pstUp.setString(1, mkMoi);
            pstUp.setString(2, nv.getMaNV());
            pstUp.executeUpdate();
            
            view.showMessage("Đổi mật khẩu thành công!");
            view.clearForm();
            
            // Cập nhật lại session
            nv.setMatKhau(mkMoi);
            
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Lỗi hệ thống: " + e.getMessage());
        }
    }
}