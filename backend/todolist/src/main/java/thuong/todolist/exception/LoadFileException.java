package thuong.todolist.exception;

import lombok.Data;

@Data
public class LoadFileException extends RuntimeException{
    private String message;
    public LoadFileException(String message) {
        this.message = message;
    }
}
