package com.example.amit.photoview.models;


import io.realm.RealmObject;

/**
 * Created by Amit on 12-12-2017.
 */

public class ImageDetails extends RealmObject{
    int imageId;
    String imageDetails;
    String imageSource;
    String user;
    boolean fevourite;
    String address;

    public ImageDetails(int imageId, String imageDetails, String imageSource, String user, boolean fevourite, String address) {
        this.imageId = imageId;
        this.imageDetails = imageDetails;
        this.imageSource = imageSource;
        this.user = user;
        this.fevourite = fevourite;
        this.address = address;
    }

    public ImageDetails() {
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageDetails() {
        return imageDetails;
    }

    public void setImageDetails(String imageDetails) {
        this.imageDetails = imageDetails;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isFevourite() {
        return fevourite;
    }

    public void setFevourite(boolean fevourite) {
        this.fevourite = fevourite;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
