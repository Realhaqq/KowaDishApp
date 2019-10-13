package com.haqq.namu;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.haqq.namu.Activities.MyWebViewClient;

public class PaymentActivity extends Activity {
    private WebView webView1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        webView1 = (WebView) findViewById(R.id.web_view);
        String summary = "<html><head><title>Title of the document</title></head><body><h1><a href=\"hrupin://second_activity\">LINK to second activity</a></h1><h1><a href=\"http://www.google.com/\">Link to GOOGLE.COM</a></h1></body></html>";
        webView1.loadData(summary, "text/html", null);
        webView1.setWebViewClient(new MyWebViewClient(this));
    }
}

