package com.sagarnileshshah.instagramclient;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sshah on 2/4/16.
 */
public class PhotoArrayAdapter extends ArrayAdapter {

    public PhotoArrayAdapter(Context context, List<PhotoItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PhotoItem photoItem = (PhotoItem) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        tvUserName.setText(photoItem.userName);

        TextView tvRelativeTimeSpan = (TextView) convertView.findViewById(R.id.tvRelativeTimeSpan);
        tvRelativeTimeSpan.setText(photoItem.getRelativeTimeSpan());

        ImageView tvIconLike = (ImageView) convertView.findViewById(R.id.ivIconLike);

        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        if (photoItem.likesCount > 0) {
            tvLikes.setText(Integer.toString(photoItem.likesCount) + " likes");
            tvLikes.setVisibility(View.VISIBLE);
            tvIconLike.setVisibility(View.VISIBLE);
        } else {
            tvLikes.setVisibility(View.GONE);
            tvIconLike.setVisibility(View.GONE);
        }

        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ForegroundColorSpan userNameColorSpan = new ForegroundColorSpan(getContext().getResources().getColor(R.color.appDarkBlue));
        StyleSpan boldStyleSpan = new StyleSpan(Typeface.BOLD);
        SpannableString captionSpannableString = new SpannableString(photoItem.userName + " " + photoItem.caption);
        captionSpannableString.setSpan(userNameColorSpan, 0, photoItem.userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        captionSpannableString.setSpan(boldStyleSpan, 0, photoItem.userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Pattern pattern = Pattern.compile("[#|@].+?\\b");
        Matcher matcher = pattern.matcher(photoItem.caption);
        while (matcher.find()) {
            int userNameoffset = photoItem.userName.length() + 1;
            int start = matcher.start() + userNameoffset;
            String text = matcher.group();
            int end = start + text.length();
            ForegroundColorSpan tagMentionColorSpan = new ForegroundColorSpan(getContext().getResources().getColor(R.color.appDarkBlue));
            captionSpannableString.setSpan(tagMentionColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvCaption.setText(captionSpannableString);

        TextView tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
        RelativeLayout.LayoutParams tvUserNamelayoutParams = (RelativeLayout.LayoutParams) tvUserName.getLayoutParams();
        if (photoItem.location != null) {
            float leftMarginPx = DeviceDimensionsHelper.convertDpToPixel(8, getContext());
            float topMarginPx = DeviceDimensionsHelper.convertDpToPixel(4, getContext());
            tvUserNamelayoutParams.setMargins((int) leftMarginPx, (int) topMarginPx, 0, 0);
            tvLocation.setText(photoItem.location);
            tvLocation.setVisibility(View.VISIBLE);
        } else {
            float leftMarginPx = DeviceDimensionsHelper.convertDpToPixel(8, getContext());
            float topMarginPx = DeviceDimensionsHelper.convertDpToPixel(12, getContext());
            tvUserNamelayoutParams.setMargins((int) leftMarginPx, (int) topMarginPx, 0, 0);
            tvLocation.setVisibility(View.GONE);
        }

        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photoItem.imageUrl).into(ivPhoto);

        RoundedImageView ivUserProfileImage = (RoundedImageView) convertView.findViewById(R.id.ivUserProfileImage);
        ivUserProfileImage.setImageResource(0);
        Picasso.with(getContext()).load(photoItem.userProfileImageUrl).into(ivUserProfileImage);

        return convertView;

    }
}
