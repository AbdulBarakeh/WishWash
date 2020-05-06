package app.project.wishwash.adaptors;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.callback.Callback;

import app.project.wishwash.fragments.booking.BookingFragment;
import app.project.wishwash.models.Booking;
import app.project.wishwash.R;
import app.project.wishwash.patterns.ICommand;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Booking} and makes a call to the
 * specified {@link BookingFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBookingRecyclerViewAdapter extends RecyclerView.Adapter<MyBookingRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "MyBookingRecyclerViewAd";
    private String key;
    String keyStringValue;
    private String value;
    private List<Booking> mValues;
    private final BookingFragment.OnListFragmentInteractionListener mListener;
    public ExecutorService executor = Executors.newSingleThreadExecutor();


    public MyBookingRecyclerViewAdapter(List<Booking> items , BookingFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        notifyDataSetChanged();
    }
    public void updateBooking(List<Booking> listOfBookings) {
        mValues = listOfBookings;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_booking , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder , final int position) {

//        holder.mItem = mValues.get(position);
        holder.name.setText(mValues.get(position).getUser().getUserName());
        holder.date.setText(mValues.get(position).getDateDayOfMonth() + "/" + mValues.get(position).getDateMonth() + "/" + mValues.get(position).getDateYear());
        holder.start_time.setText(mValues.get(position).getDateHour());
//        holder.stop_time.setText(String.valueOf(mValues.get(position).getDateHour()+2));
        holder.washing_machine.setText(mValues.get(position).getWashingMachine().getName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Delete booking from FIREBASE WIP
                class Handler implements ICommand{

                    @Override
                    public void Handle(Object data) {
                        DeleteEntry(value, key);
                        mValues.remove(position);
                    }
                }
                FindEntry(mValues.get(position).getBookingID(), new Handler());//sets the key field
            }
        });
    }

    private void DeleteEntry(String value, String key) {
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
        return mValues.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name_label, date_label, start_time_label, washing_machine_label;
        public final TextView name, date, start_time, washing_machine;
        public final Button delete;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            name_label = (TextView) view.findViewById(R.id.user_name_label);
            date_label = (TextView) view.findViewById(R.id.date_label);
            start_time_label = (TextView) view.findViewById(R.id.start_time_label);
//            stop_time_label = (TextView) view.findViewById(R.id.stop_time_label);
            washing_machine_label = (TextView) view.findViewById(R.id.washing_machine_label);

            name = (TextView) view.findViewById(R.id.user_name);
            date = (TextView) view.findViewById(R.id.date);
            start_time = (TextView) view.findViewById(R.id.start_time);
//            stop_time= (TextView) view.findViewById(R.id.stop_time);
            washing_machine = (TextView) view.findViewById(R.id.washing_machine);
            delete = (Button) view.findViewById(R.id.delete_btn);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
