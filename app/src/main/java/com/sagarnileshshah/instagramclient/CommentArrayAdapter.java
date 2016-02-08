package com.sagarnileshshah.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sshah on 2/7/16.
 */
public class CommentArrayAdapter extends ArrayAdapter {


    public CommentArrayAdapter(Context context, List<CommentItem> objects) {
        super(context, 0, objects);
    }

    public static class ViewHolder {
        @Bind(R.id.tvCommentRelativeTimeSpan)
        TextView tvCommentRelativeTimeSpan;
        @Bind(R.id.ivCommentUserProfileImage)
        RoundedImageView ivCommentUserProfileImage;
        @Bind(R.id.tvCommentText)
        TextView tvCommentText;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CommentItem commentItem = (CommentItem) getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivCommentUserProfileImage.setImageResource(0);
        Picasso.with(getContext()).load(commentItem.userProfileImageUrl).placeholder(R.drawable.ic_person_blue_24dp).error(R.drawable.ic_person_blue_24dp).into(viewHolder.ivCommentUserProfileImage);

        viewHolder.tvCommentText.setText(commentItem.text);
        Utility.formatTextViewContent(getContext(), viewHolder.tvCommentText, commentItem.userName, commentItem.text);

        viewHolder.tvCommentRelativeTimeSpan.setText(Utility.getRelativeTimeSpan(commentItem.createdUnixTimeInSec, false));

        return convertView;
    }
}
