package com.haqq.namu.Models;

/**
 * Created by sagar on 28/6/17.
 */

public class Item {

    private int image;
    private String name;

    public Item(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
