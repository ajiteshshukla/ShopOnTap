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
    public String aspectRatio;
    public String affiliateLogo;
    public String description;
    public Product(){

    }

    public String getAffiliateLogo() {
        return affiliateLogo;
    }

    public void setAffiliateLogo(String affiliateLogo) {
        this.affiliateLogo = affiliateLogo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
