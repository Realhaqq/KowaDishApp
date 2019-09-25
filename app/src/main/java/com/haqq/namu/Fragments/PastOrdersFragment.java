package com.haqq.namu.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.GetChars;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.haqq.namu.Adapters.FoodAdapter;
import com.haqq.namu.Config;
import com.haqq.namu.Models.Foods;
import com.haqq.namu.Models.PastOrders;
import com.haqq.namu.Others.PastOrdersAdapter;
import com.haqq.namu.R;
import com.haqq.namu.ViewDialog;
import com.haqq.namu.users.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagar on 29/6/17.
 */

public class PastOrdersFragment extends Fragment {

    private PastOrdersAdapter adapter;
    private RecyclerView recyclerView;
    private List<PastOrders> ordersList;
    PastOrders getOderAdapter2;
    RecyclerView.Adapter recyclerViewadapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    private FloatingActionButton fab;
    SessionHandler session;
    ViewDialog viewDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.past_orders_layout, container, false);

        session = new SessionHandler(getContext());
        ordersList=new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.past_orders_grid);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        GetOrders();
        return view;
    }
//
//    private void getCards(){
//
//        ordersList=new ArrayList<>();
//
//        //filling the cards with data(Data from JSON API will be received here)
////        ordersList.add(new PastOrders("12 May","3 items ordered","33.54 $"));
////        ordersList.add(new PastOrders("08 May","2 items ordered","21.57 $"));
////        ordersList.add(new PastOrders("02 May","2 items ordered","24.89 $"));
////        ordersList.add(new PastOrders("28 April","5 items ordered","65.21 $"));
////        ordersList.add(new PastOrders("21 April","2 items ordered","19.33 $"));
//
//        adapter=new PastOrdersAdapter(getActivity(), ordersList, new PastOrdersAdapter.VenueAdapterClickCallbacks() {
//            @Override
//            public void onCardClick(String p) {
//
//                Toast.makeText(getActivity(),p,Toast.LENGTH_LONG).show();
//                //perform the card click functionality
//            }
//        });
//        recyclerView.setAdapter(adapter);
//    }

    public void GetOrders(){
        ordersList.clear();
        String url_ = Config.url+"orders.php?userid="+ session.getUserDetails().getId();
        jsonArrayRequest = new JsonArrayRequest(url_, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                GetCoursesWebCall(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }
    private void GetCoursesWebCall(JSONArray array) {
        for (int i = 0; i < array.length(); i++){
            getOderAdapter2 = new PastOrders();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                getOderAdapter2.setDate(json.getString("ontime"));
                getOderAdapter2.setNumber_of_items(json.getString("orderid"));
                getOderAdapter2.setPrice(json.getString("amount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ordersList.add(getOderAdapter2);
        }
        recyclerViewadapter = new PastOrdersAdapter(getActivity(), ordersList, new PastOrdersAdapter.VenueAdapterClickCallbacks() {
            @Override
            public void onCardClick(String p) {

            }

//            @Override
//            public void classOnClick(View v, int position) {
////                String description = ordersList.get(position).getDescription();
////                String id = ordersList.get(position).getId();
////                AddToCart(id);
//
//            }
        });
        recyclerView.setAdapter(recyclerViewadapter);
        recyclerViewadapter.notifyDataSetChanged();

    }


}
