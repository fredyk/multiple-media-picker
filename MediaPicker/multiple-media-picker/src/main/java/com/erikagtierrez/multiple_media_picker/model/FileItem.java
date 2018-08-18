package com.erikagtierrez.multiple_media_picker.model;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;

public class FileItem implements Serializable {

    private int image;
    private String name;
    private String path;

    public FileItem() {
    }

    public FileItem(int image, String name, String path) {
        this.image = image;
        this.name = name;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
