package app.project.wishwash.fragments.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import app.project.wishwash.R;

public class SignInSignUpFragment extends Fragment {
    // Declaring variables:
    private Button btn_sign_in, btn_sign_up;
    private FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in_sign_up, container, false);

        // Assigning variables:
        btn_sign_in = v.findViewById(R.id.Button_signinsignup_signin);
        btn_sign_up = v.findViewById(R.id.Button_signinsignup_signup);

        // Accessing variables:
        // Opens respective fragments when clinking on sign in or sign up
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(SignInFragment.newInstance("",""));
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(SignUpFragment.newInstance("",""));
            }
        });

        return v;
    }

    // Inflate fragment and add to backstack
    public void openFragment(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameLayout_signinsignup, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
