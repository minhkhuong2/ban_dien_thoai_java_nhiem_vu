package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

public class MainController {
    private MainFrame view;

    public MainController(MainFrame view) {
        this.view = view;
        
        // --- GỌI HÀM LẤY SỐ LIỆU THẬT NGAY KHI KHỞI ĐỘNG ---
        loadDashboardData();

        // 1. Các sự kiện chuyển màn hình
        view.addBanHangListener(e -> {
            BanHangFrame bhFrame = new BanHangFrame();
            new BanHangController(bhFrame);
            bhFrame.setVisible(true); 
            // Khi đóng màn hình bán hàng, có thể load lại data nếu muốn (nâng cao)
        });

        view.addSanPhamListener(e -> {
            QuanLySanPhamFrame spFrame = new QuanLySanPhamFrame();
            new SanPhamController(spFrame);
            spFrame.setVisible(true);
        });

        view.addKhachHangListener(e -> {
            QuanLyKhachHangFrame khFrame = new QuanLyKhachHangFrame();
            new KhachHangController(khFrame);
            khFrame.setVisible(true);
        });

        view.addGiamGiaListener(e -> {
            QuanLyGiamGiaFrame ggFrame = new QuanLyGiamGiaFrame();
            new GiamGiaController(ggFrame);
            ggFrame.setVisible(true);
        });
        
        view.addHoaDonListener(e -> {
            QuanLyHoaDonFrame hdFrame = new QuanLyHoaDonFrame();
            new QuanLyHoaDonController(hdFrame);
            hdFrame.setVisible(true);
        });

        view.addThongKeListener(e -> {
            ThongKeFrame tkFrame = new ThongKeFrame();
            new ThongKeController(tkFrame);
            tkFrame.setVisible(true);
        });

        view.addDangXuatListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn thoát?", "Đăng xuất", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0); 
        });
    }

    // --- HÀM LẤY SỐ LIỆU TỪ DATABASE ---
    private void loadDashboardData() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            Statement st = conn.createStatement();
            DecimalFormat df = new DecimalFormat("#,###");

            // 1. Đếm tổng số sản phẩm (Số mẫu mã)
            ResultSet rsSP = st.executeQuery("SELECT COUNT(*) FROM SanPham");
            if (rsSP.next()) {
                view.lblStatSanPham.setText(rsSP.getInt(1) + "");
            }

            // 2. Đếm tổng số đơn hàng
            ResultSet rsHD = st.executeQuery("SELECT COUNT(*) FROM HoaDon");
            if (rsHD.next()) {
                view.lblStatDonHang.setText(rsHD.getInt(1) + "");
            }

            // 3. Tính tổng doanh thu
            ResultSet rsTien = st.executeQuery("SELECT SUM(tongTien) FROM HoaDon");
            if (rsTien.next()) {
                double tien = rsTien.getDouble(1);
                // Nếu > 1 tỷ thì hiển thị dạng tỷ, > 1 triệu thì hiện triệu cho gọn
                if (tien >= 1000000000) {
                    view.lblStatDoanhThu.setText(String.format("%.1f Tỷ", tien / 1000000000));
                } else if (tien >= 1000000) {
                    view.lblStatDoanhThu.setText(String.format("%.1f Tr", tien / 1000000));
                } else {
                    view.lblStatDoanhThu.setText(df.format(tien) + " đ");
                }
            }

            // 4. Đếm số khách hàng
            ResultSet rsKH = st.executeQuery("SELECT COUNT(*) FROM KhachHang");
            if (rsKH.next()) {
                view.lblStatKhachHang.setText(rsKH.getInt(1) + "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            view.lblStatSanPham.setText("Lỗi");
        }
    }
}