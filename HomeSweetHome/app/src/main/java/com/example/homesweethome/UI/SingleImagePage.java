package com.example.homesweethome.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.bumptech.glide.Glide;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.HomeSweetHome;
import com.example.homesweethome.R;

public class SingleImagePage extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat detector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image_page);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra(DataTag.IMAGE_PATH.toString());
        final ImageView img = (ImageView) findViewById(R.id.single_image);

        if (imagePath != null) {
            Bitmap imageBitmap = ((HomeSweetHome) getApplication()).getImageProcessor().decodeFileToHighBitmap(imagePath);
            Glide.with(getApplicationContext()).asBitmap().load(imageBitmap).into(img);
        } else {
            Toast toast = Toast.makeText(this, "No Image found in this phone", Toast.LENGTH_SHORT);
            toast.show();
        }

        detector = new GestureDetectorCompat(this,this);
        detector.setOnDoubleTapListener(this);
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
        ColorDrawable backgroundColor = (ColorDrawable) findViewById(R.id.single_image_background).getBackground();
        int color = backgroundColor.getColor() == Color.BLACK ? Color.WHITE : Color.BLACK;
        findViewById(R.id.single_image_background).setBackgroundColor(color);
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
