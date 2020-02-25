package com.example.qureshi.easeearncatering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class create_account extends AppCompatActivity {
Button customer,cater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        cater=(Button)findViewById(R.id.regascater);
        customer=findViewById(R.id.regascustomer);
        cater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(create_account.this,CaterRegistration.class);
                startActivity(intent);
            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),customer_form.class);
                startActivity(intent);
            }
        });

    }
}
