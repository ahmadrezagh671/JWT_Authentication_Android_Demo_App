package com.ahmadrezagh671.authdemoapp.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrezagh671.authdemoapp.Model.GlobalUserData;
import com.ahmadrezagh671.authdemoapp.R;


import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.ViewHolder> {

    private static final String TAG = "AdapterUsers";
    List<GlobalUserData> users;
    Context context;
    Activity activity;

    public AdapterUsers(List<GlobalUserData> users, Context context, Activity activity) {
        this.users = users;
        this.context = context;
        this.activity = activity;
    }


    @NonNull
    @Override
    public AdapterUsers.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_user,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUsers.ViewHolder holder, int position) {
        GlobalUserData user = users.get(position);

        holder.usernameTV.setText(user.getUsername());
        holder.nameTV.setText(user.getName());
        holder.lastLoginTV.setText(user.getLastLoginStr());
        holder.subscriptionTV.setText(activity.getResources().getStringArray(R.array.subscription_types)[user.getSubscription()]);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        TextView usernameTV, subscriptionTV, nameTV, lastLoginTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            usernameTV = view.findViewById(R.id.usernameTV);
            subscriptionTV = view.findViewById(R.id.subscriptionTV);
            nameTV = view.findViewById(R.id.nameTV);
            lastLoginTV = view.findViewById(R.id.lastLoginTV);
        }
    }
}
