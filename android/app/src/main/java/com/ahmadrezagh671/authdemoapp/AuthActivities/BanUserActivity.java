package com.ahmadrezagh671.authdemoapp.AuthActivities;

import static com.ahmadrezagh671.authdemoapp.Utilities.AppUtil.sendEmail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ahmadrezagh671.authdemoapp.R;
import com.ahmadrezagh671.authdemoapp.Requests.AuthRequest;

public class BanUserActivity extends AppCompatActivity {

    Button exitApp,bannedContact,logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ban_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        exitApp = findViewById(R.id.exitApp);
        logOut = findViewById(R.id.logOut);
        bannedContact = findViewById(R.id.bannedContact);

        exitApp.setOnClickListener(this::exitApp);
        logOut.setOnClickListener(this::logOut);
        bannedContact.setOnClickListener(this::bannedContact);

    }

    public void bannedContact(View view) {
        sendEmail(this);
    }

    public void exitApp(View view) {
        finish();
    }

    public void logOut(View view) {
        AuthRequest.logout(BanUserActivity.this);
    }
}