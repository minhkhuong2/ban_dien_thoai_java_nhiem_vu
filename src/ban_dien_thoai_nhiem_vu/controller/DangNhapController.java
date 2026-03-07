package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.model.TaiKhoanSession;
import ban_dien_thoai_nhiem_vu.view.DangKyFrame;
import ban_dien_thoai_nhiem_vu.view.DangNhapFrame;
import ban_dien_thoai_nhiem_vu.view.MainFrame;
import ban_dien_thoai_nhiem_vu.view.QuenMatKhauDialog; // Class này đã làm ở bài trước
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DangNhapController {
    private DangNhapFrame view;

    public DangNhapController(DangNhapFrame view) {
        this.view = view;
        
        // Sự kiện Đăng nhập
        view.addLoginListener(e -> xuLyDangNhap());
        
        // Sự kiện Đăng ký
        view.addDangKyListener(e -> {
            view.dispose();
            DangKyFrame dkFrame = new DangKyFrame();
            new DangKyController(dkFrame);
            dkFrame.setVisible(true);
        });
        
        // --- [MỚI] Sự kiện Quên mật khẩu ---
        view.addQuenMatKhauListener(e -> {
             // Mở dialog Quên mật khẩu (Dialog này sẽ tự xử lý gửi mail)
             new QuenMatKhauDialog(view).setVisible(true);
        });
    }

    private void xuLyDangNhap() {
        String tk = view.getTaiKhoan();
        String mk = view.getMatKhau();

        if (tk.isEmpty() || mk.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!"); return;
        }

        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT * FROM NhanVien WHERE taiKhoan = ? AND matKhau = ? AND trangThai = 1";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, tk);
            pst.setString(2, mk);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV")); 
                nv.setHoTen(rs.getString("hoTen"));
                nv.setNgaySinh(rs.getDate("ngaySinh"));
                nv.setSdt(rs.getString("sdt"));
                nv.setEmail(rs.getString("email"));
                nv.setTaiKhoan(rs.getString("taiKhoan"));
                nv.setHinhAnh(rs.getString("hinhAnh"));
                
                String dbRole = rs.getString("vaiTro");
                if (dbRole.equalsIgnoreCase("ADMIN") || dbRole.equalsIgnoreCase("QuanLy")) {
                    nv.setVaiTro("QuanLy"); 
                } else {
                    nv.setVaiTro("NhanVien");
                }
                
                nv.setTrangThai(rs.getInt("trangThai"));
                TaiKhoanSession.taiKhoanHienTai = nv;

                view.showMessage("Xin chào, " + nv.getHoTen());
                
                view.dispose();
                MainFrame main = new MainFrame();
                new MainController(main); 
                main.setVisible(true);

            } else {
                view.showMessage("Sai tài khoản hoặc mật khẩu!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Lỗi kết nối CSDL!");
        }
    }
}
