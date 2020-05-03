package app.project.wishwash.chat.models;

import java.util.List;

import app.project.wishwash.Booking;

public class User {
    private String userId;
    private String userName;
    private boolean isAdmin = false;


    public User(){}

    public User(String userId , String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
