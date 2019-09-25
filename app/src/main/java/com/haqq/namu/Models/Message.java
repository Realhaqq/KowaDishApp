package com.haqq.namu.Models;

/**
 * Created by sagar on 2/7/17.
 */

public class Message {

    private String text;
    private String name;

    public Message(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
