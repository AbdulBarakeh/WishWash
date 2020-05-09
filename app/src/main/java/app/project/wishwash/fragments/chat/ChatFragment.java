package app.project.wishwash.fragments.chat;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import app.project.wishwash.adaptors.MessageAdapter;
import app.project.wishwash.models.Message;
import app.project.wishwash.models.User;
//SRC: https://youtu.be/f1HKTg2hyf0 & https://youtu.be/1mJv4XxWlu8 AND inspiration have been gathered from other videos in the playlist
public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";
    RecyclerView messageRecyclerView;
    MessageAdapter messageAdapter;
    ArrayList<Message> data = new ArrayList<>();
    FirebaseUser firebaseUser;
    DatabaseReference dbReference;
//    Intent getFromUser;
    private String guestID;
    private String guestName;

    public void setUser(User user) {
        guestID = user.getUserId();
        guestName = user.getUserName();
    }


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG , "onCreate: "+ new Date().toString());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG , "onCreateView: I'm here");
        View view = inflater.inflate(R.layout.activity_chat, container, false);
        final Context context = view.getContext();

        final EditText messageInput = view.findViewById(R.id.msg_input);
        Button sendButton = view.findViewById(R.id.send_btn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User guest;
        //Savedinstance to save the current guest user
        if(savedInstanceState!=null){
            guestID = savedInstanceState.getString("guestId");
            guestName = savedInstanceState.getString("guestName");
            guest = new User(guestID, guestName);
            data = savedInstanceState.getParcelableArrayList("messages");
        }else{
            guest = new User(guestID,guestName);
        }

        final User owner = new User(firebaseUser.getUid(),firebaseUser.getDisplayName());
        messageRecyclerView = view.findViewById(R.id.chat_recyclerView);
        messageRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager messageLayoutManager = new LinearLayoutManager(context);
        messageRecyclerView.setLayoutManager(messageLayoutManager);
        messageAdapter = new MessageAdapter(context,data);
        messageRecyclerView.setAdapter(messageAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageInput.getText().toString().equals("")){
                    if (guestID == null || guestName == null){
                        Toast.makeText(context,"Error occurred, reopen chat tab - Message not send" , Toast.LENGTH_SHORT).show();
                    }else{
                    Message currentMessage = new Message(UUID.randomUUID().toString(), new Date().toString(),messageInput.getText().toString(), owner, guest);
                    SendMessage(currentMessage);
                    data.add(currentMessage);
                    messageInput.setText("");
                    messageAdapter.updateMessages(data);
                    }
                }
            }
        });

            DatabaseReference dbRefMessage = FirebaseDatabase.getInstance().getReference("messages");
            dbRefMessage.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    data.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()){
                        Message currentMessage = snap.getValue(Message.class);
                        if (currentMessage.getReceiver().getUserId().equals(owner.getUserId())
                                && currentMessage.getSender().getUserId().equals(guest.getUserId())
                                || currentMessage.getSender().getUserId().equals(owner.getUserId())
                                && currentMessage.getReceiver().getUserId().equals(guest.getUserId())){
                            data.add(currentMessage);
                        }
                    }
                    messageAdapter.updateMessages(data);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        return  view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("guestId", guestID);
        outState.putString("guestName", guestName);
        outState.putParcelableArrayList("messages", data);
        super.onSaveInstanceState(outState);
    }

    public void SendMessage(Message message){

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
}
