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
        loadLichTrinhHomNay();
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
            
            // 4. Các thông số Profile (Sản phẩm, Danh mục, Thương hiệu)
            int sp = 0, dm = 0, th = 0;
            ResultSet rsSP = conn.createStatement().executeQuery("SELECT COUNT(*) FROM SanPham");
            if(rsSP.next()) sp = rsSP.getInt(1);
            
            ResultSet rsDM = conn.createStatement().executeQuery("SELECT COUNT(*) FROM DanhMuc");
            if(rsDM.next()) dm = rsDM.getInt(1);
            
            ResultSet rsTH = conn.createStatement().executeQuery("SELECT COUNT(*) FROM ThuongHieu");
            if(rsTH.next()) th = rsTH.getInt(1);
            
            view.setStoreStats(sp, dm, th);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLichTrinhHomNay() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT KhachHang.tenKH, HoaDon.ngayLap, HoaDon.tenKhachHang " +
                         "FROM HoaDon " +
                         "LEFT JOIN KhachHang ON HoaDon.maKH = KhachHang.maKH " +
                         "WHERE DATE(HoaDon.ngayLap) = CURDATE() " +
                         "ORDER BY HoaDon.ngayLap DESC LIMIT 3";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            view.clearSchedule();
            int count = 0;
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm a");
            while (rs.next()) {
                String tenKH = rs.getString("tenKH");
                if (tenKH == null) tenKH = rs.getString("tenKhachHang");
                if (tenKH == null || tenKH.isEmpty()) tenKH = "Khách Lẻ";
                String time = sdf.format(rs.getTimestamp("ngayLap"));
                view.addScheduleTask("Giao hàng: " + tenKH, time);
                count++;
            }
            if (count == 0) {
                view.addScheduleTask("Chưa có đơn hàng", "Hôm nay");
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
                String strTrangThai = rs.getString("trangThai");
                if (strTrangThai == null || strTrangThai.isEmpty()) {
                    strTrangThai = "Hoàn thành"; // Mặc định nếu null do bảng cũ
                }
                
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
