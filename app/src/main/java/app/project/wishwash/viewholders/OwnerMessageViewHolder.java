package app.project.wishwash.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.project.wishwash.R;
import app.project.wishwash.models.Message;

public class OwnerMessageViewHolder extends BaseMessageViewHolder<Message> {
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private TextView name;
    private TextView text;
    public OwnerMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.owner_name);
        text = itemView.findViewById(R.id.owner_text);
    }

    @Override
    public void bind(Message message) {
        name.setText(firebaseUser.getDisplayName());
        text.setText(message.getMessage());
    }
}
