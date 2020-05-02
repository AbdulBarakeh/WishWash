package app.project.wishwash.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import app.project.wishwash.R;
import app.project.wishwash.booking.BookingFragment;
import app.project.wishwash.calender.CalendarFragment;
import app.project.wishwash.chat.fragments.UserListFragment;
import app.project.wishwash.chat.models.User;
import app.project.wishwash.tips.TipsFragment;

public class ChatShellActivity extends AppCompatActivity implements UserListFragment.UserListFragmentListener, ChatFragment.ChatFragmentListener {
    UserListFragment userListFragment;
    private Toolbar actionBar;
    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_shell);

        userListFragment = new UserListFragment();

        actionBar = findViewById(R.id.action_bar);
        bottomNavigationView = findViewById(R.id.BottomNavigation_Calendar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        setSupportActionBar(actionBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
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
                            openFragment(BookingFragment.newInstance(1));
                            return true;
                        case R.id.Navigation_Chat:
                            openFragment(UserListFragment.newInstance());
                            return true;
                        case R.id.Navigation_Tips:
                            openFragment(TipsFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };
    public void openFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameLayout_Chat, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    //UserListFragment
    @Override
    public void onUserSent(User user) {
//sop
    }
    //ChatFragment
    @Override
    public void onGuestUserChosen(User guest) {
        openFragment(ChatFragment.newInstance(guest.getUserId(),guest.getUserName()));
    }
}
