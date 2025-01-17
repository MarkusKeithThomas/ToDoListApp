package thuong.todolist.dialogfragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

import thuong.todolist.R;
import thuong.todolist.model.ToDoListItem;

public class DetailDialogFragment extends DialogFragment {

    private ToDoListItem toDoListItem;

    public DetailDialogFragment(ToDoListItem toDoListItem) {
        this.toDoListItem = toDoListItem;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Inflate layout
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_todo_detail, null);
        dialog.setContentView(view);

        // Bind views
        ImageView dialogImageDetail = view.findViewById(R.id.dialogImageDetail);
        TextView dialogTitleDetail = view.findViewById(R.id.dialogTitleDetail);
        TextView dialogDescriptionDetail = view.findViewById(R.id.dialogDescriptionDetail);
        Button dialogButtonClose = view.findViewById(R.id.dialogButtonClose);

        // Set data
        dialogTitleDetail.setText(toDoListItem.getTitle());
        dialogDescriptionDetail.setText(toDoListItem.getDescription());
        Glide.with(requireContext())
                .load(toDoListItem.formatImageUrl(toDoListItem.getImagePath()))
                .placeholder(R.drawable.registerimg)
                .error(R.drawable.baseline_terrain_24)
                .into(dialogImageDetail);

        // Close button
        dialogButtonClose.setOnClickListener(v -> dismiss());

        return dialog;
    }
}
