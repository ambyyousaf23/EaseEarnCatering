package com.example.qureshi.easeearncatering;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.example.Manager.DBManager;
import com.example.qureshi.easeearncatering.R;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class order extends AppCompatActivity {

    @BindView(R.id.product_act_toolbar)
    Toolbar toolbar;

    @BindView(R.id.product_act_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.product_act_appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.home_slider_layout)
    SliderLayout sliderLayout;

    @BindView(R.id.product_item_btn_add_to_cart)
    RelativeLayout product_item_btn_add_to_cart;

    @BindView(R.id.product_item_tv_title_name)
    TextView product_item_tv_title_name;

    @BindView(R.id.seller)
    TextView seller;

    @BindView(R.id.name)
    TextView name;

    DefaultSliderView sliderView;

    TextView quantity, total, pricetxt, date;
    Button add, sub;

    HashMap<String, Integer> slider_images;

    int q = 0;

    Intent intent;
    String menu_id, sub_menu_id, sub_menu_dish, cater_id, image, dish, cater_name, price;

    DBManager dbManager;

    SharedPreferences preferences;
    String id, firstname, lastname, email, password, contact, location, is_cater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ButterKnife.bind(this);
        getPreference();

        // set toolbar
        setSupportActionBar(toolbar);

        dbManager = new DBManager(this);
        dbManager.open();
        try {
            dbManager.createDataBase();
        } catch (IOException e) {

        }

        intent = getIntent();
        menu_id = intent.getStringExtra("menu_id");
        sub_menu_id = intent.getStringExtra("sub_menu_id");
        sub_menu_dish = intent.getStringExtra("sub_menu_dish");
        cater_id = intent.getStringExtra("cater_id");
        image = intent.getStringExtra("image");
        dish = intent.getStringExtra("main_dish");
        cater_name=intent.getStringExtra("cater_name");
        price=intent.getStringExtra("price");

        product_item_tv_title_name.setText(dish);
        name.setText(sub_menu_dish);
        seller.setText(cater_name);

        Toast.makeText(this, menu_id + " and " + sub_menu_id + " and " + sub_menu_dish + " and " + cater_id, Toast.LENGTH_SHORT).show();

        sliderLayout = (SliderLayout) findViewById(R.id.home_slider_layout);

        slider_images = new HashMap<>();
        slider_images.put("1", R.drawable.watch_1);
        slider_images.put("2", R.drawable.watch_2);
        slider_images.put("3", R.drawable.watch_3);
        slider_images.put("4", R.drawable.watch_4);

        for (String name : slider_images.keySet()) {
            sliderView = new DefaultSliderView(getApplicationContext());
            sliderView.image(slider_images.get(name))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
            ;
            sliderLayout.addSlider(sliderView);
        }

        product_item_btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(order.this);
                dialog.setContentView(R.layout.addcustomcell);

                quantity = (TextView) dialog.findViewById(R.id.quantity);
                total = (TextView) dialog.findViewById(R.id.total);
                add = (Button) dialog.findViewById(R.id.add);
                sub = (Button) dialog.findViewById(R.id.sub);
                pricetxt = (TextView) dialog.findViewById(R.id.price);
                date = (TextView) dialog.findViewById(R.id.date);
                Button finalorder = (Button) dialog.findViewById(R.id.finalorder);

                date.setText(DateUtils.formatDateTime(order.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_12HOUR));

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quantity.setText(String.valueOf(Integer.parseInt(quantity.getText().toString()) + 1));
                        total.setText(String.valueOf(Integer.parseInt(pricetxt.getText().toString()) * Integer.parseInt(quantity.getText().toString())));
                    }
                });

                sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Integer.parseInt(quantity.getText().toString()) > 0) {
                            quantity.setText(String.valueOf(Integer.parseInt(quantity.getText().toString()) - 1));
                            total.setText(String.valueOf(Integer.parseInt(pricetxt.getText().toString()) * Integer.parseInt(quantity.getText().toString())));
                        }
                    }
                });

                finalorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbManager.syncCartProduct(menu_id, sub_menu_id, sub_menu_dish, cater_id, id, quantity.getText().toString(), pricetxt.getText().toString(), total.getText().toString(), date.getText().toString(), image);
                    }
                });

                dialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_cart:
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getPreference() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        id = preferences.getString("id", "");
        firstname = preferences.getString("firstname", "");
        lastname = preferences.getString("lastname", "");
        email = preferences.getString("email", "");
        password = preferences.getString("password", "");
        contact = preferences.getString("contactno", "");
        location = preferences.getString("location", "");
        is_cater = preferences.getString("is_cater", "");
    }

}
