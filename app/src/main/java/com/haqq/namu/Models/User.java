package com.haqq.namu.Models;

import java.util.Date;

public class User {
    String phone, email, fullname, payment_status, mylocation, id;



    Date sessionExpiryDate;




    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }


    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getMylocation() {
        return mylocation;
    }

    public void setMylocation(String mylocation) {
        this.mylocation = mylocation;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }


    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


}

