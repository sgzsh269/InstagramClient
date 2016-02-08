package com.sagarnileshshah.instagramclient;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sshah on 2/5/16.
 */
public class Utility {
    public static int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getDisplayHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources r = context.getResources();
        DisplayMetrics metrics = r.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static void formatTextViewContent(Context context, TextView textView, String userName, String text) {
        ForegroundColorSpan userNameColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.appDarkBlue));
        StyleSpan boldStyleSpan = new StyleSpan(Typeface.BOLD);
        SpannableString captionSpannableString = new SpannableString(userName + " " + text);
        captionSpannableString.setSpan(userNameColorSpan, 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        captionSpannableString.setSpan(boldStyleSpan, 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Pattern pattern = Pattern.compile("[#|@].+?\\b");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int userNameoffset = userName.length() + 1;
            int start = matcher.start() + userNameoffset;
            int end = matcher.end() + userNameoffset;
            ForegroundColorSpan tagMentionColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.appDarkBlue));
            captionSpannableString.setSpan(tagMentionColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(captionSpannableString);
    }

    public static String getRelativeTimeSpan(Long unixTimeInSec, boolean abbreviated) {
        Long currUnixTimeInMilli = System.currentTimeMillis();
        Long unixTimeInMilli = unixTimeInSec * 1000;

        if (!abbreviated) {
            return (String) DateUtils.getRelativeTimeSpanString(unixTimeInMilli, currUnixTimeInMilli, DateUtils.SECOND_IN_MILLIS);
        } else {

            Long diff = Math.abs(currUnixTimeInMilli - unixTimeInMilli);

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
}
