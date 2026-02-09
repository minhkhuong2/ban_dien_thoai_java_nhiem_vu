package ban_dien_thoai_nhiem_vu.model;
import java.security.MessageDigest;

public class MaHoa {
    public static String toMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (Exception e) { return ""; }
    }
}
