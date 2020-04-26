package app.project.wishwash.chat.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;


import app.project.wishwash.R;
import app.project.wishwash.chat.models.GuestUser;

public class GuestMessageViewHolder extends BaseMessageViewHolder<GuestUser> {
    private TextView name;
    private TextView text;

    public GuestMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.guest_name);
        text = itemView.findViewById(R.id.guest_text);
    }

    @Override
    public void bind(GuestUser user) {
        name.setText(user.getName());
        text.setText(user.message.getMessage());
    }
}
