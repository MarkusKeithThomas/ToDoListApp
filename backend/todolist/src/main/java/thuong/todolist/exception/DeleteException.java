package thuong.todolist.exception;

import lombok.Data;

@Data
public class DeleteException extends RuntimeException{
    private String message;
    public DeleteException(String message) {
        super(message);
        this.message = message;
    }
}
