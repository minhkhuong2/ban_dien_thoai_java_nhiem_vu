package ban_dien_thoai_nhiem_vu.view;

import ban_dien_thoai_nhiem_vu.controller.GuiEmail;
import ban_dien_thoai_nhiem_vu.database.KetNoiCSDL;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class QuenMatKhauDialog extends JDialog {
    
    private JTextField txtEmail, txtOTP;
    private JPasswordField txtMatKhauMoi;
    private JButton btnGuiMa, btnXacNhan;
    private String serverOTP = null; 
    private String emailUser = null;

    public QuenMatKhauDialog(Frame parent) { // Sửa tham số thành Frame để tương thích với view
        super(parent, "Quên mật khẩu", true);
        setSize(450, 400); // Tăng kích thước chút cho đẹp
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 1, 10, 10));

        // --- BƯỚC 1: NHẬP EMAIL ---
        JPanel p1 = new JPanel(new BorderLayout(5, 5));
        p1.setBorder(BorderFactory.createTitledBorder("Bước 1: Nhập Email đã đăng ký"));
        txtEmail = new JTextField();
        btnGuiMa = new JButton("Gửi mã OTP");
        p1.add(txtEmail, BorderLayout.CENTER);
        p1.add(btnGuiMa, BorderLayout.EAST);
        add(p1);

        // --- BƯỚC 2: NHẬP OTP ---
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBorder(BorderFactory.createTitledBorder("Bước 2: Nhập mã xác nhận (6 số)"));
        txtOTP = new JTextField();
        txtOTP.setFont(new Font("SansSerif", Font.BOLD, 16));
        txtOTP.setHorizontalAlignment(SwingConstants.CENTER);
        p2.add(txtOTP, BorderLayout.CENTER);
        add(p2);

        // --- BƯỚC 3: MẬT KHẨU MỚI ---
        JPanel p3 = new JPanel(new BorderLayout());
        p3.setBorder(BorderFactory.createTitledBorder("Bước 3: Mật khẩu mới"));
        txtMatKhauMoi = new JPasswordField();
        p3.add(txtMatKhauMoi, BorderLayout.CENTER);
        add(p3);

        // --- BƯỚC 4: NÚT XÁC NHẬN (QUAN TRỌNG: PHẢI NEW TRƯỚC RỒI MỚI SET MÀU) ---
        btnXacNhan = new JButton("ĐỔI MẬT KHẨU"); // <--- Khởi tạo ở đây
        
        // Sau đó mới được tô màu
        btnXacNhan.setBackground(new Color(41, 98, 255)); 
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXacNhan.setFocusPainted(false);
        
        // Thêm vào Panel bọc ngoài để có padding (khoảng cách) cho đẹp
        JPanel pBtn = new JPanel(new BorderLayout());
        pBtn.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        pBtn.add(btnXacNhan, BorderLayout.CENTER);
        add(pBtn);

        // --- SỰ KIỆN GỬI MÃ ---
        btnGuiMa.addActionListener(e -> {
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
        });

        // --- SỰ KIỆN XÁC NHẬN ĐỔI PASS ---
        btnXacNhan.addActionListener(e -> {
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
        });
    }
}