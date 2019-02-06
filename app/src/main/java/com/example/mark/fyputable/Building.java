package com.example.mark.fyputable;

public class Building {

   String buildingName;
   String placeID;


    public Building(String buildingName, String placeID) {
        this.buildingName = buildingName;
        this.placeID = placeID;
    }

    public Building() {
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
}