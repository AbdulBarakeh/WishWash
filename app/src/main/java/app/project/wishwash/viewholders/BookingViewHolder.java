package app.project.wishwash.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import app.project.wishwash.R;

public class BookingViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name_label, date_label, start_time_label, washing_machine_label;
        public final TextView name, date, start_time, washing_machine;
        public final Button delete;
        public BookingViewHolder(View view) {
            super(view);
            mView = view;
            name_label = (TextView) view.findViewById(R.id.user_name_label);
            date_label = (TextView) view.findViewById(R.id.date_label);
            start_time_label = (TextView) view.findViewById(R.id.start_time_label);
            washing_machine_label = (TextView) view.findViewById(R.id.washing_machine_label);

            name = (TextView) view.findViewById(R.id.user_name);
            date = (TextView) view.findViewById(R.id.date);
            start_time = (TextView) view.findViewById(R.id.start_time);
            washing_machine = (TextView) view.findViewById(R.id.washing_machine);
            delete = (Button) view.findViewById(R.id.delete_btn);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }