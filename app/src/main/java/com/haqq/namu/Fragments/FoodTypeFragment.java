package com.haqq.namu.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haqq.namu.Activities.FoodDetailActivity;
import com.haqq.namu.Models.ItemVariety;
import com.haqq.namu.Others.FoodVarietyAdapter;
import com.haqq.namu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagar on 29/6/17.
 */

public class FoodTypeFragment extends Fragment {

    private FoodVarietyAdapter adapter;
    private RecyclerView recyclerView;
    private List<ItemVariety> itemVarietyList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_foodtype, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.food_type_grid);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getCards();
        return view;
    }

    private void getCards(){

        itemVarietyList=new ArrayList<>();

        //filling the cards with data (Data from API will be filled in cards here)
        itemVarietyList.add(new ItemVariety(R.drawable.pizza1,"14.99 $","Crispy Chicken garlic periperi pizza"));
        itemVarietyList.add(new ItemVariety(R.drawable.pizza2,"14.99 $","Paneer crispy hot veg periperi pizza"));


        adapter=new FoodVarietyAdapter(getActivity(), itemVarietyList, new FoodVarietyAdapter.VenueAdapterClickCallbacks() {
            @Override
            public void onCardClick(String p) {

                    startActivity(new Intent(getActivity(), FoodDetailActivity.class));

            }
        });
        recyclerView.setAdapter(adapter);
    }

}
