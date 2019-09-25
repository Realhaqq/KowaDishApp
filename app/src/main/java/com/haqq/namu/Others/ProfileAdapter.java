package com.haqq.namu.Others;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haqq.namu.Models.Profile;
import com.haqq.namu.R;

import java.util.List;

/**
 * Created by sagar on 29/6/17.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyHolder>  {

    private RecyclerView re;
    private final List<Profile> dataSet ;
    private Context context=null;
    private final VenueAdapterClickCallbacks venueAdapterClickCallbacks;




    public class MyHolder extends RecyclerView.ViewHolder
    {
        final TextView address_type;
        final TextView address;
        final ImageView drop_down;
        final TextView edit;
        final TextView remove;
        final CardView card;


        public MyHolder(View itemView)
        {
            super(itemView);
            this.address_type = (TextView) itemView.findViewById(R.id.address_type);
            this.address = (TextView) itemView.findViewById(R.id.address);
            this.drop_down=(ImageView)itemView.findViewById(R.id.drop_down);
            this.edit=(TextView)itemView.findViewById(R.id.edit);
            this.remove=(TextView)itemView.findViewById(R.id.remove);
            this.card=(CardView)itemView.findViewById(R.id.drop_down_card);

        }
    }

    public ProfileAdapter(Context c, List<Profile> data, VenueAdapterClickCallbacks venueAdapterClickCallback)
    {

        this.dataSet = data;
        this.venueAdapterClickCallbacks=venueAdapterClickCallback;
        context=c;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_address_layout, parent, false);
        MyHolder myNewsHolder=new MyHolder(view);
        re = (RecyclerView) parent.findViewById(R.id.profile_grid);
        return myNewsHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        TextView type = holder.address_type;
        TextView address = holder.address;
        final ImageView drop_down=holder.drop_down;
        TextView edit=holder.edit;
        TextView remove=holder.remove;
        final CardView card=holder.card;

        type.setText(dataSet.get(position).getAdd_type());
        address.setText(dataSet.get(position).getAddress());

        drop_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                card.setVisibility(View.VISIBLE);

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                card.setVisibility(View.GONE);
                venueAdapterClickCallbacks.onEditClick();


            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                card.setVisibility(View.GONE);


            }
        });




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(card.getVisibility()==View.VISIBLE)
                    card.setVisibility(View.GONE);
                venueAdapterClickCallbacks.onCardClick(dataSet.get(position).getAdd_type());

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface VenueAdapterClickCallbacks {

        void onCardClick(String p);
        void onEditClick();
    }

}

