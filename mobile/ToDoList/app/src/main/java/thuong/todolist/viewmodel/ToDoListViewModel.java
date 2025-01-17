package thuong.todolist.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.List;

import thuong.todolist.model.ToDoListItem;
import thuong.todolist.repository.ToDoListRepository;
import thuong.todolist.request.ToDoListRequest;
import thuong.todolist.response.BaseResponse;

public class ToDoListViewModel extends ViewModel{
    private ToDoListRepository toDoListRepository;
    private MutableLiveData<ToDoListRequest> toDoListRequestLiveData = new MutableLiveData<>(new ToDoListRequest());
    private MutableLiveData<String> errorTitle = new MutableLiveData<>();
    private MutableLiveData<String> errorContent = new MutableLiveData<>();
    private MutableLiveData<String> errorimage = new MutableLiveData<>();
    public ToDoListViewModel(String token) {
        // Khởi tạo Repository với token
        toDoListRepository = new ToDoListRepository(token);
    }
    public LiveData<BaseResponse> deleteToDoList(int id){
        return toDoListRepository.deleteToDoListById(id);
    }

    public boolean isValidToDoListRequest(ToDoListRequest toDoListRequest,File file){
        boolean isValid = true;
        if (toDoListRequest.getTitle() == null || toDoListRequest.getTitle().isEmpty()){
            errorTitle.setValue("Tiêu đề không được bỏ trống.");
            isValid = false;
        } else {
            errorTitle.setValue(null);
        }
        if(toDoListRequest.getContent() == null || toDoListRequest.getContent().trim().isEmpty()){
            errorContent.setValue("Nội dung không được bỏ trống.");
            Log.d("toDoListRequest","toDoListRequest "+isValid+" "+toDoListRequest.getContent());
            isValid = false;
            Log.d("toDoListRequest","toDoListRequest "+isValid);

        } else {
            errorContent.setValue(null);
        }
        if (!file.exists() || file.getAbsolutePath().isEmpty()){
            errorimage.setValue("Bạn cần chọn ảnh trước. ");
            isValid = false;
        } else {
            errorimage.setValue(null);
        }
        return isValid;
    }

    public LiveData<ToDoListRequest> getToDoListRequestLiveData() {
        return toDoListRequestLiveData;
    }

    public MutableLiveData<String> getErrorimage() {
        return errorimage;
    }

    public MutableLiveData<String> getErrorTitle() {
        return errorTitle;
    }


    public MutableLiveData<String> getErrorContent() {
        return errorContent;
    }


    // Cung cấp LiveData để Activity/Fragment quan sát
    public LiveData<List<ToDoListItem>> getToDoListLiveData() {
        return toDoListRepository.getAllToDoListByEmail();
    }
    public void uploadToDoList(ToDoListRequest request, File file){
        toDoListRepository.uploadToDoList(request,file);
    }

}
