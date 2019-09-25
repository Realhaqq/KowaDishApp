package com.haqq.namu.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haqq.namu.R;

/**
 * Created by sagar on 29/6/17.
 */

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout button_layout;

    private Toolbar toolbar;
    private CardView cardpayment;
    private TextView amount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar.setTitle("Payment Methods");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        Intent i = getIntent();

        amount = findViewById(R.id.amount);
        amount.setText(i.getStringExtra("amount"));
        cardpayment = findViewById(R.id.card4);
        cardpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                Intent intent = new Intent(PaymentActivity.this, PaymentCard.class);
                intent.putExtra("amount", i.getStringExtra("amount"));
                startActivity(intent);
            }
        });

        button_layout= findViewById(R.id.button_layout);
        button_layout.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            //intent to go to main activity by clearing all stack

            case R.id.button_layout:Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
        }
    }
}
