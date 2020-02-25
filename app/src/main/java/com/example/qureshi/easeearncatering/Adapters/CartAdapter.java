package com.example.qureshi.easeearncatering.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.Manager.DBManager;
import com.example.qureshi.easeearncatering.CartActivity;
import com.example.qureshi.easeearncatering.CustomVolleyRequest;
import com.example.qureshi.easeearncatering.DataModels.CartDataModel;
import com.example.qureshi.easeearncatering.DataModels.Cart_DataModel;
import com.example.qureshi.easeearncatering.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by Raza Saeed on 3/6/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<CartDataModel> cartsList;
    RequestQueue requestQueue;
    DBManager dbManager;
    ImageLoader imageLoader;

    SharedPreferences preferences;
    String id, firstname, lastname, email, password, contact, location, is_cater;

    public CartAdapter(Context context, List<CartDataModel> cartsList) {
        this.context = context;
        this.cartsList = cartsList;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false);
        return new CartViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        holder.cart_item_tv_product_price.setText(cartsList.get(position).getAmount());
        holder.cart_item_tv_product_quantity_title.setText(cartsList.get(position).getQuantity());
        holder.totalRs.setText(cartsList.get(position).getTotal());
        holder.cart_item_tv_product_title.setText(cartsList.get(position).getDish());

        imageLoader = CustomVolleyRequest.getInstance(context)
                .getImageLoader();
        imageLoader.get(cartsList.get(position).getImage(), ImageLoader.getImageListener(holder.cart_item_iv_product_image
                , 0, android.R.drawable
                        .ic_dialog_alert));
        holder.cart_item_iv_product_image.setImageUrl(cartsList.get(position).getImage(), imageLoader);

    }

    @Override
    public int getItemCount() {
        return cartsList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView cart_item_tv_product_price, cart_item_tv_product_title, totalRs, cart_item_tv_product_quantity_title;
        TextView cart_item_iv_product_remove;
        NetworkImageView cart_item_iv_product_image;
        Button add_q, sub_q;

        Context ctx;

        public CartViewHolder(final View itemView, final Context context) {
            super(itemView);
              this.ctx=context;
            totalRs = (TextView) itemView.findViewById(R.id.totalRs);
            add_q=(Button)itemView.findViewById(R.id.add_q);
            sub_q=(Button)itemView.findViewById(R.id.sub_q);
            cart_item_tv_product_price = (TextView) itemView.findViewById(R.id.cart_item_tv_product_price);
            cart_item_tv_product_quantity_title = (TextView) itemView.findViewById(R.id.cart_item_tv_product_quantity_title);
            cart_item_iv_product_remove = (TextView) itemView.findViewById(R.id.cart_item_iv_product_remove);
            cart_item_iv_product_image=(NetworkImageView)itemView.findViewById(R.id.cart_item_iv_product_image);
            cart_item_tv_product_title=(TextView)itemView.findViewById(R.id.cart_item_tv_product_title);

            cart_item_iv_product_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext()).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Do you really want to delete ?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();
                                    deleteCartItem(cartsList.get(getAdapterPosition()).getSubmenu_id(), cartsList.get(getAdapterPosition()).getCater_id());
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "No", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            });

            add_q.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPreference();
                    cart_item_tv_product_quantity_title.setText(String.valueOf(Integer.parseInt(cart_item_tv_product_quantity_title.getText().toString()) + 1));
                    int a=Integer.parseInt(cart_item_tv_product_price.getText().toString()) * Integer.parseInt(cart_item_tv_product_quantity_title.getText().toString());
                    Toast.makeText(context, a+"", Toast.LENGTH_SHORT).show();

                    dbManager=new DBManager(context);
                    dbManager.open();
                    try {
                        dbManager.createDataBase();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    Intent intent=new Intent(context, CartActivity.class);

                    dbManager.updateCartItem(id, cartsList.get(getAdapterPosition()).getSubmenu_id(), cartsList.get(getAdapterPosition()).getCater_id(), String.valueOf(a), cart_item_tv_product_quantity_title.getText().toString(), intent);

//                    CartActivity.cart_act_tv_product_total_price.setText(String.valueOf(Integer.parseInt(CartActivity.cart_act_tv_product_total_price.getText().toString()) + a));
                }
            });

            sub_q.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPreference();
                    if (Integer.parseInt(cart_item_tv_product_quantity_title.getText().toString()) > 0) {
                        cart_item_tv_product_quantity_title.setText(String.valueOf(Integer.parseInt(cart_item_tv_product_quantity_title.getText().toString()) - 1));
                        int b=Integer.parseInt(cart_item_tv_product_price.getText().toString()) * Integer.parseInt(cart_item_tv_product_quantity_title.getText().toString());
                        Toast.makeText(context, b+"", Toast.LENGTH_SHORT).show();

                        dbManager=new DBManager(context);
                        dbManager.open();
                        try {
                            dbManager.createDataBase();
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        Intent intent=new Intent(context, CartActivity.class);

                        dbManager.updateCartItem(id, cartsList.get(getAdapterPosition()).getSubmenu_id(), cartsList.get(getAdapterPosition()).getCater_id(), String.valueOf(b), cart_item_tv_product_quantity_title.getText().toString(), intent);

                        //                        CartActivity.cart_act_tv_product_total_price.setText(String.valueOf(Integer.parseInt(CartActivity.cart_act_tv_product_total_price.getText().toString()) - b));
                    }
                }
            });

        }

        @Override
        public void onClick(View view) {

            final Dialog dialog = new Dialog(ctx);
            dialog.setContentView(R.layout.layout_dialog_options);

            final TextView idMenu = (TextView) dialog.findViewById(R.id.idMenu);
            idMenu.setText(cartsList.get(getAdapterPosition()).getSubmenu_id());

            TextView nameMenu = (TextView) dialog.findViewById(R.id.nameMenu);
            nameMenu.setText(cartsList.get(getAdapterPosition()).getDish());

            final TextView priceMenu = (TextView) dialog.findViewById(R.id.priceMenu);
            priceMenu.setText(cartsList.get(getAdapterPosition()).getAmount());

            String[] quantity = {"Quantity", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
            final Spinner spinnerMenu = (Spinner) dialog.findViewById(R.id.spinnerMenu);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, quantity);
            spinnerMenu.setAdapter(arrayAdapter);


            final Button btnCartMenu = (Button) dialog.findViewById(R.id.btnCartMenu);
            btnCartMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (spinnerMenu.getSelectedItem().toString().equals("Quantity")) {
                        Toast.makeText(context, "Select quantity to update", Toast.LENGTH_SHORT).show();
                    } else {
                        int quantityValue = 0;
                        quantityValue = Integer.parseInt(spinnerMenu.getSelectedItem().toString());
                        float priceTotal = 0;
                        priceTotal = Float.parseFloat(priceMenu.getText().toString());
                        float mul = 0;
                        mul = quantityValue * priceTotal;
                        Toast.makeText(context, mul + "", Toast.LENGTH_SHORT).show();
                        updatemyCart(idMenu.getText().toString(), spinnerMenu.getSelectedItem().toString(), mul + "");
                        Toast.makeText(ctx, "asd", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            dialog.show();

        }
    }

    public void deleteCartItem(String submenu_id, String cater_id) {

        dbManager=new DBManager(context);
        dbManager.open();
        try {
            dbManager.createDataBase();
        }catch (IOException e){
            e.printStackTrace();
        }

        Intent intent=new Intent(context, CartActivity.class);
        dbManager.deleteCartItem(submenu_id, cater_id, intent);

    }

    public void updatemyCart(final String id, final String quantity, final String total) {

        dbManager=new DBManager(context);
        dbManager.open();
        try {
            dbManager.createDataBase();
        }catch (IOException e){
            e.printStackTrace();
        }

        Intent intent=new Intent(context, CartActivity.class);
//        dbManager.updateCartItem(SharedClass.user_id, id, SharedClass.store_mart_id, total, quantity, intent);

    }

    public void getPreference(){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        id=preferences.getString("id", "");
        firstname=preferences.getString("firstname", "");
        lastname=preferences.getString("lastname", "");
        email=preferences.getString("email", "");
        password=preferences.getString("password", "");
        contact=preferences.getString("contact", "");
        location=preferences.getString("location", "");
        is_cater=preferences.getString("is_cater", "");
    }

}
