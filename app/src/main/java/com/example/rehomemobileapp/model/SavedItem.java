package com.example.rehomemobileapp.model;

public class SavedItem {
    private String _id;
    private String savedAt;

    private Post productId;
    private User userId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(String savedAt) {
        this.savedAt = savedAt;
    }

    public Post getProductId() {
        return productId;
    }

    public void setProductId(Post productId) {
        this.productId = productId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
