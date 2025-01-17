package thuong.todolist.apiservice;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import thuong.todolist.request.LoginRequest;
import thuong.todolist.response.BaseResponse;

public interface LoginAPIService {
    @POST("/login")
    Call<BaseResponse> loginUser(@Body LoginRequest loginUser);
}
