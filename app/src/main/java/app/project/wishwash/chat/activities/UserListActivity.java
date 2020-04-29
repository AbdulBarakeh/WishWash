package app.project.wishwash.chat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.project.wishwash.R;
import app.project.wishwash.chat.adaptors.UserAdapter;
import app.project.wishwash.chat.models.Booking;
import app.project.wishwash.chat.models.User;


public class UserListActivity extends AppCompatActivity {
    List<User> users = new ArrayList<>();
    RecyclerView userRecyclerView;
    RecyclerView.LayoutManager userLayoutManager;
    UserAdapter userAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userRecyclerView = findViewById(R.id.user_recyclerView);
        userRecyclerView.setHasFixedSize(true);
        userLayoutManager = new LinearLayoutManager(this);
        userAdapter = new UserAdapter(this, users);
        userRecyclerView.setLayoutManager(userLayoutManager);
        userRecyclerView.setAdapter(userAdapter);

        GetUsers();

        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent GoToChat = new Intent(UserListActivity.this, ChatActivity.class);
                GoToChat.putExtra("userId",users.get(position).getUserId());
                GoToChat.putExtra("username",users.get(position).getUserName());
                startActivity(GoToChat);
            }
        });
    }
    public void GetUsers(){

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    User currentUser = snap.getValue(User.class);
                    users.add(currentUser);
                }
                userAdapter.updateUsers(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
//    public void GetBookings(){
//        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings");
//        bookingRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                Booking currentBooking = snapshot.getValue(Booking.class);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//    public void setBookings(Booking booking){
//        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference();
//        HashMap<String,Object> bookingMap = new HashMap<>();
//        bookingMap.put("booking",booking);
//        //Do again
//        bookingRef.child("bookings").push().setValue(bookingMap);
//
//    }
}
