package com.haqq.namu.Activities;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.haqq.namu.Fragments.FoodFragment;
import com.haqq.namu.Models.ItemData;
import com.haqq.namu.Others.PrefManager;
import com.haqq.namu.Others.SpinnerAdapter;
import com.haqq.namu.R;

import java.util.ArrayList;

import static com.haqq.namu.R.id.spinner;

/**
 * Created by sagar on 29/6/17.
 */

public class FoodActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_layout);
        prefManager=new PrefManager(getApplicationContext());
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        getToolbarSpinner();

        //opening the fragment

        Fragment fragment = new FoodFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commitAllowingStateLoss();

    }

    private void getToolbarSpinner(){

        ArrayList<ItemData> list=new ArrayList<>();

        //filling the spinner in toolbar

        list.add(new ItemData("Pizza",R.drawable.pizza_wht));
        list.add(new ItemData("Meal Combo",R.drawable.main_course_wht));
        list.add(new ItemData("Burger",R.drawable.burger_wht));
        list.add(new ItemData("Soup",R.drawable.soup_wht));
        list.add(new ItemData("Chinese",R.drawable.chinese_wht));

        Spinner sp=(Spinner)toolbar.findViewById(spinner);
        SpinnerAdapter adapter=new SpinnerAdapter(this,
                R.layout.spinner_layout,R.id.food_name,list);
        sp.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        sp.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_menu,menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_filter:startActivity(new Intent(FoodActivity.this,RefineActivity.class));
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
}
