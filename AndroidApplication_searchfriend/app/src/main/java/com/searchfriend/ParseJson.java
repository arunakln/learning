package com.searchfriend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Aruna Duraisingam - ad543
 * Date 30/Mar/2016
 * Class to parse the provided json data into FriendDataModel class
 */
public class ParseJson {

    String myLocation_lat = "51.297276";
    String myLocation_lon = "1.069723";

    public ParseJson(){
        // default constructor
    }

    /*
        Method to convert the json object to an array of FriendDataModel object
        param: jsonData - the json object obtained from the webservice call
        return: the parsed FriendList for plotting in map
     */
    public List<HashMap<String, FriendDataModel>> jsonParser(JSONObject jsonData){
        JSONArray jsonArray = null;
        try{
            jsonArray = jsonData.getJSONArray("Users");
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return getFriendList(jsonArray);
    }

    /*
        Private method to help in parsing the jsonData and store friendData obejct in a HashMap
        param: jsonData - the json object obtained from the webservice call
        return: the parsed FriendList
    */

    private List<HashMap<String, FriendDataModel>> getFriendList (JSONArray jsonData){

        List<HashMap<String, FriendDataModel>>  friendsList = new ArrayList<HashMap<String, FriendDataModel>>();
        int i = 0;

        for( i=0; i< jsonData.length(); i++){
            try{
                friendsList.add(getFriendData((JSONObject) jsonData.get(i), String.valueOf(i)));
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        friendsList.add(addMyLocation(String.valueOf(i)));
        return friendsList;
    }

    /*
        Private method to help in parse individual friend data to a hashmap.
        param: jsonData - the json object obtained from the webservice call
        return: the parsed FriendList
    */

    private HashMap<String, FriendDataModel> getFriendData(JSONObject jsonData, String index){
        HashMap<String, FriendDataModel> friendData = new HashMap<String, FriendDataModel>();

        FriendDataModel fd = new FriendDataModel();
        try{
            if(!jsonData.isNull("name")){
                fd.setName((jsonData.getString("name")));
                fd.setLatitude(jsonData.getString("lat"));
                fd.setLongitude(jsonData.getString("lon"));
                friendData.put(index,fd);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return friendData;
    }

    private HashMap<String, FriendDataModel> addMyLocation(String index){
        HashMap<String, FriendDataModel> friendData = new HashMap<String, FriendDataModel>();

        FriendDataModel fd = new FriendDataModel();

        fd.setName("My Location");
        fd.setLatitude(myLocation_lat);
        fd.setLongitude(myLocation_lon);
        friendData.put(index,fd);

        return friendData;
    }
}
