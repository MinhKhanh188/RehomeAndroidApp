package com.example.rehomemobileapp.utils;

public enum SortType {
    PRICE_LOW_TO_HIGH("Giá tăng dần"),
    PRICE_HIGH_TO_LOW("Giá giảm dần");

    private final String displayName;

    SortType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 