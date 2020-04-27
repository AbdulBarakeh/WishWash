package app.project.wishwash.chat.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.project.wishwash.R;
import app.project.wishwash.chat.adaptors.UserAdapter;


public class UserViewHolder extends RecyclerView.ViewHolder {
    public ImageView picture;
    public TextView name;
    public UserViewHolder(@NonNull View itemView, final UserAdapter.OnItemClickListener listener) {
        super(itemView);
        picture = itemView.findViewById(R.id.PicOfUser);
        name = itemView.findViewById(R.id.NameOfUser);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }

}
