package app.project.wishwash.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.example.wishwash_demo.Fragments.SignInSignUpFragment;
import com.example.wishwash_demo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    // Declaring variables:
    private Fragment fragment;
    private FragmentTransaction transaction;
    private boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Firebase - Create a password-based account - inspiration: https://firebase.google.com/docs/auth/android/password-auth
        firebaseAuth = FirebaseAuth.getInstance();

        initFirstFragment();

    }

    // From: https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    // From: https://stackoverflow.com/questions/36574183/how-to-validate-password-field-in-android
    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        // 1 Uppercase, 1 Number and 1 Symbol
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    // Inflates --- SignInSignUpFragment/fragment_sign_in_sign_up --- as the first fragment to be shown when app is started
    private void initFirstFragment() {
        fragment = new SignInSignUpFragment();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameLayout_signinsignup, fragment);
        transaction.commit();
    }

    // Press two times on device back-button to exit app
    // Inspiration from:
    // https://stackoverflow.com/questions/8430805/clicking-the-back-button-twice-to-exit-an-activity
    // https://stackoverflow.com/questions/5448653/how-to-implement-onbackpressed-in-fragments
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
        else
            getSupportFragmentManager().popBackStack();
    }
}
