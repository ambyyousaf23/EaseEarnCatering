package com.example.qureshi.easeearncatering;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qureshi.easeearncatering.Adapters.ChattingAdapter;
import com.example.qureshi.easeearncatering.DataModels.ChatDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.product_act_toolbar)
    Toolbar toolbar;

    @BindView(R.id.message)
    EditText message;

    @BindView(R.id.sendBtn)
    ImageButton sendBtn;

    @BindView(R.id.chatList)
    ListView chatList;

    RequestQueue requestQueue;

    final String SendMessageURL="https://easeearn.000webhostapp.com/sendmessage.php";
    final String ReceiveMessageURL="https://easeearn.000webhostapp.com/receive_message.php";
    String sender_number;
    String prev_date;
    String current_date;
    String date_time;
    Context context;

    Intent intent;
    Handler handler=new Handler();
    Runnable runnable;

    List<ChatDataModel> chatting;
    ChattingAdapter chattingAdapter;

    SharedPreferences preferences;
    String id, firstname, lastname, email, password, contact, location, is_cater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        // set toolbar
        setSupportActionBar(toolbar);

        getPreference();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        receiveMsg();

        chatting=new ArrayList<ChatDataModel>();
        chattingAdapter=new ChattingAdapter(this, chatting,sender_number);
        chatList.setAdapter(chattingAdapter);
        date_time=getCurrentDateTime();
        intent=getIntent();

        Toast.makeText(getApplicationContext(), intent.getStringExtra("receiver_contact")+" and "+sender_number, Toast.LENGTH_SHORT).show();

        runnable=new Runnable() {
            @Override
            public void run() {
                receiveMsg();
                handler.postDelayed(this,10000);
            }
        };
        runnable.run();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chatting.size()>0){
                    if(!getDate(chatting.get(chatting.size()-1).getDatetime()).equals(getDate(getCurrentDateTime()))){
                        ChatDataModel timeModel = new ChatDataModel();
                        timeModel.setDatetime(headerDate(getDate(getCurrentDateTime())));
                        timeModel.setSender_id("");
                        chatting.add(timeModel);
                    }
                }
                ChatDataModel chatDataModel=new ChatDataModel();
                chatDataModel.setMessage(message.getText().toString());
                chatDataModel.setDatetime(date_time);
                chatDataModel.setReceiver_id(intent.getStringExtra("receiver_contact"));
                chatDataModel.setSender_id(sender_number);
                chatting.add(chatDataModel);
                chattingAdapter.notifyDataSetChanged();
                chatList.setSelection(chatting.size()-1);

                StringRequest request=new StringRequest(Request.Method.POST, SendMessageURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        message.setText("");
                        // receiveMsg();
                        Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                        //showData();
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("sender_id", sender_number);
                        parameters.put("receiver_id", intent.getStringExtra("receiver_contact"));
                        parameters.put("message", message.getText().toString());
                        parameters.put("date_time", getCurrentDateTime());
                        return parameters;
                    }
                };
                requestQueue.add(request);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_cart:
                Intent intent=new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                return true;
            case R.id.track:
                Intent intent1=new Intent(getApplicationContext(), Track_Order.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void receiveMsg(){
        StringRequest request=new StringRequest(Request.Method.POST, ReceiveMessageURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                message.setText("");
                //Toast.makeText(getApplicationContext(), "Received", Toast.LENGTH_SHORT).show();
                parseData(response);
                //showData();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("sender",sender_number);
                parameters.put("receiver", intent.getStringExtra("receiver_contact"));
                return parameters;
            }
        };
        requestQueue.add(request);
    }
    public void parseData(String response){
        chatting.clear();
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray arry=jsonObject.getJSONArray("list");
            if(arry.length()>0) {
                ChatDataModel timeModel = new ChatDataModel();
                timeModel.setDatetime(headerDate(getDate(arry.getJSONObject(0).getString("date_time"))));
                timeModel.setSender_id("");
                chatting.add(timeModel);
                for (int i = 0; i < arry.length(); i++) {
                    JSONObject json = arry.getJSONObject(i);
                    if(!compareDate(getDate(json.getString("date_time"))).equals("")){
                        ChatDataModel model = new ChatDataModel();
                        model.setDatetime(current_date);
                        model.setSender_id("");
                        chatting.add(model);
                    }
                    ChatDataModel chatDataModel = new ChatDataModel();
                    chatDataModel.setSender_id(json.getString("sender_id"));
                    chatDataModel.setReceiver_id(json.getString("receiver_id"));
                    chatDataModel.setMessage(json.getString("message"));
                    chatDataModel.setDatetime(json.getString("date_time"));
                    chatting.add(chatDataModel);

                }

                chattingAdapter.notifyDataSetChanged();
                chatList.setSelection(chatting.size() - 1);
            }

        }
        catch (JSONException e) {
            // If an error occurs, this prints the error to the log
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getDate(String dateAndTime){
        String[] date=dateAndTime.split(",");
        return date[1];
    }
    public String getTime(String dateAndTime){
        String[] date=dateAndTime.split(",");
        return date[0];
    }
    public String headerDate(String dt){
        prev_date=dt;
        if(dt.equals(getDate(getCurrentDateTime()))){
            current_date="Today";
            return "Today";
        }
        else {
            current_date=dt;
            return dt;
        }
    }
    public String compareDate(String dt){
        if(!dt.equals(prev_date)){
            return headerDate(dt);
        }
        else {
            return "";
        }

    }
    public String getCurrentDateTime(){

        return  DateUtils.formatDateTime(context, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE |
                        DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_12HOUR);
    }


    public void getPreference(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        id=preferences.getString("id", "");
        firstname=preferences.getString("firstname", "");
        lastname=preferences.getString("lastname", "");
        email=preferences.getString("email", "");
        password=preferences.getString("password", "");
        sender_number=preferences.getString("contactno", "");
        location=preferences.getString("location", "");
        is_cater=preferences.getString("is_cater", "");
    }

}
