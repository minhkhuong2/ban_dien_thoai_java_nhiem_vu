package ban_dien_thoai_nhiem_vu.model;

import java.sql.Date;

public class NhanVien {
    private String maNV;
    private String hoTen;
    private Date ngaySinh;
    private String sdt;
    private String email;
    private String taiKhoan;
    private String matKhau;
    private String vaiTro; // "ADMIN" hoặc "STAFF"

    public NhanVien() {}

    public NhanVien(String maNV, String hoTen, String taiKhoan, String vaiTro) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.taiKhoan = taiKhoan;
        this.vaiTro = vaiTro;
    }

    // Getter & Setter (Bạn tự thêm đầy đủ nếu cần, ở đây mình tạo cái chính)
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(String taiKhoan) { this.taiKhoan = taiKhoan; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
}
