package ban_dien_thoai_nhiem_vu.model;

public class TaiKhoanSession {
    // Biến tĩnh (static) để lưu thông tin người đang đăng nhập
    // Có thể gọi ở bất cứ đâu trong chương trình mà không cần truyền qua lại
    public static NhanVien taiKhoanHienTai = null;

    // Hàm kiểm tra xem có phải Admin không
    public static boolean isAdmin() {
        if (taiKhoanHienTai == null) return false;
        return taiKhoanHienTai.getVaiTro().equalsIgnoreCase("ADMIN");
    }
}