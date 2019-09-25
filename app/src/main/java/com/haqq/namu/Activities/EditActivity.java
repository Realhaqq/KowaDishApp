package com.haqq.namu.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.haqq.namu.R;

/**
 * Created by sagar on 4/7/17.
 */

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private Spinner spinner_states_activity;
    Spinner spinner_cities_activity;
    private SpinnerAdapter adapter;
    SpinnerAdapter unit_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar.setTitle(R.string.update_add);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        //spinner containing the names of the states
        //can be filled with JSON API

        spinner_states_activity = (Spinner)findViewById(R.id.state);
        spinner_states_activity.setOnItemSelectedListener(this);
        adapter = ArrayAdapter.createFromResource(
                this, R.array.state_array, android.R.layout.simple_spinner_item);
        spinner_states_activity.setAdapter(adapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
