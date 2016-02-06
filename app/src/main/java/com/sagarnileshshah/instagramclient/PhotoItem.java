package com.sagarnileshshah.instagramclient;

/**
 * Created by sshah on 2/4/16.
 */
public class PhotoItem {
    public String userName;
    public String userProfileImageUrl;
    public String caption;
    public String imageUrl;
    public String location;
    public int imageHeight;
    public int likesCount;
    public long createdUnixTimeInSec;

    public String getRelativeTimeSpan() {
        Long currUnixTimeInMilli = System.currentTimeMillis();

        Long diff = Math.abs(currUnixTimeInMilli - (createdUnixTimeInSec * 1000));

        long monthDivisor = 30 * 24 * 60 * 60 * 1000;
        int weekDivisor = 7 * 24 * 60 * 60 * 1000;
        int dayDivisor = 24 * 60 * 60 * 1000;
        int hourDivisor = 60 * 60 * 1000;
        int minuteDivisor = 60 * 1000;
        int secondDivisor = 1000;

        if (diff / monthDivisor > 0) {
            return Long.toString(diff / monthDivisor) + "M";
        } else if (diff / weekDivisor > 0) {
            return Long.toString(diff / weekDivisor) + "w";
        } else if (diff / dayDivisor > 0) {
            return Long.toString(diff / dayDivisor) + "d";
        } else if (diff / hourDivisor > 0) {
            return Long.toString(diff / hourDivisor) + "h";
        } else if (diff / minuteDivisor > 0) {
            return Long.toString(diff / minuteDivisor) + "m";
        } else {
            return Long.toString(diff / secondDivisor) + "s";
        }

    }
}
