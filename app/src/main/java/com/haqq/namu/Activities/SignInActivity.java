package com.haqq.namu.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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


public class SignInActivity extends AppCompatActivity {

    private TextView sign_in;
    private TextView sign_up;
    private TextView sign_in_text;
    private TextView sign_up_text;
    private TextView forgot;
    private EditText name;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private Button signIn;
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private ProgressDialog pDialog;
    ViewDialog viewDialog;
    SessionHandler session;
    EditText etphone, etpassword;
    String phone, password;
    private String login_url = Config.url+"user_login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setAnimation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        session = new SessionHandler(getApplicationContext());
        viewDialog = new ViewDialog(this);
        sign_up= findViewById(R.id.sign_up);
        sign_up_text= findViewById(R.id.sign_up_text);
        layout1= findViewById(R.id.layout1);
        signIn = findViewById(R.id.sign_in_button);



        if (session.isLoggedIn()){
            loadDashboard();
        }

        etphone = findViewById(R.id.phone);
        etpassword = findViewById(R.id.password);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = etphone.getText().toString().trim();
                password = etpassword.getText().toString().trim();

                if (validateInputs()){
                    MakeLogin(phone, password);
                }
            }
        });


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }


    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void MakeLogin(final String phone, String password) {
        viewDialog.showDialog();
//        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_PHONE, phone);
            request.put(KEY_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        viewDialog.hideDialog();
//                        pDialog.dismiss();

                        try {
                            //Check if user got logged in successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                               GetUserINfo(phone);
                            } else {

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


    private void displayLoader() {
        pDialog = new ProgressDialog(SignInActivity.this);
        pDialog.setMessage("Sign In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    /**
     * Validates inputs and shows error if any
     *
     * @return
     */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(phone)) {
            etphone.setError("Phone Number cannot be empty");
            etphone.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            etpassword.setError("Password cannot be empty");
            etpassword.requestFocus();
            return false;
        }
        return true;
    }



    private void GetUserINfo(final String phone) {
        String url_ = Config.url+"get_user_info.php?phone="+ phone;
        JSONObject request = new JSONObject();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_, request, new Response.Listener<JSONObject>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt(KEY_STATUS) == 0) {

//                                cart_count.setText(response.getString("count"));
                                session.loginUser(phone, response.getString(KEY_EMAIL), response.getString(KEY_FULLNAME), response.getString("id"));
                                loadDashboard();

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
