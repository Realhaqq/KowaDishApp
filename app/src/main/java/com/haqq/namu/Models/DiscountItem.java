package com.haqq.namu.Models;

/**
 * Created by sagar on 28/6/17.
 */

public class DiscountItem {

    private int image;
    private String price;
    private String name;
    private String discount;

    public DiscountItem(int image, String price, String name, String discount) {
        this.image = image;
        this.price = price;
        this.name = name;
        this.discount = discount;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
