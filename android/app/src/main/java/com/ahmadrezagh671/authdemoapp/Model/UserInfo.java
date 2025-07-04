package com.ahmadrezagh671.authdemoapp.Model;

import static com.ahmadrezagh671.authdemoapp.Utilities.AppUtil.convertTimestampToString;

public class UserInfo {

    String id;
    String username,name;
    int subscription;
    long created;

    public UserInfo(String id, String username, String name, int subscription, long created) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.subscription = subscription;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubscription() {
        return subscription;
    }

    public void setSubscription(int subscription) {
        this.subscription = subscription;
    }

    public long getCreated() {
        return created;
    }

    public String getCreatedStr() {
        return convertTimestampToString(created);
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
