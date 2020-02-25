package com.example.qureshi.easeearncatering;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class Registration extends AppCompatActivity {

    @BindView(R.id.firstname)
    EditText firstName;

    @BindView(R.id.lastname)
    EditText lastname;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.contact)
    EditText contact;

    @BindView(R.id.group)
    RadioGroup group;

    @BindView(R.id.signup_btn)
    Button signup_btn;

    RequestQueue requestQueue;

    String type=null;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        requestQueue= Volley.newRequestQueue(this);

        Toast.makeText(this, SharedClass.location, Toast.LENGTH_SHORT).show();

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.cater:
                        type="1";
                        break;
                    case R.id.customer:
                        type="0";
                        break;
                }
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstName.getText().toString().equals("")){
                    firstName.setError("Enter first name");
                }
                else if (lastname.getText().toString().equals("")){
                    lastname.setError("Enter last name");
                }
                else if (email.getText().toString().equals("")){
                    email.setError("Enter email");
                }
                else if (!email.getText().toString().matches(emailPattern)){
                    email.setError("invalid email");
                }
                else if (password.getText().toString().equals("")){
                    password.setError("Enter password");
                }
                else if (password.getText().toString().length()<8){
                    password.setError("password must be 8 characters long");
                }
                else if (contact.getText().toString().equals("")){
                    contact.setError("Enter contact");
                }
                else if (type==null){
                    Toast.makeText(Registration.this, "Choose option", Toast.LENGTH_SHORT).show();
                }
                else {
                    insertOrder();
                }
            }
        });

    }

    public void insertOrder() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://easeearn.000webhostapp.com/registration.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Registration.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
//                deleteCart();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Registration.this, "Error in connection insertion", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("firstname", firstName.getText().toString());
                parameters.put("lastname", lastname.getText().toString());
                parameters.put("email", email.getText().toString());
                parameters.put("password", password.getText().toString());
                parameters.put("contactno", contact.getText().toString());
                parameters.put("location", SharedClass.location);
                parameters.put("is_cater", type);
                return parameters;
            }
        };
        requestQueue.add(request);
    }

}
