package app.project.wishwash.fragments.calender;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import app.project.wishwash.models.Booking;
import app.project.wishwash.R;
import app.project.wishwash.models.User;
import app.project.wishwash.models.WashingMachine;
import app.project.wishwash.patterns.ICommand;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private int dateYear, dateMonth, dateDayOfMonth;
    private String dateHour;
    private Button btn_ok;
    private Spinner spinner_times, spinner_washingMachines;
    private Booking booking;
    private WashingMachine washingMachine;
    private User userWishWash;
    private List<Booking> firebaseBookingList;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

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
        userWishWash.setUserName(firebaseUser.getDisplayName());
        userWishWash.setUserId(firebaseUser.getUid());
        washingMachine = new WashingMachine("001", "WM1");
        booking = new Booking();
        firebaseBookingList = new ArrayList<>();

        // Save data on device rotation
        if(savedInstanceState!=null){
            dateYear = savedInstanceState.getInt("dateYear");
            dateMonth = savedInstanceState.getInt("dateMonth");
            dateDayOfMonth = savedInstanceState.getInt("dateDayOfMonth");
        }else{
            DateTime dt = new DateTime();
            dateYear = dt.getYear();
            dateMonth = dt.getMonthOfYear();
            dateDayOfMonth = dt.getDayOfMonth();
        }

        calendarView = v.findViewById(R.id.CalendarView_CalendarFragment);
        btn_ok = v.findViewById(R.id.Button_calendarFragment_ok);
        spinner_times = v.findViewById(R.id.Spinner_CalendarFragment_Times);
        spinner_washingMachines = v.findViewById(R.id.Spinner_CalendarFragment_WashingMachines);

        // Set date to today (current day)
        calendarView.setDate(System.currentTimeMillis(), false, true);

        // Set new date if date is changed in calendarView.
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateYear = year;
                dateMonth = month + 1;
                dateDayOfMonth = dayOfMonth;
            }
        });

        // Set dropdown menu for time intervals to wash
        ArrayAdapter<CharSequence> spinnerTimesAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.times, R.layout.spinner);
        spinnerTimesAdapter.setDropDownViewResource(R.layout.spinner);
        spinner_times.setAdapter(spinnerTimesAdapter);
        spinner_times.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateHour = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set dropdown menu for available washing machines to choose from
        ArrayAdapter<CharSequence> spinnerWMAdapter = ArrayAdapter.createFromResource(getContext(), R.array.washing_machines, R.layout.spinner);
        spinnerWMAdapter.setDropDownViewResource(R.layout.spinner);
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

        // On OK-button click
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // Set chosen date to appropriate variables and check whether a booking with this information already exists in Firebase.
                        List<Booking> listOfBookings = firebaseBookingList;
                        booking.setBookingID(UUID.randomUUID().toString());
                        booking.setDateYear(dateYear);
                        booking.setDateMonth(dateMonth);
                        booking.setDateDayOfMonth(dateDayOfMonth);
                        booking.setDateHour(dateHour);
                        booking.setUser(userWishWash);
                        boolean alreadyInDB = false;

                        try {
                            for (Booking b : listOfBookings) {
                                // If already in Firebase, booking is not allowed and toast message is sent to user
                                if ((booking.getDateYear() == b.getDateYear() && booking.getDateMonth() == b.getDateMonth() &&
                                        booking.getDateDayOfMonth() == b.getDateDayOfMonth() && booking.getDateHour().equals(b.getDateHour()))) {
                                    Toast.makeText(getContext(), " Booking is already reserved by "
                                            + b.getUser().getUserName(), Toast.LENGTH_LONG).show();

                                    alreadyInDB = true;
                                }
                            }

                            // Book chosen date and washing machine
                            if (!alreadyInDB) {
                                setBookingInFirebase(booking);
                                List<Booking> userBookingList = new ArrayList<>();
                                userBookingList.add(booking);

                                Toast.makeText(getContext(), "You have booked "
                                        + booking.getWashingMachine().getName() + " from " + dateHour +
                                        " on " + dateDayOfMonth + "/" + dateMonth + "/" + dateYear, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                getBookingsFromFirebase();

            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("dateYear", dateYear);
        outState.putInt("dateMonth", dateMonth);
        outState.putInt("dateDayOfMonth", dateDayOfMonth);
        super.onSaveInstanceState(outState);
    }

    // Book chosen date and washing machine
    private void setBookingInFirebase(Booking booking) {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> bookingMap = new HashMap<>();
        bookingMap.put("bookingID",booking.getBookingID());
        bookingMap.put("dateYear", booking.getDateYear());
        bookingMap.put("dateMonth", booking.getDateMonth());
        bookingMap.put("dateDayOfMonth", booking.getDateDayOfMonth());
        bookingMap.put("dateHour", booking.getDateHour());
        bookingMap.put("user", booking.getUser());
        bookingMap.put("washingMachine", booking.getWashingMachine());
        bookingRef.child("bookings").push().setValue(bookingMap);
    }

    // Get all bookings from Firebase
    private void getBookingsFromFirebase() {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings");

        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firebaseBookingList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Booking currentBooking = snapshot.getValue(Booking.class);
                    firebaseBookingList.add(currentBooking);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
