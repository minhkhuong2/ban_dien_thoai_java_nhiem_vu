package ban_dien_thoai_nhiem_vu.model;

public class ChiTietHoaDon {
    private int id;
    private String maHD;
    private String maSP;
    private int soLuong;
    private double donGia;
    private double thanhTien;

    public ChiTietHoaDon(String maHD, String maSP, int soLuong, double donGia, double thanhTien) {
        this.maHD = maHD;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }
    // Bạn tự thêm Getter/Setter nhé (để code ngắn gọn mình ẩn đi)
    public String getMaSP() { return maSP; }
    public int getSoLuong() { return soLuong; }
    public double getDonGia() { return donGia; }
    public double getThanhTien() { return thanhTien; }
}