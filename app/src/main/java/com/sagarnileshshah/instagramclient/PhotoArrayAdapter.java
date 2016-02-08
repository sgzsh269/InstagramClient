package com.sagarnileshshah.instagramclient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sshah on 2/4/16.
 */
public class PhotoArrayAdapter extends ArrayAdapter {

    public PhotoArrayAdapter(Context context, List<PhotoItem> objects) {
        super(context, 0, objects);
    }

    public static class ViewHolder {
        @Bind(R.id.tvUserName)
        TextView tvUserName;
        @Bind(R.id.tvRelativeTimeSpan)
        TextView tvRelativeTimeSpan;
        @Bind(R.id.tvLikes)
        TextView tvLikes;
        @Bind(R.id.tvLocation)
        TextView tvLocation;
        @Bind(R.id.ivPhoto)
        ImageView ivPhoto;
        @Bind(R.id.ivUserProfileImage)
        RoundedImageView ivUserProfileImage;
        @Bind(R.id.llCommentsContainer)
        LinearLayout llCommentsContainer;
        @Bind(R.id.tvCommentCount)
        TextView tvCommentCount;
        @Bind(R.id.tvCaption)
        TextView tvCaption;
        @Bind(R.id.ivIconVideo)
        ImageView ivIconVideo;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final PhotoItem photoItem = (PhotoItem) getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUserName.setText(photoItem.userName);

        viewHolder.tvRelativeTimeSpan.setText(Utility.getRelativeTimeSpan(photoItem.createdUnixTimeInSec, true));

        if (photoItem.likesCount > 0) {
            viewHolder.tvLikes.setText(Integer.toString(photoItem.likesCount) + " likes");
            viewHolder.tvLikes.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvLikes.setVisibility(View.GONE);
        }

        if (photoItem.caption != null) {
            Utility.formatTextViewContent(getContext(), viewHolder.tvCaption, photoItem.userName, photoItem.caption);
        }

        RelativeLayout.LayoutParams tvUserNamelayoutParams = (RelativeLayout.LayoutParams) viewHolder.tvUserName.getLayoutParams();
        if (photoItem.location != null) {
            float leftMarginPx = Utility.convertDpToPixel(8, getContext());
            float topMarginPx = Utility.convertDpToPixel(4, getContext());
            tvUserNamelayoutParams.setMargins((int) leftMarginPx, (int) topMarginPx, 0, 0);
            viewHolder.tvLocation.setText(photoItem.location);
            viewHolder.tvLocation.setVisibility(View.VISIBLE);
        } else {
            float leftMarginPx = Utility.convertDpToPixel(8, getContext());
            float topMarginPx = Utility.convertDpToPixel(12, getContext());
            tvUserNamelayoutParams.setMargins((int) leftMarginPx, (int) topMarginPx, 0, 0);
            viewHolder.tvLocation.setVisibility(View.GONE);
        }

        viewHolder.ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photoItem.imageUrl).placeholder(R.drawable.photo_placeholder).error(R.drawable.photo_placeholder).into(viewHolder.ivPhoto);

        if (photoItem.type.equals("video")) {
            viewHolder.ivIconVideo.setVisibility(View.VISIBLE);
            viewHolder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), VideoActivity.class);
                    intent.putExtra("url", photoItem.videoUrl);
                    getContext().startActivity(intent);
                }
            });
        } else {
            viewHolder.ivIconVideo.setVisibility(View.GONE);
            viewHolder.ivPhoto.setOnClickListener(null);
        }


        viewHolder.ivUserProfileImage.setImageResource(0);
        Picasso.with(getContext()).load(photoItem.userProfileImageUrl).placeholder(R.drawable.ic_person_blue_24dp).error(R.drawable.ic_person_blue_24dp).into(viewHolder.ivUserProfileImage);

        viewHolder.llCommentsContainer.removeAllViews();

        if (photoItem.totalCommentsCount > 0) {
            viewHolder.tvCommentCount.setVisibility(View.GONE);
            int photoCommentStartIndex = 0;
            if (photoItem.totalCommentsCount == 1) {
                photoCommentStartIndex = photoItem.commentItems.size() - 1;
            } else if (photoItem.totalCommentsCount > 1) {
                photoCommentStartIndex = photoItem.commentItems.size() - 2;
            }
            if (photoItem.totalCommentsCount > 2) {
                viewHolder.tvCommentCount.setVisibility(View.VISIBLE);
                int photoCommentsSize = photoItem.commentItems.size();
                viewHolder.tvCommentCount.setText("View all " + Integer.toString(photoItem.totalCommentsCount) + " comments");
                viewHolder.tvCommentCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), CommentsActivity.class);
                        intent.putExtra("mediaId", photoItem.mediaId);
                        getContext().startActivity(intent);
                    }
                });
            }
            for (CommentItem commentItem : photoItem.commentItems.subList(photoCommentStartIndex, photoItem.commentItems.size())) {
                TextView commentView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.container_item_comment, null);
                Utility.formatTextViewContent(getContext(), commentView, commentItem.userName, commentItem.text);
                viewHolder.llCommentsContainer.addView(commentView);
            }
        } else {
            viewHolder.tvCommentCount.setVisibility(View.GONE);
        }

        return convertView;

    }
}
