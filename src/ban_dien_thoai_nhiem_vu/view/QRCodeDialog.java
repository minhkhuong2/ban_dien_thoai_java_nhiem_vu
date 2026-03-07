package ban_dien_thoai_nhiem_vu.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class QRCodeDialog extends JDialog {

    private boolean isPaid = false;
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_DANGER = new Color(220, 53, 69);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    
    private int secondsLeft = 15 * 60;
    private Timer countdownTimer;

    public QRCodeDialog(Frame parent, double amount, String transferContent) {
        super(parent, "Thanh Toán Chuyển Khoản", true);
        setSize(450, 680);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        DecimalFormat df = new DecimalFormat("#,###");
        String formattedAmount = df.format(amount) + " đ";

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTimer = new JLabel("Hết hạn sau 15:00");
        lblTimer.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTimer.setForeground(COLOR_DANGER);
        pnlHeader.add(lblTimer, BorderLayout.WEST);
        
        countdownTimer = new Timer(1000, e -> {
            secondsLeft--;
            if (secondsLeft <= 0) {
                countdownTimer.stop();
                lblTimer.setText("Đã hết hạn");
            } else {
                int m = secondsLeft / 60;
                int s = secondsLeft % 60;
                lblTimer.setText(String.format("Hết hạn sau %02d:%02d", m, s));
            }
        });
        countdownTimer.start();

        JLabel lblAmountTop = new JLabel(formattedAmount);
        lblAmountTop.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblAmountTop.setForeground(COLOR_PRIMARY);
        pnlHeader.add(lblAmountTop, BorderLayout.EAST);
        
        add(pnlHeader, BorderLayout.NORTH);

        // Body
        JPanel pnlBody = new JPanel(new BorderLayout(0, 15));
        pnlBody.setBackground(Color.WHITE);



        // QR Image
        JLabel lblQR = new JLabel();
        lblQR.setHorizontalAlignment(SwingConstants.CENTER);
        // We simulate a QR Code, in a real app this would be generated via VietQR API 
        // Example VietQR format: https://img.vietqr.io/image/<BANK_ID>-<ACCOUNT_NO>-compact2.jpg?amount=<AMOUNT>&addInfo=<CONTENT>&accountName=<ACCOUNT_NAME>
        try {
            String urlStr = "https://img.vietqr.io/image/970436-1056405604-qr_only.png?amount=" + Math.round(amount) + "&addInfo=" + transferContent.replace(" ", "%20") + "&accountName=BUI%20VAN%20KHUONG";
            java.net.URL url = new java.net.URL(urlStr);
            ImageIcon qrIcon = new ImageIcon(url);
            
            int oW = qrIcon.getIconWidth();
            int oH = qrIcon.getIconHeight();
            int targetW = 320;
            int targetH = (oW > 0) ? (oH * targetW / oW) : 320;
            
            Image img = qrIcon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
            lblQR.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblQR.setText("[ Lỗi tải mã QR ]");
            lblQR.setFont(new Font("Segoe UI", Font.BOLD, 16));
        }
        
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.add(lblQR, BorderLayout.CENTER);
        
        JLabel lblInstruction = new JLabel("Quét mã bằng ứng dụng Ngân hàng / Ví điện tử", SwingConstants.CENTER);
        lblInstruction.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInstruction.setForeground(Color.GRAY);
        lblInstruction.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnlCenter.add(lblInstruction, BorderLayout.SOUTH);
        
        pnlBody.add(pnlCenter, BorderLayout.CENTER);

        // Info Table
        JPanel pnlInfo = new JPanel(new GridLayout(4, 2, 5, 8));
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setBorder(new EmptyBorder(10, 20, 15, 20));
        
        addInfoRow(pnlInfo, "Ngân hàng", "Vietcombank");
        addInfoRow(pnlInfo, "Số tài khoản", "1056405604");
        addInfoRow(pnlInfo, "Chủ tài khoản", "BUI VAN KHUONG");
        addInfoRowBlue(pnlInfo, "Số tiền", formattedAmount);
        
        pnlBody.add(pnlInfo, BorderLayout.SOUTH);

        add(pnlBody, BorderLayout.CENTER);

        // Footer Buttons
        JPanel pnlFooter = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(new EmptyBorder(10, 20, 20, 20));

        JButton btnCancel = new JButton("Hủy giao dịch");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(241, 243, 245));
        btnCancel.setForeground(COLOR_TEXT_DARK);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(null);
        btnCancel.setPreferredSize(new Dimension(0, 45));
        
        JButton btnConfirm = new JButton("Tôi đã chuyển tiền");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirm.setBackground(COLOR_PRIMARY);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setBorder(null);

        btnCancel.addActionListener(e -> {
            isPaid = false;
            if (countdownTimer != null) countdownTimer.stop();
            dispose();
        });

        btnConfirm.addActionListener(e -> {
            isPaid = true;
            if (countdownTimer != null) countdownTimer.stop();
            dispose();
        });
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (countdownTimer != null) countdownTimer.stop();
            }
        });

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnConfirm);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void addInfoRow(JPanel pnl, String label, String value) {
        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblLbl.setForeground(Color.DARK_GRAY);
        pnl.add(lblLbl);

        JLabel lblVal = new JLabel(value, SwingConstants.RIGHT);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblVal.setForeground(COLOR_TEXT_DARK);
        pnl.add(lblVal);
    }

    private void addInfoRowBlue(JPanel pnl, String label, String value) {
        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblLbl.setForeground(Color.DARK_GRAY);
        pnl.add(lblLbl);

        JLabel lblVal = new JLabel(value, SwingConstants.RIGHT);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblVal.setForeground(COLOR_PRIMARY);
        pnl.add(lblVal);
    }

    public boolean isPaid() {
        return isPaid;
    }
}
