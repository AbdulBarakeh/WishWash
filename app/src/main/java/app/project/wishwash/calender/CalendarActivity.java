package app.project.wishwash.calender;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import app.project.wishwash.API.VideoFragment;
import app.project.wishwash.Booking;
import app.project.wishwash.R;
import app.project.wishwash.booking.BookingFragment;
import app.project.wishwash.booking.NewBookingFragment;
import app.project.wishwash.chat.ChatFragment;
import app.project.wishwash.chat.fragments.UserListFragment;
import app.project.wishwash.chat.models.User;
import app.project.wishwash.API.TipsFragment;

public class CalendarActivity extends AppCompatActivity implements CalendarFragment.CalendarFragmentListener, BookingFragment.OnListFragmentInteractionListener, UserListFragment.UserListFragmentListener {

    private BottomNavigationView bottomNavigationView;
    private Toolbar actionBar;
    private final String TAG = "CalendarActivity";
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private SharedPreferences sp;
    private CalendarFragment calendarFragment;
    private ChatFragment newChatFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_calendar);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //placed at the end of the function to ensure that the object has time to update before being accessed
        validateUserExistence(currentUser);

        calendarFragment = new CalendarFragment();

        actionBar = findViewById(R.id.action_bar);
        bottomNavigationView = findViewById(R.id.BottomNavigation_Calendar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        setSupportActionBar(actionBar);

        initCalendarFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
    }

    // Inflates --- CalendarFragment/fragment_calendar --- as the first fragment to be shown when app is started
    private void initCalendarFragment() {
        Fragment fragment = new CalendarFragment();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameLayout_Calendar, fragment);
        transaction.commit();
    }

    // When BottomNavigationView item is clicked, inflate belonging fragment.
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.Navigation_Calendar:
                            openFragment(CalendarFragment.newInstance("", ""));
                            return true;
                        case R.id.Navigation_Bookings:
                            openFragment(BookingFragment.newInstance());
                            return true;
                        case R.id.Navigation_Chat:
                            openFragment(UserListFragment.newInstance());
                            return true;
                        case R.id.Navigation_Tips:
                            openFragment(VideoFragment.newInstance());
                            return true;
                    }
                    return false;
                }
            };

    public void openFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameLayout_Calendar, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // To communicate from Fragment to Fragment through Activity
    @Override
    public void onDateChosen(Calendar c) {
        openFragment(NewBookingFragment.newInstance(c.YEAR, c.MONTH, c.DAY_OF_MONTH));
    }



    @Override
    public void onUserSent(User user) {
        //        chatFragment.getUser(user);
//        ChatFragment chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.chat_constraint);//Didn't accept R.layout.activity_chat
//        openFragment(ChatFragment.newInstance(user.getUserId(),user.getUserName()));
        newChatFragment = new ChatFragment();
        newChatFragment.getUser(user);
        newChatFragment.newInstance(user.getUserId(),user.getUserName());
        openFragment(newChatFragment);
    }
    private void addUserToDB(User user){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userId", user.getUserId());
        userMap.put("userName", user.getUserName());
        dbRef.child("users").push().setValue(userMap);
    }
    private void validateUserExistence(final FirebaseUser user){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        String currentUserId = user.getUid();
        dbRef.child("users").orderByChild("userId").equalTo(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    User newUser = new User(user.getUid(), user.getDisplayName());
                    addUserToDB(newUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Booking item) {

    }
}
