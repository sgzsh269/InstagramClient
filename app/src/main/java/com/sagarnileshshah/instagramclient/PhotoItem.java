package com.sagarnileshshah.instagramclient;

import java.util.ArrayList;

/**
 * Created by sshah on 2/4/16.
 */
public class PhotoItem {
    public String type;
    public String mediaId;
    public String userName;
    public String userProfileImageUrl;
    public String caption;
    public String imageUrl;
    public String location;
    public int imageHeight;
    public int likesCount;
    public long createdUnixTimeInSec;
    public int totalCommentsCount;
    public ArrayList<CommentItem> commentItems;
    public String videoUrl;
}
