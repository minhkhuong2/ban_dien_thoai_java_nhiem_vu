package ban_dien_thoai_nhiem_vu.model;
import java.sql.Date;

public class KhachHang {
    private int maKH;
    private String tenKH;
    private String sdt;
    private String diaChi;
    private String email;
    private Date ngayThamGia;

    public KhachHang() {}

    public KhachHang(int maKH, String tenKH, String sdt, String diaChi, String email, Date ngayThamGia) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.email = email;
        this.ngayThamGia = ngayThamGia;
    }

    // Getters & Setters
    public int getMaKH() { return maKH; }
    public void setMaKH(int maKH) { this.maKH = maKH; }
    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Date getNgayThamGia() { return ngayThamGia; }
    public void setNgayThamGia(Date ngayThamGia) { this.ngayThamGia = ngayThamGia; }
}