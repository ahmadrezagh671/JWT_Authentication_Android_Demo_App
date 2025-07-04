package com.ahmadrezagh671.authdemoapp.Requests;

import static android.content.Context.MODE_PRIVATE;
import static com.ahmadrezagh671.authdemoapp.Utilities.AppUtil.getAppVersionCode;
import static com.ahmadrezagh671.authdemoapp.Utilities.AppUtil.getDeviceModel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ahmadrezagh671.authdemoapp.R;
import com.ahmadrezagh671.authdemoapp.AuthActivities.SignInActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthRequest {

    static public void sendSignUpRequest(String username, String password, SignUpCallback callback, Activity activity) {
        OkHttpClient client = new OkHttpClient();
        String json = "{"
                + "\"username\":\"" + username + "\","
                + "\"password\":\"" + password + "\""
                + "}";

        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json; charset=utf-8")
        );

        String address = activity.getResources().getString(R.string.url);

        Request request = new Request.Builder()
                .url(address+ "/signup")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onNetworkError(activity.getResources().getString(R.string.networkError));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError("Username is unavailable.");
                        }
                    }
                });
            }
        });
    }

    public interface SignUpCallback {
        void onSuccess();
        void onError(String error);
        void onNetworkError(String error);
    }

    static public void sendSignInRequest(String username, String password, SignInCallback callback, Activity activity) {
        OkHttpClient client = new OkHttpClient();
        String json = "{"
                + "\"username\":\"" + username + "\","
                + "\"password\":\"" + password + "\""
                + "}";

        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json; charset=utf-8")
        );

        String address = activity.getResources().getString(R.string.url);

        Request request = new Request.Builder()
                .url(address+ "/signin")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onNetworkError(activity.getResources().getString(R.string.networkError));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("auth", MODE_PRIVATE);
                                String token = new JSONObject(response.body().string()).getString("token");
                                sharedPreferences.edit().putString("token", token).apply();
                                callback.onSuccess();
                            } catch (IOException | JSONException e) {
                                callback.onError("We’re having trouble with your account. Please try again or contact support.");
                            }
                        } else {
                            if (response.code() == 409){
                                callback.onError("Invalid username or password.");
                            }else {
                                callback.onError("Server error: " + response.code());
                            }

                        }
                    }
                });
            }
        });
    }

    public interface SignInCallback {
        void onSuccess();
        void onError(String error);
        void onNetworkError(String error);
    }

    static public void sendAuthRequest(AuthCallback callback, Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("auth", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null){
            callback.onNoTokenFound(activity.getResources().getString(R.string.tokenFailedError));
        }else {
            int appVersion = getAppVersionCode(activity);
            String deviceId = getDeviceModel();

            OkHttpClient client = new OkHttpClient();
            String json = "{"
                    + "\"appVersion\":" + appVersion + ","
                    + "\"device\":\"" + deviceId + "\""
                    + "}";

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.get("application/json; charset=utf-8")
            );

            String address = activity.getResources().getString(R.string.url);

            Request request = new Request.Builder()
                    .url(address+ "/authenticate")
                    .addHeader("Authorization", "Bearer " + token)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onNetworkError(activity.getResources().getString(R.string.networkError));
                        }
                    });
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()) {
                                callback.onSuccess();
                            } else if (response.code() == 403){
                                callback.onUserBanned();
                            }else {
                                callback.onError(String.valueOf(response.code()));
                            }
                        }
                    });
                }
            });
        }
    }

    public interface AuthCallback {
        void onSuccess();
        void onError(String error);
        void onNoTokenFound(String error);
        void onNetworkError(String error);
        void onUserBanned();
    }

    static public void logout(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("auth", MODE_PRIVATE);
        sharedPreferences.edit().remove("token").apply();

        activity.startActivity(new Intent(activity, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        activity.finish();
    }
}

