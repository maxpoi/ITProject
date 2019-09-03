package com.example.homesweethome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SingleImage extends AppCompatActivity{

    private String src;
    private int test_src;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add a new artifact");
        getSupportActionBar().setSubtitle("Change/delete this subtitle if needed");

        Intent intent = getIntent();
        test_src = intent.getIntExtra("id", R.drawable.img_1);
        img = (ImageView) findViewById(R.id.upload_image_button);
        Glide.with(getApplicationContext()).load(test_src).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), com.example.homesweethome.OnlyImage.class);
                i.putExtra("id", test_src);
                startActivity(i);
            }
        });

        final FloatingActionButton edit_button = (FloatingActionButton) findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), com.example.homesweethome.Add.class);
                startActivity(i);
                finish();
            }
        });
    }
}
