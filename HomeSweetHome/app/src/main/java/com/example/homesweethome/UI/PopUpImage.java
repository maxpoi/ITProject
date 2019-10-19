package com.example.homesweethome.UI;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.HomeSweetHome;
import com.example.homesweethome.R;

public class PopUpImage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_screenshot);

        String imagePath = getIntent().getStringExtra(DataTag.SCREEN_SHOT_PATH.toString());
        ImageView imageView = findViewById(R.id.screenshot_image_view);

        Bitmap image = ((HomeSweetHome) getApplication()).getImageProcessor().decodeFileToHighBitmap(imagePath);
        Glide.with(getApplicationContext()).asBitmap().load(image).into(imageView);
    }
}
