package com.example.qureshi.easeearncatering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class cater_login_view extends AppCompatActivity {
Button menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cater_login_view);
        menu=(Button)findViewById(R.id.btnmenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(cater_login_view.this,addmenu.class);
                startActivity(i);
            }
        });
    }
}
