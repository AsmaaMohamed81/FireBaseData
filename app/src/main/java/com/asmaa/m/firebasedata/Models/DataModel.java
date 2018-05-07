package com.asmaa.m.firebasedata.Models;

import java.io.Serializable;

/**
 * Created by m on 4/26/2018.
 */

public class DataModel  implements Serializable{

    private String title;
    private String description ;
    private String image;
    private String image_key;

    public DataModel() {
    }

    public DataModel(String title, String description, String image, String image_key) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.image_key = image_key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_key() {
        return image_key;
    }

    public void setImage_key(String image_key) {
        this.image_key = image_key;
    }
}
