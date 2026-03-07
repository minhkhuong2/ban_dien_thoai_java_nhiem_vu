# PNC Store - Hệ Thống Quản Lý Bán Điện Thoại 📱

Chào mừng các bạn đến với dự án **PNC Store**! Đây là đồ án xây dựng phần mềm quản lý cửa hàng kinh doanh điện thoại di động sử dụng ngôn ngữ **Java (Swing)** và hệ quản trị cơ sở dữ liệu **MySQL**.

Tài liệu này được viết rất chi tiết từ con số 0 để hỗ trợ tất cả các thành viên trong nhóm (kể cả những bạn mới làm quen với Java) đều có thể tự cài đặt và chạy được ứng dụng trên máy tính cá nhân của mình.

---

## 🚀 1. Các Tính Năng Của Phần Mềm
- **Đăng nhập & Xác thực:** Phân quyền nhân viên, Quên mật khẩu gửi mã OTP qua Email.
- **Trang chủ (Dashboard):** Thống kê doanh thu, đơn hàng, khách hàng trực quan.
- **Bán hàng (POS):** 
  - Giao diện tính tiền thông minh, áp dụng mã giảm giá (Voucher).
  - Lựa chọn hình thức giao hàng.
  - Tích hợp thanh toán bằng cách **Quét mã QR Code (VietQR)**.
  - In hóa đơn ra file PDF.
- **Quản lý Sản phẩm:** Thêm, sửa, xóa các dòng điện thoại, quản lý Tồn kho, Thương hiệu.
- **Quản lý Khách hàng & Nhân viên:** Quản lý thông tin, tra cứu lịch sử mua hàng, lập tài khoản cho nhân viên mới.

---

## 🛠 2. Công Cụ Cần Cài Đặt (Yêu Cầu Chuẩn Bị)
Trước khi chạy code, máy tính của bạn bắt buộc phải có sẵn các phần mềm sau (nhấn vào link để tải nếu chưa có):
1. **[XAMPP](https://www.apachefriends.org/download.html)**: Để chạy máy chủ ảo tạo cơ sở dữ liệu MySQL cục bộ.
2. **Java JDK (Phiên bản 8 trở lên)**: [Tải JDK](https://www.oracle.com/java/technologies/downloads/). (Lưu ý: nhớ set biến môi trường Environment Variables cho Java).
3. **Phần mềm lập trình (IDE)**: Đề xuất sử dụng **[Apache NetBeans](https://netbeans.apache.org/download/index.html)** hoặc Eclipse, IntelliJ IDEA.

---

## 📥 3. Hướng Dẫn Cài Đặt Chi Tiết Từng Bước

### Bước 1: Tải Code Về Máy
- Bấm vào nút màu xanh **`Code` -> `Download ZIP`** trên Github.
- Giải nén file ZIP vừa tải về ra một thư mục Dễ tìm (Khuyên dùng: Để ở ổ `D:` hoặc `C:\Users\Tên_Bạn\Documents`, tránh để tên thư mục có dấu tiếng Việt).

### Bước 2: Thiết Lập Cơ Sở Dữ Liệu (Quan Trọng Nhất 🚨)
Phần mềm cần có dữ liệu để hoạt động, bạn phải tạo "kho chứa" dữ liệu bằng XAMPP:
1. Mở phần mềm **XAMPP Control Panel** lên.
2. Nhấn nút **Start** ở 2 hàng đầu tiên là: **Apache** và **MySQL** (khi nào chữ sáng màu xanh lá cây là OK).
3. Mở trình duyệt web (Chrome/Cốc Cốc), truy cập vào đường dẫn: [http://localhost/phpmyadmin/](http://localhost/phpmyadmin/)
4. Bấm vào chữ **Mới (New)** ở cột bên trái để tạo cơ sở dữ liệu.
   - Ô "Tên cơ sở dữ liệu" nhập chính xác chữ này: **`quanlydienthoai_pnc`** *(Viết thường, không dấu, copy dán vào cho chắc chắn).*
   - Ô "Bảng mã" (Collation) bên cạnh chọn: `utf8mb4_general_ci`.
   - Bấm **Tạo (Create)**.
5. Import cấu trúc bảng:
   - Nhấn chuột chọn vào cơ sở dữ liệu `quanlydienthoai_pnc` vừa tạo ở cột trái.
   - Nhìn lên menu ngang phía trên, bấm tab **Nhập (Import)**.
   - Chọn nút **Choose File** -> Tìm đến file có tên `quanlydienthoai_pnc.sql` ở ngay trong thư mục code bạn vừa giải nén.
   - Lăn chuột xuống dưới cùng, bấm **Nhập / Go**. Chờ màn hình báo màu xanh lá cây là Add dữ liệu thành công!

### Bước 3: Mở Code Trong Netbeans / IDE
1. Mở phần mềm NetBeans (hoặc Eclipse/IntelliJ).
2. Vào Menu **File** -> **Open Project**.
3. Tìm đến thư mục ban nãy bạn giải nén (nhấn trúng thư mục `Ban_dien_thoai_nhiem_vu`) và nhấn **Open**.

*(Lưu ý: Nếu dùng dòng IDE khác, bạn có thể phải "Add thư viện (JAR)" bằng tay bằng cách nhấp phải vào project -> Build Path -> Add External JARs, tải thư viện MySQL Connector, iTextPDF, Java Mail nếu IDE báo đỏ).*

### Bước 4: Chỉnh Lại Kết Nối Database (Nếu cần)
Mở file `src/ban_dien_thoai_nhiem_vu/database/KetNoiCSDL.java`.
Tìm đoạn:
```java
String user = "root";
String password = ""; 
```
- Nếu XAMPP của bạn để mặc định, hãy **giữ nguyên phần password trống `""`**.
- Nếu bạn từng cài mật khẩu riêng cho MySQL của mình, hãy nhập mật khẩu đó vào khoảng trống.

### Bước 5: Chạy Ứng Dụng Và Trải Nghiệm 🚀
- Trong cây thư mục bên trái, tìm đến file khởi động chính: `src/ban_dien_thoai_nhiem_vu/view/DangNhapFrame.java`.
- Click **chuột phải** vào file đó -> Chọn **Run File** (hoặc nhấn phím tắt `Shift + F6` trên NetBeans).
- Khi thanh màn hình nhảy lên, bạn dùng **Tài khoản đăng nhập mặc định**:
  > **Tên đăng nhập:** `admin` (hoặc email nhân viên bạn xem trong bảng NhanVien của Database).  
  > **Mật khẩu:** `123` (hoặc theo Database).

---

## 💡 4. Cách Sửa Lỗi Thường Gặp Của Nhóm
* **Lỗi `ClassNotFoundException: com.mysql.cj.jdbc.Driver` hoặc Cắm Cờ Đỏ Thư Viện:** 
  - Máy của bạn chưa nhận Driver kết nối DB. Hãy nhấp chuột phải vào tên Project (trong NetBeans) -> chọn **Properties** -> mục **Libraries** -> bấm **Add JAR/Folder** -> Trỏ đường dẫn đến cục thư viện chứa file `.jar` (MySQL Connector) để gắn vào lại. Tương tự với thư viện PDF, QR Code.

* **Lỗi `Access denied for user 'root'@'localhost'`:**
  - Bạn điền sai mật khẩu Database rồi. Quay lại **Bước 4** và check pass cẩn thận.

* **Lỗi Port 3306 in use trong XAMPP:**
  - Máy bạn đang bị đụng phần mềm (thường do MySQL cài ngầm từ trước). Mở XAMPP -> Config ở dòng MySQL -> My.ini đổi cổng 3306 thành 3307, sau đó trong `KetNoiCSDL.java` cũng đổi URL thành `localhost:3307`.


