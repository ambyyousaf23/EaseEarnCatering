package com.example.qureshi.easeearncatering.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qureshi.easeearncatering.ChatActivity;
import com.example.qureshi.easeearncatering.DataModels.CatersDataModel;
import com.example.qureshi.easeearncatering.DataModels.CustomerDataModel;
import com.example.qureshi.easeearncatering.DataModels.UsersDataModel;
import com.example.qureshi.easeearncatering.R;

import java.util.List;

/**
 * Created by qureshi on 10/05/2018.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyAdapter> {

    Context context;
    List<UsersDataModel> catersList;

    public UsersAdapter(Context context, List<UsersDataModel> catersList) {
        this.context = context;
        this.catersList = catersList;
    }

    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_users_customcell, null);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, int position) {
holder.username.setText(catersList.get(position).getFirstname()+" "+catersList.get(position).getLastname());
    }

    @Override
    public int getItemCount() {
        return catersList.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        TextView username;

        public MyAdapter(View itemView) {
            super(itemView);
            username=(TextView)itemView.findViewById(R.id.username);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ChatActivity.class);
                    intent.putExtra("receiver_contact", catersList.get(getAdapterPosition()).getContact());
                    context.startActivity(intent);
                }
            });

        }
    }
}
