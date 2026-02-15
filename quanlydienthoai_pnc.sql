-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 15, 2026 lúc 02:47 PM
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
(5, 'HD1770612244626', 'SP05', 1, 14500000, 14500000),
(6, 'HD1770626347762', 'SP01', 1, 33990000, 33990000),
(7, 'HD1770626796282', 'SP01', 1, 33990000, 33990000),
(8, 'HD1770628161288', 'SP05', 1, 14500000, 14500000),
(9, 'HD1770628176021', 'SP05', 1, 14500000, 14500000),
(10, 'HD1770628936433', 'SP01', 1, 33990000, 33990000),
(11, 'HD1770685771126', 'SP04', 1, 22990000, 22990000),
(12, 'HD1770685771126', 'SP01', 1, 33990000, 33990000),
(13, 'HD1770706592713', 'SP03', 1, 21990000, 21990000),
(14, 'HD1770706592713', 'SP04', 1, 22990000, 22990000),
(15, 'HD1770706592713', 'SP01', 1, 33990000, 33990000),
(16, 'HD1771061708874', 'SP04', 3, 22990000, 68970000),
(17, 'HD1771061708874', 'SP01', 1, 33990000, 33990000),
(18, 'HD1771061708874', 'SP02', 1, 30990000, 30990000),
(19, 'HD1771163070353', 'SP05', 2, 14500000, 29000000),
(20, 'HD1771163070353', 'SP03', 1, 21990000, 21990000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietthuoctinh`
--

CREATE TABLE `chitietthuoctinh` (
  `maSP` varchar(50) NOT NULL,
  `thuocTinhID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `danhmuc`
--

CREATE TABLE `danhmuc` (
  `maDM` int(11) NOT NULL,
  `tenDM` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `danhmuc`
--

INSERT INTO `danhmuc` (`maDM`, `tenDM`) VALUES
(1, 'Flagship cao cấp'),
(2, 'Điện thoại Gaming'),
(3, 'Pin khủng'),
(4, 'Giá rẻ');

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
('10K', 'GIẢM 10K', 13, 14, '2026-03-15', 1),
('PNC10', 'Chào bạn mới', 10, 100, '2025-12-31', 1),
('VIP20', 'Tri ân khách VIP', 20, 47, '2026-03-01', 1),
('XAGIA', 'Xả kho lỗ vốn', 50, 4, '2026-07-30', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `maHD` varchar(20) NOT NULL,
  `ngayLap` datetime DEFAULT current_timestamp(),
  `maNV` varchar(20) DEFAULT NULL,
  `tenKhachHang` varchar(100) DEFAULT NULL,
  `tongTien` decimal(15,0) DEFAULT NULL,
  `maKH` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `hoadon`
--

INSERT INTO `hoadon` (`maHD`, `ngayLap`, `maNV`, `tenKhachHang`, `tongTien`, `maKH`) VALUES
('HD1770608411823', '2026-02-09 10:40:11', 'NV01', '123', 18392000, NULL),
('HD1770608574151', '2026-02-09 10:42:54', 'NV01', 'ko', 54376000, NULL),
('HD1770612244626', '2026-02-09 11:44:04', 'NV01', '123', 37490000, NULL),
('HD1770626347762', '2026-02-09 15:39:07', 'NV01', 'ad', 27192000, NULL),
('HD1770626796282', '2026-02-09 15:46:36', 'NV01', 'QE', 29571300, NULL),
('HD1770628161288', '2026-02-09 16:09:21', 'NV01', '123123123', 14500000, NULL),
('HD1770628176021', '2026-02-09 16:09:36', 'NV01', '123123123', 14500000, NULL),
('HD1770628936433', '2026-02-09 16:22:16', 'NV01', '123', 33990000, NULL),
('HD1770685771126', '2026-02-10 08:09:31', 'NV01', '123', 56980000, NULL),
('HD1770706592713', '2026-02-10 13:56:32', 'NV01', '123123', 39485000, NULL),
('HD1771061708874', '2026-02-14 16:35:08', 'NV01', '123', 133950000, NULL),
('HD1771163070353', '2026-02-15 20:44:30', 'NV01', 'user1', 50990000, NULL);

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
(3, 'Nguyễn Sơn Trường', '0123456789', 'Nam định', 'truong@gmail.com', '2026-02-09'),
(5, 'Son', '0234455666', 'Sơn Tây, Hà Nội', 'son@gmail.com', '2026-02-10');

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
('NV01', 'Admin Quản Trị', NULL, NULL, NULL, 'admin', '123456', 'ADMIN', 1),
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
  `pin` varchar(50) DEFAULT NULL,
  `maDM` int(11) DEFAULT NULL,
  `maTH` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`maSP`, `tenSP`, `hangSanXuat`, `giaNhap`, `giaBan`, `soLuongTon`, `hinhAnh`, `moTa`, `danhMuc`, `manHinh`, `heDieuHanh`, `cameraSau`, `cameraTruoc`, `chip`, `ram`, `rom`, `pin`, `maDM`, `maTH`) VALUES
('123', '132', 'Aplle', 100000, 120000, 47, 'C:\\Users\\ADMIN\\OneDrive\\Pictures\\dc5fa09d90f71aa943e6.jpg', '12123', NULL, '', '', '', '', '', '', '', '', NULL, NULL),
('SP01', 'iPhone 15 Pro Max', 'Apple', 28000000, 33990000, 44, 'C:\\Users\\ADMIN\\OneDrive\\Pictures\\d5e7cc4b98db118548ca.jpg', 'Titan tự nhiên, chip A17 Pro siêu mạnh.', NULL, '6.7 inch OLED', 'iOS 17', '48MP + 12MP + 12MP', '12MP', 'Apple A17 Pro', '8GB', '256GB', '4422 mAh', NULL, NULL),
('SP02', 'Samsung Galaxy S24 Ultra', 'Samsung', 26000000, 30990000, 44, 'C:\\Users\\ADMIN\\OneDrive\\Pictures\\Ảnh bán điện thoại\\samsung a55\\images_2.jpg', 'Quyền năng Galaxy AI, bút S-Pen thần thánh.', NULL, '6.8 inch Dynamic AMOLED 2X', 'Android 14', '200MP + 50MP + 12MP + 10MP', '12MP', 'Snapdragon 8 Gen 3', '12GB', '512GB', '5000 mAh', NULL, NULL),
('SP03', 'Xiaomi 14', 'Xiaomi', 18000000, 21990000, 27, '', 'Thấu kính Leica huyền thoại, nhỏ gọn.', 'Cao cấp', '6.36 inch OLED', 'Android 14 (HyperOS)', '50MP + 50MP + 50MP', '32MP', 'Snapdragon 8 Gen 3', '12GB', '256GB', '4610 mAh', NULL, NULL),
('SP04', 'OPPO Find N3 Flip', 'OPPO', 19000000, 22990000, 11, 'C:\\Users\\ADMIN\\OneDrive\\Pictures\\070943236593edcdb482.jpg', 'Gập mở sành điệu, camera Hasselblad.', NULL, 'Chính 6.8 inch, Phụ 3.26 inch', 'Android 13', '50MP + 48MP + 32MP', '32MP', 'Dimensity 9200', '12GB', '256GB', '4300 mAh', NULL, NULL),
('SP05', 'iPhone 13', 'Apple', 12000000, 14500000, 95, '', 'Thiết kế vuông vức, hiệu năng vẫn rất ngon.', 'Tầm trung', '6.1 inch OLED', 'iOS 16', '12MP + 12MP', '12MP', 'Apple A15 Bionic', '4GB', '128GB', '3240 mAh', NULL, NULL),
('SP06', 'Samsung Galaxy A55 5G', 'Samsung', 8500000, 9990000, 60, '', 'Thiết kế Key Island độc đáo, kháng nước IP67.', 'Tầm trung', '6.6 inch Super AMOLED', 'Android 14', '50MP + 12MP + 5MP', '32MP', 'Exynos 1480', '8GB', '128GB', '5000 mAh', NULL, NULL),
('SP07', 'Realme C67', 'Realme', 4500000, 5290000, 15, '', 'Camera 108MP zoom 3x, sạc nhanh 33W.', 'Giá rẻ', '6.72 inch IPS LCD', 'Android 14', '108MP + 2MP', '8MP', 'Snapdragon 685', '8GB', '128GB', '5000 mAh', NULL, NULL),
('SP08', 'Vivo V29e', 'Vivo', 7500000, 8990000, 40, '', 'Vòng sáng Aura 2.0, chụp đêm siêu đỉnh.', 'Tầm trung', '6.67 inch AMOLED 120Hz', 'Android 13', '64MP + 8MP', '50MP', 'Snapdragon 695', '8GB', '256GB', '4800 mAh', NULL, NULL),
('SP09', 'iPhone 11', 'Apple', 9000000, 10990000, 15, '', 'Huyền thoại giữ giá, vẫn mượt mà.', 'Giá rẻ', '6.1 inch IPS LCD', 'iOS 15', '12MP + 12MP', '12MP', 'Apple A13 Bionic', '4GB', '64GB', '3110 mAh', NULL, NULL),
('SP10', 'Samsung Galaxy Z Fold5', 'Samsung', 35000000, 40990000, 10, '', 'Mở ra thế giới mới, đa nhiệm cực đỉnh.', 'Flagship', 'Chính 7.6 inch, Phụ 6.2 inch', 'Android 13', '50MP + 12MP + 10MP', '10MP', 'Snapdragon 8 Gen 2', '12GB', '512GB', '4400 mAh', NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thuoctinh`
--

CREATE TABLE `thuoctinh` (
  `id` int(11) NOT NULL,
  `tenThuocTinh` varchar(50) NOT NULL,
  `giaTri` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `thuoctinh`
--

INSERT INTO `thuoctinh` (`id`, `tenThuocTinh`, `giaTri`) VALUES
(1, 'Màu sắc', 'Đen'),
(2, 'Màu sắc', 'Trắng'),
(3, 'Màu sắc', 'Vàng'),
(4, 'RAM', '8GB'),
(5, 'RAM', '16GB'),
(6, 'Bộ nhớ trong', '128GB'),
(7, 'Bộ nhớ trong', '256GB'),
(8, 'Bộ nhớ trong', '64'),
(9, 'Bộ nhớ trong', '32');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thuonghieu`
--

CREATE TABLE `thuonghieu` (
  `maTH` int(11) NOT NULL,
  `tenTH` varchar(100) NOT NULL,
  `logo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `thuonghieu`
--

INSERT INTO `thuonghieu` (`maTH`, `tenTH`, `logo`) VALUES
(1, 'Apple', NULL),
(2, 'Samsung', NULL),
(3, 'Xiaomi', NULL),
(4, 'OPPO', NULL);

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
-- Chỉ mục cho bảng `chitietthuoctinh`
--
ALTER TABLE `chitietthuoctinh`
  ADD PRIMARY KEY (`maSP`,`thuocTinhID`),
  ADD KEY `thuocTinhID` (`thuocTinhID`);

--
-- Chỉ mục cho bảng `danhmuc`
--
ALTER TABLE `danhmuc`
  ADD PRIMARY KEY (`maDM`);

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
  ADD KEY `maNV` (`maNV`),
  ADD KEY `fk_hd_kh` (`maKH`);

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
  ADD PRIMARY KEY (`maSP`),
  ADD KEY `fk_sp_dm` (`maDM`),
  ADD KEY `fk_sp_th` (`maTH`);

--
-- Chỉ mục cho bảng `thuoctinh`
--
ALTER TABLE `thuoctinh`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `thuonghieu`
--
ALTER TABLE `thuonghieu`
  ADD PRIMARY KEY (`maTH`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT cho bảng `danhmuc`
--
ALTER TABLE `danhmuc`
  MODIFY `maDM` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `maKH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `thuoctinh`
--
ALTER TABLE `thuoctinh`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `thuonghieu`
--
ALTER TABLE `thuonghieu`
  MODIFY `maTH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

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
-- Ràng buộc cho bảng `chitietthuoctinh`
--
ALTER TABLE `chitietthuoctinh`
  ADD CONSTRAINT `chitietthuoctinh_ibfk_1` FOREIGN KEY (`maSP`) REFERENCES `sanpham` (`maSP`) ON DELETE CASCADE,
  ADD CONSTRAINT `chitietthuoctinh_ibfk_2` FOREIGN KEY (`thuocTinhID`) REFERENCES `thuoctinh` (`id`) ON DELETE CASCADE;

--
-- Ràng buộc cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `fk_hd_kh` FOREIGN KEY (`maKH`) REFERENCES `khachhang` (`maKH`),
  ADD CONSTRAINT `hoadon_ibfk_1` FOREIGN KEY (`maNV`) REFERENCES `nhanvien` (`maNV`);

--
-- Ràng buộc cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD CONSTRAINT `fk_sp_dm` FOREIGN KEY (`maDM`) REFERENCES `danhmuc` (`maDM`),
  ADD CONSTRAINT `fk_sp_th` FOREIGN KEY (`maTH`) REFERENCES `thuonghieu` (`maTH`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
