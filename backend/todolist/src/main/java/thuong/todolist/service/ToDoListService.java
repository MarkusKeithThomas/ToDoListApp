package thuong.todolist.service;

import thuong.todolist.baserespone.BaseRespone;
import thuong.todolist.dto.ToDoListDTO;
import thuong.todolist.request.ToDoListRequest;

import java.util.List;
public interface ToDoListService {
    BaseRespone getAllToDoListByUserId(String email);
    BaseRespone insertToDoList(ToDoListRequest toDoListRequest, String authorizationHeader);
    BaseRespone deleteToDoList(int idToDoList);
}
