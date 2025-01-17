package thuong.todolist.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import thuong.todolist.repository.RegisterRepository;
import thuong.todolist.request.RegisterRequest;

public class RegisterViewModel extends ViewModel {
    private RegisterRepository registerRepository;
    private MutableLiveData<RegisterRequest> registerMutableLiveData = new MutableLiveData<>(new RegisterRequest());
    private final MutableLiveData<String> nameError = new MutableLiveData<>();
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<String> passwordError = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPasswordError = new MutableLiveData<>();


    public RegisterViewModel() {
        registerRepository = new RegisterRepository();
    }
    public LiveData<RegisterRequest> getRegisterLive() {
        return registerMutableLiveData;
    }
    public LiveData<String> register(RegisterRequest register) {
            return registerRepository.register(register);
    }
    // Getter cho `user` để DataBinding có thể sử dụng
    public void createUser(RegisterRequest register) {
        if (register != null && validateUserInput(register)){
            registerRepository.register(register);
        }
    }
    // Getters for each error
    public LiveData<String> getNameError() {
        return nameError;
    }

    public LiveData<String> getEmailError() {
        return emailError;
    }


    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    public LiveData<String> getConfirmPasswordError() {
        return confirmPasswordError;
    }
    // Method to validate user input and set error messages
    public boolean validateUserInput(RegisterRequest request) {
        boolean isValid = false;

        if (request.getUserName() == null || request.getUserName().isEmpty()) {
            nameError.setValue("Vui lòng điền tên.");
            return isValid = false;
        } else {
            nameError.setValue(null);  // Clear error if valid
            isValid = true;
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            emailError.setValue("Vui lòng điền email.");
            return isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(request.getEmail()).matches()) {
            emailError.setValue("Email không đúng định dạng.");
            return isValid = false;
        } else {
            emailError.setValue(null);
            isValid = true;

        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            passwordError.setValue("Vui lòng điền mật khẩu.");
            return isValid = false;
        } else if (request.getPassword().length() < 6) {
            passwordError.setValue("Mật khẩu phải ít nhất 6 ký tự.");
            return isValid = false;
        } else {
            passwordError.setValue(null);
            isValid = true;

        }

        if (request.getRe_password() == null  )
        {
            confirmPasswordError.setValue("Mật khẩu xác nhận cần điền.");
            return isValid = false;
        } else if (request.getRe_password().equals(request.getPassword()) && request.getRe_password() != null) {
            confirmPasswordError.setValue("Mật khẩu xác nhận cần giống nhau");
            return isValid = false;
        } else {
            confirmPasswordError.setValue(null);
            isValid = true;

        }

        return isValid;
    }
}
