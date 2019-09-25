package com.haqq.namu.Others;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haqq.namu.Models.Message;
import com.haqq.namu.R;

import java.util.ArrayList;

/**
 * Created by sagar on 2/7/17.
 */

class MessageAdapter extends BaseAdapter {

    private final Context messageContext;
    private final ArrayList<Message> messageList;
    private Message message;

    public MessageAdapter(Context context, ArrayList<Message> messages){
        messageList = messages;
        messageContext = context;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageViewHolder holder;

        LayoutInflater messageInflater = (LayoutInflater) messageContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        message=(Message) getItem(position);
        Log.e("getView NAMEEE : : : ",message.getName()+" position  "+String.valueOf(position));


        if(message.getName().equals("Server"))
            convertView = messageInflater.inflate(R.layout.message_receive, null);
        else
            convertView = messageInflater.inflate(R.layout.message_send, null);

        holder = new MessageViewHolder();
        holder.bodyView = (TextView) convertView.findViewById(R.id.message_body);
        convertView.setTag(holder);
        return convertView;
    }

    public void add(Message message){
        messageList.add(message);
        notifyDataSetChanged();
        Log.e("Adapter Data : add : ",message.getText()+"  "+message.getName());
    }

    public void add_sender_message(Message message){
        messageList.add(message);
        notifyDataSetChanged();
        Log.e("Adapter Data : send:",message.getText()+"  "+message.getName());
    }



    private static class MessageViewHolder {
        public TextView bodyView;
    }
}
