package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.DangKyFrame;
import ban_dien_thoai_nhiem_vu.view.DangNhapFrame;
import ban_dien_thoai_nhiem_vu.view.MainFrame;
// --- CÁC DÒNG IMPORT QUAN TRỌNG MỚI THÊM ---
import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.model.TaiKhoanSession;
// --------------------------------------------
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DangNhapController {
    private DangNhapFrame view;

    public DangNhapController(DangNhapFrame view) {
        this.view = view;

        // 1. Sự kiện Đăng nhập
        view.addDangNhapListener(e -> xuLyDangNhap());

        // 2. Sự kiện chuyển sang Đăng Ký
        view.addChuyenSangDangKyListener(e -> {
            view.dispose(); 
            DangKyFrame dkFrame = new DangKyFrame();
            new DangKyController(dkFrame);
            dkFrame.setVisible(true);
        });
    }

    private void xuLyDangNhap() {
        String tk = view.getTaiKhoan();
        String mk = view.getMatKhau();

        if (tk.isEmpty() || mk.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT * FROM NhanVien WHERE taiKhoan = ? AND matKhau = ? AND trangThai = 1";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, tk);
            pst.setString(2, mk);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // 1. Tạo đối tượng Nhân viên
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV"));
                nv.setHoTen(rs.getString("hoTen"));
                nv.setTaiKhoan(rs.getString("taiKhoan"));
                nv.setVaiTro(rs.getString("vaiTro")); 
                
                // 2. LƯU VÀO SESSION (Hết báo lỗi đỏ)
                TaiKhoanSession.taiKhoanHienTai = nv;
                
                view.showMessage("Xin chào, " + nv.getHoTen() + " (" + nv.getVaiTro() + ")");
                view.dispose(); 
                
                // 3. Mở màn hình chính
                MainFrame mainView = new MainFrame();
                new MainController(mainView);
                mainView.setVisible(true);
                
            } else {
                view.showMessage("Sai tài khoản hoặc mật khẩu!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showMessage("Lỗi kết nối: " + ex.getMessage());
        }
    }
}