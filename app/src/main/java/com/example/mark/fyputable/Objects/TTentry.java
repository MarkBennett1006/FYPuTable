package com.example.mark.fyputable.Objects;

public class TTentry {
   private Long EntryID;
    private String Location;
    private String ModuleID;
    private String Name;
    private String Day;
    private String Time;

    public TTentry() {
    }

    public TTentry(Long entryID, String location, String moduleID, String name, String day, String time) {
        EntryID = entryID;
        Location = location;
        ModuleID = moduleID;
        Name = name;
        Day = day;
        Time = time;
    }



    public Long getEntryID() {
        return EntryID;
    }

    public void setEntryID(Long entryID) {
        EntryID = entryID;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getModuleID() {
        return ModuleID;
    }

    public void setModuleID(String moduleID) {
        ModuleID = moduleID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }



}
