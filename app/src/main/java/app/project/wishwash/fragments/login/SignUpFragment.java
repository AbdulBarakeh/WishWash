package app.project.wishwash.fragments.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import app.project.wishwash.R;
import app.project.wishwash.activities.CalendarActivity;
import app.project.wishwash.activities.SignInActivity;
import app.project.wishwash.models.User;
import app.project.wishwash.patterns.ICommand;

public class SignUpFragment extends Fragment {
    // Declaring variables:
    private EditText editText_email, editText_password, editText_password_reentered, editText_full_name;
    private Button btn_back, btn_signup;
    private FirebaseAuth firebaseAuth;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Assigning variables:
        editText_full_name = v.findViewById(R.id.EditText_signupFragment_full_name);
        editText_email = v.findViewById(R.id.EditText_signupFragment_email);
        editText_password = v.findViewById(R.id.EditText_signupFragment_password);
        editText_password_reentered = v.findViewById(R.id.EditText_signupFragment_passwordReenter);
        btn_back = v.findViewById(R.id.Button_signupFragment_back);
        btn_signup = v.findViewById(R.id.Button_signupFragment_signup);

        // Accessing variables:
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String full_name = editText_full_name.getText().toString();
                String email = editText_email.getText().toString();
                String password = editText_password.getText().toString();
                String password_reentered = editText_password_reentered.getText().toString();

                // Checking user information's using different requirement
                if (!SignInActivity.isValidEmail(email)) {
                    Toast.makeText(getContext(), R.string.email_not_accepted, Toast.LENGTH_SHORT).show();
                } else if (password.length()<8) {
                    Toast.makeText(getContext(), R.string.password_8characters, Toast.LENGTH_SHORT).show();
                } else if (!SignInActivity.isValidPassword(password)) {
                    Toast.makeText(getContext(), R.string.password_not_enough_complex, Toast.LENGTH_SHORT).show();
                } else if (!password.equals(password_reentered)) {
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                } else

                    // Create user with email and password
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(), R.string.signup_unsuccessful, Toast.LENGTH_SHORT).show();
                            } else {
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(full_name).build();
                                user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        validateUserExistence(user);
                                    }
                                });
                                Toast.makeText(getActivity(), "You have successfully signed up to our service", Toast.LENGTH_SHORT).show();
                                Log.d("SignUpFragment", "Successful sign up.");
                                Intent toCalendarActivityIntent = new Intent(getActivity(), CalendarActivity.class);
                                startActivity(toCalendarActivityIntent);
                            }
                        }
                    });
            }
        });
        return v;
    }

/*
* The following code validtes user existence and adds
* the users name and ID to the database on first time sign up.
* The reason is to enable finding them in the contacts list
* */

    // Checking Firebase for existing user
    private void validateUserExistence(final FirebaseUser user){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        String currentUserId = user.getUid();
        dbRef.child("users").orderByChild("userId").equalTo(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    User newUser = new User(user.getUid(), user.getDisplayName());
                    addUserToDB(newUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // Add a new user to a DB, used in chat for sender and receiver.
    private void addUserToDB(User user){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("isAdmin",user.isAdmin());
        userMap.put("userId", user.getUserId());
        userMap.put("userName", user.getUserName());
        dbRef.child("users").push().setValue(userMap);
    }
}
