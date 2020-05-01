package app.project.wishwash.booking;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wishwash_demo.Fragments.Abdul.BookingFragment.OnListFragmentInteractionListener;
import com.example.wishwash_demo.Fragments.Abdul.dummy.BookingContent.BookingItem;
import com.example.wishwash_demo.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BookingItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBookingRecyclerViewAdapter extends RecyclerView.Adapter<MyBookingRecyclerViewAdapter.ViewHolder> {

    private final List<BookingItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyBookingRecyclerViewAdapter(List<BookingItem> items , OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
        holder.name.setText(mValues.get(position).name);
        holder.date.setText(mValues.get(position).date);
        holder.start_time.setText(mValues.get(position).startTime);
        holder.stop_time.setText(mValues.get(position).stopTime);
        holder.washing_machine.setText(mValues.get(position).washingMachine);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Delete booking from FIREBASE
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
        public final TextView name_label, date_label, start_time_label, stop_time_label, washing_machine_label;
        public final TextView name, date, start_time, stop_time, washing_machine;
        public final Button delete;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            name_label = (TextView) view.findViewById(R.id.user_name_label);
            date_label = (TextView) view.findViewById(R.id.date_label);
            start_time_label = (TextView) view.findViewById(R.id.start_time_label);
            stop_time_label = (TextView) view.findViewById(R.id.stop_time_label);
            washing_machine_label = (TextView) view.findViewById(R.id.washing_machine_label);

            name = (TextView) view.findViewById(R.id.user_name);
            date = (TextView) view.findViewById(R.id.date);
            start_time = (TextView) view.findViewById(R.id.start_time);
            stop_time= (TextView) view.findViewById(R.id.stop_time);
            washing_machine = (TextView) view.findViewById(R.id.washing_machine);
            delete = (Button) view.findViewById(R.id.delete_btn);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
