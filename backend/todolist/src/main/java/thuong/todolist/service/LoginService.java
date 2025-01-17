package thuong.todolist.service;

import thuong.todolist.baserespone.BaseRespone;

public interface LoginService {
    BaseRespone login(String email,String password);
}
