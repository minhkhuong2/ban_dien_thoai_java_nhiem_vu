/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ban_dien_thoai_nhiem_vu;
public class Main {
    public static void main(String[] args) {
        ban_dien_thoai_nhiem_vu.view.BanHangFrame view = new ban_dien_thoai_nhiem_vu.view.BanHangFrame();
        new ban_dien_thoai_nhiem_vu.controller.BanHangController(view);
        view.setVisible(true);
    }
}
