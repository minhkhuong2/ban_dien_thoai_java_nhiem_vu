package ban_dien_thoai_nhiem_vu.controller;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class GuiEmail {

    // Thay bằng Email và Mật khẩu ứng dụng của bạn
    private static final String EMAIL_GUI = "khuongbuivan826@gmail.com"; 
    private static final String MAT_KHAU_UNG_DUNG = "zemk ekhi yeel mirf"; // 16 ký tự App Password

    public static String guiMaOTP(String emailNhan) {
        // 1. Tạo mã OTP ngẫu nhiên 6 số
        String otp = String.format("%06d", new Random().nextInt(999999));

        // 2. Cấu hình Server Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // 3. Đăng nhập
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_GUI, MAT_KHAU_UNG_DUNG);
            }
        });

        try {
            // 4. Tạo nội dung Email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_GUI));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailNhan));
            message.setSubject("MÃ XÁC NHẬN - QUÊN MẬT KHẨU PNC STORE");
            message.setText("Chào bạn,\n\nMã xác nhận (OTP) để lấy lại mật khẩu của bạn là: " + otp + "\n\nVui lòng không chia sẻ mã này cho ai.\n\nTrân trọng,\nPNC Store System");

            // 5. Gửi đi
            Transport.send(message);
            System.out.println("Đã gửi OTP thành công!");
            return otp; // Trả về mã OTP để hệ thống đối chiếu

        } catch (MessagingException e) {
            e.printStackTrace();
            return null; // Gửi thất bại
        }
    }
    public static void guiHoaDon(String emailNhan, String tieuDe, String noiDungHTML) {
        // Cấu hình Server Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_GUI, MAT_KHAU_UNG_DUNG);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_GUI, "PNC MOBILE STORE")); // Hiện tên Shop cho đẹp
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailNhan));
            message.setSubject(tieuDe);
            
            // Quan trọng: Set nội dung là HTML để kẻ bảng được
            message.setContent(noiDungHTML, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Đã gửi hóa đơn điện tử thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Gửi mail thất bại: " + e.getMessage());
        }
    }
}