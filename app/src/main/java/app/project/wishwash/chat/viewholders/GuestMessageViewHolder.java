package app.project.wishwash.chat.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import app.project.wishwash.R;
import app.project.wishwash.chat.models.Message;

//BaseViewholder is an abstract template class which makes it possible to have two viewholders in a recyclerview
public class GuestMessageViewHolder extends BaseMessageViewHolder<Message> {
    private TextView name;
    private TextView text;

    public GuestMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.guest_name);
        text = itemView.findViewById(R.id.guest_text);
    }

    @Override
    public void bind(Message message) {
        name.setText(message.getSender().getName());
        text.setText(message.getMessage());
    }
}

