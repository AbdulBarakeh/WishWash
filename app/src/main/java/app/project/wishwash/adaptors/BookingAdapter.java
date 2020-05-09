package app.project.wishwash.adaptors;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import app.project.wishwash.R;
import app.project.wishwash.models.Booking;
import app.project.wishwash.patterns.ICommand;
import app.project.wishwash.viewholders.BookingViewHolder;


public class BookingAdapter extends RecyclerView.Adapter<BookingViewHolder> {
    private static final String TAG = "MyBookingRecyclerViewAd";
    private String key;
    private List<Booking> listOfBookings;


    public BookingAdapter(List<Booking> listOfBookings) {
        this.listOfBookings = listOfBookings;
        notifyDataSetChanged();
    }
    public void updateBooking(List<Booking> listOfBookings) {
        this.listOfBookings = listOfBookings;
        notifyDataSetChanged();
    }

    @Override
    public BookingViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_booking , parent , false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookingViewHolder holder , final int position) {
        holder.name.setText(listOfBookings.get(position).getUser().getUserName());
        holder.date.setText(listOfBookings.get(position).getDateDayOfMonth() + "/" + listOfBookings.get(position).getDateMonth() + "/" + listOfBookings.get(position).getDateYear());
        holder.start_time.setText(listOfBookings.get(position).getDateHour());
        holder.washing_machine.setText(listOfBookings.get(position).getWashingMachine().getName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class Handler implements ICommand{

                    @Override
                    public void Handle(Object data) {
                        DeleteEntry(key);
                        listOfBookings.remove(position);
                    }
                }
                FindEntry(listOfBookings.get(position).getBookingID(), new Handler());//sets the key field
            }
        });
    }

    private void DeleteEntry(String key) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bookings");
        ref.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifyDataSetChanged();
            }
        });
    }

    private void FindEntry(final String id, final ICommand handler) {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bookings");
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                Booking currentBooking = snap.getValue(Booking.class);
                if (currentBooking.getBookingID().equals(id)) {
                    Log.d(TAG , "onDataChange: " + currentBooking.getBookingID() + " = " + id);
                    key = snap.getKey();
                    handler.Handle(null);
                    return;
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }

    @Override
    public int getItemCount() {
        return listOfBookings.size();
    }
}
