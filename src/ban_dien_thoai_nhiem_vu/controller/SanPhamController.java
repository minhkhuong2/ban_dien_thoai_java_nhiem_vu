package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.SanPham;
import ban_dien_thoai_nhiem_vu.view.QuanLySanPhamFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SanPhamController {
    private QuanLySanPhamFrame view;
    private List<SanPham> listSanPham = new ArrayList<>(); // Cache dữ liệu để lấy cho nhanh

    public SanPhamController(QuanLySanPhamFrame view) {
        this.view = view;
        
        // Load dữ liệu ban đầu
        loadData();

        // 1. Sự kiện Lưu (Thêm mới)
        this.view.addLuuListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kiểm tra xem đang ở chế độ Thêm hay Sửa (dựa vào việc mã SP có bị khóa không)
                if (view.txtMaSP.isEditable()) {
                    xuLyThemSanPham();
                } else {
                    xuLyCapNhat();
                }
            }
        });
        
        // (Nếu bạn giữ nút Cập nhật riêng ở giao diện cũ thì dùng cái này, 
        // còn nếu dùng giao diện mới chỉ có nút Lưu thì logic trên đã bao gồm cả 2)
        // Tuy nhiên, để tương thích, mình vẫn giữ hàm xử lý riêng.

        // 2. Sự kiện Chọn Ảnh
        this.view.addChonAnhListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyChonAnh();
            }
        });

        // 3. Sự kiện Click vào bảng (QUAN TRỌNG: Lấy dữ liệu đầy đủ từ List)
        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0 && row < listSanPham.size()) {
                    // Lấy object từ List (chứa đủ Chip, Ram, Rom...) thay vì lấy từ bảng
                    SanPham sp = listSanPham.get(row);
                    
                    // Đổ dữ liệu vào Form (Bạn cần cập nhật hàm setFormThongTin bên View nữa nhé)
                    fillDataToForm(sp);
                    
                    // Chuyển sang tab nhập liệu để người dùng sửa
                    // (Nếu bạn muốn click cái hiện form sửa luôn)
                    // view.showFormNhap(); 
                }
            }
        });
        
        // Các nút khác (Xóa, Tìm, Làm mới - Nếu giao diện bạn còn giữ)
        // ... (Code giữ nguyên logic cũ)
    }

    // ==========================================================
    // LOGIC TẢI DỮ LIỆU (LOAD DATA)
    // ==========================================================
    private void loadData() {
        try {
            Connection conn = KetNoiCSDL.getConnection();
            Statement st = conn.createStatement();
            // Lấy TẤT CẢ các cột
            ResultSet rs = st.executeQuery("SELECT * FROM SanPham");

            listSanPham.clear();
            view.getTableModel().setRowCount(0);

            while (rs.next()) {
                // 1. Tạo đối tượng và set thông tin cơ bản
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("maSP"));
                sp.setTenSP(rs.getString("tenSP"));
                sp.setHangSanXuat(rs.getString("hangSanXuat"));
                sp.setGiaNhap(rs.getDouble("giaNhap"));
                sp.setGiaBan(rs.getDouble("giaBan"));
                sp.setTonKho(rs.getInt("soLuongTon"));
                sp.setHinhAnh(rs.getString("hinhAnh"));
                
                // 2. Set thông tin chi tiết (Quan trọng)
                sp.setDanhMuc(rs.getString("danhMuc"));
                sp.setMoTa(rs.getString("moTa"));
                sp.setManHinh(rs.getString("manHinh"));
                sp.setHeDieuHanh(rs.getString("heDieuHanh"));
                sp.setCameraSau(rs.getString("cameraSau"));
                sp.setCameraTruoc(rs.getString("cameraTruoc"));
                sp.setChip(rs.getString("chip"));
                sp.setRam(rs.getString("ram"));
                sp.setRom(rs.getString("rom"));
                sp.setPin(rs.getString("pin"));

                // 3. Thêm vào List quản lý
                listSanPham.add(sp);

                // 4. Hiển thị tóm tắt lên bảng
                Vector<Object> row = new Vector<>();
                row.add(sp.getMaSP());
                row.add(sp.getTenSP());
                row.add(sp.getHangSanXuat());
                row.add(sp.getGiaBan());
                row.add(sp.getTonKho());
                row.add(sp.getHinhAnh());
                view.getTableModel().addRow(row);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================================
    // LOGIC THÊM MỚI
    // ==========================================================
    private void xuLyThemSanPham() {
        SanPham sp = view.getSanPhamInput();
        if (sp == null) {
            view.showMessage("Vui lòng kiểm tra lại dữ liệu nhập!");
            return;
        }

        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "INSERT INTO SanPham "
                    + "(maSP, tenSP, hangSanXuat, giaNhap, giaBan, soLuongTon, hinhAnh, "
                    + "moTa, manHinh, heDieuHanh, cameraSau, cameraTruoc, chip, ram, rom, pin) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, sp.getMaSP());
            pst.setString(2, sp.getTenSP());
            pst.setString(3, sp.getHangSanXuat());
            pst.setDouble(4, sp.getGiaNhap());
            pst.setDouble(5, sp.getGiaBan());
            pst.setInt(6, sp.getTonKho());
            pst.setString(7, sp.getHinhAnh());
            pst.setString(8, sp.getMoTa());
            pst.setString(9, sp.getManHinh());
            pst.setString(10, sp.getHeDieuHanh());
            pst.setString(11, sp.getCameraSau());
            pst.setString(12, sp.getCameraTruoc());
            pst.setString(13, sp.getChip());
            pst.setString(14, sp.getRam());
            pst.setString(15, sp.getRom());
            pst.setString(16, sp.getPin());

            pst.executeUpdate();
            view.showMessage("Thêm thành công!");
            view.clearForm();
            loadData();
            view.showDanhSach(); // Quay về danh sách
            
        } catch (Exception ex) {
            view.showMessage("Lỗi thêm: " + ex.getMessage());
        }
    }

    // ==========================================================
    // LOGIC CẬP NHẬT (SỬA) - QUAN TRỌNG
    // ==========================================================
    private void xuLyCapNhat() {
        SanPham sp = view.getSanPhamInput(); // Lấy dữ liệu từ form
        if (sp == null) return;

        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Câu lệnh Update cập nhật TOÀN BỘ trường
            String sql = "UPDATE SanPham SET "
                    + "tenSP=?, hangSanXuat=?, giaNhap=?, giaBan=?, soLuongTon=?, hinhAnh=?, "
                    + "moTa=?, manHinh=?, heDieuHanh=?, cameraSau=?, cameraTruoc=?, chip=?, ram=?, rom=?, pin=? "
                    + "WHERE maSP=?";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, sp.getTenSP());
            pst.setString(2, sp.getHangSanXuat());
            pst.setDouble(3, sp.getGiaNhap());
            pst.setDouble(4, sp.getGiaBan());
            pst.setInt(5, sp.getTonKho());
            pst.setString(6, sp.getHinhAnh() == null ? "" : sp.getHinhAnh());
            
            // Các thông số kỹ thuật
            pst.setString(7, sp.getMoTa());
            pst.setString(8, sp.getManHinh());
            pst.setString(9, sp.getHeDieuHanh());
            pst.setString(10, sp.getCameraSau());
            pst.setString(11, sp.getCameraTruoc());
            pst.setString(12, sp.getChip());
            pst.setString(13, sp.getRam());
            pst.setString(14, sp.getRom());
            pst.setString(15, sp.getPin());
            
            // Điều kiện WHERE (Mã sản phẩm) - Tham số số 16
            pst.setString(16, sp.getMaSP());

            int kq = pst.executeUpdate();
            if (kq > 0) {
                view.showMessage("Cập nhật thành công!");
                view.clearForm();
                loadData();
                view.showDanhSach();
            } else {
                view.showMessage("Không tìm thấy mã SP để sửa!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showMessage("Lỗi cập nhật: " + ex.getMessage());
        }
    }
    
    // ==========================================================
    // CÁC HÀM PHỤ TRỢ
    // ==========================================================
    
    private void xuLyChonAnh() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Ảnh (JPG, PNG)", "jpg", "png");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            view.hienThiAnh(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    // Hàm đổ dữ liệu vào Form (Dùng để hiển thị khi click chuột hoặc muốn sửa)
    private void fillDataToForm(SanPham sp) {
        // Tab 1: Thông tin chung
        view.txtMaSP.setText(sp.getMaSP());
        view.txtMaSP.setEditable(false); // Khóa mã lại không cho sửa
        view.txtTenSP.setText(sp.getTenSP());
        view.txtHangSX.setText(sp.getHangSanXuat());
        view.txtGiaNhap.setText(String.valueOf(sp.getGiaNhap()));
        view.txtGiaBan.setText(String.valueOf(sp.getGiaBan()));
        view.txtTonKho.setText(String.valueOf(sp.getTonKho()));
        view.txtMoTa.setText(sp.getMoTa());
        view.hienThiAnh(sp.getHinhAnh());

        // Tab 2: Thông số kỹ thuật
        view.txtManHinh.setText(sp.getManHinh());
        view.txtHDH.setText(sp.getHeDieuHanh());
        view.txtCamSau.setText(sp.getCameraSau());
        view.txtCamTruoc.setText(sp.getCameraTruoc());
        view.txtChip.setText(sp.getChip());
        view.txtRam.setText(sp.getRam());
        view.txtRom.setText(sp.getRom());
        view.txtPin.setText(sp.getPin());
    }
}