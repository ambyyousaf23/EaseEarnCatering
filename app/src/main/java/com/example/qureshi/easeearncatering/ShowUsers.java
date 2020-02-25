package com.example.qureshi.easeearncatering;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qureshi.easeearncatering.Adapters.UsersAdapter;
import com.example.qureshi.easeearncatering.DataModels.CatersDataModel;
import com.example.qureshi.easeearncatering.DataModels.ChatDataModel;
import com.example.qureshi.easeearncatering.DataModels.CustomerDataModel;
import com.example.qureshi.easeearncatering.DataModels.UsersDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ShowUsers extends AppCompatActivity {

    List<UsersDataModel> catersDataModels=new ArrayList<UsersDataModel>();
    List<UsersDataModel> customersDataModels=new ArrayList<UsersDataModel>();
    List<ChatDataModel> chatDataModels=new ArrayList<ChatDataModel>();
    ArrayList<String> senderId=new ArrayList<String>();
    HashSet<String> hashSet=new HashSet<String>();
    RequestQueue requestQueue;
    RecyclerView usersList;
    int check;

    ProgressDialog progressDialog;

    SharedPreferences preferences;
    String id, firstname, lastname, email, password, contact, location, is_cater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        getPreference();

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");

        Toast.makeText(this, contact, Toast.LENGTH_SHORT).show();

        requestQueue= Volley.newRequestQueue(this);
        usersList=(RecyclerView)findViewById(R.id.usersList);
        usersList.setLayoutManager(new LinearLayoutManager(this));

//        if (check==1){
        progressDialog.show();
            checkCustomers();
//        }
//        else {
//            receiveCaters();
//        }

    }

    // FOR GETTING CUSTOMERS

    public void checkCustomers() {
        chatDataModels.clear();
        StringRequest request = new StringRequest(Request.Method.POST, "https://easeearn.000webhostapp.com/getMyChats.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseDataCustomers(response);
                Toast.makeText(getApplicationContext(), "Received items", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error in connection while products", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
//                parameters.put("Mart_ID", SharedClass.store_mart_id);
                parameters.put("receiver_id", contact);
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void parseDataCustomers(String response) {
        chatDataModels.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray arry = jsonObject.getJSONArray("list");

            for (int i = 0; i < arry.length(); i++) {
                JSONObject json = arry.getJSONObject(i);
                ChatDataModel categories = new ChatDataModel();
//                categories.setId(json.getString("Category_ID"));
                categories.setSender_id(json.getString("sender_id"));
                chatDataModels.add(categories);
            }

            for (int i=0; i<chatDataModels.size(); i++){
                senderId.add(chatDataModels.get(i).getSender_id());
//                hashSet.addAll(senderId);
//                senderId.clear();
//                senderId.addAll(hashSet);
                Toast.makeText(this, senderId.get(i), Toast.LENGTH_SHORT).show();
                receiveCustomers(chatDataModels.get(i).getSender_id());
            }


        } catch (JSONException e) {
            // If an error occurs, this prints the error to the log
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void receiveCustomers(final String senderId) {
        customersDataModels.clear();
        StringRequest request = new StringRequest(Request.Method.POST, "https://easeearn.000webhostapp.com/getMyUserChats.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseDataCategories(response);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Received items", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error in connection while products", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
//                parameters.put("Mart_ID", SharedClass.store_mart_id);
//                parameters.put("is_cater", "0");
                parameters.put("contactno", senderId);
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void parseDataCategories(String response) {
        customersDataModels.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray arry = jsonObject.getJSONArray("list");

            for (int i = 0; i < arry.length(); i++) {
                JSONObject json = arry.getJSONObject(i);
                UsersDataModel categories = new UsersDataModel();
                categories.setId(json.getString("id"));
                categories.setFirstname(json.getString("firstname"));
                categories.setLastname(json.getString("lastname"));
                categories.setEmail(json.getString("email"));
                categories.setPassword(json.getString("password"));
                categories.setContact(json.getString("contactno"));
                categories.setLocation(json.getString("location"));
                customersDataModels.add(categories);
            }

            UsersAdapter adapterClass = new UsersAdapter(ShowUsers.this, customersDataModels);
            usersList.setAdapter(adapterClass);

        } catch (JSONException e) {
            // If an error occurs, this prints the error to the log
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // FOR GETTING CATERS


    public void receiveCaters() {
        catersDataModels.clear();
        StringRequest request = new StringRequest(Request.Method.POST, "url", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseDataCaters(response);
                Toast.makeText(getApplicationContext(), "Received items", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error in connection while products", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
//                parameters.put("Mart_ID", SharedClass.store_mart_id);
                parameters.put("is_cater", "1");
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void parseDataCaters(String response) {
        catersDataModels.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray arry = jsonObject.getJSONArray("list");

            for (int i = 0; i < arry.length(); i++) {
                JSONObject json = arry.getJSONObject(i);
                UsersDataModel categories = new UsersDataModel();
                categories.setId(json.getString("Category_ID"));
                categories.setFirstname(json.getString("Cat_Name"));
                categories.setLastname(json.getString("ParentCat_ID"));
                categories.setEmail(json.getString("Updated_Date"));
                categories.setPassword(json.getString("Cat_Status"));
                categories.setContact(json.getString("Mart_ID"));
                categories.setLocation(json.getString("Mart_ID"));
                catersDataModels.add(categories);
            }

            UsersAdapter adapterClass = new UsersAdapter(ShowUsers.this, catersDataModels);
            usersList.setAdapter(adapterClass);

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
        contact=preferences.getString("contactno", "");
        location=preferences.getString("location", "");
        is_cater=preferences.getString("is_cater", "");
    }

}
