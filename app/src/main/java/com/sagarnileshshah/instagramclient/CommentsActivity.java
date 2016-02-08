package com.sagarnileshshah.instagramclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CommentsActivity extends AppCompatActivity {


    private static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<CommentItem> mCommentItemArrayList;
    private CommentArrayAdapter mCommentArrayAdapter;
    private ListView mLvCommentItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCommentItemArrayList = new ArrayList<>();
        mCommentArrayAdapter = new CommentArrayAdapter(this, mCommentItemArrayList);

        mLvCommentItems = (ListView) findViewById(R.id.lvCommentItems);
        mLvCommentItems.setAdapter(mCommentArrayAdapter);

        String mediaId = getIntent().getStringExtra("mediaId");

        fetchComments(mediaId);
    }

    private void fetchComments(String mediaId) {
        AsyncHttpClient commentAsyncHttpClient = new AsyncHttpClient();

        String commentsUrl = "https://api.instagram.com/v1/media/" + mediaId + "/comments?client_id=" + CLIENT_ID;

        commentAsyncHttpClient.get(commentsUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject commentJSONObject = data.getJSONObject(i);
                        CommentItem commentItem = new CommentItem();
                        commentItem.text = commentJSONObject.getString("text");
                        commentItem.createdUnixTimeInSec = commentJSONObject.getLong("created_time");
                        commentItem.userName = commentJSONObject.getJSONObject("from").getString("username");
                        commentItem.userProfileImageUrl = commentJSONObject.getJSONObject("from").getString("profile_picture");
                        mCommentItemArrayList.add(commentItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mCommentArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(this.toString(), responseString);
            }
        });
    }
}
