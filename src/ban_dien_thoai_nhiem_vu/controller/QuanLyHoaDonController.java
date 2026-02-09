package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.QuanLyHoaDonPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;
import java.text.NumberFormat;
import java.util.Locale;

public class QuanLyHoaDonController {
    private QuanLyHoaDonPanel view;
    private NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public QuanLyHoaDonController(QuanLyHoaDonPanel view) {
        this.view = view;
        loadDanhSachHoaDon();

        // Sự kiện Click vào bảng Hóa đơn
        view.addClickHoaDonListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTblHoaDon().getSelectedRow();
                if (row >= 0) {
                    String maHD = view.getTblHoaDon().getValueAt(row, 0).toString();
                    loadChiTietHoaDon(maHD);
                }
            }
        });
    }

    private void loadDanhSachHoaDon() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Sắp xếp ngày lập mới nhất lên đầu (DESC)
            String sql = "SELECT * FROM HoaDon ORDER BY ngayLap DESC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            view.getModelHoaDon().setRowCount(0); // Xóa bảng cũ

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("maHD"));
                row.add(rs.getTimestamp("ngayLap"));
                row.add(rs.getString("maNV")); 
                row.add(rs.getString("tenKhachHang"));
                
                // Format tiền cho đẹp
                double tongTien = rs.getDouble("tongTien");
                row.add(currencyVN.format(tongTien));

                view.getModelHoaDon().addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadChiTietHoaDon(String maHD) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Join bảng để lấy tên SP
            String sql = "SELECT ct.maSP, sp.tenSP, ct.soLuong, ct.donGia, ct.thanhTien " +
                         "FROM ChiTietHoaDon ct " +
                         "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                         "WHERE ct.maHD = ?";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, maHD);
            ResultSet rs = pst.executeQuery();

            view.getModelChiTiet().setRowCount(0); 

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("maSP"));
                row.add(rs.getString("tenSP"));
                row.add(rs.getInt("soLuong"));
                
                row.add(currencyVN.format(rs.getDouble("donGia")));
                row.add(currencyVN.format(rs.getDouble("thanhTien")));

                view.getModelChiTiet().addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
