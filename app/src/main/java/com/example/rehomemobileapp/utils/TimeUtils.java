package com.example.rehomemobileapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    /**
     * Format timestamp to display time in chat
     * @param timestamp The timestamp to format
     * @return Formatted time string
     */
    public static String formatChatTime(long timestamp) {
        Date messageDate = new Date(timestamp);
        Date currentDate = new Date();

        Calendar messageCal = Calendar.getInstance();
        messageCal.setTime(messageDate);

        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(currentDate);

        // If same day, show only time
        if (isSameDay(messageCal, currentCal)) {
            return TIME_FORMAT.format(messageDate);
        }

        // If yesterday
        currentCal.add(Calendar.DAY_OF_YEAR, -1);
        if (isSameDay(messageCal, currentCal)) {
            return "Hôm qua " + TIME_FORMAT.format(messageDate);
        }

        // If same year, show date without year
        currentCal.setTime(currentDate);
        if (messageCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR)) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
            return format.format(messageDate);
        }

        // Show full date and time
        return DATE_TIME_FORMAT.format(messageDate);
    }

    /**
     * Format timestamp for chat list (last message time)
     * @param timestamp The timestamp to format
     * @return Formatted time string
     */
    public static String formatChatListTime(long timestamp) {
        Date messageDate = new Date(timestamp);
        Date currentDate = new Date();

        Calendar messageCal = Calendar.getInstance();
        messageCal.setTime(messageDate);

        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(currentDate);

        // If same day, show only time
        if (isSameDay(messageCal, currentCal)) {
            return TIME_FORMAT.format(messageDate);
        }

        // If yesterday
        currentCal.add(Calendar.DAY_OF_YEAR, -1);
        if (isSameDay(messageCal, currentCal)) {
            return "Hôm qua";
        }

        // If within a week, show day of week
        currentCal.setTime(currentDate);
        long diffInDays = (currentDate.getTime() - messageDate.getTime()) / (24 * 60 * 60 * 1000);
        if (diffInDays < 7) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            return dayFormat.format(messageDate);
        }

        // Show date
        return DATE_FORMAT.format(messageDate);
    }

    /**
     * Get relative time string (e.g., "2 phút trước", "1 giờ trước")
     * @param timestamp The timestamp to compare
     * @return Relative time string
     */
    public static String getRelativeTime(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long diffInMillis = currentTime - timestamp;

        if (diffInMillis < 60 * 1000) { // Less than 1 minute
            return "Vừa xong";
        } else if (diffInMillis < 60 * 60 * 1000) { // Less than 1 hour
            long minutes = diffInMillis / (60 * 1000);
            return minutes + " phút trước";
        } else if (diffInMillis < 24 * 60 * 60 * 1000) { // Less than 1 day
            long hours = diffInMillis / (60 * 60 * 1000);
            return hours + " giờ trước";
        } else if (diffInMillis < 7 * 24 * 60 * 60 * 1000) { // Less than 1 week
            long days = diffInMillis / (24 * 60 * 60 * 1000);
            return days + " ngày trước";
        } else {
            return DATE_FORMAT.format(new Date(timestamp));
        }
    }

    /**
     * Check if two Calendar objects represent the same day
     * @param cal1 First calendar
     * @param cal2 Second calendar
     * @return true if same day, false otherwise
     */
    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Check if timestamp is today
     * @param timestamp The timestamp to check
     * @return true if today, false otherwise
     */
    public static boolean isToday(long timestamp) {
        Calendar messageCal = Calendar.getInstance();
        messageCal.setTime(new Date(timestamp));

        Calendar currentCal = Calendar.getInstance();

        return isSameDay(messageCal, currentCal);
    }

    /**
     * Check if timestamp is yesterday
     * @param timestamp The timestamp to check
     * @return true if yesterday, false otherwise
     */
    public static boolean isYesterday(long timestamp) {
        Calendar messageCal = Calendar.getInstance();
        messageCal.setTime(new Date(timestamp));

        Calendar currentCal = Calendar.getInstance();
        currentCal.add(Calendar.DAY_OF_YEAR, -1);

        return isSameDay(messageCal, currentCal);
    }
}
