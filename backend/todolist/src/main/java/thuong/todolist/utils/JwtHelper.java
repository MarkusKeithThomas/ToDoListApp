package thuong.todolist.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtHelper {
    @Value("${jwt.key}")
    private String keyJwt;

    public boolean decryptToken(String token){
        boolean isSuccess = false;
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(keyJwt));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return isSuccess = true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return isSuccess; // Token hết hạn
        } catch (Exception e){
            return isSuccess;
        }
    }

    public String decryptEmail(String authHeader) {
        String email = "";
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Bỏ "Bearer " để lấy token thực
            try {
                SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(keyJwt));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                return claims.getSubject(); // Email được lưu trong subject
            } catch (Exception e) {
                System.out.println("Failed to extract email: " + e.getMessage());
                return email;
            }
        }
        return email;
    }
    public Integer decryptUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Nếu không có token hoặc không bắt đầu bằng "Bearer", trả về null
            return null;
        }
        String token = authHeader.substring(7); // Bỏ "Bearer " để lấy token thực
        try {
            // Lấy khóa từ chuỗi mã hóa Base64
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(keyJwt));
            // Giải mã token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // Lấy id người dùng
            return claims.get("id", Integer.class);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Token đã hết hạn
            System.out.println("Token expired: {} " + e.getMessage());
        } catch (Exception e) {
            // Các lỗi khác
            System.out.println("Failed to decrypt token: {}"+ e.getMessage());
        }
        return null;
    }

}
