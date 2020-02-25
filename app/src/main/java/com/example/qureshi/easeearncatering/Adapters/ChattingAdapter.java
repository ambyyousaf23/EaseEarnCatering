package com.example.qureshi.easeearncatering.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qureshi.easeearncatering.DataModels.ChatDataModel;
import com.example.qureshi.easeearncatering.R;

import java.util.List;

/**
 * Created by Dell on 5/22/2017.
 */

public class ChattingAdapter extends BaseAdapter {


    List<ChatDataModel> chatting;
    Context context;
    String sender_number;
    public ChattingAdapter(Context context, List<ChatDataModel> chatting, String sender_number) {
        this.chatting = chatting;
        this.context = context;
        this.sender_number=sender_number;
    }

    @Override
    public int getCount() {
        return chatting.size();
    }

    @Override
    public Object getItem(int position) {
        return chatting.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView=null;
        if(chatting.get(position).getSender_id().equals("")){
            rowView= View.inflate(context, R.layout.time_header, null);
            TextView dateText=(TextView)rowView.findViewById(R.id.date_tv);
            dateText.setText(chatting.get(position).getDatetime());
            return rowView;
        }
        else if(chatting.get(position).getSender_id().equals(sender_number)){
            rowView= View.inflate(context, R.layout.sender_chat, null);
        }
        else {
            rowView= View.inflate(context, R.layout.receiver_chat, null);
        }

        TextView sendMessageText=(TextView)rowView.findViewById(R.id.sendMessageText);
        TextView timeText=(TextView)rowView.findViewById(R.id.time_tv);
        sendMessageText.setText(chatting.get(position).getMessage());
        timeText.setText(getTime(chatting.get(position).getDatetime()));
        return rowView;
    }
    public String getTime(String dateAndTime){
        String[] date=dateAndTime.split(",");
        return date[0];
    }
}
