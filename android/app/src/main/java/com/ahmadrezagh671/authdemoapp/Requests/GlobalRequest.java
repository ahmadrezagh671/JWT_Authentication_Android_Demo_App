package com.ahmadrezagh671.authdemoapp.Requests;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;

import com.ahmadrezagh671.authdemoapp.Model.GlobalUserData;
import com.ahmadrezagh671.authdemoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GlobalRequest {

    static public void getAllUsersRequest(GetAllUsersCallback callback, Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("auth", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        OkHttpClient client = new OkHttpClient();

        String address = activity.getResources().getString(R.string.url);

        Request request = new Request.Builder()
                .url(address+ "/users")
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
                                JSONArray jsonArray = new JSONArray(response.body().string());

                                List<GlobalUserData> users = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    GlobalUserData user = new GlobalUserData(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("username"),
                                            jsonObject.getString("name"),
                                            jsonObject.getInt("subscription"),
                                            jsonObject.getLong("created"),
                                            jsonObject.getLong("lastLogin")
                                    );

                                    users.add(user);
                                }

                                callback.onSuccess(users);
                            } catch (IOException | JSONException e) {
                                callback.onError("Failed to parse users.");
                            }
                        } else {
                            callback.onError(String.valueOf(response.code()));
                        }
                    }
                });
            }
        });
    }

    public interface GetAllUsersCallback {
        void onSuccess(List<GlobalUserData> users);
        void onError(String error);
        void onNetworkError(String error);
    }

}
