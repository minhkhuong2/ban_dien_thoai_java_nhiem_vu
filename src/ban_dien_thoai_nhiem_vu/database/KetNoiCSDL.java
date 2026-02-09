/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ban_dien_thoai_nhiem_vu.database;

import java.sql.Connection;
import java.sql.DriverManager;
public class KetNoiCSDL {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Thay đổi user và password nếu máy bạn khác
            String url = "jdbc:mysql://localhost:3306/QuanLyDienThoai_PNC";
            String user = "root";
            String pass = ""; 
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println(">> Kết nối thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
