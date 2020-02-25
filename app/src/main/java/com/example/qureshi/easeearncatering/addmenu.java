package com.example.qureshi.easeearncatering;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class addmenu extends Fragment {

    Button add;
    EditText items;
    ImageView image;

    Activity mActivity;
    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    ProgressDialog progressDialog;

    private static final String URL_REGISTER_CUSTOMER = "https://easeearn.000webhostapp.com/WebServices/main_menu.php";

    SharedPreferences preferences;
    String id, firstname, lastname, email, password, contact, location, is_cater;

    public addmenu() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_addmenumain, container, false);

        getPreference();

        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");

        image=(ImageView)rootView.findViewById(R.id.menuimage);
        add = rootView.findViewById(R.id.addmenu);
        items = rootView.findViewById(R.id.additemstxt);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String imageFileName = "JPEG_" + timeStamp + "_" + ".jpg";

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progressDialog.show();
                uploadImage(imageFileName);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        return rootView;
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == mActivity.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                // imageView.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private void uploadImage(final String name){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(mActivity,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://easeearn.000webhostapp.com/addMenu.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(mActivity,s , Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(mActivity, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                //     String name1 = name.getText().toString().trim();
                //   String url="https://perkier-reproductio.000webhostapp.com/pics/"+name.getText().toString()+".png";

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("Dishes", items.getText().toString());
                params.put("id", id);
                params.put("image", name);
                params.put("photo", image);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void getPreference(){
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
