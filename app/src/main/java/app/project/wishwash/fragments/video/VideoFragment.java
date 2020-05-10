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

//Inspiration source: https://pierfrancescosoffritti.github.io/android-youtube-player/ & https://github.com/PierfrancescoSoffritti/android-youtube-player#download
// originally Google's Youtube API was intended to be used, but was not supported with never software updates, hence the use of the new API.
public class VideoFragment extends Fragment {
    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    private List<Video> videoList = new ArrayList<>();
    private YouTubePlayer youTubePlayer;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video,container,false);
        Context context = view.getContext();

        final YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtubePlayer);
        videoRecyclerView = view.findViewById(R.id.recyclerView);

        getLifecycle().addObserver(youTubePlayerView);

        populateVideoList();

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                VideoFragment.this.youTubePlayer = youTubePlayer;
                // Playing random video when opening
                Random random = new Random();
                int randomInt = random.nextInt(videoList.size());
                youTubePlayer.loadVideo(videoList.get(randomInt).getLink(), 0);
            }
        });

        // Recycler for showing available videos
        videoRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        videoRecyclerView.setLayoutManager(layoutManager);
        videoAdapter = new VideoAdapter(videoList);
        videoRecyclerView.setAdapter(videoAdapter);

        // When clinking on a video, pick the right link and loads it using the youtube player
        videoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) throws InterruptedException {

                youTubePlayer.loadVideo(videoList.get(position).getLink(), 0);
            }
        });

        return view;
    }

    // Populate Recycler with all available videos
    private void populateVideoList() {
        String[] titles = getResources().getStringArray(R.array.video_title);
        String[] links = getResources().getStringArray(R.array.video_link);

        for(int i = 0 ; i < titles.length ; i++){
            Video video = new Video(titles[i], links[i]);
            videoList.add(video);
        }
    }
}
