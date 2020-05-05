package app.project.wishwash.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.project.wishwash.R;
import app.project.wishwash.models.Video;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{

    private List<Video> videoList;
    private OnItemClickListener itemClickListener;

    public VideoAdapter(List<Video> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    public void setVideo(ArrayList<Video> videoList){
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, itemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        holder.videoTextView.setText(videoList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position) throws InterruptedException;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView videoTextView;

        public ViewHolder(@NonNull View RecyclerVideo, final OnItemClickListener listener) {
            super(RecyclerVideo);

            videoTextView = RecyclerVideo.findViewById(R.id.videoTextView);

            RecyclerVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            try {
                                listener.onItemClick(position);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }
}
