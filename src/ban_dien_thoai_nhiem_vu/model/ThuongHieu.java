package ban_dien_thoai_nhiem_vu.model;

public class ThuongHieu {
    private int maTH;
    private String tenTH;
    private String logo;

    public ThuongHieu() {}
    public ThuongHieu(int maTH, String tenTH, String logo) {
        this.maTH = maTH; this.tenTH = tenTH; this.logo = logo;
    }

    // Getter & Setter
    public int getMaTH() { return maTH; }
    public void setMaTH(int maTH) { this.maTH = maTH; }
    public String getTenTH() { return tenTH; }
    public void setTenTH(String tenTH) { this.tenTH = tenTH; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    
    @Override public String toString() { return tenTH; }
}