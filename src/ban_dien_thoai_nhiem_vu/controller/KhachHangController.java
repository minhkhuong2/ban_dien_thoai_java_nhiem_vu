package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.KhachHang;
import ban_dien_thoai_nhiem_vu.view.QuanLyKhachHangFrame;
import ban_dien_thoai_nhiem_vu.view.QuanLyKhachHangFrame.TableActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;

public class KhachHangController {
    private QuanLyKhachHangFrame view;
    private List<KhachHang> listKH = new ArrayList<>();
    private int idDangChon = -1; // -1 là thêm mới, >0 là đang sửa

    public KhachHangController(QuanLyKhachHangFrame view) {
        this.view = view;
        loadData();

        // Sự kiện Lưu (Thêm/Sửa)
        view.addLuuListener(e -> xuLyLuu());
        
        // Sự kiện Làm mới
        view.addLamMoiListener(e -> {
            view.clearForm();
            idDangChon = -1;
        });

        // Sự kiện Bảng (Sửa/Xóa)
        view.setTableActionEvent(new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                if (row >= 0 && row < listKH.size()) {
                    KhachHang kh = listKH.get(row);
                    idDangChon = kh.getMaKH(); // Đánh dấu là đang sửa ID này
                    view.txtTenKH.setText(kh.getTenKH());
                    view.txtSDT.setText(kh.getSdt());
                    view.txtEmail.setText(kh.getEmail());
                    view.txtDiaChi.setText(kh.getDiaChi());
                }
            }

            @Override
            public void onDelete(int row) {
                if (row >= 0 && row < listKH.size()) {
                    int confirm = JOptionPane.showConfirmDialog(view, "Xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        xuLyXoa(listKH.get(row).getMaKH());
                    }
                }
            }
        });
    }

    private void loadData() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM KhachHang");
            listKH.clear();
            view.getModel().setRowCount(0);
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getInt("maKH"), rs.getString("tenKH"), rs.getString("sdt"), 
                    rs.getString("diaChi"), rs.getString("email"), rs.getDate("ngayThamGia")
                );
                listKH.add(kh);
                
                // --- SỬA LỖI TẠI DÒNG NÀY (Dùng Object[] thay vì Vector[]) ---
                view.getModel().addRow(new Object[]{
                    kh.getMaKH(), kh.getTenKH(), kh.getSdt(), kh.getEmail(), kh.getDiaChi(), kh.getNgayThamGia(), ""
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void xuLyLuu() {
        KhachHang kh = view.getKhachHangInput();
        if (kh == null) { JOptionPane.showMessageDialog(view, "Thiếu tên hoặc SĐT!"); return; }

        try (Connection conn = KetNoiCSDL.getConnection()) {
            if (idDangChon == -1) {
                // INSERT
                String sql = "INSERT INTO KhachHang (tenKH, sdt, email, diaChi) VALUES (?,?,?,?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, kh.getTenKH()); pst.setString(2, kh.getSdt());
                pst.setString(3, kh.getEmail()); pst.setString(4, kh.getDiaChi());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(view, "Thêm thành công!");
            } else {
                // UPDATE
                String sql = "UPDATE KhachHang SET tenKH=?, sdt=?, email=?, diaChi=? WHERE maKH=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, kh.getTenKH()); pst.setString(2, kh.getSdt());
                pst.setString(3, kh.getEmail()); pst.setString(4, kh.getDiaChi());
                pst.setInt(5, idDangChon);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                idDangChon = -1; // Reset về thêm mới
            }
            view.clearForm(); loadData();
        } catch (Exception e) { JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage()); }
    }

    private void xuLyXoa(int id) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            conn.prepareStatement("DELETE FROM KhachHang WHERE maKH=" + id).executeUpdate();
            loadData();
        } catch (Exception e) { JOptionPane.showMessageDialog(view, "Không thể xóa (KH đã mua hàng)!"); }
    }
}