package com.example.qureshi.easeearncatering.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.SharedClass;
import com.example.qureshi.easeearncatering.DataModels.CatersDataModel;
import com.example.qureshi.easeearncatering.DataModels.OrderDataModel;
import com.example.qureshi.easeearncatering.R;
import com.example.qureshi.easeearncatering.ReadInvoice;
import com.example.qureshi.easeearncatering.ViewMenu;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by qureshi on 27/04/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyAdapter> {

    Context context;
    List<OrderDataModel> caters;

    public OrderAdapter(Context context, List<OrderDataModel> caters) {
        this.context = context;
        this.caters = caters;
    }

    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ordercustomcell, null);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, int position) {
        holder.name.setText(caters.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return caters.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView name;

        public MyAdapter(View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(caters);
                    Intent intent = new Intent(context,ReadInvoice.class);
                    intent.putExtra("KEY",jsonString);

                    SharedClass.id=caters.get(getAdapterPosition()).getId();
                    SharedClass.menu_id=caters.get(getAdapterPosition()).getMenu_id();
                    SharedClass.submenu_id=caters.get(getAdapterPosition()).getSubmenu_id();
                    SharedClass.dish=caters.get(getAdapterPosition()).getDish();
                    SharedClass.user_id=caters.get(getAdapterPosition()).getUser_id();
                    SharedClass.quantity=caters.get(getAdapterPosition()).getQuantity();
                    SharedClass.amount=caters.get(getAdapterPosition()).getAmount();
                    SharedClass.total=caters.get(getAdapterPosition()).getTotal();
                    SharedClass.datetime=caters.get(getAdapterPosition()).getDatetime();
                    SharedClass.image=caters.get(getAdapterPosition()).getImage();
                    SharedClass.email=caters.get(getAdapterPosition()).getEmail();

                    context.startActivity(intent);

                }
            });

        }
    }
}
