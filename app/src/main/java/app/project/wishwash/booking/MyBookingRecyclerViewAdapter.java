package app.project.wishwash.booking;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import app.project.wishwash.Booking;
import app.project.wishwash.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Booking} and makes a call to the
 * specified {@link BookingFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBookingRecyclerViewAdapter extends RecyclerView.Adapter<MyBookingRecyclerViewAdapter.ViewHolder> {

    private List<Booking> mValues;
    private final BookingFragment.OnListFragmentInteractionListener mListener;

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
                //TODO: Delete booking from FIREBASE
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("bookings");
                String key = ref.child(mValues.get(position).getBookingID()).getKey();
                ref.child(key).removeValue();
//                ref.child("bookings").child(mValues.get(position).getBookingID()).removeValue();
                mValues.remove(position);
                notifyDataSetChanged();
            }
        });

//        holder.name_label.setText("Name: ");
//        holder.date_label.setText(mValues.get(position).date);
//        holder.start_time_label.setText(mValues.get(position).startTime);
//        holder.stop_time_label.setText(mValues.get(position).stopTime);
//        holder.washing_machine_label.setText(mValues.get(position).washingMachine);
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
