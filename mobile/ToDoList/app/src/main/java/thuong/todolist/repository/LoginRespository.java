package thuong.todolist.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thuong.todolist.apiservice.LoginAPIService;
import thuong.todolist.request.LoginRequest;
import thuong.todolist.response.BaseResponse;
import thuong.todolist.service.APIService;

public class LoginRespository {
    private LoginAPIService loginAPIService;
    public LoginRespository(Context context){
        this.loginAPIService = APIService.getInstance("").create(LoginAPIService.class);
    }

    public LiveData<String> login(LoginRequest loginRequest) {
        MutableLiveData<String> tokenLiveData = new MutableLiveData<>();
        loginAPIService.loginUser(loginRequest).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse loginResponse = response.body();
                    if (loginResponse.getMessage() != null) {
                        String token = loginResponse.getMessage();
                        //gui token ve viewmodel
                        tokenLiveData.postValue(token);
                    } else {
                        tokenLiveData.postValue(null);
                    }
                } else {
                    tokenLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                    tokenLiveData.postValue(null);
            }
        });
        return tokenLiveData;


    }

}
