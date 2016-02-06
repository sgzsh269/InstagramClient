package com.sagarnileshshah.instagramclient;

import android.os.Bundle;
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

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<PhotoItem> mPhotoItemArrayList;
    private PhotoArrayAdapter mPhotoArrayAdapter;
    private ListView mLvPhotoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPhotoItemArrayList = new ArrayList<>();
        mPhotoArrayAdapter = new PhotoArrayAdapter(this, mPhotoItemArrayList);
        mLvPhotoItems = (ListView) findViewById(R.id.lvPhotoItems);
        mLvPhotoItems.setAdapter(mPhotoArrayAdapter);

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
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        String url = "https://api.instagram.com/v1/tags/nofilter/media/recent?client_id=" + CLIENT_ID;

        asyncHttpClient.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray photoJSONArray = null;
                try {
                    photoJSONArray = response.getJSONArray("data");

                    for (int i = 0; i < photoJSONArray.length(); i++) {

                        JSONObject photoJSON = photoJSONArray.getJSONObject(i);

                        PhotoItem photoItem = new PhotoItem();
                        photoItem.userName = photoJSON.getJSONObject("user").getString("username");
                        photoItem.userProfileImageUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photoItem.caption = photoJSON.getJSONObject("caption").getString("text");
                        photoItem.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photoItem.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photoItem.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photoItem.createdUnixTimeInSec = photoJSON.getLong("created_time");

                        if (photoJSON.optJSONObject("location") != null) {
                            photoItem.location = photoJSON.getJSONObject("location").getString("name");
                        } else {
                            photoItem.location = null;
                        }

                        mPhotoItemArrayList.add(photoItem);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mPhotoArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), responseString);
            }
        });
    }
}
