package ban_dien_thoai_nhiem_vu.model;

public class ThuocTinh {
    private int id;
    private String tenThuocTinh; // Tên nhóm (RAM, Màu...)
    private String giaTri;       // Giá trị (8GB, Đỏ...)

    public ThuocTinh() {}
    public ThuocTinh(int id, String tenThuocTinh, String giaTri) {
        this.id = id;
        this.tenThuocTinh = tenThuocTinh;
        this.giaTri = giaTri;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTenThuocTinh() { return tenThuocTinh; }
    public void setTenThuocTinh(String tenThuocTinh) { this.tenThuocTinh = tenThuocTinh; }
    public String getGiaTri() { return giaTri; }
    public void setGiaTri(String giaTri) { this.giaTri = giaTri; }

    // Quan trọng: Hàm này giúp ComboBox hiển thị đúng chữ "8GB" thay vì mã code
    @Override
    public String toString() {
        return giaTri;
    }
}