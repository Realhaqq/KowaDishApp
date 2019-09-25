package com.haqq.namu.Adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haqq.namu.Activities.CartActivity;
import com.haqq.namu.Activities.ConfirmActivity;
import com.haqq.namu.Models.Foods;
import com.haqq.namu.R;

import java.text.DecimalFormat;
import java.util.List;

public class ConfirmAdapter extends RecyclerView.Adapter<ConfirmAdapter.MyViewHolder> {
    ConfirmActivity context;
    public List<Foods> coursesList;

    public DetailsAdapterListener onClickListener;
    private final ConfirmAdapter.VenueAdapterClickCallbacks venueAdapterClickCallbacks;
    private double amount=0.0;


    public ConfirmAdapter(ConfirmActivity context, List<Foods> coursesList, VenueAdapterClickCallbacks venueAdapterClickCallbacks) {
        super();
        this.coursesList = coursesList;
        this.context = context;
        this.venueAdapterClickCallbacks = venueAdapterClickCallbacks;
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        final TextView name;
        final TextView price;




        public MyViewHolder(View itemView) {
            super(itemView);

            this.name = (TextView) itemView.findViewById(R.id.item_name);
            this.price = (TextView) itemView.findViewById(R.id.price);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        TextView name = holder.name;
        TextView price = holder.price;

        name.setText(coursesList.get(position).getTitle()+" "+coursesList.get(position).getQty());

        int s = Integer.parseInt(coursesList.get(position).getQty());

//        int q=Integer.parseInt(s.substring(2,s.length()-1))


        int newone = Integer.parseInt(coursesList.get(position).getPrice());

        price.setText(newone*s+"");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                venueAdapterClickCallbacks.onCardClick(coursesList.get(position).getTitle());

            }
        });

    }


    @Override
    public int getItemCount() {
        return coursesList.size();
    }


    //region Interface Details listener
    public interface DetailsAdapterListener {

        void classOnClick(View v, int position);

    }
    //endregion

    public interface VenueAdapterClickCallbacks {
        void onCardClick(String title);
    }


}
