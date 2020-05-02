package app.project.wishwash.chat.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.project.wishwash.R;
import app.project.wishwash.chat.ChatShellActivity;
import app.project.wishwash.chat.activities.ChatActivity;
import app.project.wishwash.chat.activities.UserListActivity;
import app.project.wishwash.chat.adaptors.UserAdapter;
import app.project.wishwash.chat.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserListFragment extends Fragment {
    private UserListFragmentListener listener;
    List<User> users = new ArrayList<>();
    RecyclerView userRecyclerView;
    RecyclerView.LayoutManager userLayoutManager;
    UserAdapter userAdapter;

    public interface UserListFragmentListener{
        void onUserSent(User user);

    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UserListFragment newInstance() {
        UserListFragment fragment = new UserListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1 , param1);
//        args.putString(ARG_PARAM2 , param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_user_list , container , false);
        Context context = view.getContext();
        userRecyclerView = view.findViewById(R.id.user_recyclerView);
        userRecyclerView.setHasFixedSize(true);
        userLayoutManager = new LinearLayoutManager(context);
        userAdapter = new UserAdapter(context, users);
        userRecyclerView.setLayoutManager(userLayoutManager);
        userRecyclerView.setAdapter(userAdapter);

        GetUsers();

        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                User user = new User(users.get(position).getUserId(),users.get(position).getUserName());
                listener.onUserSent(user);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UserListFragmentListener){
            listener = (UserListFragmentListener) context;
        }
        else{
            throw new RuntimeException(context.toString() + " must implement UserListFragmentListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void GetUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    User currentUser = snap.getValue(User.class);
                    if (!firebaseUser.getUid().equals(currentUser.getUserId())){
                        users.add(currentUser);
                    }
                }
                userAdapter.updateUsers(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
