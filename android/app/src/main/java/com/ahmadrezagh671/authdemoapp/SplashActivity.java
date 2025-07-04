package com.ahmadrezagh671.authdemoapp;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ahmadrezagh671.authdemoapp.AuthActivities.BanUserActivity;
import com.ahmadrezagh671.authdemoapp.AuthActivities.SignUpActivity;
import com.ahmadrezagh671.authdemoapp.Requests.AuthRequest;


public class SplashActivity extends AppCompatActivity {

    Button retryBTN;
    ProgressBar progressBar;
    TextView eventTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        retryBTN = findViewById(R.id.retryBTN);
        retryBTN.setOnClickListener(view -> tryCheckAuthToken());

        eventTV = findViewById(R.id.eventTV);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(10);

        tryCheckAuthToken();
    }

    private void tryCheckAuthToken() {
        retryBTN.setVisibility(GONE);
        progressBar.setProgress(3);
        eventTV.setText("Connecting to the server, please wait.");
        AuthRequest.sendAuthRequest(new AuthRequest.AuthCallback() {
            @Override
            public void onSuccess() {
                eventTV.setText("You're in!");
                progressBar.setProgress(10);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(String error) {
                eventTV.setText(error);
                progressBar.setProgress(9);
                retryBTN.setVisibility(VISIBLE);
            }

            @Override
            public void onNoTokenFound(String error) {
                eventTV.setText("Oops! You need to sign in first.");
                progressBar.setProgress(10);
                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
                finish();
            }

            @Override
            public void onNetworkError(String error) {
                eventTV.setText(error);
                progressBar.setProgress(2);
                retryBTN.setVisibility(VISIBLE);
            }

            @Override
            public void onUserBanned() {
                eventTV.setText("Banned. Contact support");
                progressBar.setProgress(1);
                startActivity(new Intent(SplashActivity.this, BanUserActivity.class));
                finish();
            }
        },SplashActivity.this);
    }

}