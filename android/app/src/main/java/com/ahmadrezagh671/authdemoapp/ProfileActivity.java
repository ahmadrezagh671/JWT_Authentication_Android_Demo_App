package com.ahmadrezagh671.authdemoapp;

import static com.ahmadrezagh671.authdemoapp.Utilities.DialogUtil.editNameDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ahmadrezagh671.authdemoapp.Model.UserInfo;
import com.ahmadrezagh671.authdemoapp.Requests.UserRequest;
import com.ahmadrezagh671.authdemoapp.Utilities.DialogUtil;

public class ProfileActivity extends AppCompatActivity {

    ImageButton backButton;
    TextView nameTV, usernameTV, subscriptionTV, createdTV;
    ImageButton nameEditIB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nameTV = findViewById(R.id.nameTV);
        nameEditIB = findViewById(R.id.nameEditIB);
        nameEditIB.setOnClickListener(this::nameEditIB_Click);

        usernameTV = findViewById(R.id.usernameTV);
        subscriptionTV = findViewById(R.id.subscriptionTV);
        createdTV = findViewById(R.id.createdTV);

        UserRequest.getUserInfoRequest(new UserRequest.UserInfoCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                String name = userInfo.getName();
                nameTV.setText("Name: " + (!name.isEmpty() ? name : "No name specified."));
                usernameTV.setText("Username: " + userInfo.getUsername());
                subscriptionTV.setText("Subscription: " + getResources().getStringArray(R.array.subscription_types)[userInfo.getSubscription()]);
                createdTV.setText("Joined: " + userInfo.getCreatedStr());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetworkError(String error) {
                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        },this);
    }

    private void nameEditIB_Click(View view) {
        editNameDialog(this, new DialogUtil.EditNameDialogCallback() {
            @Override
            public void onSubmit(String input, android.app.AlertDialog dialog) {
                UserRequest.updateNameRequest(input, new UserRequest.UpdateNameCallback() {
                    @Override
                    public void onSuccess() {
                        nameTV.setText("Name: " + input);
                        Toast.makeText(ProfileActivity.this, "Name changed successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onNetworkError(String error) {
                        Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                },ProfileActivity.this);
            }

            @Override
            public void onCancel(android.app.AlertDialog dialog) {
                dialog.cancel();
            }
        });
    }
}