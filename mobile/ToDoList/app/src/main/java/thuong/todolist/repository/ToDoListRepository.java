package thuong.todolist.repository;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thuong.todolist.MainActivity;
import thuong.todolist.apiservice.ToDoListAPIService;
import thuong.todolist.model.ToDoListItem;
import thuong.todolist.request.ToDoListRequest;
import thuong.todolist.response.BaseResponse;
import thuong.todolist.response.ToDoListResponse;
import thuong.todolist.service.APIService;

public class ToDoListRepository {
    private ToDoListAPIService toDoListAPIService;
    public ToDoListRepository(String token){
        toDoListAPIService = APIService.getInstance(token).create(ToDoListAPIService.class);

    }
    public LiveData<BaseResponse> deleteToDoListById(int id){
        MutableLiveData<BaseResponse> liveBaseResponse = new MutableLiveData<>();
        toDoListAPIService.deleteToDoList(id).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                Log.d("deleteToDoListById",baseResponse.getMessage());

                if (response.isSuccessful() && response.body() != null){
                    if (baseResponse.getCode() == 200){
                        liveBaseResponse.postValue(baseResponse);
                    } else {
                        liveBaseResponse.postValue(baseResponse);
                    }
                } else {
                    liveBaseResponse.postValue(baseResponse);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
        return liveBaseResponse;
    }
    public LiveData<List<ToDoListItem>> getAllToDoListByEmail(){
        MutableLiveData<List<ToDoListItem>> liveData = new MutableLiveData<>();
        toDoListAPIService.getToDoList().enqueue(new Callback<ToDoListResponse>() {
            @Override
            public void onResponse(Call<ToDoListResponse> call, Response<ToDoListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body().getToDoList());

                } else {
                    liveData.postValue(new ArrayList<>());
                    Log.e("ToDoListRepository", "Lỗi: khong co danh sach tra ve");

                }
            }

            @Override
            public void onFailure(Call<ToDoListResponse> call, Throwable t) {
                liveData.postValue(new ArrayList<>());
                Log.e("ToDoListRepository", "Lỗi: " + t.getMessage());
            }
        });
        return liveData;
    }
    public void uploadToDoList(ToDoListRequest request, File file) {
        // Tạo các trường dữ liệu
        Map<String, RequestBody> fields = createRequestParts(request);
        // Tạo file part
        MultipartBody.Part filePart = createFilePart(file);
        // Gửi dữ liệu qua API
        toDoListAPIService.uploadToDoList(
                filePart,
                fields.get("nameToDoList"),
                fields.get("description"),
                fields.get("dateCreate")
        ).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse.getCode() == 200) {
                        Log.d("uploadToDoList", "Upload thành công");
                    } else {
                        Log.e("uploadToDoList", "Upload thất bại: " + baseResponse.getMessage());
                    }
                } else {
                    Log.e("uploadToDoList", "Phản hồi không thành công");
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("uploadToDoList", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    private Map<String, RequestBody> createRequestParts(ToDoListRequest request) {
        Map<String, RequestBody> fields = new HashMap<>();
        fields.put("nameToDoList", RequestBody.Companion.create(request.getTitle(), MediaType.parse("text/plain")));
        fields.put("description", RequestBody.Companion.create(request.getContent(), MediaType.parse("text/plain")));
        fields.put("dateCreate", RequestBody.Companion.create(request.getDateCreate().toString(), MediaType.parse("text/plain")));
        return fields;
    }

    private MultipartBody.Part createFilePart(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File không được null!");
        }
        RequestBody fileRequestBody = RequestBody.Companion.create(file, MediaType.parse("multipart/form-data"));
        return MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
    }
}
