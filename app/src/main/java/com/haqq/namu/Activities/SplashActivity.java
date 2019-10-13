package com.haqq.namu.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haqq.namu.Config;
import com.haqq.namu.MySingleton;
import com.haqq.namu.R;
import com.haqq.namu.users.SessionSetting;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sagar on 2/7/17.
 */

public class SplashActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private String setting_url = Config.url+"settings.php";

    SessionSetting session;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session = new SessionSetting(getApplicationContext());
        int SPLASH_TIME_OUT = 1000;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
//                Intent i = new Intent(SplashActivity.this, SignInActivity.class);
//                startActivity(i);
//                // close this activity
//                finish();

                GetSettings();

            }
        }, SPLASH_TIME_OUT);
    }


    private void loadDashboard(){
        Intent i = new Intent(SplashActivity.this, SignInActivity.class);
        if(Build.VERSION.SDK_INT>20){
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
            startActivity(i,options.toBundle());
        }else {
            startActivity(i);
        }
        finish();
    }
    private void GetSettings() {
        JSONObject request = new JSONObject();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, setting_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt(KEY_STATUS) == 0) {
                                session.AppSetting(response.getString("delivery_charge"), response.getString("service_tax"));
                                loadDashboard();
                            } else {
//                                Toast.makeText(getApplicationContext(),
//                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//
//                        Toast.makeText(getApplicationContext(),
//                                error.getMessage(), Toast.LENGTH_SHORT).show();

                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.coordinatorLayout), "Network Connection Error", Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        GetSettings();
                                    }
                                });

                        snackbar.show();

                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

}
