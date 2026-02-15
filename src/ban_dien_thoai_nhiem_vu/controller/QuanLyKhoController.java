package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.QuanLyKhoFrame;
import javax.swing.JOptionPane;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Vector;

public class QuanLyKhoController {
    
    private QuanLyKhoFrame view;
    private DecimalFormat df = new DecimalFormat("#,###");

    public QuanLyKhoController(QuanLyKhoFrame view) {
        this.view = view;
        
        loadDanhSach(""); // Load tất cả lúc đầu

        // Sự kiện nút Nhập Hàng
        view.addNhapHangListener(e -> xuLyNhapHang());
        
        // Sự kiện tìm kiếm
        view.addTimKiemListener(e -> loadDanhSach(view.getTuKhoa()));
    }

    private void loadDanhSach(String keyword) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT maSP, tenSP, hangSanXuat, giaBan, soLuongTon FROM SanPham";
            if (!keyword.isEmpty()) {
                sql += " WHERE tenSP LIKE ? OR maSP LIKE ?";
            }
            
            PreparedStatement pst = conn.prepareStatement(sql);
            if (!keyword.isEmpty()) {
                pst.setString(1, "%" + keyword + "%");
                pst.setString(2, "%" + keyword + "%");
            }
            
            ResultSet rs = pst.executeQuery();
            view.getModel().setRowCount(0);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("maSP"));
                row.add(rs.getString("tenSP"));
                row.add(rs.getString("hangSanXuat"));
                row.add(df.format(rs.getDouble("giaBan")));
                row.add(rs.getInt("soLuongTon")); // Cột này quan trọng nhất
                view.getModel().addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xuLyNhapHang() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) {
            view.showMessage("Vui lòng chọn sản phẩm trong danh sách để nhập thêm!");
            return;
        }

        String maSP = view.getModel().getValueAt(row, 0).toString();
        String tenSP = view.getModel().getValueAt(row, 1).toString();
        String tonHienTai = view.getModel().getValueAt(row, 4).toString();

        // Hộp thoại nhập số lượng
        String input = JOptionPane.showInputDialog(view, 
                "Nhập số lượng hàng mới về cho: " + tenSP + "\n(Tồn hiện tại: " + tonHienTai + ")", 
                "Nhập kho", JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.isEmpty()) {
            try {
                int slNhap = Integer.parseInt(input);
                if (slNhap <= 0) {
                    view.showMessage("Số lượng nhập phải lớn hơn 0!");
                    return;
                }

                // Update Database
                try (Connection conn = KetNoiCSDL.getConnection()) {
                    String sql = "UPDATE SanPham SET soLuongTon = soLuongTon + ? WHERE maSP = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, slNhap);
                    pst.setString(2, maSP);
                    
                    int kq = pst.executeUpdate();
                    if (kq > 0) {
                        view.showMessage("✅ Đã nhập thêm " + slNhap + " cái vào kho!");
                        loadDanhSach(view.getTuKhoa()); // Refresh lại bảng
                    }
                }
            } catch (NumberFormatException e) {
                view.showMessage("Vui lòng nhập số nguyên hợp lệ!");
            } catch (Exception e) {
                view.showMessage("Lỗi: " + e.getMessage());
            }
        }
    }
}