package com.example.mark.fyputable;

public class Users {

    String userID;
    String userName;
    String email;
    String userType;
    String[] RegisteredModules;

    public Users() {
    }

    public Users(String userID, String userName, String email, String userType, String[] registeredModules) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.userType = userType;
        RegisteredModules = registeredModules;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String[] getRegisteredModules() {
        return RegisteredModules;
    }

    public void setRegisteredModules(String[] registeredModules) {
        RegisteredModules = registeredModules;
    }
}
