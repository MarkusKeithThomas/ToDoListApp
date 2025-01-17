package thuong.todolist.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thuong.todolist.baserespone.BaseRespone;
import thuong.todolist.entity.UserEntity;
import thuong.todolist.repository.UserRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class LoginServiceImp implements LoginService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.key}")
    private String keyJWT;


    @Override
    public BaseRespone login(String email, String password) {
        BaseRespone baseRespone = new BaseRespone();
        String token = "";
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()){
            UserEntity userEntity = userOptional.get();
            if(passwordEncoder.matches(password,userEntity.getPassword())){
                //Tạo token JWT
                SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(keyJWT));
                long now = System.currentTimeMillis(); // Lấy thời gian hiện tại
                token = Jwts.builder()
                        .signWith(key)
                        .setSubject(userEntity.getEmail())
                        .claim("id", userEntity.getId())        // Thêm ID người dùng
                        .setIssuedAt(new Date(now)) // Ngày phát hành
                        .setExpiration(new Date(now+86400000)) // Hết hạn sau 1 ngày (1 ngày = 86400000 ms)
                        .compact();
                baseRespone.setCode(200);
                baseRespone.setMessage(token);
                baseRespone.setData(true);
            } else {
                baseRespone.setCode(200);
                baseRespone.setMessage("Sai Mật Khẩu");
                baseRespone.setData(false);
            }
        } else {
            baseRespone.setCode(200);
            baseRespone.setMessage("Tài Khoản Không Tồn Tại.");
            baseRespone.setData(false);
        }
        return baseRespone;
    }
    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
}
