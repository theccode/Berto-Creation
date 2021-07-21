package com.android.berto;

import java.util.UUID;

public class Image {
    private UUID mId;
    private String name;
    private float width;
    private float height;
    private int resolution;
    private float price;

    public Image(){
        this(UUID.randomUUID());
    }
    public Image(UUID id){
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
