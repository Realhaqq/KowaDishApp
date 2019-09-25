package com.haqq.namu.Adapters;

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
import com.haqq.namu.Models.Foods;
import com.haqq.namu.R;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    CartActivity context;
    public List<Foods> coursesList;

    public DetailsAdapterListener onClickListener;
    private final CartAdapter.VenueAdapterClickCallbacks venueAdapterClickCallbacks;
    private double amount=0.0;


    public CartAdapter(CartActivity context, List<Foods> coursesList, VenueAdapterClickCallbacks venueAdapterClickCallbacks) {
        super();
        this.coursesList = coursesList;
        this.context = context;
        this.venueAdapterClickCallbacks = venueAdapterClickCallbacks;
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        final TextView name;
        final TextView price;
        final TextView no_of_items;
        final ImageView image;
        final ImageView add;
        final ImageView remove;
        final Button remove_from_cart;




        public MyViewHolder(View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.food_name);
            this.price =  itemView.findViewById(R.id.price);
            this.image = itemView.findViewById(R.id.image);
            this.add = itemView.findViewById(R.id.add);
            this.remove = itemView.findViewById(R.id.remove);
            this.no_of_items = itemView.findViewById(R.id.no_of_items);
            this.remove_from_cart = itemView.findViewById(R.id.delete_cart);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_cards, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        TextView name = holder.name;
        final TextView price = holder.price;
        ImageView image = holder.image;
        final TextView no_of_items=holder.no_of_items;
        ImageView add=holder.add;
        ImageView remove=holder.remove;
        Button remove_btn = holder.remove_from_cart;

        Foods foods = coursesList.get(position);
        name.setText(foods.getTitle());
        no_of_items.setText(foods.getQty());
        holder.price.setText("₦" + foods.getPrice());
        Glide.with(context)
                .load(foods.getImage())
//                .centerCrop(150, 150)
//                .resize(150, 150)
//                .fit()
                .placeholder(R.drawable.namudown)
//                .centerInside()
                .error(R.drawable.namudown)
                .into(image);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int cal_dist,d;
                String a=no_of_items.getText().toString();
                d=Integer.parseInt(a);
                cal_dist=++d;
                no_of_items.setText(cal_dist+"");
                DecimalFormat numberFormat = new DecimalFormat("#.00");
                price.setText("₦" +numberFormat.format( cal_dist*Double.parseDouble(coursesList.get(position).getPrice())));
                amount= amount + Double.parseDouble(coursesList.get(position).getPrice());
                String id = coursesList.get(position).getId();

//                String id = cours|
//                esList.get(position).getId();
                venueAdapterClickCallbacks.onCardClick(amount+"", id);
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int cal_dist=1,d;
                String a=no_of_items.getText().toString();
                d=Integer.parseInt(a);
                if(d>1){
                    cal_dist=--d;
                    amount= amount - Double.parseDouble(coursesList.get(position).getPrice());}
                no_of_items.setText(cal_dist+"");
                DecimalFormat numberFormat = new DecimalFormat("#.00");
                price.setText("₦" + numberFormat.format(cal_dist*Double.parseDouble(coursesList.get(position).getPrice())));

                String id = coursesList.get(position).getId();
                venueAdapterClickCallbacks.onCardMinusClick(amount + "", id);
                venueAdapterClickCallbacks.onCardClick(amount + "", amount + "");

            }
        });

        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = coursesList.get(position).getId();
                venueAdapterClickCallbacks.onButtonClick(amount + "", id);

            }
        });

        price.setText("₦"+coursesList.get(position).getPrice());
//        image.setImageResource(coursesList.get(position).getImage());

        amount= amount + Double.parseDouble(coursesList.get(position).getPrice());


        venueAdapterClickCallbacks.onCardClick(amount + "", amount+"");

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
        void onCardClick(String s, String p);

        void onCardMinusClick(String s, String id);

        void onButtonClick(String s, String id);
    }


}
