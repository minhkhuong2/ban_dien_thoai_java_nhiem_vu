package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.*; 
import ban_dien_thoai_nhiem_vu.view.QuanLySanPhamFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class QuanLySanPhamController {

    private QuanLySanPhamFrame view;
    private DecimalFormat df = new DecimalFormat("#,###");

    public QuanLySanPhamController(QuanLySanPhamFrame view) {
        this.view = view;
        loadComboBoxData(); 
        loadDynamicAttributes(); 
        loadDanhSach();

        view.setTableActionEvent(new QuanLySanPhamFrame.TableActionEvent() {
            @Override public void onEdit(int row) { hienThiLenForm(view.getTableModel().getValueAt(row, 0).toString()); }
            @Override public void onDelete(int row) { if(view.showConfirm("Xóa SP này?")==0) xoaSanPham(view.getTableModel().getValueAt(row, 0).toString()); }
        });
        view.addLuuListener(e -> xuLyLuuSanPham());
        view.addChonAnhListener(e -> xuLyChonAnh());
        view.addNhapHangListener(e -> xuLyNhapHang());
        view.getBtnMoFormThem().addActionListener(e -> { view.clearForm(); loadDynamicAttributes(); view.showFormNhap(); });
    }
    
    // --- LOAD THUỘC TÍNH ĐỘNG (RAM, MÀU...) ---
    private void loadDynamicAttributes() {
        view.clearDynamicAttributes(); 
        Map<String, List<ThuocTinh>> dataMap = new HashMap<>();
        try (Connection conn = KetNoiCSDL.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM ThuocTinh ORDER BY tenThuocTinh, giaTri");
            while (rs.next()) {
                String group = rs.getString("tenThuocTinh");
                dataMap.putIfAbsent(group, new ArrayList<>());
                dataMap.get(group).add(new ThuocTinh(rs.getInt("id"), group, rs.getString("giaTri")));
            }
            for (String group : dataMap.keySet()) {
                DefaultComboBoxModel<ThuocTinh> model = new DefaultComboBoxModel<>();
                for (ThuocTinh tt : dataMap.get(group)) model.addElement(tt);
                view.addDynamicAttribute(group, model);
            }
        } catch (Exception e) {}
    }

    // --- LƯU (INSERT/UPDATE) ---
    private void xuLyLuuSanPham() {
        SanPham sp = getSanPhamFromView();
        if (sp == null) { view.showMessage("Vui lòng nhập Mã và Tên SP!"); return; }
        
        Connection conn = null;
        try {
            conn = KetNoiCSDL.getConnection(); conn.setAutoCommit(false);
            
            // 1. Lưu Bảng SanPham (Gồm cả thông số CỨNG)
            if (view.txtMaSP.isEditable()) { // Insert
                String sql = "INSERT INTO SanPham (maSP, tenSP, maDM, maTH, giaNhap, giaBan, soLuongTon, moTa, hinhAnh, hangSanXuat, manHinh, heDieuHanh, cameraSau, cameraTruoc, chip, pin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                setParams(pst, sp, true);
                pst.executeUpdate();
            } else { // Update
                String sql = "UPDATE SanPham SET tenSP=?, maDM=?, maTH=?, giaNhap=?, giaBan=?, soLuongTon=?, moTa=?, hinhAnh=?, hangSanXuat=?, manHinh=?, heDieuHanh=?, cameraSau=?, cameraTruoc=?, chip=?, pin=? WHERE maSP=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                setParams(pst, sp, false);
                pst.executeUpdate();
            }

            // 2. Lưu Bảng ChiTietThuocTinh (Thông số ĐỘNG)
            PreparedStatement pstDel = conn.prepareStatement("DELETE FROM ChiTietThuocTinh WHERE maSP=?");
            pstDel.setString(1, sp.getMaSP()); pstDel.executeUpdate();
            
            String sqlAttr = "INSERT INTO ChiTietThuocTinh (maSP, thuocTinhID) VALUES (?, ?)";
            PreparedStatement pstAttr = conn.prepareStatement(sqlAttr);
            for (JComboBox<ThuocTinh> combo : view.dynamicCombos.values()) {
                ThuocTinh selected = (ThuocTinh) combo.getSelectedItem();
                if (selected != null) {
                    pstAttr.setString(1, sp.getMaSP()); pstAttr.setInt(2, selected.getId()); pstAttr.addBatch();
                }
            }
            pstAttr.executeBatch();
            
            conn.commit(); view.showMessage("Lưu thành công!"); view.clearForm(); loadDanhSach(); view.showDanhSach();
        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(Exception ex){}
            e.printStackTrace(); view.showMessage("Lỗi: " + e.getMessage());
        } finally { try { if(conn!=null) conn.close(); } catch(Exception ex){} }
    }
    
    // --- HIỂN THỊ SP LÊN FORM ---
    private void hienThiLenForm(String maSP) {
        loadDynamicAttributes();
        try (Connection conn = KetNoiCSDL.getConnection()) {
            // Lấy thông tin cơ bản + Cứng
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM SanPham WHERE maSP=?");
            pst.setString(1, maSP); ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                view.txtMaSP.setText(rs.getString("maSP"));
                view.txtTenSP.setText(rs.getString("tenSP"));
                setSelectedCombo(view.cboDanhMuc, rs.getInt("maDM"));
                setSelectedCombo(view.cboThuongHieu, rs.getInt("maTH"));
                view.txtGiaNhap.setText(String.format("%.0f", rs.getDouble("giaNhap")));
                view.txtGiaBan.setText(String.format("%.0f", rs.getDouble("giaBan")));
                view.txtTonKho.setText(rs.getString("soLuongTon"));
                view.txtMoTa.setText(rs.getString("moTa"));
                view.hienThiAnh(rs.getString("hinhAnh"));
                
                // Set text thông số CỨNG
                view.txtManHinh.setText(rs.getString("manHinh"));
                view.txtHDH.setText(rs.getString("heDieuHanh"));
                view.txtCamSau.setText(rs.getString("cameraSau"));
                view.txtCamTruoc.setText(rs.getString("cameraTruoc"));
                view.txtChip.setText(rs.getString("chip"));
                view.txtPin.setText(rs.getString("pin"));

                // Set thông số ĐỘNG
                PreparedStatement pstAttr = conn.prepareStatement("SELECT thuocTinhID FROM ChiTietThuocTinh WHERE maSP=?");
                pstAttr.setString(1, maSP); ResultSet rsAttr = pstAttr.executeQuery();
                List<Integer> ids = new ArrayList<>(); while(rsAttr.next()) ids.add(rsAttr.getInt("thuocTinhID"));
                
                for (JComboBox<ThuocTinh> combo : view.dynamicCombos.values()) {
                    for (int i=0; i<combo.getItemCount(); i++) {
                        if (ids.contains(combo.getItemAt(i).getId())) { combo.setSelectedIndex(i); break; }
                    }
                }
                view.txtMaSP.setEditable(false); view.showFormNhap();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- HELPER: LẤY DATA TỪ VIEW ---
    private SanPham getSanPhamFromView() {
        try {
            SanPham sp = new SanPham();
            sp.setMaSP(view.txtMaSP.getText()); sp.setTenSP(view.txtTenSP.getText());
            DanhMuc dm = (DanhMuc) view.cboDanhMuc.getSelectedItem(); if(dm!=null) sp.setMaDM(dm.getMaDM());
            ThuongHieu th = (ThuongHieu) view.cboThuongHieu.getSelectedItem(); if(th!=null) {sp.setMaTH(th.getMaTH()); sp.setHangSanXuat(th.getTenTH());}
            sp.setGiaNhap(Double.parseDouble(view.txtGiaNhap.getText()));
            sp.setGiaBan(Double.parseDouble(view.txtGiaBan.getText()));
            sp.setTonKho(Integer.parseInt(view.txtTonKho.getText()));
            sp.setMoTa(view.txtMoTa.getText());
            
            // Lấy thông số CỨNG từ TextFields
            sp.setManHinh(view.txtManHinh.getText());
            sp.setHeDieuHanh(view.txtHDH.getText());
            sp.setCameraSau(view.txtCamSau.getText());
            sp.setCameraTruoc(view.txtCamTruoc.getText());
            sp.setChip(view.txtChip.getText());
            sp.setPin(view.txtPin.getText());
            
            // Set ảnh (dùng ảnh cũ nếu không chọn mới, xử lý đơn giản ở đây)
            sp.setHinhAnh(""); 
            return sp;
        } catch (Exception e) { return null; }
    }
    
    private void setParams(PreparedStatement pst, SanPham sp, boolean isInsert) throws SQLException {
        if(isInsert) {
            pst.setString(1, sp.getMaSP()); pst.setString(2, sp.getTenSP()); pst.setInt(3, sp.getMaDM()); pst.setInt(4, sp.getMaTH());
            pst.setDouble(5, sp.getGiaNhap()); pst.setDouble(6, sp.getGiaBan()); pst.setInt(7, sp.getTonKho()); pst.setString(8, sp.getMoTa());
            pst.setString(9, sp.getHinhAnh()); pst.setString(10, sp.getHangSanXuat());
            // Params cứng
            pst.setString(11, sp.getManHinh()); pst.setString(12, sp.getHeDieuHanh()); pst.setString(13, sp.getCameraSau());
            pst.setString(14, sp.getCameraTruoc()); pst.setString(15, sp.getChip()); pst.setString(16, sp.getPin());
        } else {
            pst.setString(1, sp.getTenSP()); pst.setInt(2, sp.getMaDM()); pst.setInt(3, sp.getMaTH());
            pst.setDouble(4, sp.getGiaNhap()); pst.setDouble(5, sp.getGiaBan()); pst.setInt(6, sp.getTonKho()); pst.setString(7, sp.getMoTa());
            pst.setString(8, sp.getHinhAnh()); pst.setString(9, sp.getHangSanXuat());
            // Params cứng
            pst.setString(10, sp.getManHinh()); pst.setString(11, sp.getHeDieuHanh()); pst.setString(12, sp.getCameraSau());
            pst.setString(13, sp.getCameraTruoc()); pst.setString(14, sp.getChip()); pst.setString(15, sp.getPin());
            // Where ID
            pst.setString(16, sp.getMaSP());
        }
    }
    
    // --- CÁC HÀM KHÁC GIỮ NGUYÊN (LoadCombo, LoadList, Delete...) ---
    private void loadComboBoxData() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            view.cboDanhMuc.removeAllItems(); ResultSet rsDM = conn.createStatement().executeQuery("SELECT * FROM DanhMuc");
            while (rsDM.next()) view.cboDanhMuc.addItem(new DanhMuc(rsDM.getInt("maDM"), rsDM.getString("tenDM")));
            view.cboThuongHieu.removeAllItems(); ResultSet rsTH = conn.createStatement().executeQuery("SELECT * FROM ThuongHieu");
            while (rsTH.next()) view.cboThuongHieu.addItem(new ThuongHieu(rsTH.getInt("maTH"), rsTH.getString("tenTH"), rsTH.getString("logo")));
        } catch (Exception e) {}
    }
    private void loadDanhSach() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT maSP, tenSP, hangSanXuat, giaBan, soLuongTon, hinhAnh FROM SanPham");
            view.getTableModel().setRowCount(0);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("maSP")); row.add(rs.getString("tenSP")); row.add(rs.getString("hangSanXuat"));
                row.add(df.format(rs.getDouble("giaBan"))); row.add(rs.getInt("soLuongTon")); row.add(rs.getString("hinhAnh")); row.add(null);
                view.getTableModel().addRow(row);
            }
        } catch (Exception e) {}
    }
    private void setSelectedCombo(JComboBox<?> combo, int id) { /* Logic chọn combo */ }
    private void xoaSanPham(String maSP) { try{ KetNoiCSDL.getConnection().createStatement().executeUpdate("DELETE FROM SanPham WHERE maSP='"+maSP+"'"); loadDanhSach(); }catch(Exception e){} }
    private void xuLyNhapHang() { /* Logic nhập hàng */ }
    private void xuLyChonAnh() { JFileChooser fc = new JFileChooser(); if(fc.showOpenDialog(view)==JFileChooser.APPROVE_OPTION) view.hienThiAnh(fc.getSelectedFile().getAbsolutePath()); }
}