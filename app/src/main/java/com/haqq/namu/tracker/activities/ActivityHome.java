package com.haqq.namu.tracker.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.haqq.namu.MySingleton;
import com.haqq.namu.R;
import com.haqq.namu.tracker.Application;
import com.haqq.namu.tracker.Constants;
import com.haqq.namu.users.SessionHandler;
import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.core.TrackingListener;
import com.teliver.sdk.models.MarkerOption;
import com.teliver.sdk.models.TLocation;
import com.teliver.sdk.models.TrackingBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ActivityHome extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {


    private Application application;

    private TextView txtPayment, txtInKitchen, txtOnRoute, txtDeliverHint, txtDelivered;

    private ImageView imgOne, imgTwo, imgThree, imgFour, imgViewOne, imgViewtwo, imgViewThree;

    private GoogleMap googleMap;

    private GoogleApiClient googleApiClient;

    private LinearLayout layoutDelivered;

    private RelativeLayout layoutTracking;

    private AlertDialog dialog;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAANwp-pDk:APA91bHZnirjgX_16vSzP5SzXFg1DrdjHZKh2AzVnws0IvTVP7OGjh8u_NqjcY1TJdChQJB95rG31gtIhplfZ2RhnjLqvA98V-P-C1oroKwNcIXyleWR24yQO_X_nK9TtQV01wlWjc8P";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    private SessionHandler sessionHandler;

//    private String track = "TELIVERTRK_101";
    private String track;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionHandler = new SessionHandler(getApplicationContext());

        Intent i = getIntent();
        track = i.getStringExtra("orderid");

        subscribeToPushService();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("message"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        supportMapFragment.getMapAsync(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.layout_tracking_steps, null);
        view.setPadding(8, 0, 8, 0);

        txtPayment = view.findViewById(R.id.txtpayment);
        txtInKitchen = view.findViewById(R.id.txtInKitchen);
        txtOnRoute = view.findViewById(R.id.initTeliver);
        txtOnRoute.setOnClickListener(this);
        txtDeliverHint = view.findViewById(R.id.txtDeliveryHint);
        txtDelivered = view.findViewById(R.id.txtDelivered);
        imgOne = view.findViewById(R.id.imgOne);
        imgTwo = view.findViewById(R.id.imgTwo);
        imgThree = view.findViewById(R.id.imgThree);
        imgFour = view.findViewById(R.id.imgFour);
        imgViewOne = view.findViewById(R.id.viewOne);
        imgViewtwo = view.findViewById(R.id.viewTwo);
        imgViewThree = view.findViewById(R.id.viewThree);
        layoutDelivered = view.findViewById(R.id.layoutdelivered);
        layoutTracking = view.findViewById(R.id.layoutTracker);

        alert.setView(view);

        application = (Application) getApplicationContext();
        alert.setCancelable(false);
        dialog = alert.create();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                    finish();
                return false;
            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.BOTTOM;
        params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(params);


        dialog.show();

        final Intent intent = getIntent();

        if (intent != null) {
            String msg = getIntent().getStringExtra("msg");
            setStates(msg);
        }
        txtOnRoute.setEnabled(true);
    }

    private void maintainState() {
        if (application.getBooleanInPef(Constants.STEP_ONE)) {
            txtPayment.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
            setCompletedIcon(imgOne, imgViewOne);
        }
        if (application.getBooleanInPef(Constants.STEP_TWO)) {
            setCompletedText(txtPayment, txtInKitchen, imgTwo, imgViewtwo);
            setCompletedText(txtPayment, txtDeliverHint, imgTwo, imgViewtwo);
            setCompletedIcon(imgTwo, imgViewtwo);
            txtOnRoute.setEnabled(true);
        }
        if (application.getBooleanInPef(Constants.STEP_THREE)) {
            setCompletedText(txtInKitchen, txtOnRoute, imgThree, imgViewThree);
            setCompletedText(txtDeliverHint, txtOnRoute, imgThree, imgViewThree);
            setCompletedIcon(imgThree, imgViewThree);
        }
        if (application.getBooleanInPef(Constants.STEP_FOUR)) {
            storeStepCompletion(Constants.STEP_FOUR);
            layoutTracking.setVisibility(View.GONE);
            layoutDelivered.setVisibility(View.VISIBLE);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
        if (application.getBooleanInPef(Constants.STEP_ONE_RUNNING)) {
            txtPayment.setTextColor(ContextCompat.getColor(this, R.color.colorOrange));
            changeTintMode(imgOne, imgViewOne);
        }
        if (application.getBooleanInPef(Constants.STEP_TWO_RUNNING)) {
            txtInKitchen.setTextColor(ContextCompat.getColor(this, R.color.colorOrange));
            txtDeliverHint.setTextColor(ContextCompat.getColor(this, R.color.colorOrange));
            changeTintMode(imgTwo, imgViewtwo);
        }
        if (application.getBooleanInPef(Constants.STEP_THREE_RUNNING)) {
            changeTintMode(imgThree, imgViewThree);
            txtOnRoute.setTextColor(ContextCompat.getColor(this, R.color.colorOrange));
        }
    }

    private void setCompletedText(TextView txtNow, TextView txtNext, ImageView img, ImageView imgViewLine) {
        txtNow.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
        txtNext.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
        img.setColorFilter(ContextCompat.getColor(this, R.color.colorGreen));
        imgViewLine.setColorFilter(ContextCompat.getColor(this, R.color.colorGreen));
    }

    private void setCompletedIcon(ImageView img, ImageView imgViewLine) {
        img.setColorFilter(ContextCompat.getColor(this, R.color.colorGreen));
        imgViewLine.setColorFilter(ContextCompat.getColor(this, R.color.colorGreen));
    }

    private void changeTintMode(ImageView imgStatus, ImageView imgView) {
        imgStatus.setColorFilter(ContextCompat.getColor(this, R.color.colorOrange));
        imgView.setColorFilter(ContextCompat.getColor(this, R.color.colorOrange));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setStates(String message) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),uri);
        String description;
        if (message != null) {
            switch (message) {
                case "1":
                    txtPayment.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
                    setCompletedIcon(imgOne, imgViewOne);
                    application.storeBooleanInPref(Constants.STEP_ONE_RUNNING, false);
                    changeColors(txtPayment, txtInKitchen, imgTwo, imgViewtwo);
                    changeColors(txtPayment, txtDeliverHint, imgTwo, imgViewtwo);
                    changeTintMode(imgTwo, imgViewtwo);
                    storeStepCompletion(Constants.STEP_ONE);
                    storeStepCompletion(Constants.STEP_TWO_RUNNING);
                    setCompletedIcon(imgOne, imgViewOne);
                    ringtone.play();

                    description = "Order has been confirmed. Waiting for Delivery Agent";

                    send(description);

                    break;
                case "2":
                    txtOnRoute.setEnabled(true);
                    changeColors(txtInKitchen, txtOnRoute, imgThree, imgViewThree);
                    changeColors(txtDeliverHint, txtOnRoute, imgThree, imgViewThree);
                    changeTintMode(imgThree, imgViewThree);
                    storeStepCompletion(Constants.STEP_TWO);
                    storeStepCompletion(Constants.STEP_THREE_RUNNING);
                    application.storeBooleanInPref(Constants.STEP_TWO_RUNNING, false);
                    setCompletedIcon(imgTwo, imgViewtwo);
                    ringtone.play();

                    description = "Your order is now out for delivery.Track your Order";
                    send(description);
                    break;
                case "3":
                    setCompletedIcon(imgThree, imgViewThree);
                    storeStepCompletion(Constants.STEP_THREE);
                    application.storeBooleanInPref(Constants.STEP_THREE_RUNNING, false);
                    storeStepCompletion(Constants.STEP_FOUR);
                    layoutTracking.setVisibility(View.GONE);
                    layoutDelivered.setVisibility(View.VISIBLE);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.gravity = Gravity.CENTER;
                    window.setAttributes(params);
                    ringtone.play();

                    description = "Your order has been delivered successfully.";
                    send(description);
                    break;
                default:
                    break;
            }
        }
    }


    private void changeColors(TextView txtView, TextView txtNext, ImageView img, ImageView imgView) {
        txtView.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
        txtNext.setTextColor(ContextCompat.getColor(this, R.color.colorOrange));
        img.setColorFilter(ContextCompat.getColor(this, R.color.colorOrange));
        imgView.setColorFilter(ContextCompat.getColor(this, R.color.colorOrange));

    }

    private void storeStepCompletion(String step) {
        application.storeBooleanInPref(step, true);
    }

    public void startTracking(String track) {

        TrackingBuilder builder = new TrackingBuilder(new MarkerOption(track)).withListener(new TrackingListener() {
            @Override
            public void onTrackingStarted(String trackingId) {

            }

            @Override
            public void onLocationUpdate(String trackingId, TLocation location) {

            }

            @Override
            public void onTrackingEnded(String trackingId) {

            }

            @Override
            public void onTrackingError(String reason) {

            }
        });
        Teliver.startTracking(builder.build());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.initTeliver:
                startTracking(track);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onResume() {
        maintainState();
        if (application.getBooleanInPef(Constants.STEP_FOUR))
            application.deletePreference();
        if (dialog != null)
            dialog.show();
        super.onResume();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            setStates(intent.getStringExtra("msg"));
        }
    };

    @Override
    protected void onPause() {
        if (dialog != null)
            dialog.dismiss();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (dialog != null)
            dialog.dismiss();
        super.onDestroy();
    }


    private void subscribeToPushService() {
        Intent intent = getIntent();
        FirebaseMessaging.getInstance().subscribeToTopic(sessionHandler.getUserDetails().getId());
//        FirebaseMessaging.getInstance().subscribeToTopic("announcement");


        Log.d("AndroidBash", "Subscribed");
//        Toast.makeText(LoginActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();

        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        Log.d("AndroidBash", token);

//        RegFirebase(token);
//        Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
    }

    public void send(String description){
        Intent intent = getIntent();
        TOPIC = "/topics/" + sessionHandler.getUserDetails().getId(); //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = "Namu";
        NOTIFICATION_MESSAGE = description;
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }




    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityHome.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}
