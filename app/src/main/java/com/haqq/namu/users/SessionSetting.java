package com.haqq.namu.users;

import android.content.Context;
import android.content.SharedPreferences;

import com.haqq.namu.Models.Settings;

import java.util.Date;


public class SessionSetting {

    private static final String PREF_NAME = "AppSession";
    private static final String KEY_DELIVERY_CHARGE = "delivery_charge";
    private static final String KEY_SERVICE_TAX = "service_tax";
    private static final String KEY_EMPTY = "";


    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionSetting(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     * @param delivery_charge
     * @param service_tax
     */


    public void AppSetting(String delivery_charge, String service_tax) {
        mEditor.putString(KEY_DELIVERY_CHARGE, delivery_charge);
        mEditor.putString(KEY_SERVICE_TAX, service_tax);



        Date date = new Date();

        //Set user session for next 7 days
//        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
//        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

//    public void SetProgram(String program){
//        mEditor.putString(KEY_PROGRAM, program);
//        mEditor.commit();
//    }


    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public Settings getSetting() {
        //Check if user is logged in first

        Settings settings = new Settings();

        settings.setDelivery_charge(mPreferences.getString(KEY_DELIVERY_CHARGE, KEY_EMPTY));
        settings.setService_tax(mPreferences.getString(KEY_SERVICE_TAX, KEY_EMPTY));


        return settings;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutSetting(){
        mEditor.clear();
        mEditor.commit();
    }

}
