package com.example.homesweethome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddPage extends AppCompatActivity {

    private int REQUEST_LOAD_CODE = 1;
    private TextView title, date, desc, uploadImg;
    private ImageButton uploadVideo;
    private ArrayList<Cell> imgs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("AddPage a new artifact");
        getSupportActionBar().setSubtitle("Change/delete this subtitle if needed");

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView rv = (RecyclerView) findViewById(R.id.gallery);
        rv.setHasFixedSize(true);
        rv.setItemViewCacheSize(100);
        rv.setDrawingCacheEnabled(true);
        rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(lm);

//        ImageAdapter ia = new ImageAdapter(getApplicationContext(), this.getClass().getName(), imgs, R.id.recycleview_image, R.layout.recycleview_image);
//        rv.setAdapter(ia);

        // main activities
        title = findViewById(R.id.edit_title);
        date = findViewById(R.id.edit_date);
        desc = findViewById(R.id.edit_desc);
        uploadImg = findViewById(R.id.upload_image_button);
        uploadVideo = findViewById(R.id.upload_video_button);

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, REQUEST_LOAD_CODE);
                if (uploadPhoto() == RESULT_OK) {
                    // disable upload
//                    imgs.add(new Cell())
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Fail to upload, try again", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode ==  REQUEST_LOAD_CODE && resultCode == RESULT_OK && data != null) {
//            Uri img = data.getData();
//            uploadImg.setImageURI(img);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int uploadPhoto() {
//        Bitmap bitmap = ((BitmapDrawable) uploadImg.getDrawable()).getBitmap();
//        ByteArrayOutputStream arr = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arr);
//        String encodedImg = Base64.encodeToString(arr.toByteArray(),  Base64.DEFAULT);

        return RESULT_CANCELED;
    }
}
