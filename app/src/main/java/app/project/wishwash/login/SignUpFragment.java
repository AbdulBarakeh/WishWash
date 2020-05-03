package app.project.wishwash.login;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import app.project.wishwash.R;
import app.project.wishwash.calender.CalendarActivity;
import app.project.wishwash.chat.models.User;

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

                if (!SignInActivity.isValidEmail(email)) {
                    Toast.makeText(getContext(), "Email not accepted - try again!", Toast.LENGTH_SHORT).show();
                } else if (password.length()<8) {
                    Toast.makeText(getContext(), "Password needs to be at least 8 characters long!", Toast.LENGTH_SHORT).show();
                } else if (!SignInActivity.isValidPassword(password)) {
                    Toast.makeText(getContext(), "Password not enough complex!", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(password_reentered)) {
                    Toast.makeText(getContext(), "Passwords do not match - try again!", Toast.LENGTH_SHORT).show();
                } else
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(), "SignUp unsuccessful. Please try again", Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(full_name).build();
                                user.updateProfile(profileUpdates);
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


}
