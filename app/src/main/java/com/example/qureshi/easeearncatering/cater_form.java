package com.example.qureshi.easeearncatering;

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
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qureshi on 06/02/2018.
 */

public class cater_form extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imv;

    @BindView(R.id.caterfirstname)
    EditText f_name;

    @BindView(R.id.caterlastname)
    EditText l_name;

    @BindView(R.id.cateremail)
    EditText email;

    @BindView(R.id.caterpassword)
    EditText pass;

    @BindView(R.id.caterphnnum)
    EditText contctno;

    @BindView(R.id.btncreateaccount)
    Button btncreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cater_form);

        ButterKnife.bind(this);

    }
}





