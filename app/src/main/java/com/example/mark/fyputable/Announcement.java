package com.example.mark.fyputable;

public class Announcement {

    private String Title;
    private Long AnnouncementID;
    private String ModuleID;
    private String Content;

    public Announcement() {
    }

    public Announcement(String title, Long announcementID, String moduleID, String content) {
        Title = title;
        AnnouncementID = announcementID;
        ModuleID = moduleID;
        Content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Long getAnnouncementID() {
        return AnnouncementID;
    }

    public void setAnnouncementID(Long announcementID) {
        AnnouncementID = announcementID;
    }

    public String getModuleID() {
        return ModuleID;
    }

    public void setModuleID(String moduleID) {
        ModuleID = moduleID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
