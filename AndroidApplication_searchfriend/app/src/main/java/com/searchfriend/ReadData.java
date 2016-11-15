package com.searchfriend;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Aruna Duraisingam - ad543 on 30/03/2016.
 * Class to read the data using the restful webservice
 */
public class ReadData extends AsyncTask<Object, Integer, String> {

    private String locationData = null;
    private GoogleMap googleMap;


    /*
    * Method that calls the webservice utility class to make a restful connection to the server
    * and obtain the friend location data asynchronously.
    * Param: Input object which is google map and the webservice URL location
    * */
    @Override
    protected String doInBackground(Object... inputObj) {
        try {
            googleMap = (GoogleMap) inputObj[0];
            String serverURL = (String) inputObj[1];
            WebServiceUtility http = new WebServiceUtility();
            locationData = http.read(serverURL);
        } catch (Exception e) {
            Log.d("ReadData", e.toString());
        }
        return locationData;
    }

    /*
    * method pass the obtained json data to displayfriend class to plot the friend
    * location asynchronously.
    * Param: the obtained json data from the server.
    * */
    @Override
    protected void onPostExecute(String result) {
        DisplayFriend displayLocation = new DisplayFriend();
        displayLocation.execute(new Object[] {googleMap, result});
    }
}
