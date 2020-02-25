package com.example.qureshi.easeearncatering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qureshi.easeearncatering.Adapters.MenuAdapter;
import com.example.qureshi.easeearncatering.Adapters.MenuForCaterAdapter;
import com.example.qureshi.easeearncatering.DataModels.MenuDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMenuForCater extends AppCompatActivity {

    RecyclerView menuList;
    RequestQueue requestQueue;
    List<MenuDataModel> menu=new ArrayList<MenuDataModel>();
    final String getMenu="https://easeearn.000webhostapp.com/getMenu.php";
    Intent intent;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    String id, firstname, lastname, email, password, contact, location, is_cater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);

        requestQueue= Volley.newRequestQueue(this);

        getPreference();

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");

        intent=getIntent();

        menuList=(RecyclerView)findViewById(R.id.menuList);
        menuList.setLayoutManager(new LinearLayoutManager(ViewMenuForCater.this));
        menuList.setItemAnimator(new DefaultItemAnimator());
        menuList.setNestedScrollingEnabled(false);
        menuList.setFocusable(false);

        progressDialog.show();

        viewMenu();

    }

    public void viewMenu() {
        menu.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getMenu, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseProdFilter(response);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Received", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
//                parameters.put("Mart_ID", SharedClass.store_mart_id);
                parameters.put("id", id);
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void parseProdFilter(String response) {
        menu.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray arry = jsonObject.getJSONArray("list");

            for (int i = 0; i < arry.length(); i++) {
                JSONObject json = arry.getJSONObject(i);
                MenuDataModel menuDataModel = new MenuDataModel();
                menuDataModel.setDishes(json.getString("Dishes"));
                menuDataModel.setMenu_id(json.getString("menuID"));
                menuDataModel.setImage(json.getString("image"));
                menu.add(menuDataModel);
            }

            MenuForCaterAdapter catersAdapter=new MenuForCaterAdapter(ViewMenuForCater.this, menu);
            menuList.setAdapter(catersAdapter);


        } catch (JSONException e) {
            // If an error occurs, this prints the error to the log
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPreference(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        id=preferences.getString("id", "");
        firstname=preferences.getString("firstname", "");
        lastname=preferences.getString("lastname", "");
        email=preferences.getString("email", "");
        password=preferences.getString("password", "");
        contact=preferences.getString("contact", "");
        location=preferences.getString("location", "");
        is_cater=preferences.getString("is_cater", "");
    }

}
