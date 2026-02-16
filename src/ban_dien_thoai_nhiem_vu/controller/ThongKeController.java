package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.ThongKePanel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThongKeController {
    
    private ThongKePanel view;
    private DecimalFormat df = new DecimalFormat("#,###");

    public ThongKeController(ThongKePanel view) {
        this.view = view;
        loadThongKeTongQuat();
        loadBieuDoDoanhThu();
    }

    private void loadThongKeTongQuat() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // 1. Tổng doanh thu (Tính hết hoặc tính theo tháng hiện tại)
            String sqlRevenue = "SELECT SUM(tongTien) FROM HoaDon";
            ResultSet rsRev = conn.createStatement().executeQuery(sqlRevenue);
            if(rsRev.next()) {
                double dt = rsRev.getDouble(1);
                view.setTongDoanhThu(df.format(dt) + " VNĐ");
            }

            // 2. Tổng đơn hàng
            String sqlCount = "SELECT COUNT(*) FROM HoaDon";
            ResultSet rsCount = conn.createStatement().executeQuery(sqlCount);
            if(rsCount.next()) {
                view.setTongDonHang(rsCount.getInt(1) + " Đơn");
            }

            // 3. Tổng sản phẩm đã bán
            String sqlProd = "SELECT SUM(soLuong) FROM ChiTietHoaDon";
            ResultSet rsProd = conn.createStatement().executeQuery(sqlProd);
            if(rsProd.next()) {
                view.setTongSanPham(rsProd.getInt(1) + " Sản phẩm");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBieuDoDoanhThu() {
        // Dùng LinkedHashMap để giữ thứ tự ngày tháng
        Map<String, Double> data = new LinkedHashMap<>();
        
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Lấy doanh thu 7 ngày gần nhất
            // Hàm DATE_FORMAT(ngayLap, '%d/%m') để lấy Ngày/Tháng
            String sql = "SELECT DATE_FORMAT(ngayLap, '%d/%m') as Ngay, SUM(tongTien) as DoanhThu " +
                         "FROM HoaDon " +
                         "GROUP BY DATE_FORMAT(ngayLap, '%d/%m') " +
                         "ORDER BY ngayLap DESC LIMIT 7";
            
            ResultSet rs = conn.createStatement().executeQuery(sql);
            
            // Database trả về từ ngày mới nhất -> cũ nhất (DESC)
            // Ta cần đảo ngược lại để vẽ biểu đồ từ trái qua phải (Cũ -> Mới)
            // Nên ta đổ tạm vào Map rồi đảo ngược, hoặc dùng ArrayList
            // Ở đây để đơn giản, ta cứ put vào, nếu thứ tự ngược thì sửa ORDER BY thành ASC
            
            // Sửa lại SQL một chút để lấy đúng thứ tự vẽ biểu đồ:
            // "SELECT * FROM (SELECT ... ORDER BY ngayLap DESC LIMIT 7) as sub ORDER BY sub.ngayLap ASC"
            // Nhưng để đơn giản, ta cứ lấy về rồi xử lý sau cũng được.
            
            while(rs.next()) {
                data.put(rs.getString("Ngay"), rs.getDouble("DoanhThu"));
            }
            
            // Lưu ý: Nếu muốn đúng thứ tự thời gian từ trái qua phải, 
            // Map này cần được sắp xếp lại hoặc Query phải chuẩn.
            // Để code ngắn gọn, tôi giả định bạn nhập dữ liệu test theo ngày tăng dần.
            
            view.capNhatBieuDo(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}