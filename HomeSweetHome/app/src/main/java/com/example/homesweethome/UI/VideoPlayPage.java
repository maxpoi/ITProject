package com.example.homesweethome.UI;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GestureDetectorCompat;

import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.R;

import java.io.File;

public class VideoPlayPage extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat detector;

    int videoWidth;
    int videoHeight;

    VideoView video;
    MediaController mc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_page);

        String videoPath = getIntent().getStringExtra(DataTag.ARTIFACT_VIDEO.toString());
        getVideoAspectRatio(videoPath);

        video = findViewById(R.id.video_play_page);
        if (!isVideoLandscaped())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        video.setVideoPath(videoPath);
        video.start();

        mc = new MediaController(this);
        video.setMediaController(mc);

        detector = new GestureDetectorCompat(this,this);
        detector.setOnDoubleTapListener(this);
    }

    private void getVideoAspectRatio(String path) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this, Uri.parse(path));
        String height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        videoWidth = Integer.parseInt(width);
        videoHeight = Integer.parseInt(height);
    }

    private boolean isVideoLandscaped() {
        return videoWidth > videoHeight;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.detector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        ColorDrawable backgroundColor = (ColorDrawable) findViewById(R.id.video_play_background).getBackground();
        int color = backgroundColor.getColor() == Color.BLACK ? Color.WHITE : Color.BLACK;
        findViewById(R.id.video_play_background).setBackgroundColor(color);

        mc.show(3000);
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
