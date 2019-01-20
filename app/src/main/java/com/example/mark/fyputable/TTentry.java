package com.example.mark.fyputable;

public class TTentry {
   private Long EntryID;
    private String Location;
    private String ModuleID;
    private String Name;

    public TTentry() {
    }

    public TTentry(Long entryID, String location, String moduleID, String name) {
        EntryID = entryID;
        Location = location;
        ModuleID = moduleID;
        Name = name;
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
}
