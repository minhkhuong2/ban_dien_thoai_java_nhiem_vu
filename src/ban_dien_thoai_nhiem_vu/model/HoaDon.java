package ban_dien_thoai_nhiem_vu.model;
import java.sql.Timestamp;

public class HoaDon {
    private String maHD;
    private Timestamp ngayLap;
    private String maNV;
    private String tenKhachHang;
    private double tongTien;

    public HoaDon() {}

    public HoaDon(String maHD, Timestamp ngayLap, String maNV, String tenKhachHang, double tongTien) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.maNV = maNV;
        this.tenKhachHang = tenKhachHang;
        this.tongTien = tongTien;
    }

    // Getter & Setter
    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }
    public Timestamp getNgayLap() { return ngayLap; }
    public void setNgayLap(Timestamp ngayLap) { this.ngayLap = ngayLap; }
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }
    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
}