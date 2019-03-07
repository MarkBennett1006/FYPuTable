package com.example.mark.fyputable.Objects;

public class Entry {

    private String Building;
    private String moduleCode;
    private String moduleName;
    private String Date;
    private String startTime;
    private String endTime;
    private String classType;
    private String Room;
    private String entryID;
    private String userID;





    public Entry() {
    }

    public Entry(String building, String moduleCode, String moduleName, String date, String startTime, String endTime, String classType, String room, String entryID, String userID) {
        Building = building;
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        Date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classType = classType;
        Room = room;
        this.entryID = entryID;
        this.userID = userID;
    }

    public String getBuilding() {
        return Building;
    }

    public void setBuilding(String building) {
        Building = building;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getEntryID() {
        return entryID;
    }

    public void setEntryID(String entryID) {
        this.entryID = entryID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
