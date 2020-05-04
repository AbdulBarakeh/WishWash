package app.project.wishwash.API;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import app.project.wishwash.R;


public class NewVideoFragment extends Fragment {
    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    List<Video> videoList = new ArrayList<>();

    public NewVideoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewVideoFragment newInstance() {
        NewVideoFragment fragment = new NewVideoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_video,container,false);
        Context context = view.getContext();
        YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtubePlayer);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo("hc5h5pT4sVA",0);
            }
        });

        videoRecyclerView = view.findViewById(R.id.recyclerView);
        videoRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        videoRecyclerView.setLayoutManager(layoutManager);
        videoAdapter = new VideoAdapter(videoList);
        videoRecyclerView.setAdapter(videoAdapter);
        return view;
    }
}
