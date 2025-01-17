package thuong.todolist.exception;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class InsertException extends RuntimeException{
    private String message;
    public InsertException(String message) {
        super(message);
        this.message = message;
    }
}
