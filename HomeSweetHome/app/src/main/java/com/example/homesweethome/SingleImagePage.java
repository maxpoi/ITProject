package com.example.homesweethome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SingleImagePage extends AppCompatActivity {

    private String image;
    ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image_page);

        Intent intent = getIntent();
        int cellPos = intent.getIntExtra("cell", 0);
        int imagePos = intent.getIntExtra("image", 0);
        image = UserCache.getInstance().getImagesByCell(cellPos).get(imagePos).getMediumImage();
        img = (ImageView) findViewById(R.id.single_image);
        Glide.with(getApplicationContext()).asBitmap().load(ImageProcessor.getInstance().restoreImage(image)).into(img);
    }
}
