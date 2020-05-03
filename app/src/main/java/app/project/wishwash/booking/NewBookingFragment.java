package app.project.wishwash.booking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.project.wishwash.Booking;
import app.project.wishwash.R;
import app.project.wishwash.User;
import app.project.wishwash.WashingMachine;

public class NewBookingFragment extends Fragment {
    private NewBookingFragmentListener listener;
    private Calendar calendar;
    private Button btn_ok;
    private static final String CHOSEN_YEAR = "chosenYear";
    private static final String CHOSEN_MONTH = "chosenMonth";
    private static final String CHOSEN_DAY = "chosenDay";
    List<Booking> bookings;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Calendar date;
    private User myUser;
    private WashingMachine washingMachine;

    public interface NewBookingFragmentListener {
        void onTimeChosen(Calendar c);
    }


    private int mChosenYear;
    private int mChosenMonth;
    private int mChosenDay;

    public NewBookingFragment() {
        // Required empty public constructor
    }

    public static NewBookingFragment newInstance(int chosenYear, int chosenMonth, int chosenDay) {
        NewBookingFragment fragment = new NewBookingFragment();
        Bundle args = new Bundle();
        args.putInt(CHOSEN_YEAR, chosenYear);
        args.putInt(CHOSEN_MONTH, chosenMonth);
        args.putInt(CHOSEN_DAY, chosenDay);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChosenYear = getArguments().getInt(CHOSEN_YEAR);
            mChosenMonth = getArguments().getInt(CHOSEN_MONTH);
            mChosenDay = getArguments().getInt(CHOSEN_DAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_booking, container, false);

        // Declare firebase reference object to be able to access the database.
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userId = user.getUid();


        washingMachine = new WashingMachine("123", "WM1");
        calendar = Calendar.getInstance();
        myUser = new User();
        date.set(mChosenYear, mChosenMonth, mChosenDay);
        bookings = new ArrayList<>();

        // Implement method for TableLayout / RecyclerView
        // When time interval chosen -> set calendar time ( calendar.set() )

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTimeChosen(calendar);
            }
        });

        return v;
    }

    public void getBookings() {
        final DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings");

        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Booking currentBooking;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    currentBooking = snapshot.getValue(Booking.class);
                    bookings.add(currentBooking);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setBookings(Booking booking){
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> bookingMap = new HashMap<>();
        bookingMap.put("booking",booking);
        //Do again
        bookingRef.child("bookings").push().setValue(bookingMap);

    }
}
