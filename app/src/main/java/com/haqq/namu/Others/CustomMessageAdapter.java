package com.haqq.namu.Others;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haqq.namu.Models.Message;
import com.haqq.namu.R;

import java.util.List;


/**
 * Created by sagar on 3/7/17.
 */
public class CustomMessageAdapter extends RecyclerView.Adapter<CustomMessageAdapter.MyHolder> {

        private RecyclerView re;
        private final List<Message> dataSet ;
        private Context context=null;
        private final VenueAdapterClickCallbacks venueAdapterClickCallbacks;

public class MyHolder extends RecyclerView.ViewHolder
{
    final TextView message;


    public MyHolder(View itemView)
    {
        super(itemView);
        this.message = (TextView) itemView.findViewById(R.id.message_body);

    }
}

    public CustomMessageAdapter(Context c, List<Message> data, CustomMessageAdapter.VenueAdapterClickCallbacks venueAdapterClickCallback)
    {

        this.dataSet = data;
        this.venueAdapterClickCallbacks=venueAdapterClickCallback;
        context=c;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_receive, parent, false);
        MyHolder myNewsHolder=new MyHolder(view);
        re = (RecyclerView) parent.findViewById(R.id.messages_view);
        return myNewsHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        TextView message = holder.message;
        message.setText(dataSet.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface VenueAdapterClickCallbacks {
        void onCardClick( String p);

    }
}


