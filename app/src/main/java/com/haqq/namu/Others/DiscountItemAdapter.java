package com.haqq.namu.Others;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haqq.namu.Models.DiscountItem;
import com.haqq.namu.R;

import java.util.List;

/**
 * Created by sagar on 28/6/17.
 */

public class DiscountItemAdapter extends RecyclerView.Adapter<DiscountItemAdapter.MyHolder>{

    private RecyclerView re;
    private final List<DiscountItem> dataSet ;
    private Context context=null;
    private final VenueAdapterClickCallbacks venueAdapterClickCallbacks;

    public class MyHolder extends RecyclerView.ViewHolder
    {
        final TextView name;
        final TextView price;
        final TextView discount;
        final ImageView image;

        public MyHolder(View itemView)
        {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.item_name);
            this.price = (TextView) itemView.findViewById(R.id.price);
            this.discount=(TextView)itemView.findViewById(R.id.discount);
            this.image=(ImageView)itemView.findViewById(R.id.image);
        }
    }

    public DiscountItemAdapter(Context c, List<DiscountItem> data, VenueAdapterClickCallbacks venueAdapterClickCallback)
    {

        this.dataSet = data;
        this.venueAdapterClickCallbacks=venueAdapterClickCallback;
        context=c;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discount_item_layout, parent, false);
        MyHolder myNewsHolder=new MyHolder(view);
//        re = (RecyclerView) parent.findViewById(R.id.discount_recycle_grid);
        return myNewsHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        TextView name = holder.name;
        TextView price = holder.price;
        TextView discount = holder.discount;
        ImageView image=holder.image;
        name.setText(dataSet.get(position).getName());

        ImageSpan is = new ImageSpan(context, R.drawable.non_veg);
        SpannableString texts = new SpannableString(name.getText().toString().concat("   "));
        texts.setSpan(is,texts.length()-1,texts.length(),0);
        name.setText(texts);

        price.setText(dataSet.get(position).getPrice());
        discount.setText(dataSet.get(position).getDiscount());
        image.setImageResource(dataSet.get(position).getImage());

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


