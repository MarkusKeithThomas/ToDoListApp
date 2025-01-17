package thuong.todolist.apiservice;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import thuong.todolist.request.RegisterRequest;
import thuong.todolist.response.BaseResponse;

public interface RegisterAPIService {
    @POST("/register")
    Call<BaseResponse> registerUser(@Body RegisterRequest registerRequest);
}
