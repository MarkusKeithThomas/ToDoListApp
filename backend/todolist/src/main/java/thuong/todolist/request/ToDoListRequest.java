package thuong.todolist.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
@Data
public class ToDoListRequest {
    private String nameToDoList;
    private String description;
    private MultipartFile file;
    private LocalDate dateCreate;
    private int idUser;
}
