package thuong.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thuong.todolist.baserespone.BaseRespone;
import thuong.todolist.entity.UserEntity;
import thuong.todolist.repository.UserRepository;
import thuong.todolist.request.RegisterRequest;

@Service
public class RegisterServiceImp implements RegisterService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public BaseRespone insertUser(RegisterRequest request) {
        BaseRespone baseRespone = new BaseRespone();
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            baseRespone.setCode(200);
            baseRespone.setMessage("Tài khoản đã tồn tại.");
            baseRespone.setData(false);
        } else {
            UserEntity user = new UserEntity();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setUserName(request.getUserName());
            userRepository.save(user);
            baseRespone.setCode(200);
            baseRespone.setMessage("Successes");
            baseRespone.setData(true);
        }
        return baseRespone;
    }
}
