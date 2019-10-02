package com.haqq.namu.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.haqq.namu.Adapters.CartAdapter;
import com.haqq.namu.Adapters.ConfirmAdapter;
import com.haqq.namu.Config;
import com.haqq.namu.Models.BillItem;
import com.haqq.namu.Models.Foods;
import com.haqq.namu.Others.BillAdapter;
import com.haqq.namu.Others.PrefManager;
import com.haqq.namu.R;
import com.haqq.namu.RecyclerTouchListener;
import com.haqq.namu.RequestHandler;
import com.haqq.namu.ViewDialog;
import com.haqq.namu.users.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sagar on 29/6/17.
 */

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    LinearLayout button_layout;
    private RecyclerView.LayoutManager layoutManager;
    ConfirmAdapter adapter;
    RecyclerView recyclerView;
    private int ctotal;
    Toolbar toolbar;
    List<Foods> GetFoodAdaper1;
    Foods getFoodAdapter2;
    RecyclerView.Adapter recyclerViewadapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    SessionHandler session;
    ViewDialog viewDialog;

    private final int PLACE_PICKER_REQUEST = 1;
    private PrefManager pref;
    private ImageView edit;
    private TextView sub_total, service_tax, payment_amount, delivery_charge, mylocation;
//    SessionHandler session;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order_layout);

        session = new SessionHandler(getApplicationContext());

        viewDialog = new ViewDialog(this);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        edit = findViewById(R.id.edit);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar.setTitle("Confirm Order");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        Intent intent = getIntent();

        sub_total = findViewById(R.id.sub_total);
        service_tax = findViewById(R.id.service_tax);
        payment_amount = findViewById(R.id.payment_amount);
        delivery_charge = findViewById(R.id.delivery_charge);
        mylocation = findViewById(R.id.mylocation);

        sub_total.setText("₦" + intent.getStringExtra("subtotal"));
        payment_amount.setText("₦" + intent.getStringExtra("total"));
        service_tax.setText("₦" + intent.getStringExtra("service_tax"));
        delivery_charge.setText("₦" + intent.getStringExtra("delivery_charge"));
        mylocation.setText(session.getUserDetails().getMylocation());



        edit.setOnClickListener(this);

        GetFoodAdaper1 = new ArrayList<>();
        recyclerView =  findViewById(R.id.bill_item_grid);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        GetFoods();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener(){
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        button_layout=findViewById(R.id.button_layout);
        button_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

           case R.id.button_layout:

               Intent intent = getIntent();
               String subtotal = intent.getStringExtra("subtotal");
               String paymentamount = intent.getStringExtra("total");
               String servicetax = intent.getStringExtra("service_tax");
               String deliverycharge = intent.getStringExtra("delivery_charge");
               String note = intent.getStringExtra("note");
               String location = session.getUserDetails().getMylocation();

//               startActivity(new Intent(ConfirmActivity.this,PaymentActivity.class));
               //intent to go to payment activity to perform payment

               SaveOrder(subtotal, paymentamount, servicetax, deliverycharge, location, note);
               break;
            case R.id.edit:
//                startActivity(new Intent(ConfirmActivity.this,EditActivity.class));
                //intent to go to payment activity to edit delivery address

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(ConfirmActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void GetFoods(){
        GetFoodAdaper1.clear();
//        mSwipeRefreshLayout.
        String url_ = Config.url+"user_cart.php?userid="+session.getUserDetails().getId();
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
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void GetCoursesWebCall(JSONArray array) {
        for (int i = 0; i < array.length(); i++){
            getFoodAdapter2 = new Foods();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                getFoodAdapter2.setDescription(json.getString("description"));
                getFoodAdapter2.setId(json.getString("id"));
                getFoodAdapter2.setImage(json.getString("image"));
                getFoodAdapter2.setTitle(json.getString("title"));
                getFoodAdapter2.setPrice(json.getString("price"));
                getFoodAdapter2.setQty(json.getString("qty"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GetFoodAdaper1.add(getFoodAdapter2);
        }


        adapter=new ConfirmAdapter(this, GetFoodAdaper1, new ConfirmAdapter.VenueAdapterClickCallbacks() {
            @Override
            public void onCardClick(String title) {
                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();

            }

            });
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onRefresh() {
        GetFoods();
    }


    public void SaveOrder(final String subtotal, final String paymentamount, final String servicetax, final String deliverycharge, final String location, final String note){
        class addtocart extends AsyncTask<Bitmap,Void,String> {

            RequestHandler rh = new RequestHandler();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                viewDialog.showDialog();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                viewDialog.hideDialog();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent i = getIntent();
                Intent intent = new Intent(ConfirmActivity.this, PaymentActivity.class);
                intent.putExtra("amount", i.getStringExtra("total"));
                startActivity(intent);
                finish();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();

                data.put("subtotal", subtotal);
                data.put("amount", paymentamount);
                data.put("tax", servicetax);
                data.put("delivery_charge", deliverycharge);
                data.put("location", location);
                data.put("note", note);
                data.put("service_fee", servicetax);
                data.put("payment_status", "UNPDAID");
                data.put("order_status", "PENDING");
                data.put("userid", session.getUserDetails().getId());
                String result = rh.sendPostRequest(Config.url + "add_new_order.php",data);

                return result;
            }
        }

        addtocart ui = new addtocart();
        ui.execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(ConfirmActivity.this, data);
//                String toastMsg = String.format("Place: %s", place.getName());
                String add[] = place.getAddress().toString().split(",");
                if (add.length >= 4) {
                    mylocation.setText(add[add.length - 3]);
                    String str = add[add.length - 4];
                    if (str.length() >= 15) {
                        mylocation.setText(place.getName());
                        session.SetDefaultLocation(place.getAddress().toString());
                    }else
                        mylocation.setText(place.getName());
                    session.SetDefaultLocation(place.getAddress().toString());

                } else {
                    mylocation.setText(place.getName());
//                    street.setText(place.getName());
                }
            }
        }
    }
}
