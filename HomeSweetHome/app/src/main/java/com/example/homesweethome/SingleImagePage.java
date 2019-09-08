package com.example.homesweethome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SingleImagePage extends AppCompatActivity {

    private String imageSrc;
    private int test_src;
    ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image_page);

        Intent intent = getIntent();
        test_src = intent.getIntExtra("id", R.drawable.img_1);
        img = (ImageView) findViewById(R.id.single_image);
        Glide.with(getApplicationContext()).load(test_src).into(img);
    }
}
