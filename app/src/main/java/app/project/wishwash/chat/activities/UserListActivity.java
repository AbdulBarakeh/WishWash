package app.project.wishwash.chat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

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

        User user1 = new User();
        user1.setId(1);
        user1.setName("BOB1");

        User user2 = new User();
        user2.setId(2);
        user2.setName("BOB2");

        User user3 = new User();
        user3.setId(3);
        user3.setName("BOB3");

        User user4 = new User();
        user4.setId(4);
        user4.setName("BOB4");

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        userRecyclerView = findViewById(R.id.user_recyclerView);
        userRecyclerView.setHasFixedSize(true);
        userLayoutManager = new LinearLayoutManager(this);
        userAdapter = new UserAdapter(this, users);
        userRecyclerView.setLayoutManager(userLayoutManager);
        userRecyclerView.setAdapter(userAdapter);

        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent GoToChat = new Intent(UserListActivity.this, ChatActivity.class);
                GoToChat.putExtra("userId",users.get(position).getId());
                startActivity(GoToChat);
            }
        });
    }
}
