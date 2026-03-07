# PNC Store - Hệ Thống Quản Lý Cửa Hàng Điện Thoại 📱

Chào mừng các bạn đến với dự án **PNC Store**! Đây là phần mềm được ứng dụng rộng rãi trong việc quản lý bán hàng cho hệ thống cửa hàng điện thoại, được xây dựng bằng **Java Swing** và cơ sở dữ liệu **MySQL**.

## 🚀 Tính Năng Nổi Bật
- **Tổng quan (Dashboard):** Xem nhanh Thống kê doanh thu, số lượng đơn hàng, biểu đồ tổng quan dễ nhìn.
- **Bán hàng (POS):** 
  - Lập hóa đơn nhanh, thêm sản phẩm giỏ hàng tiện lợi.
  - Tùy chọn phương thức Giao hàng (Tiêu chuẩn/Hỏa tốc) và nhập Voucher giảm giá.
  - Tích hợp thanh toán **Tiền mặt (COD)** và **Quét mã QR Code (VietQR / Napas247)** tự động nhận diện giá tiền.
  - In hóa đơn ra file PDF chuẩn xác.
- **Quản lý Sản Phẩm:** Quản lý kho, tồn kho, thương hiệu, danh mục. (Tích hợp Logo Thương hiệu chuyên nghiệp).
- **Quản lý Khách Hàng:** Lưu trữ và quản lý thông tin khách mua.
- **Quản lý Nhân Viên:** Phân quyền đăng nhập, Khôi phục mật khẩu thông qua mã OTP tự động được gửi tới **Email nhân viên**.

## 🛠 Nền Tảng Công Nghệ
- **Ngôn ngữ:** Java (JDK 8+)
- **Giao diện UI/UX:** Java Swing (Sử dụng biểu đồ, bo tròn giao diện hiện đại).
- **Cơ sở dữ liệu:** MySQL
- **Tool server:** XAMPP (Apache + MySQL Local)

---

## 📥 Hướng Dẫn Cài Đặt (Dành Cho Team Dev)

Để dự án có thể chạy trơn tru trên máy tính của từng thành viên mà không bị lỗi DB hay lỗi báo đỏ, mọi người làm lần lượt theo 5 bước sau nhé:

### Bước 1: Cài đặt Phần Mềm Cơ Bản
1. Đảm bảo máy đã được cài **Java Development Kit (JDK 8** trở lên).
2. Tải và cài đặt **XAMPP** để tạo cơ sở dữ liệu cục bộ.
3. Mở sẵn IDE mà bạn đang xài (Đề xuất: IntelliJ IDEA, NetBeans, hoặc Eclipse).

### Bước 2: Thiết lập Cơ Sở Dữ Liệu (Database)
1. Mở **XAMPP Control Panel** và nhấn `Start` cho 2 mục: **Apache** và **MySQL**.
2. Mở trình duyệt web, vào địa chỉ: `http://localhost/phpmyadmin/`.
3. Nhập tạo một Database mới tinh, đặt tên (Cực kì quan trọng): `quanlydienthoai_pnc` (Khuyến nghị chọn Bảng mã `utf8mb4_general_ci` hoặc `utf8_unicode_ci`).
4. Import cấu trúc bảng:
   - Trong giao diện phpMyAdmin của `quanlydienthoai_pnc`, bấm vào tab **Nhập (Import)**.
   - Tìm đến file `quanlydienthoai_pnc.sql` ở ngay ngoài thư mục mã nguồn này.
   - Nhấn "Go / Thực hiện" để import danh sách các bảng (SanPham, HoaDon, ThuongHieu...).

### Bước 3: Mở Code Trong IDE
- Tùy vào IDE để bạn Open Project thư mục `Ban_dien_thoai_nhiem_vu`.
- **Cực kì quan trọng:** Dự án này có sử dụng vài file `.jar` hỗ trợ (PDF, Javax.mail gửi Email, và MySQL Connector). Bạn cần nhấp chuột phải vào Project -> chọn **Build Path / Add Libraries/Add JARs** -> Kéo thả hết thư viện `.jar` từ thư mục `lib` vào dự án để hết báo lỗi đỏ.

### Bước 4: Kiểm Tra Đường Truyền Database
Bạn nào dùng pass thư mục MySQL (XAMPP tự custom) thì mở đường dẫn file này:
👉 `src/ban_dien_thoai_nhiem_vu/database/KetNoiCSDL.java`

Kiểm tra 3 dòng chữ này xem đã khớp với máy mình chưa:
```java
String url = "jdbc:mysql://localhost:3306/quanlydienthoai_pnc...";
String user = "root";
String password = ""; // Điền pass Xampp của ae vào đây (mặc định để trống)
```

### Bước 5: Chạy Ứng Dụng 🚀
- Tìm đến file chứa hàm cấu trúc `main()` khởi động app:
  Mở `src/ban_dien_thoai_nhiem_vu/view/DangNhapFrame.java` (hoặc `Main.java` nếu có file khởi tạo)
- Chuột phải trên màn hình code -> `Run As -> Java Application` (Hoặc nhấn `Shift + F6` trên NetBeans).

## 💡 Ghi Chú Hỗ Trợ Nhau
Nếu tải code về mà có ai cài đặt bị bung lỗi `ClassNotFoundException: com.mysql.cj.jdbc.Driver` thì là **bước 3** anh em import file Libraries chưa nhận. Mọi người cứ hú trên group để team_teamview hỗ trợ nhé! Chúc project "PNC Store" chạy ngon lành! 🔥
