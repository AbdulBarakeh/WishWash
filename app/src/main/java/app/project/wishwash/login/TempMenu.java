package app.project.wishwash.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.project.wishwash.R;
import app.project.wishwash.chat.activities.UserListActivity;

public class TempMenu extends AppCompatActivity {
    Intent GoToChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_menu);
        GoToChat = new Intent(this, UserListActivity.class);

        Button GoToChat_btn = findViewById(R.id.temp_go_to_chat);
        GoToChat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GoToChat);
                finish();
            }
        });

    }
}
