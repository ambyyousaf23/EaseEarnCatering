package com.example.qureshi.easeearncatering;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qureshi.easeearncatering.Adapters.FinalDataAdapter;
import com.example.qureshi.easeearncatering.Adapters.OrderAdapter;
import com.example.qureshi.easeearncatering.DataModels.FinalDataModel;
import com.example.qureshi.easeearncatering.DataModels.OrderDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Track_Order extends AppCompatActivity {

    @BindView(R.id.finalDetail)
    ListView finalDetail;

    @BindView(R.id.cart_act_tv_product_total_price)
    TextView cart_act_tv_product_total_price;

    @BindView(R.id.discount)
    TextView discount;

    @BindView(R.id.h2)
    TextView h2;

    RequestQueue requestQueue;
    final String getfinalOrder = "https://easeearn.000webhostapp.com/getFinalOrder.php";

    SharedPreferences preferences;
    String id, firstname, lastname, email, password, contact, location, is_cater;

    List<FinalDataModel> finalData = new ArrayList<FinalDataModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track__order);

        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(this);

        getPreference();
        h2.setPaintFlags(h2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        viewOrders();


    }

    public void viewOrders() {
        finalData.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getfinalOrder, new Response.Listener<String>() {
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
                parameters.put("user_id", id);
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
                FinalDataModel orderDataModel = new FinalDataModel();
                orderDataModel.setId(json.getString("id"));
                orderDataModel.setInvoice_id(json.getString("invoice_id"));
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
                orderDataModel.setTotal_items(json.getString("total_items"));
                orderDataModel.setDiscounted_amount(json.getString("discounted_amount"));
                orderDataModel.setDiscount_percentage(json.getString("discount_percentage"));
                finalData.add(orderDataModel);
            }

            for (int i = 0; i < finalData.size(); i++) {
                cart_act_tv_product_total_price.setText("Rs. " + String.valueOf(finalData.get(i).getDiscounted_amount()));
                discount.setText(finalData.get(i).getDiscount_percentage());
            }

            FinalDataAdapter catersAdapter = new FinalDataAdapter(Track_Order.this, finalData);
            finalDetail.setAdapter(catersAdapter);

        } catch (JSONException e) {
            // If an error occurs, this prints the error to the log
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getPreference() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        id = preferences.getString("id", "");
        firstname = preferences.getString("firstname", "");
        lastname = preferences.getString("lastname", "");
        email = preferences.getString("email", "");
        password = preferences.getString("password", "");
        contact = preferences.getString("contact", "");
        location = preferences.getString("location", "");
        is_cater = preferences.getString("is_cater", "");
    }

}
