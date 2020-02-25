package com.example.qureshi.easeearncatering.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qureshi.easeearncatering.DataModels.FinalDataModel;
import com.example.qureshi.easeearncatering.DataModels.OrderDataModel;
import com.example.qureshi.easeearncatering.R;

import java.util.List;

public class FinalDataAdapter extends BaseAdapter {

    Context context;
    List<FinalDataModel> invoice;

    public FinalDataAdapter(Context context, List<FinalDataModel> invoice) {
        this.context = context;
        this.invoice = invoice;
    }

    @Override
    public int getCount() {
        return invoice.size();
    }

    @Override
    public Object getItem(int i) {
        return invoice.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView=View.inflate(context, R.layout.invoice_customcell, null);

        TextView dish=(TextView)rowView.findViewById(R.id.i_dish);
        TextView quantity=(TextView)rowView.findViewById(R.id.i_quantity);
        TextView total=(TextView)rowView.findViewById(R.id.i_total);
        TextView datetime=(TextView)rowView.findViewById(R.id.i_datetime);

        dish.append(invoice.get(i).getDish()+"\n");
        quantity.append(invoice.get(i).getQuantity()+"\n");
        total.append(invoice.get(i).getTotal()+"\n");
        datetime.append(invoice.get(i).getDatetime()+"\n");

        return rowView;
    }
}
