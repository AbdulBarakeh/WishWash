package app.project.wishwash.chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import app.project.wishwash.R;
import app.project.wishwash.chat.adaptors.MessageAdapter;
import app.project.wishwash.chat.models.GuestUser;
import app.project.wishwash.chat.models.Message;
import app.project.wishwash.chat.models.OwnerUser;
import app.project.wishwash.chat.models.User;

public class ChatActivity extends AppCompatActivity {
    RecyclerView messageRecyclerView;
    MessageAdapter messageAdapter;
    FirebaseDatabase messageDB = FirebaseDatabase.getInstance();
    DatabaseReference dbReference = messageDB.getReference();
    DatabaseReference messageReference = dbReference.child("messages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getFromUser = getIntent();
        final int guestId = getFromUser.getIntExtra("userId",69);

        final EditText messageInput = findViewById(R.id.msg_input);
        Button sendButton = findViewById(R.id.send_btn);

        final ArrayList<User> users = new ArrayList<>();
        final GuestUser guest1 = new GuestUser();
//        guest1.setId(1);
//        guest1.setName("Bob");
//        guest1.setMessage(new Message("TIHI", Calendar.getInstance().toString()));
//        users.add(guest1);
//
        final OwnerUser owner1 = new OwnerUser();
        owner1.setId(2);
        owner1.setName("Jack");
//        owner1.setMessage(new Message("TAHA", Calendar.getInstance().toString()));
        users.add(owner1);
//
//        GuestUser guest2 = new GuestUser();
//        guest2.setId(3);
//        guest2.setName("Bob2");
//        guest2.setMessage(new Message("TUHU", Calendar.getInstance().toString()));
//        users.add(guest2);
//
//        OwnerUser owner2 = new OwnerUser();
//        owner2.setId(4);
//        owner2.setName("Jack2");
//        owner2.setMessage(new Message("TUCTUC", new Date().toString()));
//        users.add(owner2);

        messageRecyclerView = findViewById(R.id.chat_recyclerView);
        messageRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager messageLayoutManager = new LinearLayoutManager(this);
        messageAdapter = new MessageAdapter(this, users);
        messageRecyclerView.setLayoutManager(messageLayoutManager);
        messageRecyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                owner1.setMessage(new Message(messageInput.getText().toString(),Calendar.getInstance().toString() ));
//                users.add(owner1);


                SendMessage(new Message(messageInput.getText().toString(),new Date().toString(),guestId,owner1.getId()));
                messageInput.setText("");
                messageAdapter.updateMessages(users);

            }
        });

//        dbReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        })
    }
//SOURCE: https://www.youtube.com/watch?v=f1HKTg2hyf0
    public void SendMessage( Message message){

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> map = new HashMap<>();
        map.put("messageId",message.getId());
        map.put("messageDate", message.getTime());
        map.put("message",message.getMessage());
        map.put("sender", message.getSender());
        map.put("receiver",message.getReceiver());

        dbRef.child("messages").push().setValue(map);

    }
}
