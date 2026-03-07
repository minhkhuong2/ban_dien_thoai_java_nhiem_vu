package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.controller.GuiEmail;
import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class QuenMatKhauDialog extends JDialog {
    
    private JTextField txtEmail, txtOTP;
    private JPasswordField txtMatKhauMoi;
    private JButton btnGuiMa, btnXacNhan;
    private String serverOTP = null; 
    private String emailUser = null;

    public QuenMatKhauDialog(Frame parent) {
        super(parent, "Khôi phục mật khẩu - PNC STORE", true);
        setSize(480, 580); 
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0; 
        g.gridx = 0; g.gridy = 0;

        // --- TITLE ---
        JLabel lblTitle = new JLabel("Quên mật khẩu?");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 33, 33));
        g.insets = new Insets(0, 0, 5, 0);
        pnlMain.add(lblTitle, g);
        
        // --- SUBTITLE ---
        g.gridy++;
        JLabel lblSubtitle = new JLabel("<html>Đừng lo lắng, chuyện này thường xảy ra.<br>Nhập email để nhận mã OTP khôi phục.</html>");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(117, 117, 117));
        g.insets = new Insets(0, 0, 25, 0);
        pnlMain.add(lblSubtitle, g);

        // --- EMAIL INPUT TIER ---
        g.gridy++;
        g.insets = new Insets(0, 0, 10, 0);
        txtEmail = new JTextField();
        setupModernTextField(txtEmail, "Bước 1: Nhập Email đã đăng ký");
        pnlMain.add(txtEmail, g);

        // Nút gửi email
        g.gridy++;
        g.insets = new Insets(0, 0, 20, 0);
        btnGuiMa = new JButton("Gửi mã OTP xác nhận");
        styleButton(btnGuiMa, new Color(245, 246, 250), new Color(41, 98, 255));
        pnlMain.add(btnGuiMa, g);

        // --- OTP INPUT ---
        g.gridy++;
        g.insets = new Insets(0, 0, 15, 0);
        txtOTP = new JTextField();
        txtOTP.setHorizontalAlignment(SwingConstants.CENTER);
        setupModernTextField(txtOTP, "Bước 2: Nhập mã xác nhận (OTP)");
        pnlMain.add(txtOTP, g);

        // --- NEW PASSWORD INPUT ---
        g.gridy++;
        g.insets = new Insets(0, 0, 25, 0);
        txtMatKhauMoi = new JPasswordField();
        setupModernTextField(txtMatKhauMoi, "Bước 3: Tạo Mật khẩu mới");
        pnlMain.add(txtMatKhauMoi, g);

        // --- SUBMIT COMPLETED ---
        g.gridy++;
        g.insets = new Insets(10, 0, 15, 0);
        btnXacNhan = new JButton("ĐỔI MẬT KHẨU");
        styleButton(btnXacNhan, new Color(41, 98, 255), Color.WHITE);
        pnlMain.add(btnXacNhan, g);

        add(pnlMain, BorderLayout.CENTER);

        // --- SỰ KIỆN GỬI MÃ ---
        btnGuiMa.addActionListener(e -> actionGuiMa());

        // --- SỰ KIỆN XÁC NHẬN ---
        btnXacNhan.addActionListener(e -> actionXacNhan());
    }

    private void actionGuiMa() {
        String email = txtEmail.getText().trim();
        if(email.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng nhập Email!"); return; }

        try (Connection conn = KetNoiCSDL.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM NhanVien WHERE email = ?");
            pst.setString(1, email);
            if (pst.executeQuery().next()) {
                btnGuiMa.setText("Đang gửi...");
                btnGuiMa.setEnabled(false);
                
                new Thread(() -> {
                    serverOTP = GuiEmail.guiMaOTP(email);
                    SwingUtilities.invokeLater(() -> {
                        if (serverOTP != null) {
                            JOptionPane.showMessageDialog(this, "Đã gửi mã OTP về email: " + email);
                            emailUser = email;
                            txtEmail.setEditable(false);
                            txtOTP.requestFocus();
                        } else {
                            JOptionPane.showMessageDialog(this, "Lỗi gửi mail! Kiểm tra lại kết nối.");
                        }
                        btnGuiMa.setText("Gửi lại mã");
                        btnGuiMa.setEnabled(true);
                    });
                }).start();
            } else {
                JOptionPane.showMessageDialog(this, "Email này chưa được đăng ký trong hệ thống!");
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void actionXacNhan() {
        if (serverOTP == null) { JOptionPane.showMessageDialog(this, "Vui lòng lấy mã OTP trước!"); return; }
        
        String userOTP = txtOTP.getText().trim();
        String newPass = new String(txtMatKhauMoi.getPassword());

        if (!userOTP.equals(serverOTP)) {
            JOptionPane.showMessageDialog(this, "Mã OTP không đúng!");
            return;
        }
        if (newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới!");
            return;
        }

        try (Connection conn = KetNoiCSDL.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("UPDATE NhanVien SET matKhau = ? WHERE email = ?");
            pst.setString(1, newPass);
            pst.setString(2, emailUser);
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công! Hãy đăng nhập lại.");
            dispose(); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setupModernTextField(JTextField field, String titleText) {
        field.setPreferredSize(new Dimension(0, 45));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        Border lineBorder = BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lineBorder, " " + titleText + " ", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.PLAIN, 11), new Color(117, 117, 117));
        Border margin = new EmptyBorder(0, 10, 5, 10); 
        
        field.setBorder(BorderFactory.createCompoundBorder(titledBorder, margin));
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(0, 45));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
