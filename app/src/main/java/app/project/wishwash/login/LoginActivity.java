package app.project.wishwash.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import app.project.wishwash.R;
import app.project.wishwash.chat.models.User;

public class LoginActivity extends AppCompatActivity {

    private Button signInBtn;

    private static final int RC_SIGN_IN = 1708;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInBtn = findViewById(R.id.signInBtn);

            signInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK){
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, user.getUid());

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
                Intent intent = new Intent(LoginActivity.this, TempMenu.class);
                startActivity(intent);
            } else {
                Log.d(TAG, response.getError().getMessage());
            }
        }
    }

    private void addUserToDB(User user){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userId", user.getId());
        userMap.put("userName", user.getName());
        dbRef.child("users").push().setValue(userMap);
    }
}
