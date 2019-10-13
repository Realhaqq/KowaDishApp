package com.haqq.namu.Activities;

import android.webkit.WebViewClient;

import com.haqq.namu.PaymentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    private Context context;

    public MyWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(url.equals("hrupin://second_activity")){
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
}

