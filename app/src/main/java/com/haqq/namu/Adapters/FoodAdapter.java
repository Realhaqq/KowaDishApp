package com.haqq.namu.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haqq.namu.Activities.FoodListActivity;
import com.haqq.namu.Models.Foods;
import com.haqq.namu.R;

import java.text.DecimalFormat;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {
    FoodListActivity context;
    public List<Foods> coursesList;
    public DetailsAdapterListener onClickListener;


    public FoodAdapter(List<Foods> coursesList, FoodListActivity context, DetailsAdapterListener listener) {
        super();
        this.coursesList = coursesList;
        this.context = context;
        this.onClickListener = listener;

    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, price;
        public Button btn_add_to_cart;
        public ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            btn_add_to_cart = itemView.findViewById(R.id.add_to_cart);

            btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.classOnClick(v, getAdapterPosition());
                }
            });

            img = itemView.findViewById(R.id.image);

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Foods foods = coursesList.get(position);
        holder.title.setText(foods.getTitle());
        holder.price.setText("₦" + foods.getPrice());
        Glide.with(context)
                .load(foods.getImage())
//                .centerCrop(150, 150)
//                .resize(150, 150)
//                .fit()
                .placeholder(R.drawable.namudown)
//                .centerInside()
                .error(R.drawable.namudown)
                .into(holder.img);
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
}
