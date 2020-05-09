package app.project.wishwash.fragments.booking;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.project.wishwash.adaptors.BookingAdapter;
import app.project.wishwash.models.Booking;
import app.project.wishwash.R;


public class BookingFragment extends Fragment {

    private List<Booking> listOfBookings = new ArrayList<>();
    private DatabaseReference dbRef;
    private FirebaseUser user;
    BookingAdapter bookingAdapter;
    RecyclerView recyclerView;
    Context context;

    public BookingFragment() {
        //Mandatory empty constructor for the fragment manager to instantiate the
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookingFragment newInstance() {
        BookingFragment fragment = new BookingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_list , container , false);
        context = view.getContext();
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        bookingAdapter = new BookingAdapter(listOfBookings);
        recyclerView.setAdapter(bookingAdapter);
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("bookings");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listOfBookings.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Booking currentBooking = snap.getValue(Booking.class);
                    if (currentBooking.getUser().getUserId().equals(user.getUid())){
                    listOfBookings.add(currentBooking);
                    }
                }
                bookingAdapter.updateBooking(listOfBookings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Booking item);

    }
}
