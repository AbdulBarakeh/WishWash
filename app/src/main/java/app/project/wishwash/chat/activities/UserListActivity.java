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
import java.util.List;

import app.project.wishwash.R;
import app.project.wishwash.chat.adaptors.UserAdapter;
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
                GoToChat.putExtra("userId",users.get(position).getId());
                GoToChat.putExtra("username",users.get(position).getName());
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
                    User currentuser = snap.getValue(User.class);
                    Toast.makeText(UserListActivity.this , currentuser.getId() + "1", Toast.LENGTH_SHORT).show();
                    Toast.makeText(UserListActivity.this , currentuser.getName() + "2" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
