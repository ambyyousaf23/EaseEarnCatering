package com.example.qureshi.easeearncatering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qureshi.easeearncatering.Adapters.CatersAdapter;
import com.example.qureshi.easeearncatering.Adapters.MenuAdapter;
import com.example.qureshi.easeearncatering.DataModels.CatersDataModel;
import com.example.qureshi.easeearncatering.DataModels.MenuDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewMenu extends AppCompatActivity {

    @BindView(R.id.product_act_toolbar)
    Toolbar toolbar;

    RecyclerView menuList;
    RequestQueue requestQueue;
    List<MenuDataModel> menu=new ArrayList<MenuDataModel>();
    final String getMenu="https://easeearn.000webhostapp.com/getMenu.php";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);

        ButterKnife.bind(this);
        // set toolbar
        setSupportActionBar(toolbar);

        requestQueue= Volley.newRequestQueue(this);

        intent=getIntent();

        menuList=(RecyclerView)findViewById(R.id.menuList);
        menuList.setLayoutManager(new LinearLayoutManager(ViewMenu.this));
        menuList.setItemAnimator(new DefaultItemAnimator());
        menuList.setNestedScrollingEnabled(false);
        menuList.setFocusable(false);

        viewMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_cart:
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewMenu() {
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
                parameters.put("id", intent.getStringExtra("id"));
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

            MenuAdapter catersAdapter=new MenuAdapter(ViewMenu.this, menu, intent.getStringExtra("id"), intent.getStringExtra("cater_name"));
            menuList.setAdapter(catersAdapter);


        } catch (JSONException e) {
            // If an error occurs, this prints the error to the log
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
