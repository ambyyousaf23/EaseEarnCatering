package com.example.qureshi.easeearncatering;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qureshi.easeearncatering.Adapters.CatersAdapter;
import com.example.qureshi.easeearncatering.Adapters.OrderAdapter;
import com.example.qureshi.easeearncatering.Adapters.SubMenuAdapter;
import com.example.qureshi.easeearncatering.DataModels.CatersDataModel;
import com.example.qureshi.easeearncatering.DataModels.OrderDataModel;
import com.example.qureshi.easeearncatering.DataModels.SubMenuDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewOrders extends AppCompatActivity {

    RecyclerView orderList;
    RequestQueue requestQueue;
    List<OrderDataModel> orders=new ArrayList<OrderDataModel>();
    ProgressDialog progressDialog;
    final String getOrders="https://easeearn.000webhostapp.com/getInvoiceRecord.php";

    SharedPreferences preferences;
    String id, firstname, lastname, email, password, contact, location, is_cater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");

        requestQueue= Volley.newRequestQueue(this);

        getPreference();

        orderList=(RecyclerView)findViewById(R.id.orderList);
        orderList.setLayoutManager(new LinearLayoutManager(ViewOrders.this));
        orderList.setItemAnimator(new DefaultItemAnimator());
        orderList.setNestedScrollingEnabled(false);
        orderList.setFocusable(false);

        progressDialog.show();

        viewOrders();

    }

//    public void viewOrders() {
//        orders.clear();
//        JsonObjectRequest req2 = new JsonObjectRequest(getOrders, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//
//                    JSONArray arry = response.getJSONArray("list");
//                    for (int i = 0; i < arry.length(); i++) {
//                        JSONObject json = arry.getJSONObject(i);
//                        OrderDataModel orderDataModel = new OrderDataModel();
//                        orderDataModel.setId(json.getString("id"));
//                        orderDataModel.setMenu_id(json.getString("menu_id"));
//                        orderDataModel.setSubmenu_id(json.getString("submenu_id"));
//                        orderDataModel.setDish(json.getString("dish"));
//                        orderDataModel.setUser_id(json.getString("user_id"));
//                        orderDataModel.setQuantity(json.getString("quantity"));
//                        orderDataModel.setAmount(json.getString("amount"));
//                        orderDataModel.setTotal(json.getString("total"));
//                        orderDataModel.setDatetime(json.getString("datetime"));
//                        orderDataModel.setImage(json.getString("image"));
//                        orderDataModel.setEmail(json.getString("email"));
//                        orders.add(orderDataModel);
//                    }
//
//                    OrderAdapter catersAdapter=new OrderAdapter(ViewOrders.this, orders);
//                    orderList.setAdapter(catersAdapter);
//
//                } catch (JSONException e) {
//                    // If an error occurs, this prints the error to the log
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "Error Response", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        requestQueue.add(req2);
//    }


    public void viewOrders() {
        orders.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getOrders, new Response.Listener<String>() {
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
                parameters.put("cater_id", id);
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void parseProdFilter(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray arry = jsonObject.getJSONArray("list");

            for (int i = 0; i < arry.length(); i++) {
                JSONObject json = arry.getJSONObject(i);
                OrderDataModel orderDataModel = new OrderDataModel();
                orderDataModel.setId(json.getString("id"));
                orderDataModel.setMenu_id(json.getString("menu_id"));
                orderDataModel.setSubmenu_id(json.getString("submenu_id"));
                orderDataModel.setDish(json.getString("dish"));
                orderDataModel.setUser_id(json.getString("user_id"));
                orderDataModel.setQuantity(json.getString("quantity"));
                orderDataModel.setAmount(json.getString("amount"));
                orderDataModel.setTotal(json.getString("total"));
                orderDataModel.setDatetime(json.getString("datetime"));
                orderDataModel.setImage(json.getString("image"));
                orderDataModel.setEmail(json.getString("email"));
                orders.add(orderDataModel);
            }

            OrderAdapter catersAdapter=new OrderAdapter(ViewOrders.this, orders);
            orderList.setAdapter(catersAdapter);

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
