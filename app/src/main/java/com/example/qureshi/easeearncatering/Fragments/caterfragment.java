package com.example.qureshi.easeearncatering.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qureshi.easeearncatering.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qureshi on 19/03/2018.
 */

public class caterfragment extends Fragment {

    ImageView imv;
    EditText f_name;
    EditText l_name;
    EditText email;
    EditText pass;
    EditText contctno;
    Button btncreate;

    String url,url1,url2, finalurl, image, image1, image2;
    Activity mActivity;
    private Bitmap bitmap, bitmap1, bitmap2;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    private int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL ="https://perkier-reproductio.000webhostapp.com/finalImage.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cater_form, container, false);

        imv=(ImageView)rootView.findViewById(R.id.caterimage);
        f_name=(EditText)rootView.findViewById(R.id.caterfirstname);
        l_name=(EditText)rootView.findViewById(R.id.caterlastname);
        email=(EditText)rootView.findViewById(R.id.emailTxt);
        pass=(EditText)rootView.findViewById(R.id.passwordTxt);
        contctno=(EditText)rootView.findViewById(R.id.contact);
        btncreate=(Button)rootView.findViewById(R.id.btncreateaccount);

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (f_name.getText().toString().length() <= 0)
                {
                    f_name.setError("Please Enter Title !");
                }
                else if (l_name.getText().toString().length() <= 0)
                {
                    l_name.setError("Please Enter Title !");
                }
                else if (email.getText().toString().length() <= 0)
                {
                    email.setError("Please Enter Description !");
                }
                else if (pass.getText().toString().length() <= 0)
                {
                    pass.setError("Please Enter Price !");
                }
                else if (contctno.getText().toString().length() <= 0)
                {
                    contctno.setError("Please Enter Name !");
                }
                else {
                    url=contctno.getText().toString()+"one";
                    finalurl=url;
                    Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
                    uploadImage();
                }
            }
        });
        imv.setOnClickListener(new View.OnClickListener() {
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
                imv.setImageBitmap(bitmap);
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
    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(mActivity,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
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
                params.put("firstname", f_name.getText().toString());
                params.put("lastname", l_name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("password", pass.getText().toString());
                params.put("contactno", contctno.getText().toString());
                params.put("image", image);
                params.put("photoname", url);
                params.put("location", "0,0");

                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
