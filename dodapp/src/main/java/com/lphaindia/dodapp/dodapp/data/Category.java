package com.lphaindia.dodapp.dodapp.data;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public class Category {
    private String categoryName;
    private String imageUrl;
    private int drawable;

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }

    public Category(String categoryName, int drawable) {
        this.categoryName = categoryName;
        this.drawable = drawable;
    }

    public String getName() {
        return categoryName;
    }

    public int getDrawable() {
        return drawable;
    }
}
