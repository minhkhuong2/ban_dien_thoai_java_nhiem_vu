package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
// Import đầy đủ các View
import ban_dien_thoai_nhiem_vu.view.MainFrame;
import ban_dien_thoai_nhiem_vu.view.TrangChuPanel;
import ban_dien_thoai_nhiem_vu.view.BanHangPanel;
import ban_dien_thoai_nhiem_vu.view.QuanLySanPhamFrame;
import ban_dien_thoai_nhiem_vu.view.QuanLyKhoFrame;
import ban_dien_thoai_nhiem_vu.view.QuanLyKhachHangPanel;
import ban_dien_thoai_nhiem_vu.view.QuanLyGiamGiaPanel;
import ban_dien_thoai_nhiem_vu.view.QuanLyHoaDonPanel;
import ban_dien_thoai_nhiem_vu.view.ThongKePanel;
import ban_dien_thoai_nhiem_vu.view.DangNhapFrame;
import ban_dien_thoai_nhiem_vu.view.QuanLyDanhMucPanel;
import ban_dien_thoai_nhiem_vu.view.QuanLyThuongHieuPanel;
import ban_dien_thoai_nhiem_vu.view.QuanLyThuocTinhPanel;
import ban_dien_thoai_nhiem_vu.controller.ThuocTinhController;
// Import các thư viện SQL & Utils
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// Import thư viện JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import ban_dien_thoai_nhiem_vu.view.QuanLyNhanVienPanel;
import ban_dien_thoai_nhiem_vu.controller.NhanVienController;

import ban_dien_thoai_nhiem_vu.view.ThongTinTaiKhoanPanel;
import ban_dien_thoai_nhiem_vu.controller.ThongTinTaiKhoanController;

public class MainController {
    
    // Khai báo biến view
    private MainFrame view;

    // Constructor
    public MainController(MainFrame view) {
        this.view = view; // Gán tham số vào biến toàn cục
        
        // 1. Setup dữ liệu ban đầu cho Dashboard
        loadDashboardData(); 
        
        // 2. Load biểu đồ & bảng cho Trang Chủ
        // Lưu ý: MainFrame đã tự tạo TrangChuPanel và add vào rồi, nhưng ở đây ta tạo mới để refresh
        // Hoặc nếu muốn lấy panel đang hiện, ta phải cast từ ContentPane (hơi phức tạp)
        // Cách đơn giản nhất: Tạo mới và set vào
        TrangChuPanel initHome = new TrangChuPanel(view.lblStatDoanhThu, view.lblStatDonHang, view.lblStatKhachHang);
        loadRevenueChart(initHome); 
        loadRecentOrders(initHome); 
        view.showPanel(initHome);   
        
        // 3. Cài đặt sự kiện click cho các nút Menu
        setupListeners();
    }

    private void setupListeners() {
        // --- TRANG CHỦ ---
        view.addTrangChuListener(e -> {
            view.setActiveButton(view.getBtnTrangChu());
            
            loadDashboardData(); // Update số liệu 3 cái label trên MainFrame
            
            // Tạo panel mới, truyền label vào để nó tự bind dữ liệu
            TrangChuPanel homePanel = new TrangChuPanel(view.lblStatDoanhThu, view.lblStatDonHang, view.lblStatKhachHang);
            loadRecentOrders(homePanel);
            loadRevenueChart(homePanel);
            
            view.showPanel(homePanel);
        });

        // --- BÁN HÀNG ---
        view.addBanHangListener(e -> openBanHangPanel(""));

        // --- KHO (MỚI THÊM) ---
        view.addKhoListener(e -> {
            view.setActiveButton(view.getBtnKho());
            QuanLyKhoFrame khoFrame = new QuanLyKhoFrame();
            new QuanLyKhoController(khoFrame);
            // Nhúng content của Frame Kho vào Panel chính
            view.showPanel(khoFrame.getContentPanePanel());
        });

        // --- SẢN PHẨM ---
        // 1. Quản lý Sản Phẩm (Danh sách cũ)
view.getMenuQuanLySP().addActionListener(e -> {
    view.setActiveButton(view.getBtnSanPham());
    QuanLySanPhamFrame spFrame = new QuanLySanPhamFrame(); 
    new QuanLySanPhamController(spFrame);
    view.showPanel((JPanel) spFrame.getContentPane());
});

// 2. Quản lý Danh Mục
view.getMenuDanhMuc().addActionListener(e -> {
    view.setActiveButton(view.getBtnSanPham());
    // [CẬP NHẬT] Khởi tạo View và Controller, sau đó hiển thị View
    QuanLyDanhMucPanel dmPanel = new QuanLyDanhMucPanel();
    new DanhMucController(dmPanel); 
    view.showPanel(dmPanel);
});
// 3. Quản lý Thương Hiệu
view.getMenuThuongHieu().addActionListener(e -> {
    view.setActiveButton(view.getBtnSanPham());
    // [CẬP NHẬT] Khởi tạo View và Controller
    QuanLyThuongHieuPanel thPanel = new QuanLyThuongHieuPanel();
    new ThuongHieuController(thPanel);
    view.showPanel(thPanel);
});

// 4. Quản lý Thuộc Tính
view.getMenuThuocTinh().addActionListener(e -> {
    view.setActiveButton(view.getBtnSanPham());
    
    // Khởi tạo Panel và Controller
    QuanLyThuocTinhPanel ttPanel = new QuanLyThuocTinhPanel();
    new ThuocTinhController(ttPanel); // <-- Gọi controller để kích hoạt logic loadData()
    
    view.showPanel(ttPanel);
});

        // --- KHÁCH HÀNG ---
        view.addKhachHangListener(e -> {
     view.setActiveButton(view.getBtnKhachHang());
     QuanLyKhachHangPanel p = new QuanLyKhachHangPanel();
     new KhachHangController(p); // Gọi Controller để chạy logic
     view.showPanel(p);
});
        view.getMenuNhanVien().addActionListener(e -> {
    view.setActiveButton(view.getBtnHeThong());
    QuanLyNhanVienPanel nvPanel = new QuanLyNhanVienPanel();
    new NhanVienController(nvPanel);
    view.showPanel(nvPanel);
});

// --- HỆ THỐNG: THÔNG TIN TÀI KHOẢN (Cho mọi người) ---
view.getMenuTaiKhoan().addActionListener(e -> {
    view.setActiveButton(view.getBtnHeThong());
    
    // Khởi tạo Panel và Controller
    ThongTinTaiKhoanPanel tkPanel = new ThongTinTaiKhoanPanel();
    new ThongTinTaiKhoanController(tkPanel);
    
    // Hiển thị
    view.showPanel(tkPanel);
});
        
        // --- VOUCHER ---
        view.addGiamGiaListener(e -> {
             view.setActiveButton(view.getBtnGiamGia());
             QuanLyGiamGiaPanel p = new QuanLyGiamGiaPanel();
             new GiamGiaController(p);
             view.showPanel(p);
        });
        
        // --- HÓA ĐƠN ---
        view.addHoaDonListener(e -> {
             view.setActiveButton(view.getBtnHoaDon());
             QuanLyHoaDonPanel p = new QuanLyHoaDonPanel();
             new QuanLyHoaDonController(p);
             view.showPanel(p);
        });
        
        // --- THỐNG KÊ ---
        view.addThongKeListener(e -> {
             view.setActiveButton(view.getBtnThongKe());
             ThongKePanel p = new ThongKePanel();
             new ThongKeController(p);
             view.showPanel(p);
        });
        
        // --- ĐĂNG XUẤT ---
        view.addDangXuatListener(e -> {
            if(JOptionPane.showConfirmDialog(view, "Đăng xuất khỏi hệ thống?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                view.dispose();
                DangNhapFrame login = new DangNhapFrame();
                new DangNhapController(login);
                login.setVisible(true);
            }
        });
    }

    // --- CÁC HÀM LOAD DỮ LIỆU ---

    private void loadDashboardData() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            Statement st = conn.createStatement();
            DecimalFormat df = new DecimalFormat("#,###");

            // 1. Tổng SP
            ResultSet rsSP = st.executeQuery("SELECT COUNT(*) FROM SanPham");
            if (rsSP.next()) view.lblStatSanPham.setText(rsSP.getInt(1) + "");

            // 2. Tổng Đơn
            ResultSet rsHD = st.executeQuery("SELECT COUNT(*) FROM HoaDon");
            if (rsHD.next()) view.lblStatDonHang.setText(rsHD.getInt(1) + "");

            // 3. Doanh Thu
            ResultSet rsTien = st.executeQuery("SELECT SUM(tongTien) FROM HoaDon");
            if (rsTien.next()) {
                double tien = rsTien.getDouble(1);
                if (tien >= 1000000000) view.lblStatDoanhThu.setText(String.format("%.1f Tỷ", tien / 1000000000));
                else if (tien >= 1000000) view.lblStatDoanhThu.setText(String.format("%.1f Tr", tien / 1000000));
                else view.lblStatDoanhThu.setText(df.format(tien) + " đ");
            }
            
            // 4. Khách Hàng
            ResultSet rsKH = st.executeQuery("SELECT COUNT(*) FROM KhachHang");
            if (rsKH.next()) view.lblStatKhachHang.setText(rsKH.getInt(1) + "");

        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void loadRecentOrders(TrangChuPanel homePanel) {
        if (homePanel == null) return;
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
        if (homePanel == null) return;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Query lấy doanh thu 7 ngày gần nhất
            String sql = "SELECT DATE(ngayLap) as ngay, SUM(tongTien) as doanhThu FROM HoaDon GROUP BY DATE(ngayLap) ORDER BY ngay DESC LIMIT 7";
            List<String> days = new ArrayList<>();
            List<Double> revenues = new ArrayList<>();
            ResultSet rs = conn.createStatement().executeQuery(sql);
            
            while(rs.next()) {
                days.add(0, rs.getString("ngay"));
                revenues.add(0, rs.getDouble("doanhThu"));
            }
            
            for(int i=0; i<days.size(); i++) dataset.addValue(revenues.get(i), "Doanh Thu", days.get(i));
        } catch (Exception e) { e.printStackTrace(); }
        
        JFreeChart barChart = ChartFactory.createBarChart("", "Ngày", "VNĐ", dataset, PlotOrientation.VERTICAL, false, true, false);
        homePanel.setChart(barChart);
    }

    private void openBanHangPanel(String keyword) {
        view.setActiveButton(view.getBtnBanHang());
        BanHangPanel bhPanel = new BanHangPanel();
        if(keyword != null && !keyword.isEmpty()) new BanHangController(bhPanel, keyword);
        else new BanHangController(bhPanel);
        view.showPanel(bhPanel);
    }
    
    // Getter cho View (nếu cần dùng ở ngoài)
    public MainFrame getView() {
        return view;
    }
}