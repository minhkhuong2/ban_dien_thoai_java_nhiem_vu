package ban_dien_thoai_nhiem_vu.controller;

import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import ban_dien_thoai_nhiem_vu.model.*; 
import ban_dien_thoai_nhiem_vu.view.QuanLySanPhamFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    
    // [QUAN TRỌNG] Biến lưu đường dẫn ảnh hiện tại (Tuyệt đối hoặc Tương đối)
    private String duongDanAnh = null; 

    public QuanLySanPhamController(QuanLySanPhamFrame view) {
        this.view = view;
        
        // 1. Load dữ liệu ban đầu
        loadComboBoxData();         
        loadDynamicAttributes();    
        loadDanhSach();             

        // 2. Sự kiện Bảng (Sửa/Xóa)
        view.setTableActionEvent(new QuanLySanPhamFrame.TableActionEvent() {
            @Override 
            public void onEdit(int row) { 
                hienThiLenForm(view.getTableModel().getValueAt(row, 0).toString()); 
            }
            @Override 
            public void onDelete(int row) { 
                if(view.showConfirm("Bạn có chắc chắn muốn xóa sản phẩm này?") == 0) 
                    xoaSanPham(view.getTableModel().getValueAt(row, 0).toString()); 
            }
        });

        // 3. Sự kiện các nút chức năng
        view.addLuuListener(e -> xuLyLuuSanPham());
        view.addChonAnhListener(e -> xuLyChonAnh());
        view.addNhapHangListener(e -> xuLyNhapHang());
        
        // Khi bấm nút "+ Thêm Mới":
        view.getBtnMoFormThem().addActionListener(e -> { 
            view.clearForm(); 
            duongDanAnh = null; // Reset đường dẫn ảnh
            view.hienThiAnh(""); // Xóa ảnh cũ trên form
            loadDynamicAttributes(); 
            view.showFormNhap(); 
        });
    }
    
    // ==========================================================
    // PHẦN 1: XỬ LÝ THUỘC TÍNH ĐỘNG
    // ==========================================================
    private void loadDynamicAttributes() {
        view.clearDynamicAttributes(); 
        Map<String, List<ThuocTinh>> dataMap = new HashMap<>();
        
        try (Connection conn = KetNoiCSDL.getConnection()) {
            String sql = "SELECT * FROM ThuocTinh ORDER BY tenThuocTinh, giaTri";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            
            while (rs.next()) {
                String group = rs.getString("tenThuocTinh"); 
                dataMap.putIfAbsent(group, new ArrayList<>());
                dataMap.get(group).add(new ThuocTinh(rs.getInt("id"), group, rs.getString("giaTri")));
            }
            
            for (String group : dataMap.keySet()) {
                DefaultComboBoxModel<ThuocTinh> model = new DefaultComboBoxModel<>();
                for (ThuocTinh tt : dataMap.get(group)) {
                    model.addElement(tt);
                }
                view.addDynamicAttribute(group, model);
            }
            
            view.pnlThuocTinhDong.revalidate();
            view.pnlThuocTinhDong.repaint();
            
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ==========================================================
    // PHẦN 2: LƯU SẢN PHẨM (INSERT / UPDATE)
    // ==========================================================
    private void xuLyLuuSanPham() {
        SanPham sp = getSanPhamFromView();
        if (sp == null) { 
            view.showMessage("Vui lòng nhập Mã và Tên sản phẩm!"); 
            return; 
        }
        
        Connection conn = null;
        try {
            conn = KetNoiCSDL.getConnection(); 
            conn.setAutoCommit(false); 
            
            if (view.txtMaSP.isEditable()) { // THÊM MỚI
                PreparedStatement pstCheck = conn.prepareStatement("SELECT maSP FROM SanPham WHERE maSP=?");
                pstCheck.setString(1, sp.getMaSP());
                if(pstCheck.executeQuery().next()) {
                    view.showMessage("Mã sản phẩm đã tồn tại!"); return;
                }

                String sql = "INSERT INTO SanPham (maSP, tenSP, maDM, maTH, giaNhap, giaBan, soLuongTon, moTa, hinhAnh, hangSanXuat, manHinh, heDieuHanh, cameraSau, cameraTruoc, chip, pin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                setParams(pst, sp, true);
                pst.executeUpdate();
            } else { // CẬP NHẬT
                String sql = "UPDATE SanPham SET tenSP=?, maDM=?, maTH=?, giaNhap=?, giaBan=?, soLuongTon=?, moTa=?, hinhAnh=?, hangSanXuat=?, manHinh=?, heDieuHanh=?, cameraSau=?, cameraTruoc=?, chip=?, pin=? WHERE maSP=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                setParams(pst, sp, false);
                pst.executeUpdate();
            }

            // Lưu thuộc tính động
            PreparedStatement pstDel = conn.prepareStatement("DELETE FROM ChiTietThuocTinh WHERE maSP=?");
            pstDel.setString(1, sp.getMaSP()); 
            pstDel.executeUpdate();
            
            String sqlAttr = "INSERT INTO ChiTietThuocTinh (maSP, thuocTinhID) VALUES (?, ?)";
            PreparedStatement pstAttr = conn.prepareStatement(sqlAttr);
            
            for (JComboBox<ThuocTinh> combo : view.dynamicCombos.values()) {
                ThuocTinh selected = (ThuocTinh) combo.getSelectedItem();
                if (selected != null) {
                    pstAttr.setString(1, sp.getMaSP()); 
                    pstAttr.setInt(2, selected.getId()); 
                    pstAttr.executeUpdate();
                }
            }
            
            conn.commit(); 
            view.showMessage("Lưu sản phẩm thành công!"); 
            view.clearForm(); 
            duongDanAnh = null; // Reset ảnh sau khi lưu
            loadDanhSach(); 
            view.showDanhSach();
            
        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(Exception ex){}
            e.printStackTrace(); 
            view.showMessage("Lỗi: " + e.getMessage());
        } finally { 
            try { if(conn!=null) conn.close(); } catch(Exception ex){} 
        }
    }

    // ==========================================================
    // PHẦN 3: HIỂN THỊ SP LÊN FORM ĐỂ SỬA
    // ==========================================================
    private void hienThiLenForm(String maSP) {
        loadDynamicAttributes();
        
        try (Connection conn = KetNoiCSDL.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM SanPham WHERE maSP=?");
            pst.setString(1, maSP); 
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                view.txtMaSP.setText(rs.getString("maSP"));
                view.txtTenSP.setText(rs.getString("tenSP"));
                
                setSelectedCombo(view.cboDanhMuc, rs.getInt("maDM"));
                setSelectedCombo(view.cboThuongHieu, rs.getInt("maTH"));
                
                view.txtGiaNhap.setText(String.format("%.0f", rs.getDouble("giaNhap")));
                view.txtGiaBan.setText(String.format("%.0f", rs.getDouble("giaBan")));
                view.txtTonKho.setText(rs.getString("soLuongTon"));
                view.txtMoTa.setText(rs.getString("moTa"));
                
                // [QUAN TRỌNG] Lấy đường dẫn ảnh từ DB gán vào biến toàn cục
                duongDanAnh = rs.getString("hinhAnh");
                view.hienThiAnh(duongDanAnh);
                
                view.txtManHinh.setText(rs.getString("manHinh"));
                view.txtHDH.setText(rs.getString("heDieuHanh"));
                view.txtCamSau.setText(rs.getString("cameraSau"));
                view.txtCamTruoc.setText(rs.getString("cameraTruoc"));
                view.txtChip.setText(rs.getString("chip"));
                view.txtPin.setText(rs.getString("pin"));

                PreparedStatement pstAttr = conn.prepareStatement("SELECT thuocTinhID FROM ChiTietThuocTinh WHERE maSP=?");
                pstAttr.setString(1, maSP); 
                ResultSet rsAttr = pstAttr.executeQuery();
                
                List<Integer> selectedIds = new ArrayList<>(); 
                while(rsAttr.next()) selectedIds.add(rsAttr.getInt("thuocTinhID"));
                
                for (JComboBox<ThuocTinh> combo : view.dynamicCombos.values()) {
                    for (int i=0; i<combo.getItemCount(); i++) {
                        if (selectedIds.contains(combo.getItemAt(i).getId())) { 
                            combo.setSelectedIndex(i); 
                            break; 
                        }
                    }
                }
                
                view.txtMaSP.setEditable(false);
                view.showFormNhap();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ==========================================================
    // PHẦN 4: CÁC HÀM HỖ TRỢ (HELPER)
    // ==========================================================
    
    private SanPham getSanPhamFromView() {
        try {
            if(view.txtMaSP.getText().isEmpty() || view.txtTenSP.getText().isEmpty()) return null;

            SanPham sp = new SanPham();
            sp.setMaSP(view.txtMaSP.getText()); 
            sp.setTenSP(view.txtTenSP.getText());
            
            DanhMuc dm = (DanhMuc) view.cboDanhMuc.getSelectedItem(); 
            if(dm!=null) sp.setMaDM(dm.getMaDM());
            
            ThuongHieu th = (ThuongHieu) view.cboThuongHieu.getSelectedItem(); 
            if(th!=null) {
                sp.setMaTH(th.getMaTH()); 
                sp.setHangSanXuat(th.getTenTH());
            }
            
            try { sp.setGiaNhap(Double.parseDouble(view.txtGiaNhap.getText())); } catch(Exception e) { sp.setGiaNhap(0.0); }
            try { sp.setGiaBan(Double.parseDouble(view.txtGiaBan.getText())); } catch(Exception e) { sp.setGiaBan(0.0); }
            try { sp.setTonKho(Integer.parseInt(view.txtTonKho.getText())); } catch(Exception e) { sp.setTonKho(0); }
            
            sp.setMoTa(view.txtMoTa.getText());
            sp.setManHinh(view.txtManHinh.getText());
            sp.setHeDieuHanh(view.txtHDH.getText());
            sp.setCameraSau(view.txtCamSau.getText());
            sp.setCameraTruoc(view.txtCamTruoc.getText());
            sp.setChip(view.txtChip.getText());
            sp.setPin(view.txtPin.getText());
            
            // [QUAN TRỌNG] Gán đường dẫn ảnh hiện tại vào đối tượng
            sp.setHinhAnh(duongDanAnh); 
            
            return sp;
        } catch (Exception e) { return null; }
    }
    
    private void setParams(PreparedStatement pst, SanPham sp, boolean isInsert) throws SQLException {
        if(isInsert) {
            pst.setString(1, sp.getMaSP()); pst.setString(2, sp.getTenSP()); 
            pst.setInt(3, sp.getMaDM()); pst.setInt(4, sp.getMaTH());
            pst.setDouble(5, sp.getGiaNhap()); pst.setDouble(6, sp.getGiaBan()); 
            pst.setInt(7, sp.getTonKho()); pst.setString(8, sp.getMoTa());
            pst.setString(9, sp.getHinhAnh()); pst.setString(10, sp.getHangSanXuat());
            pst.setString(11, sp.getManHinh()); pst.setString(12, sp.getHeDieuHanh()); 
            pst.setString(13, sp.getCameraSau()); pst.setString(14, sp.getCameraTruoc()); 
            pst.setString(15, sp.getChip()); pst.setString(16, sp.getPin());
        } else {
            pst.setString(1, sp.getTenSP()); pst.setInt(2, sp.getMaDM()); pst.setInt(3, sp.getMaTH());
            pst.setDouble(4, sp.getGiaNhap()); pst.setDouble(5, sp.getGiaBan()); 
            pst.setInt(6, sp.getTonKho()); pst.setString(7, sp.getMoTa());
            pst.setString(8, sp.getHinhAnh()); pst.setString(9, sp.getHangSanXuat());
            pst.setString(10, sp.getManHinh()); pst.setString(11, sp.getHeDieuHanh()); 
            pst.setString(12, sp.getCameraSau()); pst.setString(13, sp.getCameraTruoc()); 
            pst.setString(14, sp.getChip()); pst.setString(15, sp.getPin());
            pst.setString(16, sp.getMaSP());
        }
    }
    
    private void loadComboBoxData() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            view.cboDanhMuc.removeAllItems(); 
            ResultSet rsDM = conn.createStatement().executeQuery("SELECT * FROM DanhMuc");
            while (rsDM.next()) view.cboDanhMuc.addItem(new DanhMuc(rsDM.getInt("maDM"), rsDM.getString("tenDM")));
            
            view.cboThuongHieu.removeAllItems(); 
            ResultSet rsTH = conn.createStatement().executeQuery("SELECT * FROM ThuongHieu");
            while (rsTH.next()) view.cboThuongHieu.addItem(new ThuongHieu(rsTH.getInt("maTH"), rsTH.getString("tenTH"), rsTH.getString("logo")));
        } catch (Exception e) {}
    }

    private void loadDanhSach() {
        try (Connection conn = KetNoiCSDL.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT maSP, tenSP, hangSanXuat, giaBan, soLuongTon, hinhAnh FROM SanPham");
            view.getTableModel().setRowCount(0);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("maSP")); row.add(rs.getString("tenSP")); 
                row.add(rs.getString("hangSanXuat"));
                row.add(df.format(rs.getDouble("giaBan"))); 
                row.add(rs.getInt("soLuongTon")); 
                row.add(rs.getString("hinhAnh")); row.add(null);
                view.getTableModel().addRow(row);
            }
        } catch (Exception e) {}
    }

    private void xoaSanPham(String maSP) { 
        try{ 
            KetNoiCSDL.getConnection().createStatement().executeUpdate("DELETE FROM SanPham WHERE maSP='"+maSP+"'"); 
            loadDanhSach(); 
        }catch(Exception e){
            view.showMessage("Không thể xóa sản phẩm này (đang có trong hóa đơn)!");
        } 
    }
    
    private void setSelectedCombo(JComboBox<?> combo, int id) { 
        for(int i=0; i<combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            if(item instanceof DanhMuc && ((DanhMuc)item).getMaDM() == id) { combo.setSelectedIndex(i); return; }
            if(item instanceof ThuongHieu && ((ThuongHieu)item).getMaTH() == id) { combo.setSelectedIndex(i); return; }
        }
    }
    
    private void xuLyNhapHang() { 
        String maSP = JOptionPane.showInputDialog(view, "Nhập Mã SP cần thêm tồn kho:");
        if(maSP != null && !maSP.isEmpty()) {
             String slStr = JOptionPane.showInputDialog(view, "Nhập số lượng nhập thêm:");
             try {
                 int sl = Integer.parseInt(slStr);
                 KetNoiCSDL.getConnection().createStatement().executeUpdate("UPDATE SanPham SET soLuongTon = soLuongTon + "+sl+" WHERE maSP='"+maSP+"'");
                 loadDanhSach();
                 view.showMessage("Đã nhập thêm kho!");
             } catch(Exception e) { view.showMessage("Lỗi nhập liệu!"); }
        }
    }
    
    // [QUAN TRỌNG] Hàm chọn ảnh: copy ảnh vào thư mục images/ trong project, lưu tên file tương đối
    private void xuLyChonAnh() { 
        JFileChooser fc = new JFileChooser(); 
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png", "jpeg");
        fc.setFileFilter(filter);
        if (fc.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File filGoc = fc.getSelectedFile(); // File ảnh gốc người dùng chọn
            
            try {
                // Tạo thư mục images/ nếu chưa tồn tại (nằm cùng chỗ chạy chương trình)
                File thuMucAnh = new File("images");
                if (!thuMucAnh.exists()) thuMucAnh.mkdirs();
                
                // Copy file ảnh vào thư mục images/ với tên gốc
                Path nguon = filGoc.toPath();
                Path dich = Paths.get("images", filGoc.getName());
                Files.copy(nguon, dich, StandardCopyOption.REPLACE_EXISTING);
                
                // Chỉ lưu tên file (tương đối), ví dụ: "samsung_a55.jpg"
                duongDanAnh = filGoc.getName();
                
                // Hiển thị ảnh từ đường dẫn đầy đủ (để preview ngay)
                view.hienThiAnh(dich.toAbsolutePath().toString());
                
            } catch (IOException e) {
                view.showMessage("Lỗi khi copy ảnh: " + e.getMessage());
            }
        }
    }
}