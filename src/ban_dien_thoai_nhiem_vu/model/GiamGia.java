package ban_dien_thoai_nhiem_vu.model;
import java.sql.Date;

public class GiamGia {
    private String code;
    private String tenChuongTrinh;
    private int phanTramGiam;
    private int soLuong;
    private Date ngayKetThuc;

    public GiamGia() {}

    public GiamGia(String code, String ten, int phanTram, int sl, Date ngayKetThuc) {
        this.code = code;
        this.tenChuongTrinh = ten;
        this.phanTramGiam = phanTram;
        this.soLuong = sl;
        this.ngayKetThuc = ngayKetThuc;
    }

    // Getters & Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTenChuongTrinh() { return tenChuongTrinh; }
    public void setTenChuongTrinh(String tenChuongTrinh) { this.tenChuongTrinh = tenChuongTrinh; }
    public int getPhanTramGiam() { return phanTramGiam; }
    public void setPhanTramGiam(int phanTramGiam) { this.phanTramGiam = phanTramGiam; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
}