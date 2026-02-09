package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.GiamGia;
import ban_dien_thoai_nhiem_vu.view.QuanLyGiamGiaPanel;
import ban_dien_thoai_nhiem_vu.view.QuanLyGiamGiaPanel.TableActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class GiamGiaController {
    private QuanLyGiamGiaPanel view;
    private List<GiamGia> listGG = new ArrayList<>();
    private boolean isEdit = false;

    public GiamGiaController(QuanLyGiamGiaPanel view) {
        this.view = view;
        loadData();

        view.addLuuListener(e -> xuLyLuu());
        view.addLamMoiListener(e -> { view.clearForm(); isEdit = false; });
        
        view.setTableActionEvent(new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                if (row >= 0) {
                    GiamGia gg = listGG.get(row);
                    view.txtCode.setText(gg.getCode()); view.txtCode.setEditable(false); // Không sửa mã
                    view.txtTenCT.setText(gg.getTenChuongTrinh());
                    view.txtPhanTram.setText(gg.getPhanTramGiam()+"");
                    view.txtSoLuong.setText(gg.getSoLuong()+"");
                    view.txtNgayKetThuc.setText(gg.getNgayKetThuc().toString());
                    isEdit = true;
                }
            }
            @Override
            public void onDelete(int row) {
                if (row >= 0 && JOptionPane.showConfirmDialog(view, "Xóa mã này?", "Xác nhận", JOptionPane.YES_NO_OPTION)==0) 
                    xuLyXoa(listGG.get(row).getCode());
            }
        });
    }

    private void loadData() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM GiamGia");
            listGG.clear();
            view.getModel().setRowCount(0);
            while (rs.next()) {
                GiamGia gg = new GiamGia(rs.getString("code"), rs.getString("tenChuongTrinh"), 
                                         rs.getInt("phanTramGiam"), rs.getInt("soLuong"), rs.getDate("ngayKetThuc"));
                listGG.add(gg);
                view.getModel().addRow(new Object[]{gg.getCode(), gg.getTenChuongTrinh(), gg.getPhanTramGiam()+"%", gg.getSoLuong(), gg.getNgayKetThuc(), ""});
            }
        } catch (Exception e) {}
    }

    private void xuLyLuu() {
        GiamGia gg = view.getGiamGiaInput();
        if (gg == null) { JOptionPane.showMessageDialog(view, "Dữ liệu không hợp lệ!"); return; }
        try (Connection conn = KetNoiCSDL.getConnection()) {
            if (!isEdit) { // Thêm mới
                String sql = "INSERT INTO GiamGia (code, tenChuongTrinh, phanTramGiam, soLuong, ngayKetThuc) VALUES (?,?,?,?,?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, gg.getCode()); pst.setString(2, gg.getTenChuongTrinh());
                pst.setInt(3, gg.getPhanTramGiam()); pst.setInt(4, gg.getSoLuong());
                pst.setDate(5, gg.getNgayKetThuc());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(view, "Thêm mã thành công!");
            } else { // Cập nhật
                String sql = "UPDATE GiamGia SET tenChuongTrinh=?, phanTramGiam=?, soLuong=?, ngayKetThuc=? WHERE code=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, gg.getTenChuongTrinh()); pst.setInt(2, gg.getPhanTramGiam());
                pst.setInt(3, gg.getSoLuong()); pst.setDate(4, gg.getNgayKetThuc());
                pst.setString(5, gg.getCode());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            }
            view.clearForm(); loadData(); isEdit = false;
        } catch (Exception e) { JOptionPane.showMessageDialog(view, "Lỗi (Có thể trùng mã): " + e.getMessage()); }
    }

    private void xuLyXoa(String code) {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            conn.prepareStatement("DELETE FROM GiamGia WHERE code='"+code+"'").executeUpdate();
            loadData();
        } catch (Exception e) {}
    }
}
