package thuong.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import thuong.todolist.adapter.ToDoListAdapter;
import thuong.todolist.dialogfragment.DetailDialogFragment;
import thuong.todolist.helper.TokenManager;
import thuong.todolist.interfaceclick.ClickInterface;
import thuong.todolist.model.ToDoListItem;
import thuong.todolist.response.BaseResponse;
import thuong.todolist.useraction.SwipeToDeleteCallback;
import thuong.todolist.viewmodel.ToDoListViewModel;
import thuong.todolist.viewmodelfactory.ToDoListViewModelFactory;

public class ToDoListActivity extends AppCompatActivity {
    private TextView toolbarTime;
    private ToDoListViewModel toDoListViewModel;
    private FloatingActionButton fab;
    private ToDoListAdapter toDoListAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swip;
    private TokenManager tokenManager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        tokenManager = new TokenManager(this);

        fab = findViewById(R.id.fabBuyer);
        // Tìm Toolbar và thiết lập
        Toolbar toolbar = findViewById(R.id.toolbar_saler);
        // Lấy header view từ NavigationView
        setSupportActionBar(toolbar);
        // Tìm TextView để hiển thị thời gian
        toolbarTime = findViewById(R.id.toolbar_time);
        // Sử dụng SimpleDateFormat để định dạng ngày tháng năm
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String currentDate = sdf.format(Calendar.getInstance().getTime()); // Lấy thời gian hiện tại
        // Hiển thị thời gian lên TextView
        toolbarTime.setText(currentDate);
        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Tìm kiếm công việc ...");

        //ViewModel
        ToDoListViewModelFactory factory = new ToDoListViewModelFactory(tokenManager.getToken());
        toDoListAdapter = new ToDoListAdapter(new ArrayList<>(),this);
        recyclerView = findViewById(R.id.recyclerView_to_do_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(toDoListAdapter);
        toDoListViewModel = new ViewModelProvider(this,factory).get(ToDoListViewModel.class);
        swip = findViewById(R.id.swipeRefreshLayout);

        toDoListViewModel.getToDoListLiveData().observe(this, new Observer<List<ToDoListItem>>() {
            @Override
            public void onChanged(List<ToDoListItem> toDoListItems) {
                Log.d("ToDoList123", "Received data: " + toDoListItems);
                if (toDoListItems != null && !toDoListItems.isEmpty()) {
                    List<ToDoListItem> newList = new ArrayList<>();
                    for (int i=toDoListItems.size(); i>0; i--){
                        ToDoListItem toDoListItem = toDoListItems.get(i-1);
                        toDoListItem.setSubDescription(subString(toDoListItem.getDescription())+" ...");
                        newList.add(toDoListItem);
                    }
                    toDoListAdapter.updateTicketList(newList);
                }
            }
        });
        fab.setOnClickListener(v -> {
            Intent intent1 = new Intent(this,AddToDoListActivity.class);
            startActivity(intent1);

        });
        swip.setOnRefreshListener(() ->{
            toDoListViewModel.getToDoListLiveData().observe(this, toDoListItems -> {
                List<ToDoListItem> newList = new ArrayList<>();
                for (int i=toDoListItems.size(); i>0; i--){
                    ToDoListItem toDoListItem = toDoListItems.get(i-1);
                    toDoListItem.setSubDescription(subString(toDoListItem.getDescription())+" ...");
                    newList.add(toDoListItem);
                }
                toDoListAdapter.updateTicketList(newList);
                swip.setRefreshing(false);
            });
        });
        //Xoa mot item
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(toDoListAdapter, toDoListAdapter.getContext(),toDoListViewModel);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Lắng nghe sự thay đổi trong nội dung tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Người dùng nhấn Enter
                performSearch(query); // Gọi hàm tìm kiếm
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Nội dung thay đổi khi người dùng nhập
                performSearch(newText); // Cập nhật tìm kiếm ngay lập tức
                return true;
            }
        });



    }
    private String subString(String item){
        String result = item.length() > 25 ? item.substring(0, 25) : item;
    return result;
    }
    // Hàm tìm kiếm
    private void performSearch(String query) {
        List<ToDoListItem> filteredList = new ArrayList<>();
        toDoListViewModel.getToDoListLiveData().observe(this, new Observer<List<ToDoListItem>>() {
            @Override
            public void onChanged(List<ToDoListItem> toDoListItems) {
                ToDoListItem toDoListItem = new ToDoListItem();
                for (ToDoListItem item : toDoListItems) {
                    if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        toDoListItem = item;
                        toDoListItem.setSubDescription(subString(item.getDescription()));
                        filteredList.add(toDoListItem);
                    }
                }
                toDoListAdapter.updateTicketList(filteredList);
            }
        });

    }
}