package thuong.todolist.diff;

import androidx.recyclerview.widget.DiffUtil;
import java.util.List;
import thuong.todolist.model.ToDoListItem;

public class ToDoListDiff extends DiffUtil.Callback {
    private final List<ToDoListItem> oldList;
    private final List<ToDoListItem> newList;

    public ToDoListDiff(List<ToDoListItem> oldList, List<ToDoListItem> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // So sánh ID của hai mục để xác định xem chúng có phải cùng một đối tượng không
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // So sánh toàn bộ nội dung
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}