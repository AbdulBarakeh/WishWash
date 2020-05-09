package app.project.wishwash.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import app.project.wishwash.R;
import app.project.wishwash.fragments.booking.BookingFragment;
import app.project.wishwash.fragments.calender.CalendarFragment;
import app.project.wishwash.fragments.chat.ChatFragment;
import app.project.wishwash.fragments.chat.UserListFragment;
import app.project.wishwash.fragments.video.VideoFragment;
import app.project.wishwash.models.User;
import app.project.wishwash.service.WishWashService;

public class CalendarActivity extends AppCompatActivity implements UserListFragment.UserListFragmentListener {
    private Intent service;
    private BottomNavigationView bottomNavigationView;
    private Toolbar actionBar;
    private final String TAG = "CalendarActivity";
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private SharedPreferences sp;
    private CalendarFragment calendarFragment;
    private ChatFragment newChatFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_calendar);
        calendarFragment = new CalendarFragment();
        actionBar = findViewById(R.id.action_bar);
        bottomNavigationView = findViewById(R.id.BottomNavigation_Calendar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        setSupportActionBar(actionBar);
        fragmentManager = getSupportFragmentManager();
        initializeService();

        if (savedInstanceState != null) {
            currentFragment = fragmentManager.getFragment(savedInstanceState, "currentFragment");
            openFragment(currentFragment);
        }else{
            currentFragment = initCalendarFragment();
        }
    }

    private void initializeService() {
        service = new Intent(this, WishWashService.class);
        startService(service);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
    }

    // Inflates --- CalendarFragment/fragment_calendar --- as the first fragment to be shown when app is started
    private Fragment initCalendarFragment() {
        Fragment fragment = new CalendarFragment();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameLayout_Calendar, fragment);
        transaction.commit();
        return fragment;
    }

    // When BottomNavigationView item is clicked, inflate belonging fragment.
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.Navigation_Calendar:
                        currentFragment = CalendarFragment.newInstance("", "");
                        openFragment(currentFragment);
                        return true;

                    case R.id.Navigation_Bookings:
                        currentFragment = BookingFragment.newInstance();
                        openFragment(currentFragment);
                        return true;

                    case R.id.Navigation_Chat:
                        currentFragment = UserListFragment.newInstance();
                        openFragment(currentFragment);
                        return true;

                    case R.id.Navigation_Tips:
                        currentFragment = VideoFragment.newInstance();
                        openFragment(currentFragment);
                        return true;
                }
                return false;
            }
        };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        getSupportFragmentManager().putFragment(outState, "currentFragment", currentFragment);
        super.onSaveInstanceState(outState);
    }

    public void openFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameLayout_Calendar, fragment);
        transaction.commit();
    }

    @Override
    public void onUserSent(User user) {
        newChatFragment = new ChatFragment();
        currentFragment = newChatFragment;
        newChatFragment.setUser(user);
        openFragment(newChatFragment);
    }

}
