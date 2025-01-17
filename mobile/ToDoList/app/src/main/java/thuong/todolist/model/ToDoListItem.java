package thuong.todolist.model;

import com.google.gson.annotations.SerializedName;

public class ToDoListItem {
    @SerializedName("id")
    private int id;
    @SerializedName("nameToDoList")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private String imagePath;
    @SerializedName("dateCreate")
    private String dateCreate;
    private String subDescription;


    public String formatImageUrl(String url) {
        if (url.contains("localhost")) {
            // Chuyển đổi "localhost" thành "10.0.2.2" cho Emulator
            return url.replace("localhost", "10.0.2.2");
        }
        return url; // Giữ nguyên nếu không phải localhost
    }

    public String getSubDescription() {
        return subDescription;
    }

    public void setSubDescription(String subDescription) {
        this.subDescription = subDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }
}
