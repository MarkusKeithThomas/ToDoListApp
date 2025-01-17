package thuong.todolist;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import thuong.todolist.databinding.ActivityAddToDoListBinding;
import thuong.todolist.helper.TokenManager;
import thuong.todolist.request.ToDoListRequest;
import thuong.todolist.viewmodel.ToDoListViewModel;
import thuong.todolist.viewmodelfactory.ToDoListViewModelFactory;

public class AddToDoListActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private TextView toolbarTime;
    private ProgressBar progressBar;
    private ImageView imgTask;
    private Button btnSave;
    private ToDoListViewModel toDoListViewModel;
    private File file;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityAddToDoListBinding activityToDoListBinding;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_add_to_do_list);
        activityToDoListBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_to_do_list);
        initViews();
        initToolbar();
        initViewModel();
        checkPermissions();
        setupImagePicker();

        btnSave.setOnClickListener(view ->{
            if (uploadToServer()){
                Intent intent = new Intent(this,ToDoListActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initViews() {
        imgTask = findViewById(R.id.image_task);
        btnSave = findViewById(R.id.btnsave_backhome);
        toolbarTime = findViewById(R.id.toolbar_time);
        progressBar = findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.INVISIBLE);
        tokenManager = new TokenManager(this);

        // Hiển thị ngày hiện tại
        String currentDate = getCurrentDate();
        toolbarTime.setText(currentDate);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_saler);
        setSupportActionBar(toolbar);
    }

    private void initViewModel() {
        ToDoListViewModelFactory factory = new ToDoListViewModelFactory(tokenManager.getToken());
        toDoListViewModel = new ViewModelProvider(this, factory).get(ToDoListViewModel.class);
        activityToDoListBinding.setViewmodelitem(toDoListViewModel);
        activityToDoListBinding.setLifecycleOwner(this);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        }
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        imgTask.setImageURI(selectedImageUri);
                        file = createTempFileFromUri(selectedImageUri);
                    }
                }
        );

        imgTask.setOnClickListener(v -> openImagePicker());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private boolean uploadToServer() {
        boolean isSuccess = false;
        if (file == null) {
            return isSuccess;
        }
        ToDoListRequest toDoListRequest = toDoListViewModel.getToDoListRequestLiveData().getValue();
        toDoListRequest.setDateCreate(LocalDate.now());
        if (toDoListRequest != null){
            obserError();
            if(toDoListViewModel.isValidToDoListRequest(toDoListRequest,file)){
                try {
                    toDoListViewModel.uploadToDoList(toDoListRequest, file);
                    isSuccess = true;
                } catch (Exception e) {
                }
            }
        }
        return isSuccess;
    }

    private void obserError(){
        toDoListViewModel.getErrorTitle().observe(this,item -> activityToDoListBinding.errorTitle.setText(item));
        toDoListViewModel.getErrorContent().observe(this,item -> activityToDoListBinding.errorContent.setText(item));
        toDoListViewModel.getErrorimage().observe(this,item -> activityToDoListBinding.errorImage.setText(item));
    }

    private File createTempFileFromUri(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            String uniqueFileName = "image_" + System.currentTimeMillis() + ".jpg";
            File tempFile = new File(getCacheDir(), uniqueFileName);
            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }
            return tempFile;
        } catch (Exception e) {
            Log.e("FileAccess", "Lỗi khi tạo file tạm: " + e.getMessage());
            return null;
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(Calendar.getInstance().getTime());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "Quyền truy cập bộ nhớ đã được cấp!");
            } else {
                Log.e("Permissions", "Quyền truy cập bộ nhớ bị từ chối!");
            }
        }
    }
}