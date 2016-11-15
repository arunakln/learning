package com.searchfriend;

/**
 * Created by Aruna Duraisingam - ad543 on 23/03/2016.
 * Data Model object class to hold the friend data
 */
public class FriendDataModel {

    private String name;
    private String latitude;
    private String longitude;

    public FriendDataModel(String name, String latitude, String longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public FriendDataModel(){
        // default constructor
    }

    // accessors

    public String getName(){
        return this.name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    // mutators

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}

