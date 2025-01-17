package thuong.todolist.apiservice;

import android.content.Intent;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import thuong.todolist.response.BaseResponse;
import thuong.todolist.response.ToDoListResponse;

public interface ToDoListAPIService {
    @GET("/todolist")
    Call<ToDoListResponse> getToDoList();

    @Multipart
    @POST("todolist/add")
    Call<BaseResponse> uploadToDoList(
            @Part MultipartBody.Part file,
            @Part("nameToDoList") RequestBody nameToDoList,
            @Part("description") RequestBody description,
            @Part("dateCreate") RequestBody dateCreate);
    @DELETE("/todolist/{idToDoList}")
    Call<BaseResponse> deleteToDoList(
            @Path("idToDoList") int idToDoList);

}
