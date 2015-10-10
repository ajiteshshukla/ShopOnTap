package com.lphaindia.dodapp.dodapp.data;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public class Category {
    private String categoryName;
    private String imageUrl;

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return categoryName;
    }
}
