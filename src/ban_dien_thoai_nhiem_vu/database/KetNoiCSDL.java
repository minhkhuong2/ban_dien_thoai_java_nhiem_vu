package ban_dien_thoai_nhiem_vu.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class KetNoiCSDL {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 1. Nạp Driver (Bắt buộc với các bản NetBeans/Java mới)
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            // 2. Sửa lại tên CSDL cho đúng với phpMyAdmin của bạn: quanlydienthoai_pnc
            String url = "jdbc:mysql://localhost:3306/quanlydienthoai_pnc?useSSL=false&rewriteBatchedStatements=true&characterEncoding=UTF-8";
            
            String user = "root";
            String password = ""; // Mật khẩu (XAMPP thường để trống)
            
            conn = DriverManager.getConnection(url, user, password);
            
        } catch (Exception e) {
            System.out.println("Lỗi kết nối CSDL: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}