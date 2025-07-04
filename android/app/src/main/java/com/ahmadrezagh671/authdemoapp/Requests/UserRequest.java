package com.ahmadrezagh671.authdemoapp.Requests;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;

import com.ahmadrezagh671.authdemoapp.Model.UserInfo;
import com.ahmadrezagh671.authdemoapp.R;

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

public class UserRequest {

    static public void getUserInfoRequest(UserInfoCallback callback, Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("auth", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        OkHttpClient client = new OkHttpClient();

        String address = activity.getResources().getString(R.string.url);

        Request request = new Request.Builder()
                .url(address+ "/userinfo")
                .addHeader("Authorization", "Bearer " + token)
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
                                JSONObject jsonObject = new JSONObject(response.body().string());

                                UserInfo userinfo = new UserInfo(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("username"),
                                        jsonObject.getString("name"),
                                        jsonObject.getInt("subscription"),
                                        jsonObject.getLong("created")
                                );

                                callback.onSuccess(userinfo);
                            } catch (IOException | JSONException e) {
                                callback.onError("Failed to parse user info.");
                            }
                        } else {
                            callback.onError(String.valueOf(response.code()));
                        }
                    }
                });
            }
        });
    }

    public interface UserInfoCallback {
        void onSuccess(UserInfo userInfo);
        void onError(String error);
        void onNetworkError(String error);
    }

    static public void updateNameRequest(String name, UpdateNameCallback callback, Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("auth", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        OkHttpClient client = new OkHttpClient();

        String json = "{"
                + "\"name\":\"" + name + "\""
                + "}";

        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json; charset=utf-8")
        );

        String address = activity.getResources().getString(R.string.url);

        Request request = new Request.Builder()
                .url(address+ "/name")
                .addHeader("Authorization", "Bearer " + token)
                .patch(body)
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
                            callback.onError("Failed to set name.");
                        }
                    }
                });
            }
        });
    }

    public interface UpdateNameCallback {
        void onSuccess();
        void onError(String error);
        void onNetworkError(String error);
    }
}
