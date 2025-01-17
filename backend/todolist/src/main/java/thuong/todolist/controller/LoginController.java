package thuong.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thuong.todolist.baserespone.BaseRespone;
import thuong.todolist.request.LoginRequest;
import thuong.todolist.service.LoginServiceImp;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginServiceImp loginServiceImp;

    @PostMapping
    public BaseRespone login(@RequestBody LoginRequest loginRequest){
        return loginServiceImp.login(loginRequest.getEmail(),loginRequest.getPassword());
    }
}
