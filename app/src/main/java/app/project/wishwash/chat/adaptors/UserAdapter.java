package app.project.wishwash.chat.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.project.wishwash.R;
import app.project.wishwash.chat.models.User;
import app.project.wishwash.chat.viewholders.UserViewHolder;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    //Inspiration: Assignment 2 by AU590917
    private List<User> users = new ArrayList<>();
    private OnItemClickListener CardClickListener;
    private Context context;

    public UserAdapter(Context context, List<User> users){
        this.context = context;
        this.users = users;
        notifyDataSetChanged();
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    //Setting up clicklistener for every card
    public void setOnItemClickListener(OnItemClickListener listener){
        CardClickListener = listener;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        UserViewHolder VH = new UserViewHolder(view,CardClickListener);
        return VH;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder , int position) {
        User currentUser = users.get(position);
        holder.picture.setImageResource(R.drawable.guest_24dp);
        holder.name.setText(currentUser.getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
