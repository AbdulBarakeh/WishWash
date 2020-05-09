package app.project.wishwash.fragments.chat;

import android.content.Context;
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
import app.project.wishwash.adaptors.UserAdapter;
import app.project.wishwash.models.User;


// SRC: https://developer.android.com/training/basics/fragments/communicating
public class UserListFragment extends Fragment {
    UserListFragmentListener callback;
    List<User> users = new ArrayList<>();
    RecyclerView userRecyclerView;
    RecyclerView.LayoutManager userLayoutManager;
    UserAdapter userAdapter;

    public void setUserListFragmentListener(UserListFragmentListener callback){
        this.callback = callback;
    }
    public interface UserListFragmentListener{
        void onUserSent(User user);

    }

    public UserListFragment() {
        // Required empty public constructor
    }

    public static UserListFragment newInstance() {
        UserListFragment fragment = new UserListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list , container , false);
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
                callback.onUserSent(user);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UserListFragmentListener){
            callback = (UserListFragmentListener) context;
        }
        else{
            throw new RuntimeException(context.toString() + " must implement UserListFragmentListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
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
