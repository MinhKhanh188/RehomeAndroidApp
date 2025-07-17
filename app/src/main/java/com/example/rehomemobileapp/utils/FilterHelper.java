package com.example.rehomemobileapp.utils;

import com.example.rehomemobileapp.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FilterHelper {
    public static List<Post> filterAndSortPosts(List<Post> originalPosts, 
                                               String selectedCategory, 
                                               SortType sortType,
                                               double minPrice,
                                               double maxPrice) {
        List<Post> filteredPosts = new ArrayList<>();
        
        // Filter by category
        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            for (Post post : originalPosts) {
                if (selectedCategory.equals(post.getCategory())) {
                    filteredPosts.add(post);
                }
            }
        } else {
            filteredPosts.addAll(originalPosts);
        }
        
        // Filter by price range
        if (minPrice > 0 || maxPrice > 0) {
            List<Post> priceFilteredPosts = new ArrayList<>();
            for (Post post : filteredPosts) {
                double price = post.getPrice();
                if ((minPrice <= 0 || price >= minPrice) && 
                    (maxPrice <= 0 || price <= maxPrice)) {
                    priceFilteredPosts.add(post);
                }
            }
            filteredPosts = priceFilteredPosts;
        }
        
        // Sort posts
        switch (sortType) {
            case PRICE_LOW_TO_HIGH:
                Collections.sort(filteredPosts, new Comparator<Post>() {
                    @Override
                    public int compare(Post p1, Post p2) {
                        return Double.compare(p1.getPrice(), p2.getPrice());
                    }
                });
                break;
            case PRICE_HIGH_TO_LOW:
                Collections.sort(filteredPosts, new Comparator<Post>() {
                    @Override
                    public int compare(Post p1, Post p2) {
                        return Double.compare(p2.getPrice(), p1.getPrice());
                    }
                });
                break;
        }
        
        return filteredPosts;
    }
} 