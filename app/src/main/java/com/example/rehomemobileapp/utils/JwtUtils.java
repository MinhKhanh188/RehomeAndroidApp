package com.example.rehomemobileapp.utils;
import android.util.Base64;
import org.json.JSONObject;
public class JwtUtils {
    public static String getUserIdFromToken(String jwt) {
        try {
            // Tách token thành 3 phần: header.payload.signature
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return null;

            // Phần payload là phần thứ 2
            String payload = parts[1];

            // Decode Base64 (chú ý thêm padding nếu thiếu)
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE | Base64.NO_WRAP);
            String decodedPayload = new String(decodedBytes, "UTF-8");

            // Parse JSON để lấy userId
            JSONObject jsonObject = new JSONObject(decodedPayload);

            // Tuỳ theo cấu trúc backend, bạn có thể đổi "userId" thành "sub", "id",...
            return jsonObject.getString("id");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
