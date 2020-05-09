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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param guestId Parameter 1.
//     * @param guestName Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static ChatFragment newInstance(String guestId, String guestName) {
//        ChatFragment fragment = new ChatFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, guestId);
//        args.putString(ARG_PARAM2, guestName);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG , "onCreate: "+ new Date().toString());
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG , "onCreateView: I'm here");
        View view = inflater.inflate(R.layout.activity_chat, container, false);
        Context context = view.getContext();

        final EditText messageInput = view.findViewById(R.id.msg_input);
        Button sendButton = view.findViewById(R.id.send_btn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User guest;
        if(savedInstanceState!=null){
            String newID = savedInstanceState.getString("guestId");
            String newName = savedInstanceState.getString("guestName");
            guest = new User(newID, newName);
            data = savedInstanceState.getParcelableArrayList("messages");
        }else{
            guest = new User(guestID,guestName);
        }

        final User owner = new User(firebaseUser.getUid(),firebaseUser.getDisplayName());
//        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(guestId);


        messageRecyclerView = view.findViewById(R.id.chat_recyclerView);
        messageRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager messageLayoutManager = new LinearLayoutManager(context);
        messageRecyclerView.setLayoutManager(messageLayoutManager);
        messageAdapter = new MessageAdapter(context,data);
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

//        DatabaseReference dbRefMessage = FirebaseDatabase.getInstance().getReference("users");
//        dbRefMessage.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User myUser = new User(firebaseUser.getUid(),firebaseUser.getDisplayName());
//                for (DataSnapshot snap : dataSnapshot.getChildren()){
//                    User currentUser = snap.getValue(User.class);
//                    if (!currentUser.getUserId().equals(myUser.getUserId())){
//                        ReadMessage(new Message(UUID.randomUUID().toString(),new Date().toString(),"",currentUser,myUser));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
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
//    public void ReadMessage(final Message message){
//        dbReference = FirebaseDatabase.getInstance().getReference("messages");
//        dbReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                data.clear();
//                List<Message>listOfMessages = new ArrayList<>();
//                for (DataSnapshot snap : dataSnapshot.getChildren()){
//                    Message currentMessage = snap.getValue(Message.class);
//                    if (currentMessage.getReceiver().getUserId().equals(message.getReceiver().getUserId()) && //TODO: FIX THIS BS
//                            currentMessage.getSender().getUserId().equals(message.getSender().getUserId()) ||
//                            currentMessage.getReceiver().getUserId().equals(message.getSender().getUserId()) &&
//                                    currentMessage.getSender().getUserId().equals(message.getReceiver().getUserId())){
//                        listOfMessages.add(currentMessage);
//
//                    }
//                }
//                if (listOfMessages.size() != 0){
//                    messageAdapter.updateMessages(listOfMessages);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
