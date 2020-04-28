package app.project.wishwash.chat.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

//BaseViewholder is an abstract template class which makes it possible to have two viewholders in a recyclerview
public abstract class BaseMessageViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseMessageViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public abstract void bind(T viewType);
}
