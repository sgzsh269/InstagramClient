package com.sagarnileshshah.instagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<PhotoItem> mPhotoItemArrayList;
    private PhotoArrayAdapter mPhotoArrayAdapter;

    @Bind(R.id.lvPhotoItems)
    ListView mLvPhotoItems;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mPhotoItemArrayList = new ArrayList<>();
        mPhotoArrayAdapter = new PhotoArrayAdapter(this, mPhotoItemArrayList);

        mLvPhotoItems.setAdapter(mPhotoArrayAdapter);

        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeContainer.setRefreshing(true);
                fetchPopularPhotos();
            }
        });
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        fetchPopularPhotos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void fetchPopularPhotos() {
        AsyncHttpClient photoAsyncHttpClient = new AsyncHttpClient();

        String popularPhotosUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        photoAsyncHttpClient.get(popularPhotosUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mPhotoArrayAdapter.clear();
                JSONArray photoJSONArray = null;
                try {
                    photoJSONArray = response.getJSONArray("data");

                    for (int i = 0; i < photoJSONArray.length(); i++) {

                        JSONObject photoJSON = photoJSONArray.getJSONObject(i);

                        PhotoItem photoItem = new PhotoItem();
                        photoItem.type = photoJSON.getString("type");
                        photoItem.userName = photoJSON.getJSONObject("user").getString("username");
                        photoItem.userProfileImageUrl = photoJSON.getJSONObject("user").getString("profile_picture");

                        if (photoJSON.optJSONObject("caption") != null) {
                            photoItem.caption = photoJSON.getJSONObject("caption").getString("text");
                        } else {
                            photoItem.caption = null;
                        }

                        photoItem.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photoItem.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photoItem.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photoItem.createdUnixTimeInSec = photoJSON.getLong("created_time");
                        photoItem.mediaId = photoJSON.getString("id");

                        if (photoJSON.optJSONObject("location") != null) {
                            photoItem.location = photoJSON.getJSONObject("location").getString("name");
                        } else {
                            photoItem.location = null;
                        }

                        photoItem.commentItems = new ArrayList<CommentItem>();
                        if (photoJSON.optJSONObject("comments") != null) {
                            JSONObject commentsJSONObject = photoJSON.getJSONObject("comments");
                            photoItem.totalCommentsCount = commentsJSONObject.getInt("count");
                            JSONArray data = commentsJSONObject.getJSONArray("data");
                            for (int j = 0; j < data.length(); j++) {
                                CommentItem commentItem = new CommentItem();
                                JSONObject commentJSONObject = data.getJSONObject(j);
                                commentItem.text = commentJSONObject.getString("text");
                                commentItem.createdUnixTimeInSec = commentJSONObject.getLong("created_time");
                                commentItem.userName = commentJSONObject.getJSONObject("from").getString("username");
                                commentItem.userProfileImageUrl = commentJSONObject.getJSONObject("from").getString("profile_picture");
                                photoItem.commentItems.add(commentItem);
                            }
                        }

                        if (photoJSON.optJSONObject("videos") != null) {
                            JSONObject videosJSONObject = photoJSON.getJSONObject("videos");
                            photoItem.videoUrl = videosJSONObject.getJSONObject("standard_resolution").getString("url");
                        }

                        mPhotoItemArrayList.add(photoItem);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mPhotoArrayAdapter.notifyDataSetChanged();
                mSwipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                    throwable) {
                Log.e(this.toString(), responseString);
                mSwipeContainer.setRefreshing(false);
            }
        });
    }
}
