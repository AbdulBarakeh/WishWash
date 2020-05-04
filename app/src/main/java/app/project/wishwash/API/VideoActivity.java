package app.project.wishwash.API;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

import app.project.wishwash.R;

public class VideoActivity extends YouTubeBaseActivity {

    private Button playVideoBtn;
    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private String VIDEO_URL = "hc5h5pT4sVA";

//    private final String YOUTUBE_API_KEY = "AIzaSyCUQ57wV7kkQZOKKootla_jkTwJmgTSnwE";
    private final String YOUTUBE_API_KEY = "AIzaSyBBD-bfV06com_AUtQpU0x8tf-j8S1IkKg";
    private ArrayList<Video> videoList;

    private YouTubePlayer youTubePlayer;
    private boolean firstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        youTubePlayerView = findViewById(R.id.youtubePlayer);
        videoRecyclerView = findViewById(R.id.recyclerView);
        //videoRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        videoRecyclerView.setLayoutManager(layoutManager);

        videoList = new ArrayList<Video>();
        Video video = new Video("MC", "hc5h5pT4sVA");
        Video video1 = new Video("MC crash", "LcU4acbU31U");
        videoList.add(video);
        videoList.add(video1);
        videoAdapter = new VideoAdapter(videoList);
        videoRecyclerView.setAdapter(videoAdapter);


        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                VideoActivity.this.youTubePlayer = youTubePlayer;
                youTubePlayer.loadVideo(GetURL());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        };

        videoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SetURL(videoList.get(position).getLink());
                if(firstRun) {
                    youTubePlayerView.initialize(YOUTUBE_API_KEY, onInitializedListener);
                    firstRun = false;
                }else{
                    youTubePlayer.loadVideo(GetURL());
                }
            }
        });

        playVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstRun)
                    youTubePlayerView.initialize(YOUTUBE_API_KEY, onInitializedListener);
            }
        });
    }

    private String GetURL(){ return VIDEO_URL; }
    private void SetURL(String URL){ this.VIDEO_URL = URL; }
}
