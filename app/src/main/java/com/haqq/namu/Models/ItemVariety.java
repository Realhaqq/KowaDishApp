package com.haqq.namu.Models;

/**
 * Created by sagar on 29/6/17.
 */

public class ItemVariety {

    private int image;
    private String price;
    private String name;


    public ItemVariety(int image, String price, String name) {
        this.image = image;
        this.price = price;
        this.name = name;

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

}
