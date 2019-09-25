package com.haqq.namu.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.haqq.namu.Models.Message;
import com.haqq.namu.Others.CustomMessageAdapter;
import com.haqq.namu.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sagar on 2/7/17.
 */

public class ChatActivity extends AppCompatActivity {


    private LinearLayoutManager layoutManager;
    private CustomMessageAdapter adapter;
    private RecyclerView recyclerView;
    private List<Message> messageList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact_chat);

        recyclerView = (RecyclerView) findViewById(R.id.messages_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //prepare chat cards
        getCards();
    }


    private void getCards(){


        messageList=new ArrayList<>();
        messageList.add(new Message("Hey !How may I help you?","Server"));

        //here cards will be filled with contents of chat

        adapter = new CustomMessageAdapter(getApplicationContext(), messageList, new CustomMessageAdapter.VenueAdapterClickCallbacks() {
            @Override
            public void onCardClick(String p) {

                // to perform on chat card's click
            }
        });

        recyclerView.setAdapter(adapter);

    }
}