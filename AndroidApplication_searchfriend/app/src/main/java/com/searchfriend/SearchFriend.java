package com.searchfriend;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;
import java.util.Locale;

/*
* Created by Aruna Duraisingam - ad543 on 28/03/2016
*
* This Application will get the friend location son data which is present in university of kent
* server using restful webservice call and plot the friend location in the google Map.
*
* It gets the latitude longitude coordinates from the json and use
* Geocoder API to get the address and displays it.
*
* It has menu options to choose the menu type like terrain, hrbrid etc...
*
* This is the main class which helps in plotting the friend location in map.
* */


public class SearchFriend extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String dataURL = "https://www.cs.kent.ac.uk/people/staff/iau/LocalUsers.php";

    /*
    * Main method to create the google map with friend location markers
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Method that reads the friend location asynchronously from server and plot it in the map
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(mMap == null){
            Toast.makeText(getApplicationContext(), "Sorry! not able to create map", Toast.LENGTH_SHORT).show();
        }
        ReadData asyncRead = new ReadData();
        asyncRead.execute(new Object[] {mMap, dataURL.toString()});

        // Setting a custom info window adapter for the google map
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {


            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            public View getInfoContents(Marker marker) {

                // Getting view from the layout file info_window_layout                             
                View v = getLayoutInflater().inflate(R.layout.custom_marker_info_window, null);

                // Setting the title
                String title = marker.getTitle();
                TextView vTitle = ((TextView) v.findViewById(R.id.title));


                if (title != null) {
                    SpannableString titleText = new SpannableString(title);
                    titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                    vTitle.setText(titleText);
                } else {
                    vTitle.setText("");
                }

                String snippet = marker.getSnippet();
                ImageView profilepic = (ImageView) v.findViewById(R.id.profilePic);
                profilepic.setImageResource(getResources().getIdentifier(
                        snippet, "drawable", "com.searchfriend"));

                TextView snippetUi = ((TextView) v.findViewById(R.id.snippet));
                snippetUi.setText(getCompleteAddressString(marker.getPosition().latitude, marker.getPosition().longitude));

                // Returning the view containing InfoWindow contents
                return v;
            }


        });
    }

    /*
    * method that creates menu to display various mapType options
    * Param: menu to be displayed
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
    * method that displays the map according to the MapType selected by the user
    * Param: menu item selected by the user
    * */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {

        String strAdd = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,
                    LONGITUDE, 1);

            if (addresses != null) {

                Address returnedAddress = addresses.get(0);

                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {

                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            ",");
                }

                strAdd = strReturnedAddress.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }
}
