package app.project.wishwash.fragments.video;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.project.wishwash.models.Video;
import app.project.wishwash.adaptors.VideoAdapter;
import app.project.wishwash.R;


public class NewVideoFragment extends Fragment {
    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    private List<Video> videoList = new ArrayList<>();
    private YouTubePlayer youTubePlayer;

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

        final YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtubePlayer);
        videoRecyclerView = view.findViewById(R.id.recyclerView);

        getLifecycle().addObserver(youTubePlayerView);

        populateVideoList();

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                NewVideoFragment.this.youTubePlayer = youTubePlayer;
                Random random = new Random();
                int randomInt = random.nextInt(videoList.size());
                youTubePlayer.loadVideo(videoList.get(randomInt).getLink(), 0);
            }
        });

        videoRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        videoRecyclerView.setLayoutManager(layoutManager);
        videoAdapter = new VideoAdapter(videoList);
        videoRecyclerView.setAdapter(videoAdapter);

        videoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) throws InterruptedException {

                youTubePlayer.loadVideo(videoList.get(position).getLink(), 0);
            }
        });

        return view;
    }

    private void populateVideoList() {
        String[] titles = getResources().getStringArray(R.array.video_title);
        String[] links = getResources().getStringArray(R.array.video_link);

        for(int i = 0 ; i < titles.length ; i++){
            Video video = new Video(titles[i], links[i]);
            videoList.add(video);
        }
    }
}
