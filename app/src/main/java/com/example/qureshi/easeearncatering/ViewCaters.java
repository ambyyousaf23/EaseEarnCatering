package com.example.qureshi.easeearncatering;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.qureshi.easeearncatering.Adapters.CatersAdapter;
import com.example.qureshi.easeearncatering.DataModels.CatersDataModel;
import com.example.qureshi.easeearncatering.Fragments.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewCaters extends AppCompatActivity {

    @BindView(R.id.product_act_toolbar)
    Toolbar toolbar;

    RecyclerView catersList;
    RequestQueue requestQueue;
    List<CatersDataModel> caters=new ArrayList<CatersDataModel>();
    final String getCaters="https://easeearn.000webhostapp.com/getCaterInfo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_caters);

        ButterKnife.bind(this);

        // set toolbar
        setSupportActionBar(toolbar);

        requestQueue= Volley.newRequestQueue(this);

        catersList=(RecyclerView)findViewById(R.id.catersList);
        catersList.setLayoutManager(new LinearLayoutManager(ViewCaters.this));
        catersList.setItemAnimator(new DefaultItemAnimator());
        catersList.setNestedScrollingEnabled(false);
        catersList.setFocusable(false);

        viewCaters();

    }

    public void viewCaters() {
        caters.clear();
        JsonObjectRequest req2 = new JsonObjectRequest(getCaters, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray arry = response.getJSONArray("list");
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject json = arry.getJSONObject(i);
                        CatersDataModel catersDataModel = new CatersDataModel();
                        catersDataModel.setId(json.getString("id"));
                        catersDataModel.setFirstname(json.getString("firstname"));
                        catersDataModel.setLastname(json.getString("lastname"));
                        catersDataModel.setEmail(json.getString("email"));
                        catersDataModel.setPassword(json.getString("password"));
                        catersDataModel.setContact(json.getString("contactno"));
                        catersDataModel.setLocation(json.getString("location"));
                        catersDataModel.setIs_cater(json.getString("is_cater"));
                        caters.add(catersDataModel);
                    }

                    CatersAdapter catersAdapter=new CatersAdapter(ViewCaters.this, caters);
                    catersList.setAdapter(catersAdapter);



//                    catersList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), catersList, new RecyclerTouchListener.ClickListener() {
//                        @Override
//                        public void onClick(View view, int position) {
//                            try {
//                                Intent my_callIntent = new Intent(Intent.ACTION_CALL);
//                                my_callIntent.setData(Uri.parse("tel:"+caters.get(position).getContact()));
//                                //here the word 'tel' is important for making a call...
//                                startActivity(my_callIntent);
//                            } catch (ActivityNotFoundException e) {
//                                Toast.makeText(getApplicationContext(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onLongClick(View view, int position) {
//
//                        }
//                    }));


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
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_cart:
                Intent intent=new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                return true;
            case R.id.track:
                Intent intent1=new Intent(getApplicationContext(), Track_Order.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
