package app.project.wishwash.calender;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wishwash_demo.Booking;
import com.example.wishwash_demo.R;
import com.example.wishwash_demo.User;
import com.example.wishwash_demo.WashingMachine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CalendarFragment extends Fragment {
    private CalendarFragmentListener listener;
    private CalendarView calendarView;
    private Calendar calendar;
    private Button btn_ok;
    private Spinner spinner_times, spinner_washingMachines;
    private Booking booking;
    private WashingMachine washingMachine;
    private User userWishWash;
    private List<Booking> bookingList;
    private int cYear, cMonth, cDayOfMonth, cHour;

    public interface CalendarFragmentListener {
        void onDateChosen(Calendar c);
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userWishWash = new User();
        userWishWash.setName(firebaseUser.getDisplayName());
        userWishWash.setUserId(firebaseUser.getUid());

        washingMachine = new WashingMachine("001", "WM1");
        booking = new Booking(calendar, userWishWash, washingMachine);
        calendar = Calendar.getInstance();

        calendarView = v.findViewById(R.id.CalendarView_CalendarFragment);
        btn_ok = v.findViewById(R.id.Button_calendarFragment_ok);
        spinner_times = v.findViewById(R.id.Spinner_CalendarFragment_Times);
        spinner_washingMachines = v.findViewById(R.id.Spinner_CalendarFragment_WashingMachines);

        calendarView.setDate(System.currentTimeMillis(), false, true);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                cYear = year;
                cMonth = month;
                cDayOfMonth = dayOfMonth;
                calendar.set(cYear, cMonth, cDayOfMonth);
            }
        });

        // Spinners viser ikke værdier
        ArrayAdapter<CharSequence> spinnerTimesAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.times, android.R.layout.simple_spinner_item);
        spinnerTimesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_times.setAdapter(spinnerTimesAdapter);
        spinner_times.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String time = parent.getItemAtPosition(position).toString();
                /*int timeInt = Integer.parseInt(time.substring(0,2));
                calendar.set(cYear, cMonth, cDayOfMonth, timeInt, 00);
                booking.setDate(calendar);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> spinnerWMAdapter = ArrayAdapter.createFromResource(getContext(), R.array.washing_machines, android.R.layout.simple_spinner_item);
        spinnerWMAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_washingMachines.setAdapter(spinnerWMAdapter);
        spinner_washingMachines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String wm = parent.getItemAtPosition(position).toString();
                washingMachine.setName(wm);
                booking.setWashingMachine(washingMachine);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBookingsFromFirebase(booking);
                boolean alreadyInDB = false;

                for (Booking b: bookingList) {
                    if (booking.getDate() == b.getDate()) {
                        Toast.makeText(getContext(), " Booking is already reserved by "
                                + b.getUser().getName(), Toast.LENGTH_LONG);

                        alreadyInDB = true;
                    }
                }

                if (!alreadyInDB) {
                    setBookingInFirebase(booking);
                    bookingList.clear();
                    bookingList = userWishWash.getBookingList();
                    bookingList.add(booking);
                    userWishWash.setBookingList(bookingList);

                    Toast.makeText(getContext(), " You have booked "
                            + booking.getWashingMachine() + " at " + cHour
                            + " and two hours ahead", Toast.LENGTH_LONG);
                }
            }
        });

        return v;
    }

    public void setBookingInFirebase(Booking booking) {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> bookingMap = new HashMap<>();
        bookingMap.put("date", booking.getDate());
        bookingMap.put("user", booking.getUser());
        bookingMap.put("washingMachine", booking.getWashingMachine());
        bookingRef.child("bookings").push().setValue(bookingMap);
    }

    public void getBookingsFromFirebase(Booking newBooking){
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings");
        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Booking> bookings = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Booking currentBooking = snapshot.getValue(Booking.class);
                    bookings.add(currentBooking);
                }

                bookingList.clear();
                bookingList = bookings;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // When fragment is attached to an activity, check if interface is implemented.
    // Inspiration from: https://www.youtube.com/watch?v=i22INe14JUc
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CalendarFragmentListener) {
            listener = (CalendarFragmentListener) context;
        } else
            throw new RuntimeException(context.toString() + " must implement CalendarFragmentListener");
    }

    // When fragment is detached from the activity.
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
