package app.project.wishwash.chat.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import abdul.firebase.chat.ViewHolders.BaseMessageViewHolder;

public class OwnerMessageViewHolder extends BaseMessageViewHolder<OwnerUser> {
    private TextView name;
    private TextView text;
    public OwnerMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.owner_name);
        text = itemView.findViewById(R.id.owner_text);
    }

    @Override
    public void bind(OwnerUser user) {
        name.setText(user.getName());
        text.setText(user.message.getMessage());
    }
}
