package com.sagarnileshshah.instagramclient;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    VideoView vvVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        vvVideo = (VideoView) findViewById(R.id.vvVideo);

        String url = getIntent().getStringExtra("url");

        vvVideo.setMediaController(new MediaController(this));
        vvVideo.setOnCompletionListener(this);
        vvVideo.setVideoURI(Uri.parse(url));
        vvVideo.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        this.finish();
    }
}
