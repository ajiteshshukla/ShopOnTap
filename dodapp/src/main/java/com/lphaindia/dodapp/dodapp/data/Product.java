package com.lphaindia.dodapp.dodapp.data;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public class Product {
    public String title;
    public String brand;
    public String currency;
    public String maximumRetailPrice;
    public String sellingPrice;
    public String discountPercentage;
    public String imageUrl;
    public String color;
    public String sizeUnit;
    public String productId;
    public String category;
    public String productUrl;
    public String affiliate;
    public Product(){

    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", brand='" + brand + '\'' +
                ", currency='" + currency + '\'' +
                ", maximumRetailPrice='" + maximumRetailPrice + '\'' +
                ", sellingPrice='" + sellingPrice + '\'' +
                ", discountPercentage='" + discountPercentage + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", color='" + color + '\'' +
                ", sizeUnit='" + sizeUnit + '\'' +
                ", productId='" + productId + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
