package com.example.qureshi.easeearncatering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qureshi on 06/02/2018.
 */

public  class customer_form extends AppCompatActivity{
    ImageView imv;
    private static int LOAD_IMAGE_RESULT = 1;
    Bitmap bitm;
    EditText frstname, lastname, email, pass, retypepass, contactno;
    Button Rcustomer;
    ProgressDialog progressDialog;
    private static final String URL_REGISTER_CUSTOMER="https://easeearn.000webhostapp.com/WebServices/RegisterCustomer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_form);
        imv =  findViewById(R.id.customerimg);
        frstname =  findViewById(R.id.customorfirstname);
        lastname =  findViewById(R.id.customorlastname);
        email =  findViewById(R.id.customeremail);
        pass =  findViewById(R.id.customerpassword);
        retypepass =  findViewById(R.id.customerrepassword);
        contactno= findViewById(R.id.customernumber);
        Rcustomer =  findViewById(R.id.customerbutton);



        Rcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               resultvolley();
            }
        });

        imv.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if( requestCode==1 && resultCode==RESULT_OK && data!=null) {
            Uri filepath= data.getData();
            try{
                bitm=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),filepath);}
            catch (Exception error){
                System.out.println(error);
            }

            bitm=getRoundedShape(bitm);
            imv.setImageBitmap(bitm);
        }

//        Rcustomer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(customer_form.this, "msg", Toast.LENGTH_SHORT).show();
//                resultvolley();
//            }
//        });



    }
    public  Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 250;
        int targetHeight = 250;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1) / 2, (Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()), new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;}


// volley code init
    public void resultvolley() {
       Toast.makeText(this, "msgggg", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_CUSTOMER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(customer_form.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(customer_form.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstname", frstname.getText().toString());
                params.put("lastname", lastname.getText().toString());
                params.put("email", email.getText().toString());
                params.put("password", pass.getText().toString());
                params.put("retypepassword", retypepass.getText().toString());
                params.put("contactno", contactno.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}





