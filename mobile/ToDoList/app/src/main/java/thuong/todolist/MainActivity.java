package thuong.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import thuong.todolist.request.LoginRequest;
import thuong.todolist.service.APIService;
import thuong.todolist.viewmodel.LoginViewModel;

public class MainActivity extends AppCompatActivity {
    private EditText edtEmail,edtPassword;
    private TextView txtPassword,btnsignUp;
    private Button btnLogin;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnSignIn);
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        txtPassword = findViewById(R.id.error_user_signin);
        btnsignUp = findViewById(R.id.sign_up);


        btnLogin.setOnClickListener(view ->{
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(edtEmail.getText().toString().trim());
            loginRequest.setPassword(edtPassword.getText().toString().trim());
            loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
            loginViewModel.login(loginRequest).observe(this, token ->{
                if (token != null){
                    Intent intent = new Intent(this,ToDoListActivity.class);
                    startActivity(intent);
                } else {
                    txtPassword.setText("Vui lòng kiểm tra lại email hoặc mật khẩu.");
                }
            });
        });

        btnsignUp.setOnClickListener(view ->{
            startActivity(new Intent(this,RegisterActivity.class));
        });
    }
}