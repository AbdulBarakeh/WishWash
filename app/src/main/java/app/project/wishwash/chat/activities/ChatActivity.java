package app.project.wishwash.chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

import app.project.wishwash.R;
import app.project.wishwash.chat.adaptors.MessageAdapter;
import app.project.wishwash.chat.models.Message;

public class ChatActivity extends AppCompatActivity {
    RecyclerView messageRecyclerView;
    MessageAdapter messageAdapter;

    FirebaseUser firebaseUser;
    DatabaseReference dbReference;
    Intent getFromUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final EditText messageInput = findViewById(R.id.msg_input);
        Button sendButton = findViewById(R.id.send_btn);

        getFromUser = getIntent();
        final String guestId = getFromUser.getStringExtra("userId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(guestId);


        messageRecyclerView = findViewById(R.id.chat_recyclerView);
        messageRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager messageLayoutManager = new LinearLayoutManager(this);
        messageAdapter = new MessageAdapter(this, users);
        messageRecyclerView.setLayoutManager(messageLayoutManager);
        messageRecyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendMessage(new Message(messageInput.getText().toString(),new Date().toString(),guestId,firebaseUser.getUid()));
                messageInput.setText("");
                messageAdapter.updateMessages(users);

            }
        });


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
