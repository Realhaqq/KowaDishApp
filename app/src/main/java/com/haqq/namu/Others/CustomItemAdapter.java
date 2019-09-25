package com.haqq.namu.Others;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haqq.namu.Models.Item;
import com.haqq.namu.R;

import java.util.List;

/**
 * Created by sagar on 28/6/17.
 */

public class CustomItemAdapter extends RecyclerView.Adapter<CustomItemAdapter.MyHolder>{

    private RecyclerView re;
    private final List<Item> dataSet ;
    private Context context=null;
    private final VenueAdapterClickCallbacks venueAdapterClickCallbacks;



    public class MyHolder extends RecyclerView.ViewHolder
    {
        final TextView name;
        final ImageView image;

        public MyHolder(View itemView)
        {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.food_name);
            this.image=(ImageView)itemView.findViewById(R.id.food_image);
        }
    }

    public CustomItemAdapter(Context c, List<Item> data, VenueAdapterClickCallbacks venueAdapterClickCallback)
    {

        this.dataSet = data;
        this.venueAdapterClickCallbacks=venueAdapterClickCallback;
        context=c;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item, parent, false);
        MyHolder myNewsHolder=new MyHolder(view);
        re = (RecyclerView) parent.findViewById(R.id.horizontal_recycler_view);
        return myNewsHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        TextView name = holder.name;
        ImageView imageView=holder.image;

        name.setText(dataSet.get(position).getName());
        imageView.setImageResource(dataSet.get(position).getImage());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                venueAdapterClickCallbacks.onCardClick(dataSet.get(position).getName());

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface VenueAdapterClickCallbacks {
        void onCardClick( String p);
    }


}
