package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.view.QuanLyThuongHieuPanel;
import ban_dien_thoai_nhiem_vu.view.ThuongHieuDialog;
import ban_dien_thoai_nhiem_vu.model.ThuongHieu;
import javax.swing.*;
import java.awt.Image;
import java.io.File;
import java.sql.*;

public class ThuongHieuController {
    
    private QuanLyThuongHieuPanel view;

    public ThuongHieuController(QuanLyThuongHieuPanel view) {
        this.view = view;
        loadData();
        
        view.addThemListener(e -> themThuongHieu());
        view.addSuaListener(e -> suaThuongHieu());
        view.addXoaListener(e -> xoaThuongHieu());
    }

    private void loadData() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM ThuongHieu");
            view.getModel().setRowCount(0);
            while(rs.next()) {
                String path = rs.getString("logo");
                ImageIcon icon = null;
                if (path != null && !path.trim().isEmpty()) {
                    try {
                        File f = new File(path);
                        if (f.exists()) {
                            Image img = new ImageIcon(path).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(img);
                        } else {
                            java.net.URL url = getClass().getResource("/ban_dien_thoai_nhiem_vu/icons/" + path);
                            if (url != null) {
                                Image img = new ImageIcon(url).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                                icon = new ImageIcon(img);
                            }
                        }
                    } catch (Exception e){}
                }
                if(icon == null) {
                    // Blank placeholder
                    icon = new ImageIcon(new java.awt.image.BufferedImage(60, 60, java.awt.image.BufferedImage.TYPE_INT_ARGB));
                }
                
                view.getModel().addRow(new Object[]{rs.getInt("maTH"), icon, rs.getString("tenTH")});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void themThuongHieu() {
        ThuongHieuDialog dialog = new ThuongHieuDialog(SwingUtilities.getWindowAncestor(view), "Thêm Thương Hiệu", null);
        dialog.setVisible(true);
        ThuongHieu result = dialog.getResult();
        
        if (result != null) {
            try (Connection conn = KetNoiCSDL.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("INSERT INTO ThuongHieu(tenTH, logo) VALUES(?, ?)");
                pst.setString(1, result.getTenTH());
                pst.setString(2, result.getLogo());
                pst.executeUpdate();
                loadData();
                JOptionPane.showMessageDialog(view, "Thêm thương hiệu thành công!");
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    
    private void suaThuongHieu() {
        int row = view.getTable().getSelectedRow();
        if(row < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một thương hiệu để sửa!");
            return;
        }
        
        int maTH = Integer.parseInt(view.getModel().getValueAt(row, 0).toString());
        String tenTH = view.getModel().getValueAt(row, 2).toString();
        // Since we don't store raw path in Table, let's query the DB for the raw logo path it has.
        String logoPath = "";
        try (Connection conn = KetNoiCSDL.getConnection()) {
             PreparedStatement p = conn.prepareStatement("SELECT logo FROM ThuongHieu WHERE maTH = ?");
             p.setInt(1, maTH);
             ResultSet rs = p.executeQuery();
             if(rs.next()) logoPath = rs.getString("logo");
        } catch(Exception e){}
        
        ThuongHieu th = new ThuongHieu(maTH, tenTH, logoPath);
        
        ThuongHieuDialog dialog = new ThuongHieuDialog(SwingUtilities.getWindowAncestor(view), "Sửa Thương Hiệu", th);
        dialog.setVisible(true);
        ThuongHieu result = dialog.getResult();
        if (result != null) {
            try (Connection conn = KetNoiCSDL.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("UPDATE ThuongHieu SET tenTH = ?, logo = ? WHERE maTH = ?");
                pst.setString(1, result.getTenTH());
                pst.setString(2, result.getLogo());
                pst.setInt(3, maTH);
                pst.executeUpdate();
                loadData();
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    
    private void xoaThuongHieu() {
        int row = view.getTable().getSelectedRow();
        if(row < 0) {
            JOptionPane.showMessageDialog(view, "Chọn thương hiệu cần xóa!");
            return;
        }
        
        String id = view.getModel().getValueAt(row, 0).toString();
        if(JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = KetNoiCSDL.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("DELETE FROM ThuongHieu WHERE maTH = ?");
                pst.setString(1, id);
                pst.executeUpdate();
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Không thể xóa (Thương hiệu đang được sử dụng trong SP).");
            }
        }
    }
}
