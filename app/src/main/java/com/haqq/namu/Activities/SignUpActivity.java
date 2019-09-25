package com.haqq.namu.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haqq.namu.Config;
import com.haqq.namu.MySingleton;
import com.haqq.namu.R;
import com.haqq.namu.ViewDialog;
import com.haqq.namu.users.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

//    private ProgressDialogsBar progress_bar;
    private View parent_view;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMPTY = "";
    private ProgressDialog pDialog;

    SessionHandler session;

    private String register_url = Config.url +"user_register.php";
    private EditText fullname, email, password, phone;
    private String txtfullanme, txtemail, txtpassword, txtphone;
    private Button btn_reg;
    private TextView txtback;
    ViewDialog viewDialog;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
//        setAnimation();
        setContentView(R.layout.signup_layout);
        viewDialog = new ViewDialog(this);
        session = new SessionHandler(getApplicationContext());
        fullname =  findViewById(R.id.fullname);
        email =  findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        btn_reg = findViewById(R.id.sign_up_button);
//        txtback = findViewById(R.id.txt_login);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtemail = email.getText().toString().trim();
                txtfullanme = fullname.getText().toString();
                txtpassword = password.getText().toString().trim();
                txtphone = phone.getText().toString().trim();

                if (validateInputs()) {
                    registerUser(txtfullanme, txtemail, txtpassword, txtphone);
                }
            }
        });

//        txtback.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                ActivityOptions options = ActivityOptions
//                        .makeSceneTransitionAnimation(RegisterActivity.this);
//                // start the new activity
//                startActivity(intent, options.toBundle());
////                startActivity(intent);
////                finish();
//
//            }
//        });

    }
//    /**
//     * Display Progress bar while registering
//     */
    private void displayLoader() {
        pDialog = new ProgressDialog(SignUpActivity.this);
        pDialog.setMessage("Sign Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Launch Dashboard Activity on Successful Sign Up
     */
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
        finish();

    }


    private void registerUser(final String txtfullanme, final String txtemail, String txtpassword, final String txtphone) {
//        displayLoader();
        viewDialog.showDialog();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("password", txtpassword);
            request.put("fullname", txtfullanme);
            request.put("email", txtemail);
            request.put("phone", txtphone);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, register_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        pDialog.dismiss();
                        viewDialog.hideDialog();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
//                                session.loginUser(txtphone, txtemail,txtfullanme);
                                Toast.makeText(getApplicationContext(), "Account Created Successfully", Toast.LENGTH_LONG).show();
                                loadDashboard();

                            }else if(response.getInt(KEY_STATUS) == 1){

                                phone.setError("Phone Number already used!");
                                phone.requestFocus();

                            }else{

                                Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.dismiss();

                        viewDialog.hideDialog();
                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();



                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * Validates inputs and shows error if any
     *
     * @return
     */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(txtemail)) {
            email.setError("Email cannot be empty");
            email.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(txtfullanme)) {
            fullname.setError("Fullname cannot be empty");
            fullname.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(txtpassword)) {
            password.setError("Password cannot be empty");
            password.requestFocus();
            return false;
        }


        if (KEY_EMPTY.equals(txtphone)) {
            phone.setError("Phone Number cannot be empty");
            phone.requestFocus();
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
