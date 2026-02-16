package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.model.NhanVien;
import ban_dien_thoai_nhiem_vu.model.TaiKhoanSession;
import ban_dien_thoai_nhiem_vu.view.*;
import ban_dien_thoai_nhiem_vu.controller.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainController {

    private MainFrame view;

    public MainController(MainFrame view) {
        this.view = view;
        
        // 1. Setup thông tin & Phân quyền (QUAN TRỌNG NHẤT)
        setupUserSession();

        // 2. Gắn sự kiện
        setupListeners();
        
        // 3. Mở tab mặc định
        openBanHang();
    }

    private void setupUserSession() {
        NhanVien user = TaiKhoanSession.taiKhoanHienTai;
        
        if (user != null) {
            // Hiển thị tên & Vai trò
            view.setLblUserInfo(user.getHoTen() + " (" + user.getVaiTro() + ")");
            
            // --- LOGIC PHÂN QUYỀN ---
            // Nếu vai trò KHÔNG PHẢI là "QuanLy" (Tức là Nhân viên thường)
            if (!"QuanLy".equalsIgnoreCase(user.getVaiTro())) {
                
                // 1. Ẩn nút THỐNG KÊ (Nhân viên không được xem doanh thu)
                if (view.getBtnThongKe() != null) {
                    view.getBtnThongKe().setVisible(false);
                }
                
                // 2. Ẩn nút KHO (Nhân viên bán hàng không được nhập kho)
                if (view.getBtnKho() != null) {
                    view.getBtnKho().setVisible(false);
                }
                
                // 3. Ẩn nút QUẢN LÝ NHÂN VIÊN (Trong menu con Hệ thống)
                if (view.getMenuNhanVien() != null) {
                    view.getMenuNhanVien().setVisible(false);
                }
                
                // 4. (Tùy chọn) Nếu muốn ẩn luôn nút SẢN PHẨM để nhân viên không sửa giá
                // if (view.getBtnSanPham() != null) view.getBtnSanPham().setVisible(false);
                
                // Hoặc chỉ ẩn các menu con "nhạy cảm" của Sản phẩm (nếu có)
                // Ví dụ: view.getMenuDanhMuc().setVisible(false);
            }
        }
    }

    private void setupListeners() {
        // --- BUTTONS CHÍNH ---
        view.getBtnBanHang().addActionListener(e -> openBanHang());

        if (view.getBtnHoaDon() != null) {
            view.getBtnHoaDon().addActionListener(e -> {
                view.setActiveButton(view.getBtnHoaDon());
                QuanLyHoaDonPanel p = new QuanLyHoaDonPanel();
                new QuanLyHoaDonController(p);
                view.showPanel(p);
            });
        }

        // --- MENU CON SẢN PHẨM ---
        if (view.getMenuQuanLySP() != null) {
            view.getMenuQuanLySP().addActionListener(e -> {
                view.setActiveButton(view.getBtnSanPham());
                QuanLySanPhamFrame f = new QuanLySanPhamFrame();
                new QuanLySanPhamController(f);
                view.showPanel((JPanel) f.getContentPane());
            });
        }
        
        if (view.getMenuDanhMuc() != null) {
            view.getMenuDanhMuc().addActionListener(e -> {
                view.setActiveButton(view.getBtnSanPham());
                QuanLyDanhMucPanel p = new QuanLyDanhMucPanel();
                new DanhMucController(p);
                view.showPanel(p);
            });
        }

        if (view.getMenuThuongHieu() != null) {
            view.getMenuThuongHieu().addActionListener(e -> {
                view.setActiveButton(view.getBtnSanPham());
                QuanLyThuongHieuPanel p = new QuanLyThuongHieuPanel();
                new ThuongHieuController(p);
                view.showPanel(p);
            });
        }

        if (view.getMenuThuocTinh() != null) {
            view.getMenuThuocTinh().addActionListener(e -> {
                view.setActiveButton(view.getBtnSanPham());
                QuanLyThuocTinhPanel p = new QuanLyThuocTinhPanel();
                new ThuocTinhController(p);
                view.showPanel(p);
            });
        }

        // --- CÁC NÚT KHÁC ---
        if (view.getBtnKhachHang() != null) {
            view.getBtnKhachHang().addActionListener(e -> {
                view.setActiveButton(view.getBtnKhachHang());
                QuanLyKhachHangPanel p = new QuanLyKhachHangPanel();
                new KhachHangController(p);
                view.showPanel(p);
            });
        }
        
        if (view.getBtnGiamGia() != null) {
             view.getBtnGiamGia().addActionListener(e -> {
                 view.setActiveButton(view.getBtnGiamGia());
                 QuanLyGiamGiaPanel p = new QuanLyGiamGiaPanel();
                 new GiamGiaController(p);
                 view.showPanel(p);
             });
        }

        // --- CÁC NÚT ADMIN (Vẫn add sự kiện, nhưng nếu bị ẩn thì user không bấm được) ---
        if (view.getBtnThongKe() != null) {
            view.getBtnThongKe().addActionListener(e -> {
                view.setActiveButton(view.getBtnThongKe());
                ThongKePanel p = new ThongKePanel();
                new ThongKeController(p);
                view.showPanel(p);
            });
        }
        
        if (view.getMenuNhanVien() != null) {
            view.getMenuNhanVien().addActionListener(e -> {
                view.setActiveButton(view.getBtnHeThong());
                QuanLyNhanVienPanel p = new QuanLyNhanVienPanel();
                new QuanLyNhanVienController(p);
                view.showPanel(p);
            });
        }

        if (view.getBtnKho() != null) {
            view.getBtnKho().addActionListener(e -> {
                view.setActiveButton(view.getBtnKho());
                QuanLyKhoFrame f = new QuanLyKhoFrame();
                new QuanLyKhoController(f);
                view.showPanel(f.getContentPanePanel());
            });
        }

        if (view.getBtnTrangChu() != null) {
            view.getBtnTrangChu().addActionListener(e -> {
                view.setActiveButton(view.getBtnTrangChu());
                ThongKePanel p = new ThongKePanel();
                new ThongKeController(p);
                view.showPanel(p);
            });
        }
        
        if (view.getMenuTaiKhoan() != null) {
            view.getMenuTaiKhoan().addActionListener(e -> {
                view.setActiveButton(view.getBtnHeThong());
                ThongTinTaiKhoanPanel p = new ThongTinTaiKhoanPanel();
                new ThongTinTaiKhoanController(p);
                view.showPanel(p);
            });
        }

        view.getBtnDangXuat().addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(view, "Đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                view.dispose();
                DangNhapFrame login = new DangNhapFrame();
                new DangNhapController(login);
                login.setVisible(true);
                TaiKhoanSession.taiKhoanHienTai = null;
            }
        });
    }

    private void openBanHang() {
        view.setActiveButton(view.getBtnBanHang());
        BanHangPanel p = new BanHangPanel();
        new BanHangController(p);
        view.showPanel(p);
    }
}