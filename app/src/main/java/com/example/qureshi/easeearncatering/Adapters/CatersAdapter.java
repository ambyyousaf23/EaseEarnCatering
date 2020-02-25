package com.example.qureshi.easeearncatering.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qureshi.easeearncatering.ChatActivity;
import com.example.qureshi.easeearncatering.DataModels.CatersDataModel;
import com.example.qureshi.easeearncatering.R;
import com.example.qureshi.easeearncatering.ViewMenu;

import java.util.List;

/**
 * Created by qureshi on 27/04/2018.
 */

public class CatersAdapter extends RecyclerView.Adapter<CatersAdapter.MyAdapter> {

    Context context;
    List<CatersDataModel> caters;

    public CatersAdapter(Context context, List<CatersDataModel> caters) {
        this.context = context;
        this.caters = caters;
    }

    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.caterscustomcell, null);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, int position) {
        holder.name.setText(caters.get(position).getFirstname() +""+ caters.get(position).getLastname());
    }

    @Override
    public int getItemCount() {
        return caters.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton sms, call;

        public MyAdapter(View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.name);
            sms=(ImageButton)itemView.findViewById(R.id.sms);
            call=(ImageButton)itemView.findViewById(R.id.call);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent my_callIntent = new Intent(Intent.ACTION_CALL);
                        my_callIntent.setData(Uri.parse("tel:"+caters.get(getAdapterPosition()).getContact()));
                        //here the word 'tel' is important for making a call...
                        context.startActivity(my_callIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ViewMenu.class);
                    intent.putExtra("id", caters.get(getAdapterPosition()).getId());
                    intent.putExtra("cater_name", caters.get(getAdapterPosition()).getFirstname()+" "+caters.get(getAdapterPosition()).getLastname());
                    context.startActivity(intent);
                }
            });

            sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ChatActivity.class);
                    intent.putExtra("id", caters.get(getAdapterPosition()).getId());
                    intent.putExtra("receiver_contact", caters.get(getAdapterPosition()).getContact());
                    context.startActivity(intent);
                }
            });

        }
    }
}
