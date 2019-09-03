package com.example.homesweethome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Grid_image extends AppCompatActivity{

    private String src;
    private int test_src;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image);

        Intent intent = getIntent();
        test_src = intent.getIntExtra("id", R.drawable.img_1);
        img = (ImageView) findViewById(R.id.single_image);
        Glide.with(getApplicationContext()).load(test_src).into(img);
    }
}
