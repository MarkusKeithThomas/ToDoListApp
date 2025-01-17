package thuong.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import thuong.todolist.R;
import thuong.todolist.databinding.ListTodolistBinding;
import thuong.todolist.dialogfragment.DetailDialogFragment;
import thuong.todolist.diff.ToDoListDiff;
import thuong.todolist.interfaceclick.ClickInterface;
import thuong.todolist.model.ToDoListItem;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {
    List<ToDoListItem> toDoListItemList;
    private ClickInterface clickInterface;
    private Context context;
    public ToDoListAdapter (List<ToDoListItem> toDoListItem, Context context){
        this.toDoListItemList = toDoListItem;
        this.context = context;
    }


    @NonNull
    @Override
    public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListTodolistBinding listTodolistBinding = ListTodolistBinding.inflate(layoutInflater,parent,false);
        return new ToDoListViewHolder(listTodolistBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListViewHolder holder, int position) {
        ToDoListItem toDoListItem = toDoListItemList.get(position);
        holder.bind(toDoListItem);


    }

    @Override
    public int getItemCount() {
        return toDoListItemList.size();
    }
    public void updateTicketList(List<ToDoListItem> newTicketInformationList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ToDoListDiff(this.toDoListItemList, newTicketInformationList));
        this.toDoListItemList.clear();
        this.toDoListItemList.addAll(newTicketInformationList);
        // Sử dụng DiffUtil để chỉ cập nhật những item thay đổi
        diffResult.dispatchUpdatesTo(this);
    }

    public class ToDoListViewHolder extends RecyclerView.ViewHolder{

        private ListTodolistBinding listTodolistBinding;
        public ToDoListViewHolder(ListTodolistBinding listTodolistBinding) {
            super(listTodolistBinding.getRoot());
            this.listTodolistBinding = listTodolistBinding;
            listTodolistBinding.getRoot().setOnClickListener(view ->{
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ToDoListItem item = toDoListItemList.get(position);

                    // Hiển thị dialog
                    DetailDialogFragment dialogFragment = new DetailDialogFragment(item);
                    if (context instanceof AppCompatActivity) {
                        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "DetailDialog");
                    }
                }
            });

        }
        public void bind(ToDoListItem toDoListItem){
            listTodolistBinding.setTodolist(toDoListItem);
            String formatURL = toDoListItem.formatImageUrl(toDoListItem.getImagePath());
            // Sử dụng Glide để tải ảnh vào ImageView
            Glide.with(itemView.getContext())
                    .load(formatURL)
                    .fitCenter()
                    .placeholder(R.drawable.registerimg)
                    .error(R.drawable.baseline_terrain_24)
                    .into(listTodolistBinding.imageTicket);

            // Thực hiện binding
            listTodolistBinding.executePendingBindings();
        }

    }

    public Context getContext() {
        return context;
    }
    public ToDoListItem getToDoListItem(int position) {
        return toDoListItemList.get(position);
    }

    public void removeItem(int position) {
        toDoListItemList.remove(position);
        notifyItemRemoved(position);
    }
    public void justUpdateUI(){
        notifyDataSetChanged();
    }
    public void setClickInterface(ClickInterface clickInterface){
        this.clickInterface = clickInterface;
    }

}
