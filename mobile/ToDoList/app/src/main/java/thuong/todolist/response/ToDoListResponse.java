package thuong.todolist.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import thuong.todolist.model.ToDoListItem;

public class ToDoListResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<ToDoListItem> toDoList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ToDoListItem> getToDoList() {
        return toDoList;
    }

    public void setToDoList(List<ToDoListItem> toDoList) {
        this.toDoList = toDoList;
    }
}
