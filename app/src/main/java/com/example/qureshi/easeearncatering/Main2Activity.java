package com.example.qureshi.easeearncatering;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.SharedDataClass;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static int SPLASH_TIME_OUT=1500;
    private SparseIntArray mErrorString;
    Location gps_loc=null,network_loc=null,final_loc=null;
    private static final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(gps_loc!=null){
            final_loc=gps_loc;
            SharedDataClass.longitude = final_loc.getLongitude();
            SharedDataClass.latitude = final_loc.getLatitude();
        }
        else if(network_loc!=null){
            final_loc=network_loc;
            SharedDataClass.longitude = final_loc.getLongitude();
            SharedDataClass.latitude = final_loc.getLatitude();
        }
        else {
            SharedDataClass.longitude=0.0;
            SharedDataClass.latitude=0.0;
        }

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(SharedDataClass.latitude, SharedDataClass.longitude, 1);
            if (null != listAddresses && listAddresses.size() > 0) {
                SharedDataClass.locationArea = listAddresses.get(0).getAddressLine(0);
                SharedDataClass.locationCity = listAddresses.get(0).getAddressLine(1);
                SharedDataClass.locationCountry = listAddresses.get(0).getAddressLine(2);
                // Toast.makeText(MapsActivity.this,"asad",Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mErrorString=new SparseIntArray();
        requestAppPermission(new String[]{
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        }, R.string.msg,REQUEST_PERMISSION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent=new Intent(Main2Activity.this,navigation.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
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
        getMenuInflater().inflate(R.menu.main2, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void requestAppPermission(final String[] requestedPermission,final int stringId,final int requestCode){
        mErrorString.put(requestCode,stringId);
        int permissionCheck= PackageManager.PERMISSION_GRANTED;
        boolean showRequestPermissions=false;
        for (String permission:requestedPermission){
            permissionCheck=permissionCheck+ ContextCompat.checkSelfPermission(this,permission);
            showRequestPermissions=showRequestPermissions || ActivityCompat.shouldShowRequestPermissionRationale(this,permission);
        }
        if(permissionCheck!=PackageManager.PERMISSION_GRANTED){
            if(showRequestPermissions){
                Snackbar.make(findViewById(android.R.id.content),stringId,Snackbar.LENGTH_INDEFINITE).setAction("Grant", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  ActivityCompat.requestPermissions(AbsRunTimePermission.this,requestedPermission,requestCode);
                    }
                }).show();
            }
            else {
                ActivityCompat.requestPermissions(this,requestedPermission,requestCode);
            }
        }
        else {
            onPermissionsGranted(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck= PackageManager.PERMISSION_GRANTED;
        for (int permission:grantResults){
            permissionCheck=permissionCheck+ permission;
        }
        if(grantResults.length>0 && PackageManager.PERMISSION_GRANTED==permissionCheck){
            onPermissionsGranted(requestCode);
        }
        else {
            Snackbar.make(findViewById(android.R.id.content),mErrorString.get(requestCode), Snackbar.LENGTH_INDEFINITE).setAction("Enable", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.setData(Uri.parse("package :" + getPackageName()));
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }
            }).show();
        }
    }
    public void onPermissionsGranted(int requestCode) {
        //Toast.makeText(Splash_Activity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
    }

}
