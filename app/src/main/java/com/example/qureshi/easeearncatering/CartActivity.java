package com.example.qureshi.easeearncatering;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Manager.DBManager;
import com.example.SharedClass;
import com.example.qureshi.easeearncatering.Adapters.CartAdapter;
import com.example.qureshi.easeearncatering.DataModels.CartDataModel;
import com.example.qureshi.easeearncatering.DataModels.Cart_DataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CartActivity extends AppCompatActivity {

    RecyclerView cart_act_recycler_view;

    @BindView(R.id.cart_act_tv_product_total_item_price)
    TextView cart_act_tv_product_total_item_price;

    public static TextView cart_act_tv_product_total_price;

    @BindView(R.id.cart_act_tv_product_total_item_count)
    TextView cart_act_tv_product_total_item_count;

    @BindView(R.id.cart_act_btn_checkout)
    Button cart_act_btn_checkout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ProgressDialog progress;

    ImageButton deleteCartBtn;
    float result;
    int itemcount = 0;

    SharedPreferences preferences;
    String id, firstname, lastname, email, password, contact, location, is_cater;

    DBManager dbManager;
    String datetime, primaryEmailID;
    TextView prodIdTxt, finalprodIdTxt, quantityTxt, finalquantityTxt, amountTxt, finalamountTxt, stockTxt, finalstockTxt;
//    List<Cart_DataModel> cartsList = new ArrayList<Cart_DataModel>();
    RequestQueue requestQueue;
    String[] stock;
    String[] quantity;

    ProgressDialog progressDialog;

    public final String insert_invoice="https://easeearn.000webhostapp.com/insert_invoice.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(CartActivity.this);

        // set toolbar
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");

        getPreference();

        cart_act_tv_product_total_price=(TextView)findViewById(R.id.cart_act_tv_product_total_price);

        datetime = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_12HOUR);

        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                primaryEmailID = account.name;
            }
        }

        Toast.makeText(this, primaryEmailID, Toast.LENGTH_SHORT).show();

        dbManager=new DBManager(this);
        dbManager.open();
        try {
            dbManager.createDataBase();
        }catch (IOException e){
            e.printStackTrace();
        }

        cart_act_tv_product_total_price.setText(SharedClass.amount_total+"");
        SharedClass.amount_total=0;

        progress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setTitle("Please wait");
        progress.setMessage("Loading");
        progress.requestWindowFeature(Window.FEATURE_NO_TITLE);

        cart_act_recycler_view = (RecyclerView) findViewById(R.id.cart_act_recycler_view);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        stockTxt = (TextView) findViewById(R.id.stockTxt);
        finalstockTxt = (TextView) findViewById(R.id.finalstockTxt);
        prodIdTxt = (TextView) findViewById(R.id.prodIdTxt);
        quantityTxt = (TextView) findViewById(R.id.quantityTxt);
        amountTxt = (TextView) findViewById(R.id.amountTxt);
        finalprodIdTxt = (TextView) findViewById(R.id.finalprodIdTxt);
        finalquantityTxt = (TextView) findViewById(R.id.finalquantityTxt);
        finalamountTxt = (TextView) findViewById(R.id.finalamountTxt);
        deleteCartBtn = (ImageButton) findViewById(R.id.deleteCartBtn);

        cart_act_recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        cart_act_recycler_view.setLayoutManager(mLayoutManager);
        cart_act_recycler_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        cart_act_recycler_view.setItemAnimator(new DefaultItemAnimator());

        if (isInternetOn()) {
            receiveMartsData();
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        deleteCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(CartActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Delete whole cart ?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        cart_act_btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(CartActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Finalize Order ?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                progressDialog.show();

                                List<CartDataModel> cartsList = new ArrayList<CartDataModel>();
                                cartsList = dbManager.showCart(id);
//
                                for (int i=0; i<cartsList.size(); i++) {
                                    insertOrder(cartsList.get(i).getMenu_id(), cartsList.get(i).getSubmenu_id(), cartsList.get(i).getDish(),
                                            cartsList.get(i).getCater_id(), id, String.valueOf(SharedClass.quantity_total),
                                            cartsList.get(i).getAmount(), cart_act_tv_product_total_price.getText().toString(),
                                            cartsList.get(i).getDatetime(), cartsList.get(i).getImage());
                                }

                                insertInvoiceRecord(cartsList.get(0).getCater_id());

                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_cart_menu, menu); // Here you are setting the menu whatever options you want.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_delete_cart:
                Toast.makeText(this, "Delete cart", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void receiveMartsData() {

        List<CartDataModel> cartsList = new ArrayList<CartDataModel>();
        cartsList = dbManager.showCart(id);

            cart_act_tv_product_total_item_price.setText(String.valueOf(SharedClass.amount_total));
            cart_act_tv_product_total_price.setText(String.valueOf(SharedClass.amount_total));

                cart_act_tv_product_total_item_count.setText(SharedClass.quantity_total + " item");
                SharedClass.quantity_total=0;



        CartAdapter cartItemAdapter = new CartAdapter(CartActivity.this, cartsList);
        cart_act_recycler_view.setAdapter(cartItemAdapter);

    }

    public boolean isInternetOn() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) CartActivity.this.getSystemService(CartActivity.this.getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }
        return false;
    }

    public void insertInvoiceRecord(final String cater_id) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://easeearn.000webhostapp.com/insert_invoice_record.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(CartActivity.this, "Record", Toast.LENGTH_SHORT).show();
//                deleteCart();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this, "Error in connection insertion", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", primaryEmailID);
                parameters.put("cater_id", cater_id);
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void insertOrder(final String menu_id, final String submenu_id, final String dish, final String cater_id, final String user_id,
                            final String quantity, final String amount, final String total, final String datetime, final String image) {
        StringRequest request = new StringRequest(Request.Method.POST, insert_invoice, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(CartActivity.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
//                deleteCart();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(CartActivity.this, "Error in connection insertion", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("menu_id", menu_id);
                parameters.put("submenu_id", submenu_id);
                parameters.put("dish", dish);
                parameters.put("cater_id", cater_id);
                parameters.put("user_id", user_id);
                parameters.put("quantity", quantity);
                parameters.put("amount", amount);
                parameters.put("total", total);
                parameters.put("datetime", datetime);
                parameters.put("image", image);
                parameters.put("email", primaryEmailID);
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void updateStock(final String prod_id, final int stock) {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(CartActivity.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this, "Error in connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("Total_Stock", String.valueOf(stock));
                parameters.put("pro_id", prod_id);
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '0') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
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
