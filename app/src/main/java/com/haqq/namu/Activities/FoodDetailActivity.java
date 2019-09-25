package com.haqq.namu.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.haqq.namu.Config;
import com.haqq.namu.MySingleton;
import com.haqq.namu.R;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by sagar on 29/6/17.
 */

public class FoodDetailActivity extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout button_layout;
    private ImageView back;
    private ImageView remove;
    private ImageView add, image;
    private TextView itemname, description, price;
    private TextView no_of_items, count_item_in_cart;

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_details_layout);

        GetCartItemCount();

        Intent intent = getIntent();
        itemname = findViewById(R.id.item_name);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        image = findViewById(R.id.image);
        count_item_in_cart = findViewById(R.id.count_item_in_cart);

        Glide.with(getApplicationContext())
                .load(intent.getStringArrayExtra("image"))
//                .centerCrop(150, 150)
//                .resize(150, 150)
//                .fit()
                .placeholder(R.drawable.namudown)
//                .centerInside()
                .error(R.drawable.namudown)
                .into(image);

        //adding the image of nonveg sign at the end ot the name of pizza

        ImageSpan is = new ImageSpan(getApplicationContext(), R.drawable.non_veg);
        SpannableString texts = new SpannableString(itemname.getText().toString().concat("   "));
        texts.setSpan(is,texts.length() - 1,texts.length(),0);
        itemname.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));
        price.setText("₦" + intent.getStringExtra("price"));

        remove = findViewById(R.id.remove);
        add= findViewById(R.id.add);
        remove.setOnClickListener(this);
        add.setOnClickListener(this);

        no_of_items= findViewById(R.id.no_of_items);


        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        button_layout=(LinearLayout)findViewById(R.id.button_layout);
        button_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_layout:startActivity(new Intent(FoodDetailActivity.this,CartActivity.class));
                break;

            case R.id.back:finish();
                break;

            //cases to handle add and remove button functions
            case R.id.remove:
                int cal_dist=0,d;
                String a=no_of_items.getText().toString();
                d=Integer.parseInt(a);
                if(d>=1.0)
                    cal_dist=d;
                no_of_items.setText(cal_dist+"");
                break;

            case R.id.add:
                a=no_of_items.getText().toString();
                d=Integer.parseInt(a);
                cal_dist=++d;
                no_of_items.setText(cal_dist+"");
        }
    }


    private void GetCartItemCount() {
        String url_ = Config.url+"get_cart_count.php?userid=1";
        JSONObject request = new JSONObject();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_, request, new Response.Listener<JSONObject>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt(KEY_STATUS) == 0) {

                                count_item_in_cart.setText(response.getString("count"));

                            } else {
                                Toast.makeText(getApplicationContext(),
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
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
}