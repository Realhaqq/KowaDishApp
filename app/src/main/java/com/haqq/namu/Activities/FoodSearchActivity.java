package com.haqq.namu.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.haqq.namu.Adapters.FoodAdapter;
import com.haqq.namu.Adapters.FoodSearchAdapter;
import com.haqq.namu.Config;
import com.haqq.namu.Models.Foods;
import com.haqq.namu.MySingleton;
import com.haqq.namu.R;
import com.haqq.namu.RecyclerTouchListener;
import com.haqq.namu.RequestHandler;
import com.haqq.namu.ViewDialog;
import com.haqq.namu.users.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.haqq.namu.R.id.spinner;

public class FoodSearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {
    private Toolbar mTopToolbar;
    List<Foods> GetFoodAdaper1;
    RecyclerView recyclerView;
    Foods getFoodAdapter2;
    RecyclerView.Adapter recyclerViewadapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    private FloatingActionButton fab;
    SessionHandler session;
    ViewDialog viewDialog;
    private Spinner sp;
    ArrayList<String> restaurantList;

    private TextView count_cart_item;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
//    private String spinner_url = Config.url + "restaurant.php";

    private ImageView toolbar_logo;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setAnimation();
        setContentView(R.layout.food_search);
        mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setTitle("Search Result");
        mTopToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar_logo = mTopToolbar.findViewById(R.id.toolbar_logo);
        mTopToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mTopToolbar);

        session = new SessionHandler(getApplicationContext());
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        viewDialog = new ViewDialog(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        GetFoodAdaper1 = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        GetFoods();
        GetCartItemCount();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Intent intent = new Intent(FoodListActivity.this, FoodDetailActivity.class);
//                intent.putExtra("title", GetFoodAdaper1.get(position).getTitle());
//                intent.putExtra("description", GetFoodAdaper1.get(position).getDescription());
//                intent.putExtra("image", GetFoodAdaper1.get(position).getImage());
//                intent.putExtra("price", GetFoodAdaper1.get(position).getPrice());
//                intent.putExtra("id", GetFoodAdaper1.get(position).getId());
//                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        fab = findViewById(R.id.fab_add);
        count_cart_item = findViewById(R.id.count_item_in_cart);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodSearchActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });




    }


    @Override
    public void onRefresh() {
        GetFoods();
    }


    public void GetFoods() {
        GetFoodAdaper1.clear();
        mSwipeRefreshLayout.setRefreshing(true);
        Intent i = getIntent();

        String url_ = Config.url + "foods_search.php?filter=" + i.getStringExtra("filter");
        jsonArrayRequest = new JsonArrayRequest(url_, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                GetCoursesWebCall(response);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mSwipeRefreshLayout.setRefreshing(false);

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void GetCoursesWebCall(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            getFoodAdapter2 = new Foods();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                getFoodAdapter2.setDescription(json.getString("description"));
                getFoodAdapter2.setId(json.getString("id"));
                getFoodAdapter2.setImage(json.getString("image"));
                getFoodAdapter2.setTitle(json.getString("title"));
                getFoodAdapter2.setPrice(json.getString("price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GetFoodAdaper1.add(getFoodAdapter2);
        }
        recyclerViewadapter = new FoodSearchAdapter(GetFoodAdaper1, this, new FoodSearchAdapter.DetailsAdapterListener() {
            @Override
            public void classOnClick(View v, int position) {
//                String description = GetFoodAdaper1.get(position).getDescription();
                String id = GetFoodAdaper1.get(position).getId();
                AddToCart(id);

            }
        });
        recyclerView.setAdapter(recyclerViewadapter);
        recyclerViewadapter.notifyDataSetChanged();

    }

//
//
//    public void GetFoodsByRestaurant(String restaurant){
//        GetFoodAdaper1.clear();
//        String url_ = Config.url+"get_foods.php?restaurant=" + restaurant;
//        url_ = url_.replaceAll(" ", "%20");
//        jsonArrayRequest = new JsonArrayRequest(url_, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                GetRestaurantWebCall(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(jsonArrayRequest);
//    }
//    private void GetRestaurantWebCall(JSONArray array) {
//        for (int i = 0; i < array.length(); i++){
//            getFoodAdapter2 = new Foods();
//            JSONObject json = null;
//            try {
//                json = array.getJSONObject(i);
//                getFoodAdapter2.setDescription(json.getString("description"));
//                getFoodAdapter2.setId(json.getString("id"));
//                getFoodAdapter2.setImage(json.getString("image"));
//                getFoodAdapter2.setTitle(json.getString("title"));
//                getFoodAdapter2.setPrice(json.getString("price"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            GetFoodAdaper1.add(getFoodAdapter2);
//        }
//        recyclerViewadapter = new FoodAdapter(GetFoodAdaper1, this, new FoodAdapter.DetailsAdapterListener() {
//            @Override
//            public void classOnClick(View v, int position) {
//                String description = GetFoodAdaper1.get(position).getDescription();
//                String id = GetFoodAdaper1.get(position).getId();
//                AddToCart(id);
//
//            }
//        });
//        recyclerView.setAdapter(recyclerViewadapter);
//        recyclerViewadapter.notifyDataSetChanged();
//
//    }


    public void AddToCart(final String id){
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
                count_cart_item.setText("0");
                GetCartItemCount();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();

                data.put("foodid", id);
                data.put("userid", session.getUserDetails().getId());
                String result = rh.sendPostRequest(Config.url + "add_to_cart.php",data);

                return result;
            }
        }

        addtocart ui = new addtocart();
        ui.execute();
    }

    private void GetCartItemCount() {
        String url_ = Config.url + "get_cart_count.php?userid="+ session.getUserDetails().getId();
        JSONObject request = new JSONObject();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_, request, new Response.Listener<JSONObject>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt(KEY_STATUS) == 0) {

                                count_cart_item.setText(response.getString("count"));

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

//
//    private void getToolbarSpinner(){
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_menu,menu);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        searchView.setOnQueryTextListener(this);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

//            case R.id.action_filter:startActivity(new Intent(FoodListActivity.this,RefineActivity.class));
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

//
//
//
//    private void loadSpinnerData(String spinner_url) {
//        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
//        StringRequest stringRequest=new StringRequest(Request.Method.GET, Config.url + "restaurant.php", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    for(int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        Log.d("json", jsonObject.getString("restaurant_name"));
//                        restaurantList.add(jsonObject.getString("restaurant_name"));
//                    }
//
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FoodSearchActivity.this, R.layout.spinner_layout, R.id.food_name, restaurantList){
//                        @Override
//                        public boolean isEnabled(int position) {
//
//                            if (position == 0) {
//                                return false;
//                            }
//                            return true;
//
//                        }
//                    };
//                    sp.setAdapter(adapter);
////
//
//
////                    sp.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
//
////                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
////                            R.layout.row, R.id.restaurant, restaurantList){
////                        @Override
////                        public boolean isEnabled(int position) {
////
////                            if (position == 0) {
////                                return false;
////                            }
////                            return true;
////
////                        }
////                    };
////                    sp.setAdapter(adapter);
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//
//                Toast.makeText(getApplicationContext(), "Response" + error.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        });
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(policy);
//        requestQueue.add(stringRequest);
//    }


    public void setAnimation(){
        if(Build.VERSION.SDK_INT>20) {
            Explode explode = new Explode();
            explode.setDuration(1000);
            explode.setDuration(1000);
            explode.setInterpolator(new DecelerateInterpolator());
            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
        }
    }
}
