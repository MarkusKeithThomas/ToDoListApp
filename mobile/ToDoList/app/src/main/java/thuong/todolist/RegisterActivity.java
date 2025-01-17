package thuong.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import thuong.todolist.databinding.ActivityRegisterBinding;
import thuong.todolist.request.RegisterRequest;
import thuong.todolist.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding activityRegisterBinding;
    private RegisterViewModel registerViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        activityRegisterBinding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        activityRegisterBinding.setViewmodel(registerViewModel);
        activityRegisterBinding.setLifecycleOwner(this);

        //Quan sat cac loi co ban
        observeError();

        activityRegisterBinding.btnRegister.setOnClickListener(v->{
            RegisterRequest request = registerViewModel.getRegisterLive().getValue();
            if (request != null){
                if (registerViewModel.validateUserInput(request)){
                    registerViewModel.register(request).observe(this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        }
                    });
                }

            }

        });
        activityRegisterBinding.tvLogin.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        });
    }
    private void observeError(){
        registerViewModel.getNameError().observe(this,item -> activityRegisterBinding.errorName.setText(item));
        registerViewModel.getEmailError().observe(this,item -> activityRegisterBinding.errorEmail.setText(item));
        registerViewModel.getPasswordError().observe(this,item -> activityRegisterBinding.errorPassword.setText(item));
        registerViewModel.getConfirmPasswordError().observe(this,item -> activityRegisterBinding.errorRepassword.setText(item));
    }
}