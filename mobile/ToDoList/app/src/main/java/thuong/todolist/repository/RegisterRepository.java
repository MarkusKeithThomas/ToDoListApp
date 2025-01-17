package thuong.todolist.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thuong.todolist.apiservice.RegisterAPIService;
import thuong.todolist.request.RegisterRequest;
import thuong.todolist.response.BaseResponse;
import thuong.todolist.service.APIService;

public class RegisterRepository {
    private RegisterAPIService registerAPIService;
    public RegisterRepository(){
        this.registerAPIService = APIService.getInstance("").create(RegisterAPIService.class);
    }

    public LiveData<String> register(RegisterRequest registerRequest){
        MutableLiveData<String> message = new MutableLiveData<>();
        registerAPIService.registerUser(registerRequest).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse.getCode() == 200 && baseResponse.getData().equals(true)){
                    message.postValue("Success");
                } else {

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
        return message;
    }
}
