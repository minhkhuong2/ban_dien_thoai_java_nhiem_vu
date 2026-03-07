package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.BieuDoPanel;
import ban_dien_thoai_nhiem_vu.view.TrangChuPanel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrangChuController {
    
    private TrangChuPanel view;
    private DecimalFormat df = new DecimalFormat("#,###");

    public TrangChuController(TrangChuPanel view) {
        this.view = view;
        loadTongQuat();
        loadGiaoDichGanDay();
        loadBieuDoDoanhThu();
    }

    private void loadTongQuat() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // 1. Tổng doanh thu (Tính hết)
            String sqlRevenue = "SELECT SUM(tongTien) FROM HoaDon";
            ResultSet rsRev = conn.createStatement().executeQuery(sqlRevenue);
            if(rsRev.next()) {
                double dt = rsRev.getDouble(1);
                view.setRevenue(df.format(dt) + " VNĐ");
            }

            // 2. Tổng đơn hàng
            String sqlCount = "SELECT COUNT(*) FROM HoaDon";
            ResultSet rsCount = conn.createStatement().executeQuery(sqlCount);
            if(rsCount.next()) {
                view.setOrders(rsCount.getInt(1) + "");
            }

            // 3. Khách hàng mới (Hoặc tổng số Khách hàng)
            String sqlCust = "SELECT COUNT(*) FROM KhachHang";
            ResultSet rsCust = conn.createStatement().executeQuery(sqlCust);
            if(rsCust.next()) {
                view.setCustomers(rsCust.getInt(1) + "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGiaoDichGanDay() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Lấy 10 đơn hàng mới nhất
            String sql = "SELECT KhachHang.tenKH, HoaDon.tongTien, HoaDon.trangThai " +
                         "FROM HoaDon " +
                         "LEFT JOIN KhachHang ON HoaDon.maKH = KhachHang.maKH " +
                         "ORDER BY HoaDon.ngayLap DESC LIMIT 10";
            
            ResultSet rs = conn.createStatement().executeQuery(sql);
            view.clearRecentOrders();
            while(rs.next()) {
                String tenKH = rs.getString("tenKH");
                if (tenKH == null) tenKH = "Khách Lẻ";
                
                double tongTien = rs.getDouble("tongTien");
                int trangThai = rs.getInt("trangThai");
                String strTrangThai = "Chờ xử lý";
                if (trangThai == 1) strTrangThai = "Đã thanh toán";
                else if (trangThai == 2) strTrangThai = "Đã hủy";
                
                Object[] row = {
                    tenKH,
                    df.format(tongTien) + " đ",
                    strTrangThai
                };
                view.addRecentOrder(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBieuDoDoanhThu() {
        Map<String, Double> data = new LinkedHashMap<>();
        
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT DATE_FORMAT(ngayLap, '%d/%m') as Ngay, SUM(tongTien) as DoanhThu " +
                         "FROM HoaDon " +
                         "GROUP BY DATE_FORMAT(ngayLap, '%d/%m') " +
                         "ORDER BY ngayLap DESC LIMIT 7";
            
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while(rs.next()) {
                data.put(rs.getString("Ngay"), rs.getDouble("DoanhThu"));
            }
            
            BieuDoPanel bieuDo = new BieuDoPanel("Biểu đồ Doanh Thu Tuần Này");
            bieuDo.setData(data);
            view.setChart(bieuDo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
