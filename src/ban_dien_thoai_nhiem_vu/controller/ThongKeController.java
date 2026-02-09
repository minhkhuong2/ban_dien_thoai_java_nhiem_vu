package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.ThongKeFrame;
// --- IMPORT THƯ VIỆN BIỂU ĐỒ ---
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
// -------------------------------
import java.awt.Color;
import java.awt.Dimension;
import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class ThongKeController {
    private ThongKeFrame view;
    private NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public ThongKeController(ThongKeFrame view) {
        this.view = view;
        loadThongKe(); // Load số liệu và bảng
        veBieuDo();    // Load biểu đồ

        view.addLamMoiListener(e -> {
            loadThongKe();
            veBieuDo();
        });
    }

    private void loadThongKe() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            if (conn == null) return;
            Statement st = conn.createStatement();
            
            // 1. TÍNH TỔNG QUAN (Card)
            // Tính tổng đơn và tổng doanh thu từ bảng HoaDon (Không join để tránh sai số tiền)
            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) as tongDon, SUM(tongTien) as tongTien FROM HoaDon");
            if (rs1.next()) {
                view.setTongDoanhThu(currencyVN.format(rs1.getDouble("tongTien")));
                view.setTongDonHang(String.valueOf(rs1.getInt("tongDon")));
            }

            // Tính tổng sản phẩm bán ra từ ChiTietHoaDon
            ResultSet rs2 = st.executeQuery("SELECT SUM(soLuong) as tongSP FROM ChiTietHoaDon");
            if (rs2.next()) view.setTongSanPham(String.valueOf(rs2.getInt("tongSP")));

            // 2. TÍNH CHI TIẾT THEO NGÀY (Table)
            // Sử dụng Subquery để tính tổng số lượng sản phẩm theo ngày mà không làm sai doanh thu
            // Logic: Group HoaDon theo ngày để lấy DoanhThu/Số Đơn -> Join Subquery lấy Số Lượng SP
            String sql = "SELECT " +
                         "   DATE(hd.ngayLap) as ngay, " +
                         "   COUNT(DISTINCT hd.maHD) as soDon, " +
                         "   SUM(hd.tongTien) as doanhThu, " +
                         "   (SELECT SUM(ct.soLuong) " +
                         "    FROM ChiTietHoaDon ct " +
                         "    JOIN HoaDon hd2 ON ct.maHD = hd2.maHD " +
                         "    WHERE DATE(hd2.ngayLap) = DATE(hd.ngayLap)) as soSP " +
                         "FROM HoaDon hd " +
                         "GROUP BY DATE(hd.ngayLap) " +
                         "ORDER BY ngay DESC";
                         
            ResultSet rs = st.executeQuery(sql);
            view.getModel().setRowCount(0);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getDate("ngay"));
                row.add(rs.getInt("soDon"));
                row.add(rs.getInt("soSP")); // Lấy từ subquery
                row.add(currencyVN.format(rs.getDouble("doanhThu")));
                view.getModel().addRow(row);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- HÀM VẼ BIỂU ĐỒ CỘT (BAR CHART) ---
    private void veBieuDo() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        try (Connection conn = KetNoiCSDL.getConnection()) {
            if (conn == null) return;
            // Lấy doanh thu 7 ngày gần nhất
            // Chỉ cần query bảng HoaDon
            String sql = "SELECT DATE(ngayLap) as ngay, SUM(tongTien) as doanhThu " +
                         "FROM HoaDon " +
                         "GROUP BY DATE(ngayLap) " +
                         "ORDER BY ngay DESC LIMIT 7"; 
            
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            // Lưu dữ liệu vào list tạm để đảo ngược (Vẽ từ Ngày Cũ -> Ngày Mới)
            List<String> listNgay = new ArrayList<>();
            List<Double> listTien = new ArrayList<>();
            
            while(rs.next()) {
                Date d = rs.getDate("ngay");
                String s = (d != null) ? new SimpleDateFormat("dd/MM").format(d) : "";
                listNgay.add(s);
                listTien.add(rs.getDouble("doanhThu"));
            }
            
            // Đảo ngược danh sách để hiển thị theo thời gian tăng dần (Trái -> Phải)
            Collections.reverse(listNgay);
            Collections.reverse(listTien);
            
            for (int i = 0; i < listNgay.size(); i++) {
                dataset.addValue(listTien.get(i), "Doanh Thu", listNgay.get(i));
            }
            
        } catch (Exception e) { e.printStackTrace(); }

        // Tạo biểu đồ
        JFreeChart barChart = ChartFactory.createBarChart(
                "BIỂU ĐỒ DOANH THU 7 NGÀY GẦN NHẤT", // Tiêu đề
                "Ngày",                        // Nhãn trục ngang
                "Doanh Thu (VNĐ)",             // Nhãn trục dọc
                dataset,                       // Dữ liệu
                PlotOrientation.VERTICAL,      // Hướng đứng
                false, true, false);           // Legend, Tooltips, URLs

        // Tùy chỉnh màu sắc cho đẹp
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY); // Thêm đường kẻ ngang
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(41, 98, 255)); // Màu Royal Blue theo theme mới
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter()); // Bỏ gradient bóng bẩy cũ kĩ
        renderer.setShadowVisible(false); // Bỏ bóng

        // Đưa biểu đồ vào Panel
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 350));
        
        if (view.pnlBieuDo != null) {
            view.pnlBieuDo.removeAll();          // Xóa biểu đồ cũ
            view.pnlBieuDo.add(chartPanel);      // Thêm biểu đồ mới
            view.pnlBieuDo.revalidate();         // Refresh layout
            view.pnlBieuDo.repaint();            // Vẽ lại
        }
    }
}
