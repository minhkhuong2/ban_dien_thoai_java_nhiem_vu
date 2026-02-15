package ban_dien_thoai_nhiem_vu.model;

public class SanPham {
    // Thông tin cơ bản
    private String maSP;
    private String tenSP;
    private String hangSanXuat;
    private Double giaNhap;
    private Double giaBan;
    private int tonKho;
    private String hinhAnh;
    
    // Thông tin mới thêm (cho giống web Admin)
    private String danhMuc;
    private String moTa;
    private String manHinh;
    private String cameraSau;
    private String cameraTruoc;
    private String chip;
    private String ram;
    private String rom;
    private String pin;
    private String heDieuHanh;
    private int maDM;
    private int maTH;

    public SanPham() { }

    // Constructor đầy đủ (dùng để load dữ liệu)
    public SanPham(String maSP, String tenSP, String hangSanXuat, Double giaNhap, Double giaBan, int tonKho, String hinhAnh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.hangSanXuat = hangSanXuat;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.tonKho = tonKho;
        this.hinhAnh = hinhAnh;
    }

    // --- GETTER & SETTER (Bắt buộc phải có để Controller dùng) ---
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getHangSanXuat() { return hangSanXuat; }
    public void setHangSanXuat(String hangSanXuat) { this.hangSanXuat = hangSanXuat; }

    public Double getGiaNhap() { return giaNhap; }
    public void setGiaNhap(Double giaNhap) { this.giaNhap = giaNhap; }

    public Double getGiaBan() { return giaBan; }
    public void setGiaBan(Double giaBan) { this.giaBan = giaBan; }

    public int getTonKho() { return tonKho; }
    public void setTonKho(int tonKho) { this.tonKho = tonKho; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    // Getter Setter cho các trường mới
    public String getDanhMuc() { return danhMuc; }
    public void setDanhMuc(String danhMuc) { this.danhMuc = danhMuc; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getManHinh() { return manHinh; }
    public void setManHinh(String manHinh) { this.manHinh = manHinh; }

    public String getCameraSau() { return cameraSau; }
    public void setCameraSau(String cameraSau) { this.cameraSau = cameraSau; }

    public String getCameraTruoc() { return cameraTruoc; }
    public void setCameraTruoc(String cameraTruoc) { this.cameraTruoc = cameraTruoc; }

    public String getChip() { return chip; }
    public void setChip(String chip) { this.chip = chip; }

    public String getRam() { return ram; }
    public void setRam(String ram) { this.ram = ram; }

    public String getRom() { return rom; }
    public void setRom(String rom) { this.rom = rom; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public String getHeDieuHanh() { return heDieuHanh; }
    public void setHeDieuHanh(String heDieuHanh) { this.heDieuHanh = heDieuHanh; }
    public int getMaDM() { return maDM; }
    public void setMaDM(int maDM) { this.maDM = maDM; }
    
    public int getMaTH() { return maTH; }
    public void setMaTH(int maTH) { this.maTH = maTH; }
}