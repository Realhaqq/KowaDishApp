package com.haqq.namu.Models;

/**
 * Created by sagar on 29/6/17.
 */

public class ItemData {

    private String text;
    private Integer imageId;


    public ItemData(String text, Integer imageId){
        this.text=text;
        this.imageId=imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
