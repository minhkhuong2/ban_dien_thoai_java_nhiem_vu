-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 09, 2026 lúc 05:46 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `quanlydienthoai_pnc`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitiethoadon`
--

CREATE TABLE `chitiethoadon` (
  `id` int(11) NOT NULL,
  `maHD` varchar(20) DEFAULT NULL,
  `maSP` varchar(20) DEFAULT NULL,
  `soLuong` int(11) DEFAULT NULL,
  `donGia` decimal(15,0) DEFAULT NULL,
  `thanhTien` decimal(15,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chitiethoadon`
--

INSERT INTO `chitiethoadon` (`id`, `maHD`, `maSP`, `soLuong`, `donGia`, `thanhTien`) VALUES
(1, 'HD1770608411823', 'SP04', 1, 22990000, 22990000),
(2, 'HD1770608574151', 'SP03', 1, 21990000, 21990000),
(3, 'HD1770608574151', 'SP04', 2, 22990000, 45980000),
(4, 'HD1770612244626', 'SP04', 1, 22990000, 22990000),
(5, 'HD1770612244626', 'SP05', 1, 14500000, 14500000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `giamgia`
--

CREATE TABLE `giamgia` (
  `code` varchar(20) NOT NULL,
  `tenChuongTrinh` varchar(100) DEFAULT NULL,
  `phanTramGiam` int(11) DEFAULT 0,
  `soLuong` int(11) DEFAULT 0,
  `ngayKetThuc` date DEFAULT NULL,
  `trangThai` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `giamgia`
--

INSERT INTO `giamgia` (`code`, `tenChuongTrinh`, `phanTramGiam`, `soLuong`, `ngayKetThuc`, `trangThai`) VALUES
('PNC10', 'Chào bạn mới', 10, 100, '2025-12-31', 1),
('VIP20', 'Tri ân khách VIP', 20, 48, '2026-03-01', 1),
('XAGIA', 'Xả kho lỗ vốn', 50, 5, '2025-06-30', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `maHD` varchar(20) NOT NULL,
  `ngayLap` datetime DEFAULT current_timestamp(),
  `maNV` varchar(20) DEFAULT NULL,
  `tenKhachHang` varchar(100) DEFAULT NULL,
  `tongTien` decimal(15,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `hoadon`
--

INSERT INTO `hoadon` (`maHD`, `ngayLap`, `maNV`, `tenKhachHang`, `tongTien`) VALUES
('HD1770608411823', '2026-02-09 10:40:11', 'NV01', '123', 18392000),
('HD1770608574151', '2026-02-09 10:42:54', 'NV01', 'ko', 54376000),
('HD1770612244626', '2026-02-09 11:44:04', 'NV01', '123', 37490000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `maKH` int(11) NOT NULL,
  `tenKH` varchar(100) NOT NULL,
  `sdt` varchar(15) NOT NULL,
  `diaChi` varchar(255) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `ngayThamGia` date DEFAULT curdate()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

INSERT INTO `khachhang` (`maKH`, `tenKH`, `sdt`, `diaChi`, `email`, `ngayThamGia`) VALUES
(1, 'Nguyễn Văn A', '0909123456', 'Sài Gòn, TP.HCM', 'vana@gmail.com', '2026-02-09'),
(2, 'Trần Thị B', '0912345678', 'Hà Nội', 'thib@gmail.com', '2026-02-09'),
(3, 'Nguyễn Sơn Trường', '0123456789', 'Nam định', 'truong@gmail.com', '2026-02-09');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvien`
--

CREATE TABLE `nhanvien` (
  `maNV` varchar(20) NOT NULL,
  `hoTen` varchar(100) DEFAULT NULL,
  `ngaySinh` date DEFAULT NULL,
  `sdt` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `taiKhoan` varchar(50) NOT NULL,
  `matKhau` varchar(100) NOT NULL,
  `vaiTro` varchar(20) DEFAULT 'STAFF',
  `trangThai` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `nhanvien`
--

INSERT INTO `nhanvien` (`maNV`, `hoTen`, `ngaySinh`, `sdt`, `email`, `taiKhoan`, `matKhau`, `vaiTro`, `trangThai`) VALUES
('NV01', 'Admin Quản Trị', NULL, NULL, NULL, 'admin', '123', 'ADMIN', 1),
('NV02', 'Nhân Viên Bán Hàng', NULL, NULL, NULL, 'staff', '123', 'STAFF', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `maSP` varchar(20) NOT NULL,
  `tenSP` varchar(100) DEFAULT NULL,
  `hangSanXuat` varchar(50) DEFAULT NULL,
  `giaNhap` decimal(15,0) DEFAULT 0,
  `giaBan` decimal(15,0) DEFAULT 0,
  `soLuongTon` int(11) DEFAULT 0,
  `hinhAnh` varchar(255) DEFAULT NULL,
  `moTa` text DEFAULT NULL,
  `danhMuc` varchar(50) DEFAULT NULL,
  `manHinh` varchar(100) DEFAULT NULL,
  `heDieuHanh` varchar(50) DEFAULT NULL,
  `cameraSau` varchar(100) DEFAULT NULL,
  `cameraTruoc` varchar(100) DEFAULT NULL,
  `chip` varchar(100) DEFAULT NULL,
  `ram` varchar(50) DEFAULT NULL,
  `rom` varchar(50) DEFAULT NULL,
  `pin` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`maSP`, `tenSP`, `hangSanXuat`, `giaNhap`, `giaBan`, `soLuongTon`, `hinhAnh`, `moTa`, `danhMuc`, `manHinh`, `heDieuHanh`, `cameraSau`, `cameraTruoc`, `chip`, `ram`, `rom`, `pin`) VALUES
('SP01', 'iPhone 15 Pro Max', 'Apple', 28000000, 33990000, 50, '', 'Titan tự nhiên, chip A17 Pro siêu mạnh.', 'Flagship', '6.7 inch OLED', 'iOS 17', '48MP + 12MP + 12MP', '12MP', 'Apple A17 Pro', '8GB', '256GB', '4422 mAh'),
('SP02', 'Samsung Galaxy S24 Ultra', 'Samsung', 26000000, 30990000, 45, '', 'Quyền năng Galaxy AI, bút S-Pen thần thánh.', 'Flagship', '6.8 inch Dynamic AMOLED 2X', 'Android 14', '200MP + 50MP + 12MP + 10MP', '12MP', 'Snapdragon 8 Gen 3', '12GB', '512GB', '5000 mAh'),
('SP03', 'Xiaomi 14', 'Xiaomi', 18000000, 21990000, 29, '', 'Thấu kính Leica huyền thoại, nhỏ gọn.', 'Cao cấp', '6.36 inch OLED', 'Android 14 (HyperOS)', '50MP + 50MP + 50MP', '32MP', 'Snapdragon 8 Gen 3', '12GB', '256GB', '4610 mAh'),
('SP04', 'OPPO Find N3 Flip', 'OPPO', 19000000, 22990000, 16, '', 'Gập mở sành điệu, camera Hasselblad.', 'Màn hình gập', 'Chính 6.8 inch, Phụ 3.26 inch', 'Android 13', '50MP + 48MP + 32MP', '32MP', 'Dimensity 9200', '12GB', '256GB', '4300 mAh'),
('SP05', 'iPhone 13', 'Apple', 12000000, 14500000, 99, '', 'Thiết kế vuông vức, hiệu năng vẫn rất ngon.', 'Tầm trung', '6.1 inch OLED', 'iOS 16', '12MP + 12MP', '12MP', 'Apple A15 Bionic', '4GB', '128GB', '3240 mAh'),
('SP06', 'Samsung Galaxy A55 5G', 'Samsung', 8500000, 9990000, 60, '', 'Thiết kế Key Island độc đáo, kháng nước IP67.', 'Tầm trung', '6.6 inch Super AMOLED', 'Android 14', '50MP + 12MP + 5MP', '32MP', 'Exynos 1480', '8GB', '128GB', '5000 mAh'),
('SP07', 'Realme C67', 'Realme', 4500000, 5290000, 80, '', 'Camera 108MP zoom 3x, sạc nhanh 33W.', 'Giá rẻ', '6.72 inch IPS LCD', 'Android 14', '108MP + 2MP', '8MP', 'Snapdragon 685', '8GB', '128GB', '5000 mAh'),
('SP08', 'Vivo V29e', 'Vivo', 7500000, 8990000, 40, '', 'Vòng sáng Aura 2.0, chụp đêm siêu đỉnh.', 'Tầm trung', '6.67 inch AMOLED 120Hz', 'Android 13', '64MP + 8MP', '50MP', 'Snapdragon 695', '8GB', '256GB', '4800 mAh'),
('SP09', 'iPhone 11', 'Apple', 9000000, 10990000, 15, '', 'Huyền thoại giữ giá, vẫn mượt mà.', 'Giá rẻ', '6.1 inch IPS LCD', 'iOS 15', '12MP + 12MP', '12MP', 'Apple A13 Bionic', '4GB', '64GB', '3110 mAh'),
('SP10', 'Samsung Galaxy Z Fold5', 'Samsung', 35000000, 40990000, 10, '', 'Mở ra thế giới mới, đa nhiệm cực đỉnh.', 'Flagship', 'Chính 7.6 inch, Phụ 6.2 inch', 'Android 13', '50MP + 12MP + 10MP', '10MP', 'Snapdragon 8 Gen 2', '12GB', '512GB', '4400 mAh');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD PRIMARY KEY (`id`),
  ADD KEY `maHD` (`maHD`),
  ADD KEY `maSP` (`maSP`);

--
-- Chỉ mục cho bảng `giamgia`
--
ALTER TABLE `giamgia`
  ADD PRIMARY KEY (`code`);

--
-- Chỉ mục cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`maHD`),
  ADD KEY `maNV` (`maNV`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`maKH`),
  ADD UNIQUE KEY `sdt` (`sdt`);

--
-- Chỉ mục cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`maNV`),
  ADD UNIQUE KEY `taiKhoan` (`taiKhoan`);

--
-- Chỉ mục cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`maSP`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `maKH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Ràng buộc đối với các bảng kết xuất
--

--
-- Ràng buộc cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD CONSTRAINT `chitiethoadon_ibfk_1` FOREIGN KEY (`maHD`) REFERENCES `hoadon` (`maHD`),
  ADD CONSTRAINT `chitiethoadon_ibfk_2` FOREIGN KEY (`maSP`) REFERENCES `sanpham` (`maSP`);

--
-- Ràng buộc cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `hoadon_ibfk_1` FOREIGN KEY (`maNV`) REFERENCES `nhanvien` (`maNV`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
