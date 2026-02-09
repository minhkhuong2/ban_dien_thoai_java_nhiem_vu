package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.SanPham;
import ban_dien_thoai_nhiem_vu.view.QuanLySanPhamPanel;
import ban_dien_thoai_nhiem_vu.view.QuanLySanPhamPanel.TableActionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SanPhamController {
    private QuanLySanPhamPanel view;
    private List<SanPham> listSanPham = new ArrayList<>();

    public SanPhamController(QuanLySanPhamPanel view) {
        this.view = view;
        loadData();

        // 1. SỰ KIỆN QUAN TRỌNG NHẤT: BẮT SỰ KIỆN NÚT SỬA/XÓA TRONG BẢNG
        view.setTableActionEvent(new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                // Khi bấm nút Bút chì
                if (row >= 0 && row < listSanPham.size()) {
                    SanPham sp = listSanPham.get(row);
                    fillDataToForm(sp); // Đổ dữ liệu vào form
                    view.showFormNhap(); // Chuyển sang màn hình nhập
                }
            }

            @Override
            public void onDelete(int row) {
                // Khi bấm nút Thùng rác
                if (row >= 0 && row < listSanPham.size()) {
                    SanPham sp = listSanPham.get(row);
                    int confirm = view.showConfirm("Bạn có chắc muốn xóa sản phẩm: " + sp.getTenSP() + "?");
                    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        xuLyXoa(sp.getMaSP());
                    }
                }
            }
        });

        // 2. Sự kiện nút Lưu (Chung cho Thêm và Sửa)
        this.view.addLuuListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.txtMaSP.isEditable()) {
                    xuLyThemSanPham();
                } else {
                    xuLyCapNhat();
                }
            }
        });
        
        // 3. Sự kiện Chọn Ảnh
        this.view.addChonAnhListener(e -> xuLyChonAnh());
    }

    // --- CÁC HÀM LOGIC (GIỮ NGUYÊN NHƯ CŨ) ---

    private void loadData() {
        try {
            Connection conn = KetNoiCSDL.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM SanPham");
            listSanPham.clear();
            view.getTableModel().setRowCount(0);

            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("maSP"));
                sp.setTenSP(rs.getString("tenSP"));
                sp.setHangSanXuat(rs.getString("hangSanXuat"));
                sp.setGiaNhap(rs.getDouble("giaNhap"));
                sp.setGiaBan(rs.getDouble("giaBan"));
                sp.setTonKho(rs.getInt("soLuongTon"));
                sp.setHinhAnh(rs.getString("hinhAnh"));
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

                listSanPham.add(sp);

                Vector<Object> row = new Vector<>();
                row.add(sp.getMaSP());
                row.add(sp.getTenSP());
                row.add(sp.getHangSanXuat());
                row.add(sp.getGiaBan());
                row.add(sp.getTonKho());
                row.add(sp.getHinhAnh());
                row.add(""); // Cột hành động (để trống, renderer sẽ tự vẽ nút)
                view.getTableModel().addRow(row);
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void xuLyThemSanPham() {
        SanPham sp = view.getSanPhamInput();
        if (sp == null) { view.showMessage("Vui lòng kiểm tra dữ liệu!"); return; }
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "INSERT INTO SanPham VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            // ... (Set params giống hệt code trước, tôi viết tắt để bạn dễ nhìn) ...
            pst.setString(1, sp.getMaSP()); pst.setString(2, sp.getTenSP()); pst.setString(3, sp.getHangSanXuat());
            pst.setDouble(4, sp.getGiaNhap()); pst.setDouble(5, sp.getGiaBan()); pst.setInt(6, sp.getTonKho());
            pst.setString(7, sp.getHinhAnh()); pst.setString(8, sp.getMoTa()); pst.setString(9, sp.getDanhMuc());
            pst.setString(10, sp.getManHinh()); pst.setString(11, sp.getHeDieuHanh()); pst.setString(12, sp.getCameraSau());
            pst.setString(13, sp.getCameraTruoc()); pst.setString(14, sp.getChip()); pst.setString(15, sp.getRam());
            pst.setString(16, sp.getRom()); pst.setString(17, sp.getPin());

            pst.executeUpdate();
            view.showMessage("Thêm thành công!");
            view.clearForm(); loadData(); view.showDanhSach();
        } catch (Exception ex) { view.showMessage("Lỗi: " + ex.getMessage()); }
    }

    private void xuLyCapNhat() {
        SanPham sp = view.getSanPhamInput();
        if (sp == null) return;
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "UPDATE SanPham SET tenSP=?, hangSanXuat=?, giaNhap=?, giaBan=?, soLuongTon=?, hinhAnh=?, moTa=?, danhMuc=?, manHinh=?, heDieuHanh=?, cameraSau=?, cameraTruoc=?, chip=?, ram=?, rom=?, pin=? WHERE maSP=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, sp.getTenSP()); pst.setString(2, sp.getHangSanXuat());
            pst.setDouble(3, sp.getGiaNhap()); pst.setDouble(4, sp.getGiaBan()); pst.setInt(5, sp.getTonKho());
            pst.setString(6, sp.getHinhAnh() == null ? "" : sp.getHinhAnh());
            pst.setString(7, sp.getMoTa()); pst.setString(8, sp.getDanhMuc());
            pst.setString(9, sp.getManHinh()); pst.setString(10, sp.getHeDieuHanh()); pst.setString(11, sp.getCameraSau());
            pst.setString(12, sp.getCameraTruoc()); pst.setString(13, sp.getChip()); pst.setString(14, sp.getRam());
            pst.setString(15, sp.getRom()); pst.setString(16, sp.getPin());
            pst.setString(17, sp.getMaSP()); // WHERE

            pst.executeUpdate();
            view.showMessage("Cập nhật thành công!");
            view.clearForm(); loadData(); view.showDanhSach();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void xuLyXoa(String maSP) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM SanPham WHERE maSP=?");
            pst.setString(1, maSP);
            pst.executeUpdate();
            view.showMessage("Đã xóa!");
            loadData();
        } catch (Exception ex) { view.showMessage("Lỗi xóa: " + ex.getMessage()); }
    }
    
    private void xuLyChonAnh() {
        JFileChooser f = new JFileChooser();
        f.setFileFilter(new FileNameExtensionFilter("Ảnh", "jpg", "png"));
        if (f.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            view.hienThiAnh(f.getSelectedFile().getAbsolutePath());
        }
    }

    private void fillDataToForm(SanPham sp) {
        view.txtMaSP.setText(sp.getMaSP()); view.txtMaSP.setEditable(false);
        view.txtTenSP.setText(sp.getTenSP()); view.txtHangSX.setText(sp.getHangSanXuat());
        view.txtGiaNhap.setText(String.valueOf(sp.getGiaNhap()));
        view.txtGiaBan.setText(String.valueOf(sp.getGiaBan()));
        view.txtTonKho.setText(String.valueOf(sp.getTonKho()));
        view.txtMoTa.setText(sp.getMoTa());
        view.hienThiAnh(sp.getHinhAnh());
        view.txtManHinh.setText(sp.getManHinh()); view.txtHDH.setText(sp.getHeDieuHanh());
        view.txtCamSau.setText(sp.getCameraSau()); view.txtCamTruoc.setText(sp.getCameraTruoc());
        view.txtChip.setText(sp.getChip()); view.txtRam.setText(sp.getRam());
        view.txtRom.setText(sp.getRom()); view.txtPin.setText(sp.getPin());
    }
}
