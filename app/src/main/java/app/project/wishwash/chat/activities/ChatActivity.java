package app.project.wishwash.chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import app.project.wishwash.R;
import app.project.wishwash.chat.adaptors.MessageAdapter;
import app.project.wishwash.chat.models.Message;
import app.project.wishwash.chat.models.User;

public class ChatActivity extends AppCompatActivity {
    RecyclerView messageRecyclerView;
    MessageAdapter messageAdapter;
    List<Message> data = new ArrayList<>();
    FirebaseUser firebaseUser;
    DatabaseReference dbReference;
    Intent getFromUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final EditText messageInput = findViewById(R.id.msg_input);
        Button sendButton = findViewById(R.id.send_btn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getFromUser = getIntent();
        final String guestId = getFromUser.getStringExtra("userId");
        String guestName = getFromUser.getStringExtra("username");
        final User guest = new User(guestId,guestName);
        final User owner = new User(firebaseUser.getUid(),firebaseUser.getDisplayName());
//        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(guestId);


        messageRecyclerView = findViewById(R.id.chat_recyclerView);
        messageRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager messageLayoutManager = new LinearLayoutManager(this);
        messageRecyclerView.setLayoutManager(messageLayoutManager);
        messageAdapter = new MessageAdapter(ChatActivity.this,data);
        messageRecyclerView.setAdapter(messageAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message currentMessage = new Message(UUID.randomUUID().toString(),new Date().toString(),messageInput.getText().toString(),owner,guest);
                SendMessage(currentMessage);
                data.add(currentMessage);
                messageInput.setText("");
                messageAdapter.updateMessages(data);

            }
        });

        DatabaseReference dbRefMessage = FirebaseDatabase.getInstance().getReference("users");
        dbRefMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User myUser = new User(firebaseUser.getUid(),firebaseUser.getDisplayName());
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    User currentUser = snap.getValue(User.class);
                    if (!currentUser.getUserId().equals(myUser.getUserId())){
                        ReadMessage(new Message(UUID.randomUUID().toString(),new Date().toString(),"",currentUser,myUser));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
//SOURCE: https://www.youtube.com/watch?v=f1HKTg2hyf0
    public void SendMessage( Message message){

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> map = new HashMap<>();
        map.put("messageId",message.getMessageId());
        map.put("messageDate", message.getMessageDate());
        map.put("message",message.getMessage());
        map.put("sender", message.getSender());
        map.put("receiver",message.getReceiver());

        dbRef.child("messages").push().setValue(map);
        messageAdapter.updateMessages(data);


    }
    public void ReadMessage(final Message message){
        dbReference = FirebaseDatabase.getInstance().getReference("messages");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Message currentMessage = snap.getValue(Message.class);
                    if (currentMessage.getReceiver().getUserId().equals(message.getReceiver().getUserId()) &&
                            currentMessage.getSender().getUserId().equals(message.getSender().getUserId()) ||
                            currentMessage.getReceiver().getUserId().equals(message.getSender().getUserId()) &&
                                    currentMessage.getSender().getUserId().equals(message.getReceiver().getUserId())){
                        data.add(currentMessage);

                    }
                }
                messageAdapter.updateMessages(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
