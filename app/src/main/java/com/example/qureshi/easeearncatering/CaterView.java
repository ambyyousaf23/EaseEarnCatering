package com.example.qureshi.easeearncatering;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.SharedClass;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaterView extends AppCompatActivity {

    @BindView(R.id.view_orders)
    TextView view_orders;

    @BindView(R.id.add_Menu)
    TextView add_Menu;

    @BindView(R.id.add_Submenu)
    TextView add_Submenu;

    @BindView(R.id.view_chats)
    TextView view_chats;

    @BindView(R.id.settings)
    TextView settings;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    SharedPreferences preferences;
    String id, firstname, lastname, email, password, contact, location, is_cater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cater_view);

        ButterKnife.bind(this);

        getPreference();

        requestQueue= Volley.newRequestQueue(this);

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");

        view_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewOrders.class));
            }
        });

        add_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddMainMenu.class));
            }
        });

        add_Submenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewMenuForCater.class));
            }
        });

        view_chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShowUsers.class));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(CaterView.this);
                dialog.setContentView(R.layout.settings_layout);

                final EditText e_firstname=(EditText)dialog.findViewById(R.id.firstname);
                final EditText e_lastname=(EditText)dialog.findViewById(R.id.lastname);
                final EditText e_email=(EditText)dialog.findViewById(R.id.email);
                final EditText e_password=(EditText)dialog.findViewById(R.id.password);
                final EditText e_contact=(EditText)dialog.findViewById(R.id.contact);
                Button update=(Button)dialog.findViewById(R.id.update);

                e_firstname.setText(firstname);
                e_lastname.setText(lastname);
                e_email.setText(email);
                e_password.setText(password);
                e_contact.setText(contact);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.show();
                        update(e_firstname.getText().toString(), e_lastname.getText().toString(), e_email.getText().toString(), e_password.getText().toString(), e_contact.getText().toString());
                        Toast.makeText(CaterView.this, firstname+" "+lastname+" "+email+" "+password+" "+contact+" "+id, Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            }
        });

    }

    public void update(final String first, final String last, final String eemail, final String pass, final String ccontact) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://easeearn.000webhostapp.com/update_details.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(CaterView.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Insert(first, last, eemail, pass, ccontact);
//                deleteCart();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(CaterView.this, "Error in connection insertion", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", id);
//                parameters.put("firstname", first);
//                parameters.put("lastname", last);
//                parameters.put("email", eemail);
//                parameters.put("password", pass);
//                parameters.put("contactno", ccontact);
//                parameters.put("location", SharedClass.location);
//                parameters.put("is_cater", "1");
//                parameters.put("picture", "abc");
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void Insert(final String first, final String last, final String eemail, final String pass, final String ccontact) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://easeearn.000webhostapp.com/insertRegister.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(CaterView.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
//                deleteCart();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(CaterView.this, "Error in connection insertion", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
             //   parameters.put("id", id);
                parameters.put("firstname", first);
                parameters.put("lastname", last);
                parameters.put("email", eemail);
                parameters.put("password", pass);
                parameters.put("contactno", ccontact);
                parameters.put("location", SharedClass.location);
                parameters.put("is_cater", "1");
//                parameters.put("picture", "abc");
                return parameters;
            }
        };
        requestQueue.add(request);
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
