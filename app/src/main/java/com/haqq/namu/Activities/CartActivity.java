package com.haqq.namu.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.haqq.namu.Adapters.CartAdapter;
import com.haqq.namu.Config;
import com.haqq.namu.Models.Foods;
import com.haqq.namu.R;
import com.haqq.namu.RecyclerTouchListener;
import com.haqq.namu.RequestHandler;
import com.haqq.namu.ViewDialog;
import com.haqq.namu.users.SessionHandler;
import com.haqq.namu.users.SessionSetting;

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

public class CartActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    LinearLayout button_layout;
    private RecyclerView.LayoutManager layoutManager;
    CartAdapter adapter;
    RecyclerView recyclerView;
//    List<ItemVariety> cartList;
    TextView subtotal,service_tax,delivery_charge,total;
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
    SessionSetting sessionSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);

        session = new SessionHandler(getApplicationContext());
        sessionSetting = new SessionSetting(getApplicationContext());


        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        // Intialising layout view
        button_layout=(LinearLayout)findViewById(R.id.button_layout);
        button_layout.setOnClickListener(this);
        subtotal=(TextView)findViewById(R.id.subtotal);
        service_tax=(TextView)findViewById(R.id.service_tax);
        delivery_charge=(TextView)findViewById(R.id.delivery_charge);
        total=(TextView)findViewById(R.id.total);

//        country.add("Nigeria");

        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar.setTitle("Cart");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        GetFoodAdaper1 = new ArrayList<>();
        recyclerView =  findViewById(R.id.cart_card_grid);
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

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_layout:


                String dtotal = total.getText().toString();
                String dsubtotal = subtotal.getText().toString();
                String dservice_tax = service_tax.getText().toString();
                String ddelivery_charge = delivery_charge.getText().toString();
                showDialogAddNote(dtotal, dsubtotal, dservice_tax,ddelivery_charge);

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


        adapter=new CartAdapter(this, GetFoodAdaper1, new CartAdapter.VenueAdapterClickCallbacks() {
            @Override
            public void onCardClick(String p, String id) {

                //restricting decimal numbers to two decimal place
                double price=Double.parseDouble(p);
                double tax=0.04*price;
                DecimalFormat numberFormat = new DecimalFormat("#.00");

                subtotal.setText("₦" + numberFormat.format(price));
                service_tax.setText("₦" + numberFormat.format(tax));
                delivery_charge.setText("₦"+500);
                price=price+tax+0.50;
                total.setText("₦" + numberFormat.format(price + 500));
//                int newprice = ctotal + 500;

                UpdateCart(id);

            }

            @Override
            public void onCardMinusClick(String s, String id) {

                UpdateMinus(id);
            }

            @Override
            public void onButtonClick(String s, String id) {
                DeleteCart(id);
            }
        });
        recyclerView.setAdapter(adapter);

    }




    public void UpdateCart(final String id){
        class addtocart extends AsyncTask<Bitmap,Void,String> {

            RequestHandler rh = new RequestHandler();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                viewDialog.showDialog();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                viewDialog.hideDialog();
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();

                data.put("foodid", id);
                data.put("userid", session.getUserDetails().getId());
                String result = rh.sendPostRequest(Config.url + "update_cart_plus.php",data);

                return result;
            }
        }

        addtocart ui = new addtocart();
        ui.execute();
    }


    public void DeleteCart(final String id){
        class addtocart extends AsyncTask<Bitmap,Void,String> {

            RequestHandler rh = new RequestHandler();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                viewDialog.showDialog();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                viewDialog.hideDialog();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                GetFoods();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();

                data.put("foodid", id);
                data.put("userid", session.getUserDetails().getId());
                String result = rh.sendPostRequest(Config.url + "delete_cart.php",data);

                return result;
            }
        }

        addtocart ui = new addtocart();
        ui.execute();
    }



    public void UpdateMinus(final String id){
        class addtocart extends AsyncTask<Bitmap,Void,String> {

            RequestHandler rh = new RequestHandler();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                viewDialog.showDialog();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                viewDialog.hideDialog();
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();

                data.put("foodid", id);
                data.put("userid", session.getUserDetails().getId());
                String result = rh.sendPostRequest(Config.url + "update_cart_minus.php",data);

                return result;
            }
        }

        addtocart ui = new addtocart();
        ui.execute();
    }


    private void showDialogAddNote(final String dtotal, final String dsubtotal, final String dservice_tax, final String ddelivery_charge) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_order_note);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText note;
        note = dialog.findViewById(R.id.note);
        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.add_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ConfirmActivity.class);
                String ndtotal = dtotal.replaceAll("[^\\d.]", "");
                String ndsubtotal = dsubtotal.replaceAll("[^\\d.]", "");
                String ndservice_tax = dservice_tax.replaceAll("[^\\d.]", "");
                String nddelivery_charge = ddelivery_charge.replaceAll("[^\\d.]", "");

//                int num = Integer.parseInt(String.valueOf(ndtotal).split("\\.")[0]);
//                DecimalFormat df = new DecimalFormat("##0");

//                df.format((Math.round(ndtotal * 100.0) / 100.0));

//                String newnd =new DecimalFormat("#").format(ndtotal);
                intent.putExtra("total", ndtotal);
                intent.putExtra("subtotal", ndsubtotal);
                intent.putExtra("service_tax", ndservice_tax);
                intent.putExtra("delivery_charge", nddelivery_charge);
                intent.putExtra("note", note.getText().toString());
                startActivity(intent);
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onRefresh() {
        GetFoods();
    }
}
