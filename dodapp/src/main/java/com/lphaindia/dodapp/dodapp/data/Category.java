package com.lphaindia.dodapp.dodapp.data;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public class Category {
    private String displayName;
    private String imageUrl;
    private String categoryName;
    private int drawable;

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }

    public Category(String displayName, String categoryName, int drawable) {
        this.displayName = displayName;
        this.drawable = drawable;
        this.categoryName = categoryName;
    }

    public String getName() {
        return displayName;
    }

    public String getCategoryName() {
        return categoryName;
    }


    public int getDrawable() {
        return drawable;
    }
}
