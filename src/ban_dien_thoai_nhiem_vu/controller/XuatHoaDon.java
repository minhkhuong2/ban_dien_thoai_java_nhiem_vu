package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import java.awt.Font;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;

public class XuatHoaDon {
    
    // Hàm này nhận vào Mã HĐ và thực hiện in
    public void inHoaDon(String maHD) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // 1. Lấy thông tin chung Hóa Đơn
            String sqlHD = "SELECT hd.*, nv.hoTen FROM HoaDon hd " +
                           "LEFT JOIN NhanVien nv ON hd.maNV = nv.maNV " +
                           "WHERE maHD = ?";
            PreparedStatement pstHD = conn.prepareStatement(sqlHD);
            pstHD.setString(1, maHD);
            ResultSet rsHD = pstHD.executeQuery();
            
            if (!rsHD.next()) return;

            // 2. Lấy chi tiết sản phẩm
            String sqlCT = "SELECT ct.*, sp.tenSP FROM ChiTietHoaDon ct " +
                           "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                           "WHERE maHD = ?";
            PreparedStatement pstCT = conn.prepareStatement(sqlCT);
            pstCT.setString(1, maHD);
            ResultSet rsCT = pstCT.executeQuery();

            // 3. Tạo nội dung hóa đơn (Dạng văn bản để in nhiệt)
            StringBuilder bill = new StringBuilder();
            DecimalFormat df = new DecimalFormat("#,###");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            bill.append("\n          PNC MOBILE STORE          \n");
            bill.append("     ĐC: 123 Đường ABC, TP.HCM     \n");
            bill.append("       Hotline: 0909.123.456       \n");
            bill.append("====================================\n");
            bill.append("          HÓA ĐƠN BÁN LẺ           \n");
            bill.append("Số HĐ: ").append(maHD).append("\n");
            bill.append("Ngày:  ").append(sdf.format(rsHD.getTimestamp("ngayLap"))).append("\n");
            bill.append("NV:    ").append(rsHD.getString("hoTen")).append("\n");
            bill.append("KH:    ").append(rsHD.getString("tenKhachHang")).append("\n");
            bill.append("------------------------------------\n");
            bill.append(String.format("%-18s %3s %9s\n", "Tên SP", "SL", "T.Tiền"));
            bill.append("------------------------------------\n");

            while (rsCT.next()) {
                String tenSP = rsCT.getString("tenSP");
                if (tenSP.length() > 18) tenSP = tenSP.substring(0, 15) + "..."; // Cắt tên nếu dài
                int sl = rsCT.getInt("soLuong");
                double thanhTien = rsCT.getDouble("thanhTien");
                bill.append(String.format("%-18s %3d %9s\n", tenSP, sl, df.format(thanhTien)));
            }
            
            bill.append("------------------------------------\n");
            bill.append("TỔNG TIỀN:        ").append(df.format(rsHD.getDouble("tongTien"))).append(" đ\n");
            bill.append("====================================\n");
            bill.append("    Cảm ơn và hẹn gặp lại quý khách!   \n\n");

            // 4. Hiển thị và In
            JTextArea txtBill = new JTextArea(bill.toString());
            txtBill.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Font đơn cách giống máy in nhiệt
            
            // Lệnh này sẽ mở hộp thoại chọn máy in của Windows
            boolean print = txtBill.print(); 
            
            if (print) {
                JOptionPane.showMessageDialog(null, "Đã gửi lệnh in thành công!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi in ấn: " + e.getMessage());
        }
    }
}