package com.example.mark.fyputable;

public class Announcement {

    private String Title;
    private String announcementID;
    private String moduleCode;
    private String msgContent;
    private String Date;
    private String authName;
    private String authID;

    public Announcement() {

    }

    public Announcement(String title, String announcementID, String moduleCode, String msgContent, String date, String authName, String authID) {
        Title = title;
        this.announcementID = announcementID;
        this.moduleCode = moduleCode;
        this.msgContent = msgContent;
        Date = date;
        this.authName = authName;
        this.authID = authID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAnnouncementID() {
        return announcementID;
    }

    public void setAnnouncementID(String announcementID) {
        this.announcementID = announcementID;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getAuthID() {
        return authID;
    }

    public void setAuthID(String authID) {
        this.authID = authID;
    }
}