package com.example.qureshi.easeearncatering;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.SharedClass;
import com.example.qureshi.easeearncatering.Adapters.InvoiceCustomAdapter;
import com.example.qureshi.easeearncatering.DataModels.OrderDataModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadInvoice extends AppCompatActivity {

    @BindView(R.id.invoiceDetail)
    ListView invoiceDetail;

    @BindView(R.id.h1)
    TextView h1;

    @BindView(R.id.cart_act_tv_product_total_price)
    TextView cart_act_tv_product_total_price;

    @BindView(R.id.discount)
    TextView discount;

    @BindView(R.id.s_discount)
    Button s_discount;

    @BindView(R.id.a_discount)
    Button a_discount;

    @BindView(R.id.cart_act_btn_checkout)
    Button cart_act_btn_checkout;

    @BindView(R.id.cart_act_tv_product_total_item_count)
    TextView cart_act_tv_product_total_item_count;

    ProgressDialog progressDialog;

    int total = 0;

    float totalamount;
    RequestQueue requestQueue;
    String items;

    public final String insertfinalorder="https://easeearn.000webhostapp.com/insert_finalorder.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_invoice);

        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");

        requestQueue= Volley.newRequestQueue(this);

        Bundle bundle = getIntent().getExtras();
        String jsonString = bundle.getString("KEY");

        h1.setPaintFlags(h1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Gson gson = new Gson();
        Type listOfdoctorType = new TypeToken<List<OrderDataModel>>() {
        }.getType();
        final ArrayList<OrderDataModel> orders = gson.fromJson(jsonString, listOfdoctorType);

        for (int i = 0; i < orders.size(); i++) {
            Toast.makeText(this, orders.get(i).getDish(), Toast.LENGTH_SHORT).show();
            total = total + Integer.parseInt(orders.get(i).getTotal());
        }

        cart_act_tv_product_total_item_count.setText(String.valueOf(orders.size()) + " items");

        items=String.valueOf(orders.size());

        cart_act_tv_product_total_price.setText(String.valueOf(total));

        totalamount=Float.parseFloat(cart_act_tv_product_total_price.getText().toString());

        a_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discount.setText(String.valueOf(Integer.parseInt(discount.getText().toString()) + 10));
                float a = Float.parseFloat(discount.getText().toString()) * totalamount;
                float b = a / 100;
                float c = totalamount - b;
//                Toast.makeText(ReadInvoice.this, ""+c, Toast.LENGTH_SHORT).show();
                cart_act_tv_product_total_price.setText(String.valueOf(c));
//                int a=Integer.parseInt(discount.getText().toString()) * Integer.parseInt(cart_item_tv_product_quantity_title.getText().toString());
//                Toast.makeText(context, a+"", Toast.LENGTH_SHORT).show();
            }
        });

        s_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(discount.getText().toString()) > 0) {
                    discount.setText(String.valueOf(Integer.parseInt(discount.getText().toString()) - 10));
                    float a = Float.parseFloat(discount.getText().toString()) * totalamount;
                    float b = a / 100;
                    float c = totalamount - b;
                    cart_act_tv_product_total_price.setText(String.valueOf(c));
//                    Toast.makeText(ReadInvoice.this, ""+c, Toast.LENGTH_SHORT).show();
//                    int b = Integer.parseInt(cart_item_tv_product_price.getText().toString()) * Integer.parseInt(cart_item_tv_product_quantity_title.getText().toString());
//                    Toast.makeText(context, b + "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cart_act_btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                for (int i=0; i<orders.size(); i++) {
                    insertFinalOrder(orders.get(i).getId(), orders.get(i).getMenu_id(), orders.get(i).getSubmenu_id(),
                            orders.get(i).getDish(), orders.get(i).getUser_id(), orders.get(i).getQuantity(),
                            orders.get(i).getAmount(), orders.get(i).getTotal(), orders.get(i).getDatetime(), orders.get(i).getImage(),
                            orders.get(i).getEmail());
                }
            }
        });

        InvoiceCustomAdapter invoiceCustomAdapter = new InvoiceCustomAdapter(getApplicationContext(), orders);
        invoiceDetail.setAdapter(invoiceCustomAdapter);

    }

    public void insertFinalOrder(final String invoice_id, final String menu_id, final String submenu_id, final String dish, final String user_id, final String quantity,
                                 final String amount, final String total, final String datetime, final String image, final String email) {
        StringRequest request = new StringRequest(Request.Method.POST, insertfinalorder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(ReadInvoice.this, "Record", Toast.LENGTH_SHORT).show();
//                deleteCart();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ReadInvoice.this, "Error in connection insertion", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("invoice_id", invoice_id);
                parameters.put("menu_id", menu_id);
                parameters.put("submenu_id", submenu_id);
                parameters.put("dish", dish);
                parameters.put("user_id", user_id);
                parameters.put("quantity", quantity);
                parameters.put("amount", amount);
                parameters.put("total", total);
                parameters.put("datetime", datetime);
                parameters.put("image", image);
                parameters.put("email", email);
                parameters.put("total_items", items);
                parameters.put("discounted_amount", cart_act_tv_product_total_price.getText().toString());
                parameters.put("discount_percentage", discount.getText().toString());
                return parameters;
            }
        };
        requestQueue.add(request);
    }

}