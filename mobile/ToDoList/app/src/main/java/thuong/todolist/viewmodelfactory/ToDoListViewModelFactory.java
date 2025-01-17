package thuong.todolist.viewmodelfactory;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import thuong.todolist.viewmodel.ToDoListViewModel;

public class ToDoListViewModelFactory implements ViewModelProvider.Factory {
    private final String token;

    public ToDoListViewModelFactory(String token) {
        this.token = token;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ToDoListViewModel.class)) {
            Log.d("ToDoListViewModelFactory",token +" gia tri");
            return (T) new ToDoListViewModel(token);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
