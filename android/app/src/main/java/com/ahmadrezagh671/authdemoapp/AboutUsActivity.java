package com.ahmadrezagh671.authdemoapp;

import static com.ahmadrezagh671.authdemoapp.Utilities.AppUtil.getAppVersionStr;
import static com.ahmadrezagh671.authdemoapp.Utilities.AppUtil.openLink;
import static com.ahmadrezagh671.authdemoapp.Utilities.AppUtil.sendEmail;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutUsActivity extends AppCompatActivity {

    ImageButton backButton;
    TextView appVersionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_us);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appVersionTV = findViewById(R.id.appVersionTV);
        appVersionTV.setText("Version "  + getAppVersionStr(this));

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void openGithub(View view) {
        openLink(getResources().getString(R.string.githubLink),this);
    }

    public void contactUs(View view) {
        sendEmail(this);
    }
}