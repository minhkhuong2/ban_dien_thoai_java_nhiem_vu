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
        view.addTrangChuListener(e -> {
            view.setActiveButton(view.getBtnTrangChu());
            TrangChuPanel homePanel = new TrangChuPanel(view.lblStatDoanhThu, view.lblStatDonHang, view.lblStatKhachHang);
            view.showPanel(homePanel);
            loadDashboardData();     // Update Label Stats
            loadRecentOrders(homePanel); // Update Table
            loadRevenueChart(homePanel); // Update Chart
            
            // Add Search Listener
            homePanel.addSearchListener(evt -> {
                String keyword = homePanel.getSearchText();
                if (!keyword.isEmpty()) {
                    openBanHangPanel(keyword);
                }
            });
        });

        view.addBanHangListener(e -> openBanHangPanel(""));

        view.addSanPhamListener(e -> {
            view.setActiveButton(view.getBtnSanPham());
            QuanLySanPhamPanel spPanel = new QuanLySanPhamPanel();
            new SanPhamController(spPanel);
            view.showPanel(spPanel);
        });

        view.addKhachHangListener(e -> {
            view.setActiveButton(view.getBtnKhachHang());
            QuanLyKhachHangPanel khPanel = new QuanLyKhachHangPanel();
            new KhachHangController(khPanel);
            view.showPanel(khPanel);
        });

        view.addGiamGiaListener(e -> {
            view.setActiveButton(view.getBtnGiamGia());
            QuanLyGiamGiaPanel ggPanel = new QuanLyGiamGiaPanel();
            new GiamGiaController(ggPanel);
            view.showPanel(ggPanel);
        });
        
        view.addHoaDonListener(e -> {
            view.setActiveButton(view.getBtnHoaDon());
            QuanLyHoaDonPanel hdPanel = new QuanLyHoaDonPanel();
            new QuanLyHoaDonController(hdPanel);
            view.showPanel(hdPanel);
        });

        view.addThongKeListener(e -> {
            view.setActiveButton(view.getBtnThongKe());
            ThongKePanel tkPanel = new ThongKePanel();
            new ThongKeController(tkPanel);
            view.showPanel(tkPanel);
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

            // 1. Đếm tổng số sản phẩm
            ResultSet rsSP = st.executeQuery("SELECT COUNT(*) FROM SanPham");
            if (rsSP.next()) view.lblStatSanPham.setText(rsSP.getInt(1) + "");

            // 2. Đếm tổng số đơn hàng
            ResultSet rsHD = st.executeQuery("SELECT COUNT(*) FROM HoaDon");
            if (rsHD.next()) view.lblStatDonHang.setText(rsHD.getInt(1) + "");

            // 3. Tính tổng doanh thu
            ResultSet rsTien = st.executeQuery("SELECT SUM(tongTien) FROM HoaDon");
            if (rsTien.next()) {
                double tien = rsTien.getDouble(1);
                if (tien >= 1000000000) view.lblStatDoanhThu.setText(String.format("%.1f Tỷ", tien / 1000000000));
                else if (tien >= 1000000) view.lblStatDoanhThu.setText(String.format("%.1f Tr", tien / 1000000));
                else view.lblStatDoanhThu.setText(df.format(tien) + " đ");
            }

            // 4. Đếm số khách hàng
            ResultSet rsKH = st.executeQuery("SELECT COUNT(*) FROM KhachHang");
            if (rsKH.next()) view.lblStatKhachHang.setText(rsKH.getInt(1) + "");

        } catch (Exception e) {
            e.printStackTrace();
            view.lblStatSanPham.setText("Lỗi");
        }
    }
    
    private void loadRecentOrders(TrangChuPanel homePanel) {
        homePanel.clearRecentOrders();
        DecimalFormat df = new DecimalFormat("#,### đ");
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT tenKhachHang, tongTien, ngayLap FROM HoaDon ORDER BY ngayLap DESC LIMIT 5";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while(rs.next()) {
                homePanel.addRecentOrder(new Object[]{
                    rs.getString("tenKhachHang"),
                    df.format(rs.getDouble("tongTien")),
                    rs.getString("ngayLap")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void loadRevenueChart(TrangChuPanel homePanel) {
        org.jfree.data.category.DefaultCategoryDataset dataset = new org.jfree.data.category.DefaultCategoryDataset();
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Lấy 7 ngày gần nhất
            // Cú pháp MySQL: DATE_SUB(CURDATE(), INTERVAL 7 DAY)
            // Tuy nhiên để đơn giản hoá ta lấy 7 record theo ngày gần nhất có doanh thu
            // Hoặc query chuẩn:
            String sql = "SELECT DATE(ngayLap) as ngay, SUM(tongTien) as doanhThu FROM HoaDon GROUP BY DATE(ngayLap) ORDER BY ngay DESC LIMIT 7";
            
            // Vì ORDER BY DESC nên ta cần lật ngược lại để biểu đồ chạy từ trái sang phải (Quá khứ -> Hiện tại)
            // Dùng List tạm
            java.util.List<String> days = new java.util.ArrayList<>();
            java.util.List<Double> revenues = new java.util.ArrayList<>();
            
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while(rs.next()) {
                days.add(0, rs.getString("ngay")); // Insert đầu để reverse
                revenues.add(0, rs.getDouble("doanhThu"));
            }
            
            for(int i=0; i<days.size(); i++) {
                dataset.addValue(revenues.get(i), "Doanh Thu", days.get(i));
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        org.jfree.chart.JFreeChart barChart = org.jfree.chart.ChartFactory.createBarChart(
                "", "Ngày", "VNĐ", 
                dataset, org.jfree.chart.plot.PlotOrientation.VERTICAL, false, true, false);
        
        homePanel.setChart(barChart);
    }

    private void openBanHangPanel(String searchKeyword) {
        view.setActiveButton(view.getBtnBanHang());
        BanHangPanel bhPanel = new BanHangPanel();
        
        // Pass keyword if present
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            new BanHangController(bhPanel, searchKeyword);
        } else {
            new BanHangController(bhPanel);
        }
        
        view.showPanel(bhPanel);
    }
}
