package com.lphaindia.dodapp.dodapp.Product;

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

    public Product(String title, String maximumRetailPrice, String sellingPrice, String discountPercentage,
                   String imageUrl, String currency, String brand, String color, String sizeUnit,
                   String productId, String category, String productUrl) {
        this.title = title;
        this.maximumRetailPrice = maximumRetailPrice;
        this.sellingPrice = sellingPrice;
        this.discountPercentage = discountPercentage;
        this.imageUrl = imageUrl;
        this.currency = currency;
        this.brand = brand;
        this.color = color;
        this.sizeUnit = sizeUnit;
        this.productId = productId;
        this.category = category;
        this.productUrl = productUrl;
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
