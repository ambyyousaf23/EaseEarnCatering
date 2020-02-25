package com.example.qureshi.easeearncatering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qureshi.easeearncatering.DataModels.UserDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cardViewHolder extends AppCompatActivity {
    TextView create_acc;
    EditText emailTxt, passwordTxt;
    Button close, facebook, login_card;
    RadioGroup group;
    RadioButton cater, customer;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    //    public final String CaterLogin="https://easeearn.000webhostapp.com/CatererLogin.php";
    public final String CaterLogin = "https://easeearn.000webhostapp.com/getUserDetail.php";
    List<UserDataModel> userinfo = new ArrayList<UserDataModel>();
    int check;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SharedPreferences preferences;
    String id, firstname, lastname, email, password, contact, location, is_cater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view_holder);

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        group = (RadioGroup) findViewById(R.id.group);
        cater = (RadioButton) findViewById(R.id.cater);
        customer = (RadioButton) findViewById(R.id.customer);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.cater:
                        check = 1;
                        break;
                    case R.id.customer:
                        check = 0;
                        break;
                }
            }
        });

        login_card = (Button) findViewById(R.id.login_card);
        login_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailTxt.getText().toString().length() == 0) {
                    emailTxt.setError("Enter Email");
                }
                else if (!emailTxt.getText().toString().matches(emailPattern)){
                    emailTxt.setError("invalid email");
                }
                else if (passwordTxt.getText().toString().length() == 0)
                {
                    passwordTxt.setError("Enter Password");
                }
                else if (passwordTxt.getText().toString().length()<8){
                    passwordTxt.setError("password must be 8 characters long");
                }
                    else {
                    progressDialog.show();
                    Login();
                }
            }
        });

        close = (Button) findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cardViewHolder.this, navigation.class);
                startActivity(intent);
            }
        });
        create_acc = (TextView) findViewById(R.id.create_acc1);
        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cardViewHolder.this, create_account.class);
                startActivity(intent);
            }
        });
    }

    public void Login() {
        StringRequest request = new StringRequest(Request.Method.POST, CaterLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseDataProd(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
//                parameters.put("Mart_ID", SharedClass.store_mart_id);
                parameters.put("email", emailTxt.getText().toString());
                parameters.put("password", passwordTxt.getText().toString());
                parameters.put("is_cater", String.valueOf(check));
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void parseDataProd(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray arry = jsonObject.getJSONArray("list");

            for (int i = 0; i < arry.length(); i++) {
                JSONObject json = arry.getJSONObject(i);
                UserDataModel products = new UserDataModel();
                products.setId(json.getString("id"));
                products.setFirstname(json.getString("firstname"));
                products.setLastname(json.getString("lastname"));
                products.setEmail(json.getString("email"));
                products.setPassword(json.getString("password"));
                products.setContactno(json.getString("contactno"));
                products.setLocation(json.getString("location"));
                products.setIs_cater(json.getString("is_cater"));
                userinfo.add(products);
            }

            if (userinfo.size() > 0) {

                progressDialog.dismiss();

                editPreference(userinfo.get(0).getId(), userinfo.get(0).getFirstname(), userinfo.get(0).getLastname(), userinfo.get(0).getEmail(),
                        userinfo.get(0).getPassword(), userinfo.get(0).getContactno(), userinfo.get(0).getLocation(), userinfo.get(0).getIs_cater());

                switch (check) {
                    case 1:
                        Toast.makeText(cardViewHolder.this, "Cater Login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), CaterView.class);
                        startActivity(intent);
                        break;
                    case 0:
                        Toast.makeText(cardViewHolder.this, "Customer Login", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(getApplicationContext(), ViewCaters.class);
                        startActivity(intent1);
                }

            } else {
                progressDialog.dismiss();
                Toast.makeText(cardViewHolder.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // If an error occurs, this prints the error to the log
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editPreference(String id, String firstname, String lastname, String email, String password, String contact, String location, String is_cater) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", id);
        editor.putString("firstname", firstname);
        editor.putString("lastname", lastname);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("contactno", contact);
        editor.putString("location", location);
        editor.putString("is_cater", is_cater);
        editor.apply();
        editor.commit();
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
