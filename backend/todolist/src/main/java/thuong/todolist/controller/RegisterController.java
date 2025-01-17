package thuong.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thuong.todolist.baserespone.BaseRespone;
import thuong.todolist.request.RegisterRequest;
import thuong.todolist.service.RegisterServiceImp;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private RegisterServiceImp registerServiceImp;

    @PostMapping
    public ResponseEntity<?> registerAccount(@RequestBody RegisterRequest registerRequest){
        BaseRespone baseRespone = registerServiceImp.insertUser(registerRequest);
        return ResponseEntity.ok(baseRespone);
    }
}
