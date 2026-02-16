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
    private NhanVien nvHienTai;

    public ThongTinTaiKhoanController(ThongTinTaiKhoanPanel view) {
        this.view = view;
        this.nvHienTai = TaiKhoanSession.taiKhoanHienTai;

        // Load dữ liệu
        if (nvHienTai != null) {
            view.setThongTin(nvHienTai);
        }

        view.addLuuThongTinListener(e -> xuLyCapNhatThongTin());
        view.addDoiMatKhauListener(e -> xuLyDoiMatKhau());
    }

    private void xuLyCapNhatThongTin() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "UPDATE NhanVien SET hoTen=?, sdt=?, email=?, ngaySinh=?, hinhAnh=? WHERE maNV=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            
            pst.setString(1, view.getHoTen());
            pst.setString(2, view.getSDT());
            pst.setString(3, view.getEmail());
            
            String ns = view.getNgaySinh();
            if(ns.isEmpty()) pst.setNull(4, java.sql.Types.DATE);
            else pst.setString(4, ns);

            String pathAnh = view.getDuongDanAnhMoi();
            if (pathAnh == null) pathAnh = nvHienTai.getHinhAnh();
            pst.setString(5, pathAnh);

            pst.setString(6, nvHienTai.getMaNV());

            pst.executeUpdate();
            
            // Cập nhật lại Session để hiển thị đúng
            nvHienTai.setHoTen(view.getHoTen());
            nvHienTai.setSdt(view.getSDT());
            nvHienTai.setEmail(view.getEmail());
            // nvHienTai.setNgaySinh(...); // Nếu Model có hàm setNgaySinh(String) hoặc Date
            nvHienTai.setHinhAnh(pathAnh);
            
            view.showMessage("Cập nhật hồ sơ thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Lỗi: " + e.getMessage());
        }
    }

    private void xuLyDoiMatKhau() {
        String mkCu = view.getMatKhauCu();
        String mkMoi = view.getMatKhauMoi();
        String xacNhan = view.getXacNhan();
        
        if (mkCu.isEmpty() || mkMoi.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin mật khẩu!");
            return;
        }
        
        if (!mkMoi.equals(xacNhan)) {
            view.showMessage("Mật khẩu xác nhận không khớp!");
            return;
        }
        
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // 1. Kiểm tra mật khẩu cũ có đúng không
            String sqlCheck = "SELECT * FROM NhanVien WHERE maNV = ? AND matKhau = ?";
            PreparedStatement pstCheck = conn.prepareStatement(sqlCheck);
            pstCheck.setString(1, nvHienTai.getMaNV());
            pstCheck.setString(2, mkCu);
            
            if (!pstCheck.executeQuery().next()) {
                view.showMessage("Mật khẩu hiện tại không đúng!");
                return;
            }
            
            // 2. Cập nhật mật khẩu mới
            String sqlUpdate = "UPDATE NhanVien SET matKhau = ? WHERE maNV = ?";
            PreparedStatement pstUp = conn.prepareStatement(sqlUpdate);
            pstUp.setString(1, mkMoi);
            pstUp.setString(2, nvHienTai.getMaNV());
            pstUp.executeUpdate();
            
            view.showMessage("Đổi mật khẩu thành công!");
            view.clearPassFields();
            
            // (Tùy chọn) Cập nhật lại mật khẩu trong Session nếu Model có lưu
            // nvHienTai.setMatKhau(mkMoi); 
            
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Lỗi hệ thống: " + e.getMessage());
        }
    }
}