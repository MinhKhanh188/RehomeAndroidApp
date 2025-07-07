package com.example.rehomemobileapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class Post {

    @SerializedName("_id")
    private String id;

    private String name;
    private String category;
    private String province;
    private String productStatus;
    private double price;
    private Double originalPrice; // Nullable
    private boolean isVip;
    private List<String> images;
    private String sellerId;
    private String sellerName;
    private String sellerProfilePic;
    private String address;
    private String mapUrl;
    private String description;
    private Map<String, String> specifications;
    private String uploadDate;
    private String status;

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getProvince() { return province; }
    public String getProductStatus() { return productStatus; }
    public double getPrice() { return price; }
    public Double getOriginalPrice() { return originalPrice; }
    public boolean isVip() { return isVip; }
    public List<String> getImages() { return images; }
    public String getSellerId() { return sellerId; }
    public String getSellerName() { return sellerName; }
    public String getSellerProfilePic() { return sellerProfilePic; }
    public String getAddress() { return address; }
    public String getMapUrl() { return mapUrl; }
    public String getDescription() { return description; }
    public Map<String, String> getSpecifications() { return specifications; }
    public String getUploadDate() { return uploadDate; }
    public String getStatus() { return status; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setProvince(String province) { this.province = province; }
    public void setProductStatus(String productStatus) { this.productStatus = productStatus; }
    public void setPrice(double price) { this.price = price; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }
    public void setVip(boolean vip) { isVip = vip; }
    public void setImages(List<String> images) { this.images = images; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public void setSellerProfilePic(String sellerProfilePic) { this.sellerProfilePic = sellerProfilePic; }
    public void setAddress(String address) { this.address = address; }
    public void setMapUrl(String mapUrl) { this.mapUrl = mapUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setSpecifications(Map<String, String> specifications) { this.specifications = specifications; }
    public void setUploadDate(String uploadDate) { this.uploadDate = uploadDate; }
    public void setStatus(String status) { this.status = status; }
}
