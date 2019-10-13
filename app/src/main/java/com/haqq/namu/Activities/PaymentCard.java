package com.haqq.namu.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.haqq.namu.Config;
import com.haqq.namu.Fragments.OrdersFragment;
import com.haqq.namu.R;
import com.haqq.namu.RequestHandler;
import com.haqq.namu.ViewDialog;
import com.haqq.namu.users.SessionHandler;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class PaymentCard extends AppCompatActivity {

    ViewDialog viewDialog;

    private SessionHandler session;
    private TextInputEditText et_card_number;
    private TextInputEditText et_expire_month;
    private TextInputEditText et_expire_year;
    private TextInputEditText et_cvv;
    String cardNumber, cardCVV;

    private ProgressDialog progressDialog;
    int transactionAmount;
    private Context context;
    private ProgressBar progress_bar;
    private Button pay;
    private static final String KEY_EMPTY = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.payment_card);
        viewDialog = new ViewDialog(this);
        PaystackSdk.initialize(getApplicationContext());
        progress_bar = findViewById(R.id.progress_bar);
        session = new SessionHandler(getApplicationContext());
        et_card_number =  findViewById(R.id.et_card_number);
        et_expire_month =  findViewById(R.id.et_expire_month);
        et_expire_year =  findViewById(R.id.et_expire_year);
        et_cvv =  findViewById(R.id.et_cvv);
        progressDialog  =   new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing Transaction...");
        pay = findViewById(R.id.btn_pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cardNumber  =   et_card_number.getText().toString();
              cardCVV =   et_cvv.getText().toString();
                if (validateInputs()) {
                    payStack();
                }

            }
        });



    }
    private void payStack() {
        viewDialog.showDialog();
        DecimalFormat format = new DecimalFormat("#.#");
        format.setDecimalSeparatorAlwaysShown(false);
            Intent i = getIntent();
            int expiryMonth =   Integer.parseInt(et_expire_month.getText().toString());
            int expiryYear  =   Integer.parseInt(et_expire_year.getText().toString());
//            double topay = Double.parseDouble(i.getStringExtra("amount"));

//        System.out.println(); //prints 2
//        int strDouble = Integer.parseInt(String.format("%.2f", i.getStringExtra("amount")));

//        String tamount = String.valueOf(Integer.parseInt(i.getStringExtra("amount")));
//        transactionAmount = Integer.parseInt(i.getStringExtra("amount"));

        double doubleNumber = Double.parseDouble(i.getStringExtra("amount"));
        int intPart = (int) doubleNumber;

        transactionAmount = intPart;

        String emailText = session.getUserDetails().getEmail();
            Card card = new Card(cardNumber, expiryMonth, expiryYear, cardCVV);
            if (card.isValid()) {
                Charge charge = new Charge();
                charge.setCard(card);
                charge.setAmount(transactionAmount * 100);
                charge.setEmail(emailText);
                charge.setBearer(charge.getBearer());
                try {
                    charge.putCustomField("Customer Account Number", "Samiul Haqq");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                PaystackSdk.chargeCard(PaymentCard.this, charge, new Paystack.TransactionCallback() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        viewDialog.hideDialog();
                        Toast.makeText(getApplicationContext(),"Payment Made Successfully",Toast.LENGTH_LONG).show();

//                        UpdateData();
                    }



                    @Override
                    public void beforeValidate(Transaction transaction) {

                    }

                    @Override
                    public void onError(Throwable error, Transaction transaction) {
                        Toast.makeText(getApplicationContext(),"Error: "  + error.getMessage(),Toast.LENGTH_LONG).show();
                        viewDialog.hideDialog();
                    }
                });
            }


    }


//    private void showDialogPaymentSuccess(int transactionAmount) {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//        dialog.setContentView(R.layout.dialog_payment_success);
//        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        TextView amount, email, date, time;
//        Intent intent = getIntent();
//        amount = dialog.findViewById(R.id.amount);
//        email = dialog.findViewById(R.id.email);
//        date = dialog.findViewById(R.id.date);
//        time = dialog.findViewById(R.id.time);
//        String todate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//        amount.setText(intent.getStringExtra("amount"));
//        date.setText(todate);
//        Date currentTime = Calendar.getInstance().getTime();
////        time.setText((CharSequence) currentTime);
//        email.setText(session.getUserDetails().getEmail());
//
//        ((View) dialog.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//        dialog.getWindow().setAttributes(lp);
//    }
//


    public void UpdateData(){
        class updatedata extends AsyncTask<Bitmap,Void,String> {
            RequestHandler rh = new RequestHandler();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

//                session.EditPaymentStatus("PAID");
                Intent intent = new Intent(PaymentCard.this, OrdersFragment.class);
                startActivity(intent);
                finish();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("userid", session.getUserDetails().getId());
                String result = rh.sendPostRequest(Config.url + "update_payment.php",data);
                return result;
            }
        }

        updatedata ui = new updatedata();
        ui.execute();
    }


    /**
     * Validates inputs and shows error if any
     *
     * @return
     */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(cardNumber)) {
            et_card_number.setError("Card Number cannot be empty");
            et_card_number.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(cardCVV)) {
            et_cvv.setError("CVV cannot be empty");
            et_cvv.requestFocus();
            return false;
        }


        if (KEY_EMPTY.equals(et_expire_month)) {
            et_expire_month.setError("Expire Month cannot be empty");
            et_expire_month.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(et_card_number)) {
            et_expire_year.setError("Expire Year cannot be empty");
            et_expire_year.requestFocus();
            return false;
        }
        return true;
    }


    public void setAnimation() {
        if (Build.VERSION.SDK_INT > 20) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.LEFT);
            slide.setDuration(400);
            slide.setInterpolator(new DecelerateInterpolator());
            getWindow().setExitTransition(slide);
            getWindow().setEnterTransition(slide);
        }

    }

}
