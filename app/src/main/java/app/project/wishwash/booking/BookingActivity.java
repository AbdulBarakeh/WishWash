package app.project.wishwash.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.example.wishwash_demo.Fragments.Abdul.dummy.BookingContent;
import com.example.wishwash_demo.Fragments.BookingsFragment;
import com.example.wishwash_demo.Fragments.CalendarFragment;
import com.example.wishwash_demo.Fragments.ChatFragment;
import com.example.wishwash_demo.Fragments.TipsFragment;
import com.example.wishwash_demo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookingActivity extends AppCompatActivity implements BookingFragment.OnListFragmentInteractionListener {
    private FragmentTransaction transaction;
    private Toolbar actionBar;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private BookingFragment bookingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        bookingFragment = new BookingFragment();

        actionBar = findViewById(R.id.action_bar);
        bottomNavigationView = findViewById(R.id.BottomNavigation_Booking);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        setSupportActionBar(actionBar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
    }

    @Override
    public void onListFragmentInteraction(BookingContent.BookingItem item) {

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
                            openFragment(BookingsFragment.newInstance("", ""));
                            return true;
                        case R.id.Navigation_Chat:
                            openFragment(ChatFragment.newInstance("", ""));
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
        transaction.replace(R.id.FrameLayout_Calendar, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
