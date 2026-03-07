package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.QuanLyHoaDonPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.JOptionPane;

public class QuanLyHoaDonController {
    
    private QuanLyHoaDonPanel view;
    private DecimalFormat df = new DecimalFormat("#,### đ");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public QuanLyHoaDonController(QuanLyHoaDonPanel view) {
        this.view = view;
        
        // Load danh sách ngay khi mở
        loadDanhSachHoaDon("");
        
        // 1. Sự kiện Tìm kiếm
        view.addTimKiemListener(e -> loadDanhSachHoaDon(view.getTuKhoa()));
        
        // 2. Sự kiện Làm mới (Tải lại)
        view.addLamMoiListener(e -> loadDanhSachHoaDon(""));
        
        // 3. Sự kiện Click bảng -> Xem chi tiết
        view.addBangListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTblHoaDon().getSelectedRow();
                if (row >= 0) {
                    // Lấy dữ liệu từ dòng được chọn
                    String maHD = view.getTblHoaDon().getValueAt(row, 0).toString();
                    String ngay = view.getTblHoaDon().getValueAt(row, 1).toString();
                    String nv = view.getTblHoaDon().getValueAt(row, 2).toString();
                    String kh = view.getTblHoaDon().getValueAt(row, 3).toString();
                    String tong = view.getTblHoaDon().getValueAt(row, 4).toString();
                    String trangThai = view.getTblHoaDon().getValueAt(row, 5).toString();
                    
                    // Hiển thị lên Panel chi tiết bên phải
                    view.setThongTinChiTiet(maHD, ngay, nv, kh, tong);
                    view.getCboTrangThai().setSelectedItem(trangThai);
                    
                    loadChiTietHoaDon(maHD);
                }
            }
        });
        
        // 4. Sự kiện Cập nhật Trạng Thái Hóa Đơn
        view.addCapNhatStatusListener(e -> updateTrangThaiHoaDon());

        // 5. Sự kiện In Hóa Đơn (Nếu bạn đã thêm nút btnIn bên View)
        try {
            // Kiểm tra xem nút In có tồn tại không để tránh lỗi null
            if (view.getBtnIn() != null) {
                view.getBtnIn().addActionListener(e -> xuLyInHoaDon());
            }
        } catch (Exception ex) {
            // Nếu chưa có nút In thì bỏ qua, không làm gì
        }
    }

    // --- XỬ LÝ IN ẤN ---
    private void xuLyInHoaDon() {
        int row = view.getTblHoaDon().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn hóa đơn cần in!");
            return;
        }
        String maHD = view.getTblHoaDon().getValueAt(row, 0).toString();
        
        // Gọi class XuatHoaDon để thực hiện lệnh in
        new XuatHoaDon().inHoaDon(maHD);
    }
    
    // --- XỬ LÝ CẬP NHẬT TRẠNG THÁI ---
    private void updateTrangThaiHoaDon() {
        int row = view.getTblHoaDon().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một hóa đơn từ danh sách bên trái để cập nhật!");
            return;
        }
        
        String maHD = view.getTblHoaDon().getValueAt(row, 0).toString();
        String trangThaiMoi = view.getCboTrangThai().getSelectedItem().toString();
        
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "UPDATE HoaDon SET trangThai = ? WHERE maHD = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, trangThaiMoi);
            pst.setString(2, maHD);
            
            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(view, "Cập nhật trạng thái thành công!");
                loadDanhSachHoaDon(view.getTuKhoa());
                
                // Select lại dòng vừa cập nhật
                for (int i = 0; i < view.getTblHoaDon().getRowCount(); i++) {
                    if (view.getTblHoaDon().getValueAt(i, 0).toString().equals(maHD)) {
                        view.getTblHoaDon().setRowSelectionInterval(i, i);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(view, "Phát sinh lỗi khi cập nhật!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi kết nối CSDL!");
        }
    }

    // --- LOAD DANH SÁCH (LOGIC CHUẨN: HIỆN TÊN KHÁCH THAY VÌ SĐT) ---
    private void loadDanhSachHoaDon(String keyword) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Kỹ thuật SQL:
            // LEFT JOIN KhachHang: Để lấy thông tin tên thật từ bảng Khách hàng.
            // COALESCE(a, b, c): Lấy giá trị đầu tiên không null. 
            // -> Ưu tiên lấy tenKH (bảng KhachHang). Nếu không có (null) thì lấy tenKhachHang (lưu cứng trong HoaDon).
            
            String sql = "SELECT hd.maHD, hd.ngayLap, nv.hoTen, " +
                         "COALESCE(kh.tenKH, hd.tenKhachHang, 'Khách vãng lai') as tenHienThi, " +
                         "hd.tongTien, hd.trangThai " +
                         "FROM HoaDon hd " +
                         "LEFT JOIN NhanVien nv ON hd.maNV = nv.maNV " +
                         "LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH " + 
                         "WHERE 1=1";
            
            if (!keyword.isEmpty()) {
                // Tìm kiếm thông minh: Theo Mã HĐ hoặc Tên hoặc SĐT
                sql += " AND (hd.maHD LIKE '%" + keyword + "%' " +
                       "OR kh.tenKH LIKE '%" + keyword + "%' " +
                       "OR kh.sdt LIKE '%" + keyword + "%')";
            }
            sql += " ORDER BY hd.ngayLap DESC"; // Hóa đơn mới nhất lên đầu

            ResultSet rs = conn.createStatement().executeQuery(sql);
            view.getModelHoaDon().setRowCount(0);
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("maHD"));
                row.add(sdf.format(rs.getTimestamp("ngayLap")));
                // Xử lý tên nhân viên null (phòng khi nhân viên bị xóa)
                row.add(rs.getString("hoTen") == null ? "Unknown" : rs.getString("hoTen"));
                row.add(rs.getString("tenHienThi")); // Tên khách hàng chuẩn
                row.add(df.format(rs.getDouble("tongTien")));
                
                String trangThai = rs.getString("trangThai");
                if(trangThai == null || trangThai.isEmpty()) trangThai = "Hoàn thành"; // Fallback nếu DB rỗng
                row.add(trangThai);
                
                view.getModelHoaDon().addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- LOAD CHI TIẾT HÓA ĐƠN ---
    private void loadChiTietHoaDon(String maHD) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Join bảng SanPham để lấy tên SP
            String sql = "SELECT sp.tenSP, ct.soLuong, ct.donGia, ct.thanhTien " +
                         "FROM ChiTietHoaDon ct " +
                         "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                         "WHERE ct.maHD = ?";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, maHD);
            ResultSet rs = pst.executeQuery();
            
            view.getModelChiTiet().setRowCount(0);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("tenSP"));
                row.add(rs.getInt("soLuong"));
                row.add(df.format(rs.getDouble("donGia")));
                row.add(df.format(rs.getDouble("thanhTien")));
                view.getModelChiTiet().addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
