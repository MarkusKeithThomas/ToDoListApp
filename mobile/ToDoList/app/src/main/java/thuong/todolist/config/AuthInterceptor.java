package thuong.todolist.config;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private String authToken;
    public AuthInterceptor(String authToken){
        this.authToken = authToken;
    }


    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder()
                .header("Authorization","Bearer " + authToken);
        Log.d("AuthInterceptor", "Token: " + authToken);
        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }

}
