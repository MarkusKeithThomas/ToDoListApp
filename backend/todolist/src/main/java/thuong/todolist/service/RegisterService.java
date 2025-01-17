package thuong.todolist.service;

import thuong.todolist.baserespone.BaseRespone;
import thuong.todolist.request.RegisterRequest;

public interface RegisterService {
    BaseRespone insertUser(RegisterRequest request);
}
