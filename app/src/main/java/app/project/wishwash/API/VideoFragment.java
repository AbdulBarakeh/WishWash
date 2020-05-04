//package app.project.wishwash.API;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import com.google.android.youtube.player.YouTubeInitializationResult;
//import com.google.android.youtube.player.YouTubePlayer;
//import com.google.android.youtube.player.YouTubePlayerSupportFragment;
//import com.google.android.youtube.player.YouTubePlayerView;
//
//import java.util.ArrayList;
//
//import app.project.wishwash.R;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link VideoFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class VideoFragment extends Fragment {
//    private Button playVideoBtn;
//    private RecyclerView videoRecyclerView;
//    private VideoAdapter videoAdapter;
//    private YouTubePlayerView youTubePlayerView;
//    private YouTubePlayer.OnInitializedListener onInitializedListener;
//    private String VIDEO_URL = "hc5h5pT4sVA";
//
//    private final String YOUTUBE_API_KEY = "AIzaSyCUQ57wV7kkQZOKKootla_jkTwJmgTSnwE";
//    private ArrayList<Video> videoList;
//
//    private YouTubePlayer youTubePlayer;
//    private boolean firstRun = true;
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
////    private static final String ARG_PARAM1 = "param1";
////    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
////    private String mParam1;
////    private String mParam2;
//
//    public VideoFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
////     * @param param1 Parameter 1.
////     * @param param2 Parameter 2.
//     * @return A new instance of fragment VideoFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static Fragment newInstance() {
//        Fragment fragment = new Fragment();
//
////        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1 , param1);
////        args.putString(ARG_PARAM2 , param2);
////        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
////        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
//
//        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//        youTubePlayerFragment.initialize(YOUTUBE_API_KEY, onInitializedListener);
//        View view = inflater.inflate(R.layout.activity_video, container, false);
//        Context context = view.getContext();
//        youTubePlayerView = view.findViewById(R.id.youtubePlayer);
//        playVideoBtn = view.findViewById(R.id.playVideoBtn);
//        videoRecyclerView = view.findViewById(R.id.recyclerView);
//        //videoRecyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
//        videoRecyclerView.setLayoutManager(layoutManager);
//
//        videoList = new ArrayList<Video>();
//        Video video = new Video("MC", "hc5h5pT4sVA");
//        Video video1 = new Video("MC crash", "LcU4acbU31U");
//        videoList.add(video);
//        videoList.add(video1);
//        videoAdapter = new VideoAdapter(videoList);
//        videoRecyclerView.setAdapter(videoAdapter);
//
//
//        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                VideoFragment.this.youTubePlayer = youTubePlayer;
//                youTubePlayer.loadVideo(GetURL());
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//            }
//        };
//
//        videoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                SetURL(videoList.get(position).getLink());
//                if(firstRun) {
//                    youTubePlayerView.initialize(YOUTUBE_API_KEY, onInitializedListener);
//                    firstRun = false;
//                }else{
//                    youTubePlayer.loadVideo(GetURL());
//                }
//            }
//        });
//
//        playVideoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(firstRun)
//                    youTubePlayerView.initialize(YOUTUBE_API_KEY, onInitializedListener);
//            }
//        });
//
//        return view;
//
//    }
//    private String GetURL(){ return VIDEO_URL; }
//    private void SetURL(String URL){ this.VIDEO_URL = URL; }
//}
