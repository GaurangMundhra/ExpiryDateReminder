package com.anish.expirydatereminder;

public class NGO {
    private String name;
    private int imageResourceId;

    public NGO(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
