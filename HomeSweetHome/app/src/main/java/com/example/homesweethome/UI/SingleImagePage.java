package com.example.homesweethome.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.homesweethome.HelperClasses.ImageProcessor;
import com.example.homesweethome.R;

public class SingleImagePage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image_page);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");

        byte[] image = ImageProcessor.getInstance().readFileByte(imagePath);
        ImageView img = (ImageView) findViewById(R.id.single_image);
        Glide.with(getApplicationContext()).asBitmap().load(ImageProcessor.getInstance().restoreImage(image)).into(img);
    }
}
