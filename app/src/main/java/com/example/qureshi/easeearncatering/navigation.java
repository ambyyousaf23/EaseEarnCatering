package com.example.qureshi.easeearncatering;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.Manager.DBManager;
import com.example.SharedClass;
import com.example.SharedDataClass;
import com.example.qureshi.easeearncatering.DataModels.UserDataModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    TextView txt_login;
    int dbFlag11 = 0;

    SharedPreferences preferences;

    public final String caterinfo = "https://easeearn.000webhostapp.com/getCaterInfo.php";

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;///Location request show after how much time interval
    Location lastlocation; //For listening lat Long Recieve
    Location dest;
    Marker marker; //For marker or pointer
    Circle circle;
    static final long INTERVAL = 60 * 1000; //one minute or 60 seconds
    static final long FASTINTERVAL = 15 * 1000; //15 seconds
    public static final int REQUEST_CODE_LOCATION = 100;
    static long RADIUS = 10000;
    static float KMRange = 10.0f;
    int count = 0;
    double distance, convrtedDistance;
    String address = "";

    LatLng latLng;
    boolean check;

    RequestQueue requestQueue;
    String[] splittedArray;

    List<UserDataModel> userinfo = new ArrayList<UserDataModel>();
    List<String> lat = new ArrayList<String>();
    List<String> lng = new ArrayList<String>();

    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(this);

        getPreference();
        dbManager = new DBManager(this);
        dbManager.open();
        try {
            if (dbFlag11 == 0) {
                dbManager.copyDataBase();
                dbFlag11 = 1;
                editPreference();
            }
        } catch (IOException e) {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        txt_login = (TextView) header.findViewById(R.id.ttxt_login);

        Toast.makeText(this, SharedDataClass.longitude + " and " + SharedDataClass.latitude, Toast.LENGTH_SHORT).show();

        /// login intent going to here

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), cardViewHolder.class);
                startActivity(i);
                finish();
            }
        });

        JsonObjectRequest req2 = new JsonObjectRequest(caterinfo, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray arry = response.getJSONArray("list");
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject json = arry.getJSONObject(i);
                        UserDataModel marts = new UserDataModel();
                        marts.setFirstname(json.getString("firstname"));
                        marts.setLocation(json.getString("location"));
                        userinfo.add(marts);
                    }

                    for (int i = 0; i < userinfo.size(); i++) {
                        splittedArray = userinfo.get(i).getLocation().split(",");
                        lat.add(splittedArray[0]);
                        lng.add(splittedArray[1]);
                        splittedArray = null;
                        Toast.makeText(navigation.this, lat.get(i) + ", " + lng.get(i), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Response", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(req2);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.registration) {
            Intent i = new Intent(getApplicationContext(), Registration.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_contactus) {
Intent i=new Intent(getApplicationContext(),contactus.class);
startActivity(i);
finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // MAPS WORD

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
       /* mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                distanceCalculate(marker.getPosition());
                return true;

            }
        });*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    public boolean distanceCalculate(LatLng latLng) {
        dest = new Location(lastlocation);
        dest.setLatitude(latLng.latitude);
        dest.setLongitude(latLng.longitude);
        distance = (lastlocation.distanceTo(dest));
        convrtedDistance = distance * 0.001;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {

            List<Address> listaddresses = geocoder.getFromLocation(dest.getLatitude(), dest.getLongitude(), 1);
            address = listaddresses.get(0).getAddressLine(0) + " " + listaddresses.get(0).getCountryCode()
            /*+
                    " " + listaddresses.get(0).getLocality() + " " + listaddresses.get(0).getAdminArea() +
                    " " + listaddresses.get(0).getCountryCode() + " " + listaddresses.get(0).getCountryName() +
                    " " + listaddresses.get(0).getPostalCode() + " " + listaddresses.get(0).getFeatureName()*/;
        } catch (Exception e) {
            Toast.makeText(this, "No Address Found...", Toast.LENGTH_SHORT).show();
        }
        //sample = String.format("%.3f", distance * 0.001).split("\\.");
        if (convrtedDistance <= KMRange) {
            Toast.makeText(this, " " + address, Toast.LENGTH_LONG).show();
            Toast.makeText(navigation.this, String.format("%.3f", convrtedDistance) + " KM ", Toast.LENGTH_SHORT).show();
            return true;
        } else {

            return false;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTINTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this); //import location listener of gms at top of code
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    private boolean checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                            mMap.setMyLocationEnabled(true);
                        } else {
                            Toast.makeText(this, "Permission Denied by User..!", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (marker != null) {
            marker.remove();

        }

        if (circle != null) {
            circle.remove();
        }
        lastlocation = location;
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        SharedClass.location=String.valueOf(location.getLatitude()+","+location.getLongitude());
        MarkerOptions options = new MarkerOptions();
        options.position(currentLatLng);
        mMap.addMarker(options);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13.0f));
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(RADIUS)
                .strokeColor(Color.GREEN)
                .fillColor(Color.TRANSPARENT));

        if (count < 3) {

            for (int i = 0; i < lat.size(); i++) {
                latLng = new LatLng(Double.parseDouble(lat.get(i)), Double.parseDouble(lng.get(i)));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
//                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                check = distanceCalculate(latLng);

                if (check) {
                    count++;

                    mMap.addMarker(markerOptions);

                    if (count == 3)
                        //Toast.makeText(MapsActivity.this, (4 - count) + " Pin(s) Left", Toast.LENGTH_SHORT).show();
                        break;
                }
            }


        }

    }


    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    public void editPreference() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("dbFlag", dbFlag11);
        editor.apply();
        editor.commit();
    }

    public void getPreference() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        dbFlag11 = preferences.getInt("dbFlag", 0);
    }

}
