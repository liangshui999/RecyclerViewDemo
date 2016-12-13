package com.example.asus_cp.modle;

/**
 * Created by asus-cp on 2016-12-13.
 */

public class Address {

    private int avatar;
    private String name;

    public Address(int avatar, String name) {
        this.avatar = avatar;
        this.name = name;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
