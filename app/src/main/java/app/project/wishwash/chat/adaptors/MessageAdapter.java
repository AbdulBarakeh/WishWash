package app.project.wishwash.chat.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import app.project.wishwash.R;
import app.project.wishwash.chat.models.GuestUser;
import app.project.wishwash.chat.models.Message;
import app.project.wishwash.chat.models.OwnerUser;
import app.project.wishwash.chat.viewholders.BaseMessageViewHolder;
import app.project.wishwash.chat.viewholders.GuestMessageViewHolder;
import app.project.wishwash.chat.viewholders.OwnerMessageViewHolder;

// SOURCE: https://medium.com/@gilbertchristopher/a-recyclerview-with-multiple-view-type-22619a5ad365
public class MessageAdapter extends RecyclerView.Adapter<BaseMessageViewHolder> {
    private static final int TYPE_GUEST = 0;
    private static final int TYPE_OWNER = 1;
    private List<Message> data;
    private Context context;
    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Message> data){
        this.context = context;
        this.data = data;
        notifyDataSetChanged();
    }
    public void updateMessages(List<Message> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_GUEST){
            View view = LayoutInflater.from(context).inflate(R.layout.guest_chat,parent,false);
            return new GuestMessageViewHolder(view);
        }
        else if (viewType == TYPE_OWNER){
            View view = LayoutInflater.from(context).inflate(R.layout.owner_chat,parent,false);
            return new OwnerMessageViewHolder(view);
        }
        else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMessageViewHolder holder , int position) {
        Message message = data.get(position);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference user1Reference = FirebaseDatabase.getInstance().getReference("Users").child(message.getReceiver());
//        DatabaseReference user2Reference = FirebaseDatabase.getInstance().getReference("Users").child(message.getSender());
        if (currentUser.getUid().equals(message.getSender().getUserId())){
            holder = new OwnerMessageViewHolder(holder.itemView);
            holder.bind(message);
            //OWNER VIEW

        }
        if (!currentUser.getUid().equals(message.getSender().getUserId())){
            holder = new GuestMessageViewHolder(holder.itemView);
            holder.bind(message);
            //GUEST
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (data.get(position).getSender().getUserId().equals(firebaseUser.getUid())){
            return TYPE_OWNER;
        }
        else if (data.get(position).getReceiver().getUserId().equals(firebaseUser.getUid())){
            return TYPE_GUEST;
        }
        else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
}

