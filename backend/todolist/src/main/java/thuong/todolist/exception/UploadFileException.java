package thuong.todolist.exception;

import lombok.Data;

@Data
public class UploadFileException  extends RuntimeException{
    private String message;
    public UploadFileException(String message){
        super(message);
        this.message = message;
    }
}
