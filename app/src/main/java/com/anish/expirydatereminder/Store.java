package com.anish.expirydatereminder;

public class  Store {
    private String name;
    private int imageResourceId;

    public Store(String name, int imageResourceId) {
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

