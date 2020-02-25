package com.example.qureshi.easeearncatering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.qureshi.easeearncatering.Adapters.SubMenuAdapter;
import com.example.qureshi.easeearncatering.DataModels.MenuDataModel;
import com.example.qureshi.easeearncatering.DataModels.SubMenuDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class submenu extends AppCompatActivity {

    RecyclerView submenu;
    RequestQueue requestQueue;
    List<SubMenuDataModel> menu=new ArrayList<SubMenuDataModel>();
    Intent intent;
    final String getMenu="https://easeearn.000webhostapp.com/submenu.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submenu);

        requestQueue= Volley.newRequestQueue(this);

        submenu=(RecyclerView)findViewById(R.id.submenu);
        submenu.setLayoutManager(new LinearLayoutManager(submenu.this));
        submenu.setItemAnimator(new DefaultItemAnimator());
        submenu.setNestedScrollingEnabled(false);
        submenu.setFocusable(false);

        intent=getIntent();

        viewSubMenu();

    }

    public void viewSubMenu() {
        menu.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getMenu, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseProdFilter(response);
                Toast.makeText(getApplicationContext(), "Received", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
//                parameters.put("Mart_ID", SharedClass.store_mart_id);
                parameters.put("menuID", intent.getStringExtra("id"));
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
                SubMenuDataModel menuDataModel = new SubMenuDataModel();
                menuDataModel.setMenu_id(json.getString("submenuID"));
                menuDataModel.setDishes(json.getString("Dishes"));
                menuDataModel.setImage(json.getString("image"));
                menuDataModel.setPrice(json.getString("price"));
                menu.add(menuDataModel);
            }

            SubMenuAdapter catersAdapter=new SubMenuAdapter(submenu.this, menu, intent.getStringExtra("id"), intent.getStringExtra("cater_id"), intent.getStringExtra("dish"), intent.getStringExtra("cater_name"));
            submenu.setAdapter(catersAdapter);

        } catch (JSONException e) {
            // If an error occurs, this prints the error to the log
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
