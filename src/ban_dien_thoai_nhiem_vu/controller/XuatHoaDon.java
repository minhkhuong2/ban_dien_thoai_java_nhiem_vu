package ban_dien_thoai_nhiem_vu.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XuatHoaDon {

    // Font chữ tiếng Việt (Dùng font Arial của Windows)
    private static BaseFont bf;
    private static Font fontTieuDe;
    private static Font fontThuong;
    private static Font fontDam;

    static {
        try {
            // Đường dẫn font Arial trên Windows
            bf = BaseFont.createFont("C:\\Windows\\Fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            fontTieuDe = new Font(bf, 25, Font.BOLD, BaseColor.BLUE);
            fontThuong = new Font(bf, 12, Font.NORMAL, BaseColor.BLACK);
            fontDam = new Font(bf, 12, Font.BOLD, BaseColor.BLACK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inHoaDon(String maHD) {
        // Tên file PDF sẽ là: HoaDon_HD123456.pdf
        String path = "HoaDon_" + maHD + ".pdf";
        Document document = new Document(PageSize.A4);
        DecimalFormat df = new DecimalFormat("#,###");

        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            // 1. LẤY DỮ LIỆU TỪ DATABASE
            Connection conn = KetNoiCSDL.getConnection();
            
            // Lấy thông tin chung hóa đơn
            String sqlHD = "SELECT hd.*, nv.hoTen FROM HoaDon hd JOIN NhanVien nv ON hd.maNV = nv.maNV WHERE maHD = ?";
            PreparedStatement pstHD = conn.prepareStatement(sqlHD);
            pstHD.setString(1, maHD);
            ResultSet rsHD = pstHD.executeQuery();
            
            if (rsHD.next()) {
                String tenNV = rsHD.getString("hoTen");
                String tenKH = rsHD.getString("tenKhachHang");
                Timestamp ngayLap = rsHD.getTimestamp("ngayLap");
                double tongTien = rsHD.getDouble("tongTien");

                // 2. THIẾT KẾ GIAO DIỆN HÓA ĐƠN
                
                // --- HEADER: TÊN CỬA HÀNG ---
                Paragraph tenCuaHang = new Paragraph("PNC STORE - ĐIỆN THOẠI CHÍNH HÃNG", fontTieuDe);
                tenCuaHang.setAlignment(Element.ALIGN_CENTER);
                document.add(tenCuaHang);

                Paragraph diaChi = new Paragraph("Địa chỉ: 123 Đường ABC, Hà Nội - Hotline: 0912.345.678", fontThuong);
                diaChi.setAlignment(Element.ALIGN_CENTER);
                document.add(diaChi);
                document.add(new Paragraph(" ")); // Dòng trống
                document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));

                // --- THÔNG TIN HÓA ĐƠN ---
                PdfPTable tblInfo = new PdfPTable(2); // Bảng 2 cột không đường viền
                tblInfo.setWidthPercentage(100);
                
                addCellInfo(tblInfo, "Mã Hóa Đơn: " + maHD, Element.ALIGN_LEFT);
                addCellInfo(tblInfo, "Ngày lập: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(ngayLap), Element.ALIGN_RIGHT);
                addCellInfo(tblInfo, "Nhân viên: " + tenNV, Element.ALIGN_LEFT);
                addCellInfo(tblInfo, "Khách hàng: " + tenKH, Element.ALIGN_RIGHT);
                
                document.add(tblInfo);
                document.add(new Paragraph(" "));

                // --- BẢNG SẢN PHẨM ---
                PdfPTable table = new PdfPTable(4); // 4 Cột: Tên SP, SL, Đơn Giá, Thành Tiền
                table.setWidthPercentage(100);
                table.setWidths(new float[]{4, 1, 2, 2}); // Tỉ lệ chiều rộng cột

                // Header bảng
                addHeaderCell(table, "Tên Sản Phẩm");
                addHeaderCell(table, "SL");
                addHeaderCell(table, "Đơn Giá");
                addHeaderCell(table, "Thành Tiền");

                // Lấy chi tiết hóa đơn
                String sqlCT = "SELECT ct.*, sp.tenSP FROM ChiTietHoaDon ct JOIN SanPham sp ON ct.maSP = sp.maSP WHERE maHD = ?";
                PreparedStatement pstCT = conn.prepareStatement(sqlCT);
                pstCT.setString(1, maHD);
                ResultSet rsCT = pstCT.executeQuery();

                while (rsCT.next()) {
                    addCell(table, rsCT.getString("tenSP"), Element.ALIGN_LEFT);
                    addCell(table, String.valueOf(rsCT.getInt("soLuong")), Element.ALIGN_CENTER);
                    addCell(table, df.format(rsCT.getDouble("donGia")), Element.ALIGN_RIGHT);
                    addCell(table, df.format(rsCT.getDouble("thanhTien")), Element.ALIGN_RIGHT);
                }
                document.add(table);
                document.add(new Paragraph(" "));

                // --- TỔNG TIỀN ---
                Paragraph pTong = new Paragraph("TỔNG THANH TOÁN: " + df.format(tongTien) + " VNĐ", fontDam);
                pTong.setAlignment(Element.ALIGN_RIGHT);
                document.add(pTong);
                
                // --- FOOTER: LỜI CẢM ƠN ---
                document.add(new Paragraph(" "));
                document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
                Paragraph camOn = new Paragraph("Cảm ơn quý khách đã mua hàng tại PNC Store!", fontThuong);
                camOn.setAlignment(Element.ALIGN_CENTER);
                Paragraph henGap = new Paragraph("Hẹn gặp lại quý khách!", fontThuong);
                henGap.setAlignment(Element.ALIGN_CENTER);
                
                document.add(camOn);
                document.add(henGap);
            }

            document.close();
            
            // 3. TỰ ĐỘNG MỞ FILE PDF LÊN
            File file = new File(path);
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(null, "Đã lưu hóa đơn tại: " + file.getAbsolutePath());
                }
            } else {
                 javax.swing.JOptionPane.showMessageDialog(null, "Không tìm thấy file hóa đơn!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm phụ trợ để thêm ô vào bảng (cho gọn code)
    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, fontDam));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, fontThuong));
        cell.setHorizontalAlignment(align);
        cell.setPadding(5);
        table.addCell(cell);
    }
    
    private void addCellInfo(PdfPTable table, String text, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, fontThuong));
        cell.setHorizontalAlignment(align);
        cell.setBorder(PdfPCell.NO_BORDER); // Không viền
        table.addCell(cell);
    }
}
