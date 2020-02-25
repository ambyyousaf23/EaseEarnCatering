package com.example.qureshi.easeearncatering.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.qureshi.easeearncatering.CustomVolleyRequest;
import com.example.qureshi.easeearncatering.DataModels.MenuDataModel;
import com.example.qureshi.easeearncatering.DataModels.SubMenuDataModel;
import com.example.qureshi.easeearncatering.R;
import com.example.qureshi.easeearncatering.ViewMenu;
import com.example.qureshi.easeearncatering.order;
import com.example.qureshi.easeearncatering.submenu;

import java.util.List;

/**
 * Created by qureshi on 27/04/2018.
 */

public class SubMenuAdapter extends RecyclerView.Adapter<SubMenuAdapter.MyAdapter> {

    Context context;
    List<SubMenuDataModel> menu;
    ImageLoader imageLoader;
    String menu_id;
    String cater_id;
    String dish;
    String cater_name;

    public SubMenuAdapter(Context context, List<SubMenuDataModel> menu, String menu_id, String cater_id, String dish, String cater_name) {
        this.context = context;
        this.menu = menu;
        this.menu_id=menu_id;
        this.cater_id=cater_id;
        this.dish=dish;
        this.cater_name=cater_name;
    }

    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menucustomcell, null);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, int position) {
        holder.name.setText(menu.get(position).getDishes());

        imageLoader = CustomVolleyRequest.getInstance(context)
                .getImageLoader();
        imageLoader.get(menu.get(position).getImage(), ImageLoader.getImageListener(holder.marts_image
                , 0, android.R.drawable
                        .ic_dialog_alert));
        holder.marts_image.setImageUrl(menu.get(position).getImage(), imageLoader);
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView name;
        NetworkImageView marts_image;

        public MyAdapter(View itemView) {
            super(itemView);



            name=(TextView)itemView.findViewById(R.id.name);
            marts_image=(NetworkImageView)itemView.findViewById(R.id.marts_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, order.class);
                    intent.putExtra("menu_id", menu_id);
                    intent.putExtra("sub_menu_id", menu.get(getAdapterPosition()).getMenu_id());
                    intent.putExtra("sub_menu_dish", menu.get(getAdapterPosition()).getDishes());
                    intent.putExtra("cater_id", cater_id);
                    intent.putExtra("image", menu.get(getAdapterPosition()).getImage());
                    intent.putExtra("main_dish", dish);
                    intent.putExtra("cater_name", cater_name);
                    intent.putExtra("price", menu.get(getAdapterPosition()).getPrice());
                    context.startActivity(intent);
                }
            });

        }

    }
}
