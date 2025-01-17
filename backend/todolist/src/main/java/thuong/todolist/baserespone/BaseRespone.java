package thuong.todolist.baserespone;

import lombok.Data;

@Data
public class BaseRespone {
    private int code;
    private String message;
    private Object data;
}
