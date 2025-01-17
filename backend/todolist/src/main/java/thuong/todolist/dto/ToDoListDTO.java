package thuong.todolist.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ToDoListDTO {
    private int id;
    private String nameToDoList;
    private String description;
    private String image;
    private LocalDate dateCreate;

}
