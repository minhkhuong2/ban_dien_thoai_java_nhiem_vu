package ban_dien_thoai_nhiem_vu.model;

import java.sql.Date;

public class KhachHang {
    private int maKH;
    private String tenKH;
    private String sdt;
    private String diaChi;
    private Date ngayThamGia;

    public KhachHang() {}
    public KhachHang(int maKH, String tenKH, String sdt, String diaChi, Date ngayThamGia) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.ngayThamGia = ngayThamGia;
    }

    public int getMaKH() { return maKH; }
    public void setMaKH(int maKH) { this.maKH = maKH; }
    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public Date getNgayThamGia() { return ngayThamGia; }
    public void setNgayThamGia(Date ngayThamGia) { this.ngayThamGia = ngayThamGia; }
    
    @Override public String toString() { return tenKH + " (" + sdt + ")"; }

    public void setEmail(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}