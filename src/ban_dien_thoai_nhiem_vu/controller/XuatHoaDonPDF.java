package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

public class XuatHoaDonPDF {

    public void xuatHoaDon(String maHD) {
        try {
            // 1. Tạo file PDF và Font chữ Tiếng Việt
            String path = "HoaDon_" + maHD + ".pdf";
            Document document = new Document(PageSize.A5); // Khổ giấy A5 (nhỏ gọn cho hóa đơn)
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            // Font chữ Tiếng Việt (Lấy từ Windows)
            BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTieuDe = new Font(bf, 16, Font.BOLD);
            Font fontThuong = new Font(bf, 10, Font.NORMAL);
            Font fontDam = new Font(bf, 10, Font.BOLD);

            // 2. Lấy dữ liệu Hóa Đơn từ DB
            Connection conn = KetNoiCSDL.getConnection();
            String sqlHD = "SELECT hd.*, nv.hoTen FROM HoaDon hd JOIN NhanVien nv ON hd.maNV = nv.maNV WHERE maHD = ?";
            PreparedStatement pstHD = conn.prepareStatement(sqlHD);
            pstHD.setString(1, maHD);
            ResultSet rsHD = pstHD.executeQuery();

            if (rsHD.next()) {
                // --- HEADER ---
                Paragraph title = new Paragraph("PNC MOBILE STORE", fontTieuDe);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                
                Paragraph address = new Paragraph("ĐC: 123 Đường ABC, TP.HCM - Hotline: 0909.123.456", fontThuong);
                address.setAlignment(Element.ALIGN_CENTER);
                document.add(address);
                
                document.add(new Paragraph("----------------------------------------------------------"));
                
                Paragraph subTitle = new Paragraph("HÓA ĐƠN THANH TOÁN", new Font(bf, 14, Font.BOLD));
                subTitle.setAlignment(Element.ALIGN_CENTER);
                document.add(subTitle);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                document.add(new Paragraph("Mã HĐ: " + maHD, fontThuong));
                document.add(new Paragraph("Ngày:  " + sdf.format(rsHD.getTimestamp("ngayLap")), fontThuong));
                document.add(new Paragraph("NV:    " + rsHD.getString("hoTen"), fontThuong));
                document.add(new Paragraph("KH:    " + rsHD.getString("tenKhachHang"), fontThuong));
                document.add(Chunk.NEWLINE); // Xuống dòng

                // --- TABLE SẢN PHẨM ---
                PdfPTable table = new PdfPTable(4); // 4 Cột: Tên, SL, Giá, Thành tiền
                table.setWidthPercentage(100);
                table.setWidths(new float[]{40f, 15f, 20f, 25f}); // Tỉ lệ rộng cột

                // Header Bảng
                addCellHeader(table, "Tên SP", fontDam);
                addCellHeader(table, "SL", fontDam);
                addCellHeader(table, "Đơn Giá", fontDam);
                addCellHeader(table, "Thành Tiền", fontDam);

                // Dữ liệu Chi tiết
                String sqlCT = "SELECT ct.*, sp.tenSP FROM ChiTietHoaDon ct JOIN SanPham sp ON ct.maSP = sp.maSP WHERE maHD = ?";
                PreparedStatement pstCT = conn.prepareStatement(sqlCT);
                pstCT.setString(1, maHD);
                ResultSet rsCT = pstCT.executeQuery();

                DecimalFormat df = new DecimalFormat("#,###");
                double tongTien = rsHD.getDouble("tongTien");

                while (rsCT.next()) {
                    table.addCell(new Phrase(rsCT.getString("tenSP"), fontThuong));
                    
                    PdfPCell cellSL = new PdfPCell(new Phrase(rsCT.getString("soLuong"), fontThuong));
                    cellSL.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellSL);
                    
                    PdfPCell cellGia = new PdfPCell(new Phrase(df.format(rsCT.getDouble("donGia")), fontThuong));
                    cellGia.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cellGia);

                    PdfPCell cellThanhTien = new PdfPCell(new Phrase(df.format(rsCT.getDouble("thanhTien")), fontThuong));
                    cellThanhTien.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cellThanhTien);
                }
                document.add(table);

                // --- FOOTER TỔNG TIỀN ---
                document.add(Chunk.NEWLINE);
                Paragraph pTong = new Paragraph("TỔNG CỘNG: " + df.format(tongTien) + " VNĐ", fontDam);
                pTong.setAlignment(Element.ALIGN_RIGHT);
                document.add(pTong);
                
                Paragraph pCamOn = new Paragraph("Cảm ơn quý khách và hẹn gặp lại!", new Font(bf, 10, Font.ITALIC));
                pCamOn.setAlignment(Element.ALIGN_CENTER);
                pCamOn.setSpacingBefore(20);
                document.add(pCamOn);
            }

            document.close();
            conn.close();
            
            // TỰ ĐỘNG MỞ FILE PDF SAU KHI XUẤT
            if (Desktop.isDesktopSupported()) {
                File file = new File(path);
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(null, "Đã xuất file: " + path);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi in PDF: " + e.getMessage());
        }
    }

    // Hàm phụ để tạo Header bảng cho gọn
    private void addCellHeader(PdfPTable table, String name, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(name, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }
}