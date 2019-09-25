package com.haqq.namu.Models;

/**
 * Created by sagar on 29/6/17.
 */

public class PastOrders {

    private String date;
    private String number_of_items;
    private String price;

//    public PastOrders(String date, String number_of_items, String price) {
//        this.date = date;
//        this.number_of_items = number_of_items;
//        this.price = price;
//    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber_of_items() {
        return number_of_items;
    }

    public void setNumber_of_items(String number_of_items) {
        this.number_of_items = number_of_items;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
