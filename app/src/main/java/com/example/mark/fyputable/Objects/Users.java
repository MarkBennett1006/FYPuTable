package com.example.mark.fyputable.Objects;

import java.util.List;

public class Users {

    private String userID;
    private String userName;
    private String email;
    private String userType;
    private String institutionID;


    public Users(String userID, String userName, String email, String userType, String institutionID) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.userType = userType;
        this.institutionID = institutionID;
    }


    public Users() {
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

    public String getInstitutionID() {
        return institutionID;
    }

    public void setInstitutionID(String institutionID) {
        this.institutionID = institutionID;
    }
}


