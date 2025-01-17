package thuong.todolist.controller;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thuong.todolist.baserespone.BaseRespone;
import thuong.todolist.request.ToDoListRequest;
import thuong.todolist.service.ToDoListImp;

@RestController
@RequestMapping("/todolist")
public class ListToDoController {
    @Autowired
    private ToDoListImp toDoListImp;

    @GetMapping
    public BaseRespone getAllToDoListById(@RequestHeader("Authorization") String authorizationHeader){
        return toDoListImp.getAllToDoListByUserId(authorizationHeader);
    }
    @PostMapping("/add")
    public BaseRespone insertToDoList(@ModelAttribute ToDoListRequest toDoListRequest,@RequestHeader("Authorization") String authorizationHeader){
        return toDoListImp.insertToDoList(toDoListRequest,authorizationHeader);
    }
    @DeleteMapping("/{idToDoList}")
    public BaseRespone deleteToDoList(@PathVariable("idToDoList") int idToDoList){
        return toDoListImp.deleteToDoList(idToDoList);
    }

}
