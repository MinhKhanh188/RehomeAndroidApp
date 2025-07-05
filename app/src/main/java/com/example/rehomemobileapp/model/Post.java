package com.example.rehomemobileapp.model;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;


public class Post {
    @SerializedName("_id")
    private String id;

    private String name;
    private String category;
    private String productStatus;
    private double price;
    private List<String> images;
    private String sellerName;
    private String address;
    private String status;

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getProductStatus() { return productStatus; }
    public double getPrice() { return price; }
    public List<String> getImages() { return images; }
    public String getSellerName() { return sellerName; }
    public String getAddress() { return address; }
    public String getStatus() { return status; }

}

