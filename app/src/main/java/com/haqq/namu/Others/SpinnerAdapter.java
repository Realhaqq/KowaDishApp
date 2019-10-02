package com.haqq.namu.Others;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haqq.namu.Models.ItemData;
import com.haqq.namu.R;

import java.util.ArrayList;

/**
 * Created by sagar on 29/6/17.
 */

public class SpinnerAdapter extends ArrayAdapter<ItemData> {

    private final int groupid;
    Activity context;
    private final ArrayList<ItemData> list;
    private final LayoutInflater inflater;

    public SpinnerAdapter(Activity context, int groupid, int id, ArrayList<ItemData> list){

        super(context,id,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
    }


    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent ){
        View itemView = convertView == null ? inflater.inflate(groupid, parent, false) : convertView;
        ImageView imageView = (ImageView) itemView.findViewById(R.id.food_image);
        TextView textView = (TextView) itemView.findViewById(R.id.food_name);
        imageView.setImageResource(list.get(position).getImageId());
        textView.setText(list.get(position).getText());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent){
        return getView(position,convertView,parent);
    }
}
