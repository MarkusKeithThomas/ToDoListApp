package thuong.todolist.useraction;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import thuong.todolist.R;
import thuong.todolist.adapter.ToDoListAdapter;
import thuong.todolist.model.ToDoListItem;
import thuong.todolist.viewmodel.ToDoListViewModel;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private ToDoListAdapter adapter;
    private Drawable icon;
    private final ColorDrawable background;
    private ToDoListViewModel toDoListViewModel;

    public SwipeToDeleteCallback(ToDoListAdapter adapter, Context context,ToDoListViewModel toDoListViewModel) {
        super(0, ItemTouchHelper.LEFT); // Swipe từ phải sang trái
        this.adapter = adapter;
        this.icon = ContextCompat.getDrawable(context, R.drawable.ic_delete); // Icon delete
        this.background = new ColorDrawable(Color.RED); // Nền đỏ khi kéo
        this.toDoListViewModel = toDoListViewModel;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false; // Không hỗ trợ di chuyển item
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            ToDoListItem toDoListItem = adapter.getToDoListItem(position);
            showDeleteConfirmationDialog(() ->{
                toDoListViewModel.deleteToDoList(toDoListItem.getId()).observe((LifecycleOwner) adapter.getContext(), baseResponse -> {
                    if (baseResponse.getCode() == 200 && baseResponse != null){
                        adapter.removeItem(position);
                    } else {
                        Log.e("SwipeToDeleteCallback", "Xóa thất bại!");
                        // Khôi phục item nếu xóa thất bại
                        adapter.notifyItemChanged(position);
                    }
                });
            });

        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        // Hiển thị nền đỏ
        background.setBounds(
                itemView.getRight() + (int) dX - backgroundCornerOffset,
                itemView.getTop(),
                itemView.getRight(),
                itemView.getBottom()
        );
        background.draw(c);

        // Hiển thị icon xóa
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + iconMargin;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
        int iconRight = itemView.getRight() - iconMargin;
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        icon.draw(c);
    }
    private void showDeleteConfirmationDialog(Runnable onConfirm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa mục này không?");

        // Nút "Xóa"
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            dialog.dismiss();
            onConfirm.run(); // Thực hiện hành động xóa nếu người dùng xác nhận
        });

        // Nút "Hủy"
        builder.setNegativeButton("Hủy", (dialog, which) ->{
            dialog.dismiss();
            adapter.justUpdateUI();
        });

        // Hiển thị Dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
