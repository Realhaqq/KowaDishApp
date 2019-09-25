package com.haqq.namu.users;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.haqq.namu.Models.User;

import java.util.Date;

public class SessionHandler {

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ID = "id";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMPTY = "";
//    private static final String KEY_PAYMENT_STATUS = "payment_status";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     * @param fullname
     * @param phone
     * @param email
//     * @aram mylocation
//     * @param id
//     * @param payment_status
     */
    public void loginUser(String phone, String email, String fullname, String id) {
        mEditor.putString(KEY_FULLNAME, fullname);
        mEditor.putString(KEY_PHONE, phone);
        mEditor.putString(KEY_ID, id);
        mEditor.putString(KEY_EMAIL, email);
//        mEditor.putString(KEY_PAYMENT_STATUS, payment_status);


        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

//
//    public void EditPaymentStatus(String payment_status){
//        mEditor.putString(KEY_PAYMENT_STATUS, payment_status);
//        mEditor.commit();
//    }


//    Update Place

    public void  SetDefaultLocation(String location){
        mEditor.putString("mylocation", location);
        mEditor.commit();
    }


    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();

        user.setPhone(mPreferences.getString(KEY_PHONE, KEY_EMPTY));
        user.setFullname(mPreferences.getString(KEY_FULLNAME, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));
        user.setId(mPreferences.getString(KEY_ID, KEY_EMPTY));
        user.setEmail(mPreferences.getString(KEY_EMAIL, KEY_EMPTY));
        user.setMylocation(mPreferences.getString("mylocation", KEY_EMPTY));
//        user.setPayment_status(mPreferences.getString(KEY_PAYMENT_STATUS, KEY_EMPTY));

        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }


}
