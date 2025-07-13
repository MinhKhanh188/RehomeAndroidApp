package com.example.rehomemobileapp.utils;

import java.util.Random;

public class AvatarUtils {

    private static final String[] AVATAR_COLORS = {
            "#FF6B35", "#F7931E", "#FFD23F", "#06FFA5", "#06D6A0",
            "#118AB2", "#073B4C", "#8E44AD", "#E74C3C", "#2ECC71",
            "#3498DB", "#F39C12", "#9B59B6", "#1ABC9C", "#34495E"
    };

    /**
     * Generate avatar initials from a name
     * @param name The full name
     * @return Avatar initials (max 2 characters)
     */
    public static String generateAvatarInitials(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }

        String[] words = name.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();

        if (words.length == 1) {
            // Single word - take first character
            initials.append(words[0].charAt(0));
        } else if (words.length >= 2) {
            // Multiple words - take first character of first and last word
            initials.append(words[0].charAt(0));
            initials.append(words[words.length - 1].charAt(0));
        }

        return initials.toString().toUpperCase();
    }

    /**
     * Generate a consistent color for an avatar based on the name
     * @param name The name to generate color for
     * @return Hex color string
     */
    public static String generateAvatarColor(String name) {
        if (name == null || name.trim().isEmpty()) {
            return AVATAR_COLORS[0];
        }

        // Use hashCode to get consistent color for same name
        int hash = Math.abs(name.hashCode());
        int colorIndex = hash % AVATAR_COLORS.length;

        return AVATAR_COLORS[colorIndex];
    }

    /**
     * Get a random avatar color
     * @return Random hex color string
     */
    public static String getRandomAvatarColor() {
        Random random = new Random();
        int index = random.nextInt(AVATAR_COLORS.length);
        return AVATAR_COLORS[index];
    }

    /**
     * Check if a string is a valid hex color
     * @param color The color string to check
     * @return true if valid hex color, false otherwise
     */
    public static boolean isValidHexColor(String color) {
        if (color == null || color.isEmpty()) {
            return false;
        }

        // Remove # if present
        if (color.startsWith("#")) {
            color = color.substring(1);
        }

        // Check if it's 6 characters and all hex digits
        return color.length() == 6 && color.matches("[0-9A-Fa-f]+");
    }

    /**
     * Ensure color string has # prefix
     * @param color The color string
     * @return Color string with # prefix
     */
    public static String ensureHashPrefix(String color) {
        if (color == null || color.isEmpty()) {
            return "#000000";
        }

        if (!color.startsWith("#")) {
            return "#" + color;
        }

        return color;
    }

    /**
     * Get all available avatar colors
     * @return Array of hex color strings
     */
    public static String[] getAllAvatarColors() {
        return AVATAR_COLORS.clone();
    }
}