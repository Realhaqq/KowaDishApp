package com.haqq.namu.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haqq.namu.Activities.map.GeocodingLocation;
import com.haqq.namu.Activities.map.MainActivity;
import com.haqq.namu.Config;
import com.haqq.namu.Models.BillItem;
import com.haqq.namu.MySingleton;
import com.haqq.namu.Others.BillAdapter;
import com.haqq.namu.R;
import com.haqq.namu.tracker.activities.ActivityHome;
import com.haqq.namu.tracker.activities.ActivityTracking;
import com.haqq.namu.users.SessionHandler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagar on 29/6/17.
 */

public class ProcessOrdersFragment extends Fragment {

    private BillAdapter adapter;
    private RecyclerView recyclerView;
    private List<BillItem> billItemList;
    private FloatingActionButton fab;
    SessionHandler session;
    private TextView ontime, delivery_charge, sub_total, amount, service_tax, paymnet_method, c_amount;

    private String orderid, rider;


    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.process_layout, container, false);
        session = new SessionHandler(getContext());
        fab = view.findViewById(R.id.fab);

        ontime = view.findViewById(R.id.ontime);
        service_tax = view.findViewById(R.id.service_tax);
        delivery_charge = view.findViewById(R.id.delivery_charge);
        sub_total = view.findViewById(R.id.sub_total);
        amount = view.findViewById(R.id.amount);
        paymnet_method = view.findViewById(R.id.payment_amount);
        c_amount = view.findViewById(R.id.c_amount);

        GetOrderINfo();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderid == "") {
                    Toast.makeText(getContext(), "No processing order yet!", Toast.LENGTH_LONG).show();
                }else if(rider == ""){
                    Toast.makeText(getContext(), "No Rider assign to!", Toast.LENGTH_LONG).show();

                }else{
                    Intent intent = new Intent(getContext(), ActivityTracking.class);
                    intent.putExtra("orderid", orderid);
                    intent.putExtra("rider", rider);
                    intent.putExtra("amount", amount.getText().toString());
                    startActivity(intent);
                }

            }
        });
        return view;
    }

    private void GetOrderINfo() {
        String url_ = Config.url+"get_my_orders.php?userid="+ session.getUserDetails().getId();
        JSONObject request = new JSONObject();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_, request, new Response.Listener<JSONObject>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt(KEY_STATUS) == 0) {

                                session.OrderInfo(response.getString("orderid"), response.getString("rider"));
                                service_tax.setText("₦" + response.getString("service_fee"));
                                delivery_charge.setText("₦" + response.getString("delivery_charge"));
                                ontime.setText(response.getString("ontime"));
                                sub_total.setText("₦" + response.getString("subtotal"));
                                amount.setText("₦" + response.getString("amount"));



                            } else {
                                Toast.makeText(getContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MySingleton.getInstance(getContext()).addToRequestQueue(jsArrayRequest);
    }

}
