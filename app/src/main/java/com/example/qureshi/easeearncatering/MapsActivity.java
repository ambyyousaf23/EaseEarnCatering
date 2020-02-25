package com.example.qureshi.easeearncatering;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.SharedDataClass;
import com.example.qureshi.easeearncatering.DataModels.CatersDataModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    RequestQueue requestQueue;
    List<CatersDataModel> catersDetail=new ArrayList<CatersDataModel>();
    final String getCaters="https://easeearn.000webhostapp.com/getCaterInfo.php";
    String var;
    String[] loclatlng;
    Double finallat, finallng;
    Location me, dest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        requestQueue= Volley.newRequestQueue(this);

        me=new Location("");
        dest=new Location("");

        me.setLatitude(SharedDataClass.latitude);
        me.setLongitude(SharedDataClass.longitude);

    }


    /**
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        JsonObjectRequest req1 = new JsonObjectRequest(getCaters, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arry1 = response.getJSONArray("list");
                    for (int i = 0; i < arry1.length(); i++) {
                        JSONObject jsonObject = arry1.getJSONObject(i);
                        CatersDataModel catersDataModel = new CatersDataModel();
                        catersDataModel.setId(jsonObject.getString("id"));
                        catersDataModel.setFirstname(jsonObject.getString("firstname"));
                        catersDataModel.setLastname(jsonObject.getString("lastname"));
                        catersDataModel.setEmail(jsonObject.getString("email"));
                        catersDataModel.setPassword(jsonObject.getString("password"));
                        catersDataModel.setContact(jsonObject.getString("contactno"));
                        catersDataModel.setLocation(jsonObject.getString("location"));
                        catersDataModel.setIs_cater(jsonObject.getString("is_cater"));
                        catersDetail.add(catersDataModel);

                        //    loclatlng=donorDataModel.getLocation().split("\\s*,\\s*");

                        //     LatLng sydney = new LatLng(Integer.parseInt(loclatlng[0]), Integer.parseInt(loclatlng[1]));
                        //    mMap.addMarker(new MarkerOptions().position(sydney).title("raza"));
                        //    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                    }

                    for (int i = 0; i < catersDetail.size(); i++) {
                        var = catersDetail.get(i).getLocation();
                        loclatlng = var.split("\\s*,\\s*");
                        finallat = Double.parseDouble(loclatlng[0]);
                        finallng = Double.parseDouble(loclatlng[1]);
                        dest.setLatitude(finallat);
                        dest.setLongitude(finallng);
                        if (me.distanceTo(dest)<5000) {
                            LatLng sydney = new LatLng(dest.getLatitude(), dest.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(sydney).title(""));
//                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            float zoomLevel = 14.0f; //This goes up to 21
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
                            mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(finallat, finallng))
                                    .radius(100)
                                    .strokeColor(Color.RED)
                                    .fillColor(Color.BLUE));
                        }
                    }

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Toast.makeText(MapsActivity.this, "marker", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error in con", Toast.LENGTH_SHORT).show();
            }
        });

        // Creating the JsonObjectRequest class called obreq, passing required parameters:
        //GET is used to fetch data from the server, JsonURL is the URL to be fetched from.

        requestQueue.add(req1);
    }
}
