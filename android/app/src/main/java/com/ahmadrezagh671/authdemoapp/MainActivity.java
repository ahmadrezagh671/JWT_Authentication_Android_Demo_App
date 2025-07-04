package com.ahmadrezagh671.authdemoapp;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ahmadrezagh671.authdemoapp.Adapters.AdapterUsers;
import com.ahmadrezagh671.authdemoapp.Model.GlobalUserData;
import com.ahmadrezagh671.authdemoapp.Requests.AuthRequest;
import com.ahmadrezagh671.authdemoapp.Requests.GlobalRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    AdapterUsers adapterUsers;

    SwipeRefreshLayout swipeRefreshLayout;

    Button tryAgainButton;
    TextView errorTV;

    List<GlobalUserData> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        users = new ArrayList<>();

        tryAgainButton = findViewById(R.id.tryAgainButton);
        tryAgainButton.setOnClickListener(v -> reload());

        errorTV = findViewById(R.id.errorTV);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestGetUsers();
    }

    private void requestGetUsers() {
        refreshing(true);

        GlobalRequest.getAllUsersRequest(new GlobalRequest.GetAllUsersCallback() {
            @Override
            public void onSuccess(List<GlobalUserData> result_users) {
                users = result_users;

                if (users.isEmpty()) {
                    errorTV.setText("Nothing Found");
                    errorTV.setVisibility(VISIBLE);
                }

                adapterUsers = new AdapterUsers(users, MainActivity.this, MainActivity.this);
                recyclerView.setAdapter(adapterUsers);

                /*if(adapterUsers == null) {
                    adapterUsers = new AdapterUsers(users, MainActivity.this, MainActivity.this);
                    recyclerView.setAdapter(adapterUsers);
                }else {
                    recyclerView.scrollToPosition(0);
                    adapterUsers.notifyDataSetChanged();
                }*/

                refreshing(false);
            }

            @Override
            public void onError(String error) {
                errorTV.setText(error);
                errorTV.setVisibility(VISIBLE);
                tryAgainButton.setVisibility(VISIBLE);

                if(adapterUsers != null) {
                    users.clear();
                    adapterUsers.notifyDataSetChanged();
                }

                refreshing(false);
            }

            @Override
            public void onNetworkError(String error) {
                errorTV.setText(error);
                errorTV.setVisibility(VISIBLE);
                tryAgainButton.setVisibility(VISIBLE);

                if(adapterUsers != null) {
                    users.clear();
                    adapterUsers.notifyDataSetChanged();
                }

                refreshing(false);
            }
        },this);
    }

    private void refreshing(boolean state){
        swipeRefreshLayout.setRefreshing(state);
    }

    private void reload(){
        requestGetUsers();
        tryAgainButton.setVisibility(GONE);
        errorTV.setVisibility(GONE);
    }



    public void optionsIB(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.profile){
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else if (id == R.id.logout) {
                    AuthRequest.logout(MainActivity.this);
                }else if (id == R.id.about){
                    startActivity(new Intent(MainActivity.this, AboutUsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void onRefresh() {
        reload();
    }
}