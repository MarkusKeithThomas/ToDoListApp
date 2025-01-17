package thuong.todolist.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import thuong.todolist.helper.TokenManager;
import thuong.todolist.repository.LoginRespository;
import thuong.todolist.request.LoginRequest;
import thuong.todolist.service.APIService;

public class LoginViewModel extends AndroidViewModel {
    private LoginRespository loginRespository;
    private TokenManager tokenManager;

    public LoginViewModel(Application application){
        super(application);
        loginRespository = new LoginRespository(application);
        tokenManager = new TokenManager(application);

    }
    public LiveData<String> login(LoginRequest loginRequest){
        LiveData<String> token =loginRespository.login(loginRequest);
        token.observeForever(token1 ->{
            if (token1 != null){
                tokenManager.saveToken(token1);
            }
        });
        return loginRespository.login(loginRequest);
    }
    public String getToken(){
        return tokenManager.getToken();
    }

}
