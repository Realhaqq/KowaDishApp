package com.haqq.namu.tracker.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.haqq.namu.Activities.MainActivity;
import com.haqq.namu.Config;
import com.haqq.namu.MySingleton;
import com.haqq.namu.R;
import com.haqq.namu.tracker.AdapterCartItems;
//import com.haqq.namu.tracker.Application;
import com.haqq.namu.tracker.Constants;
import com.haqq.namu.tracker.Model;
import com.haqq.namu.tracker.Utils;
import com.haqq.namu.users.SessionHandler;
import com.teliver.sdk.core.TLog;
import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.models.UserBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityTracking extends AppCompatActivity {

    private String username;

    private Application application;
    private SessionHandler session;
    private TextView ridername, status, orderid;
    private String phone;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private FloatingActionButton fab;
    private String riderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        session = new SessionHandler(getApplicationContext());
        username = session.getUserDetails().getId();

        ridername = findViewById(R.id.ridername);
        status = findViewById(R.id.status);
        orderid = findViewById(R.id.orderId);

//        Intent intent = getIntent();
//        riderid = intent.getStringExtra("rider");
        orderid.setText(session.getOrderInfo().getOrderid());

        GetRiderINfo();
        fab = findViewById(R.id.fab);

//        fab.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:" + phone));
//
//                if (ActivityCompat.checkSelfPermission(ActivityTracking.this,
//                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                startActivity(callIntent);
//            }
//        });


        TLog.setVisible(true);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        application = (Application) getApplication();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        findViewById(R.id.btnTracker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                Intent intent = new Intent(ActivityTracking.this, ActivityHome.class);
                intent.putExtra("orderid", session.getOrderInfo().getOrderid());
                startActivity(intent);
                startActivity(new Intent(ActivityTracking.this, ActivityHome.class));
            }
        });

        if (Utils.checkPermission(this)) {
            //checkGps();
        }
        Teliver.identifyUser(new UserBuilder(username).
                setUserType(UserBuilder.USER_TYPE.CONSUMER).registerPush().build());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void checkGps() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                 if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        status.startResolutionForResult(ActivityTracking.this, Constants.SHOW_GPS_DIALOG);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SHOW_GPS_DIALOG && resultCode == RESULT_OK)
            Toast.makeText(ActivityTracking.this, "Gps is turned on", Toast.LENGTH_SHORT).show();
        else if (requestCode == Constants.SHOW_GPS_DIALOG && resultCode == RESULT_CANCELED)
            finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.COARSE_LOCATION_PERMISSION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    finish();
                else checkGps();
        }
    }

    private void GetRiderINfo() {

        String url_ = Config.url+"get_rider_info.php?rider="+ session.getOrderInfo().getRider();
        JSONObject request = new JSONObject();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url_, request, new Response.Listener<JSONObject>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt(KEY_STATUS) == 0) {

                                ridername.setText(response.getString("fullname"));
                                phone = response.getString("phone");
//                                status.setText(response.getString("status"));


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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }
}
