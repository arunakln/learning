package com.searchfriend;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aruna Duraisingam - ad543 on 28/03/2016.
 * Class to help the obtained friend data in google map asynchronously
 */

public class DisplayFriend extends AsyncTask<Object, Integer, List<HashMap<String, FriendDataModel>>> {
    JSONObject jsonData;
    GoogleMap googleMap;
    ParseJson parse = new ParseJson();
    private HashMap<MarkerOptions, String> markers;

    /*
        override method to get the json data in the background asynchronously
        Param: input object that contains the json data and the map object
        returns: returns the parsed friend location list
     */

    @Override
    protected List<HashMap<String, FriendDataModel>> doInBackground(Object... ObjectInput) {

        List<HashMap<String, FriendDataModel>> placesList = null;


        try {
            googleMap = (GoogleMap) ObjectInput[0];
            jsonData = new JSONObject((String) ObjectInput[1]);
            placesList = parse.jsonParser(jsonData);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return placesList;
    }

    /*
        override method to plot the marker in the google map,  once the json data is parsed.
        It is also executed asynchronously.
        Param: List the friend location to plot
     */

    @Override
    protected void onPostExecute(List<HashMap<String, FriendDataModel>> list) {
        googleMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if(list.size() >0) {

            for (int i = 0; i < list.size(); i++) {
                MarkerOptions marker = new MarkerOptions();
                HashMap<String, FriendDataModel> googlePlace = list.get(i);
                FriendDataModel data = googlePlace.get(String.valueOf(i));
                double lat = Double.parseDouble(data.getLatitude());
                double lng = Double.parseDouble(data.getLongitude());
                String name = data.getName();

                LatLng latLng = new LatLng(lat, lng);
                marker.position(latLng);
                marker.title(name);
                String imageName = "img" + i;
                marker.snippet(imageName);
                if (i != list.size() - 1) {
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow));
                }

                builder.include(marker.getPosition());
                googleMap.addMarker(marker);

            }

            LatLngBounds bounds = builder.build();
            int padding = 80; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.animateCamera(cu);
        }
    }



}



