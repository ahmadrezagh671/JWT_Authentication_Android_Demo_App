package com.ahmadrezagh671.authdemoapp.AuthActivities;

import static com.ahmadrezagh671.authdemoapp.Requests.AuthRequest.sendSignInRequest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ahmadrezagh671.authdemoapp.MainActivity;
import com.ahmadrezagh671.authdemoapp.R;
import com.ahmadrezagh671.authdemoapp.Requests.AuthRequest;

public class SignUpActivity extends AppCompatActivity {

    final static private String TAG = "SignUpActivity";

    EditText usernameET,passwordET,passwordET2;
    Button submitBTN;
    TextView signInTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        passwordET2 = findViewById(R.id.passwordET2);
        submitBTN = findViewById(R.id.signUpBTN);
        signInTV = findViewById(R.id.signInTV);

        signInTV.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            finish();
        });

        submitBTN.setOnClickListener(view -> {
            if (checkEditTexts()){
                setEnableForLoading(false);
                submit();
            }
        });
    }

    private boolean checkEditTexts() {
        if (usernameET.getText().toString().isEmpty()){
            usernameET.setError("Username is required");
            return false;
        }
        if (passwordET.getText().toString().isEmpty()){
            passwordET.setError("Password is required");
            return false;
        }
        if (usernameET.getText().toString().length() < 4 || usernameET.getText().toString().length() > 32){
            usernameET.setError("Username must be between 4 and 32 characters");
            return false;
        }
        if (passwordET.getText().toString().length() < 8 || passwordET.getText().toString().length() > 32){
            passwordET.setError("Password must be between 8 and 32 characters");
            return false;
        }
        if (!passwordET.getText().toString().equals(passwordET2.getText().toString())){
            passwordET2.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private void setEnableForLoading(Boolean enable){
        submitBTN.setEnabled(enable);
        usernameET.setEnabled(enable);
        passwordET.setEnabled(enable);
        passwordET2.setEnabled(enable);
    }

    private void submit() {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString();

        AuthRequest.sendSignUpRequest(username, password, new AuthRequest.SignUpCallback() {
            @Override
            public void onSuccess() {
                sendSignInRequest(username, password, new AuthRequest.SignInCallback() {
                    @Override
                    public void onSuccess() {
                        AuthRequest.sendAuthRequest(new AuthRequest.AuthCallback() {
                            @Override
                            public void onSuccess() {
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onError(String error) {
                                pushError(error);
                            }

                            @Override
                            public void onNoTokenFound(String error) {
                                pushError(error);
                            }

                            @Override
                            public void onNetworkError(String error) {
                                pushError(error);
                            }

                            @Override
                            public void onUserBanned() {
                                startActivity(new Intent(SignUpActivity.this,BanUserActivity.class));
                                finish();
                            }
                        },SignUpActivity.this);
                    }
                    @Override
                    public void onError(String error) {
                        pushError(error);
                    }

                    @Override
                    public void onNetworkError(String error) {
                        pushError(error);
                    }
                },SignUpActivity.this);
            }
            @Override
            public void onError(String error) {
                pushError(error);
            }

            @Override
            public void onNetworkError(String error) {
                pushError(error);
            }
        },this);
    }

    public void pushError(String text){
        Log.e(TAG, text);
        Toast.makeText(SignUpActivity.this, text, Toast.LENGTH_SHORT).show();
        setEnableForLoading(true);
    }
}