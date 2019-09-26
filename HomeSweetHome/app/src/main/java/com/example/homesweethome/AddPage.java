package com.example.homesweethome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AddPage extends AppCompatActivity {

    private int REQUEST_LOAD_CODE = 1;

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private Cell cell = new Cell();

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
                saveToLocal(cell);
                finish();
                openMain();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(lm);

        imageAdapter = new ImageAdapter(getApplicationContext(), this.getClass().getName(), cell.getImages(), R.id.recycleview_image, R.layout.recycleview_image);
        imageAdapter.setImageResolution(-1);
        recyclerView.setAdapter(imageAdapter);

        // set cell context
        String title, date, desc;
        title = ((EditText)findViewById(R.id.edit_title)).getText().toString();
        date = ((EditText)findViewById(R.id.edit_date)).getText().toString();
        desc = ((EditText)findViewById(R.id.edit_desc)).getText().toString();
        this.cell.setTitle(title);
        this.cell.setTitle(date);
        this.cell.setTitle(desc);

        // perform upload action
        Button uploadImageButton;
        ImageButton uploadVideoButton;
        uploadImageButton = findViewById(R.id.upload_image_button);
        uploadVideoButton = findViewById(R.id.upload_video_button);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, REQUEST_LOAD_CODE);
            }
        });

        uploadVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==  REQUEST_LOAD_CODE && resultCode == RESULT_OK && data != null) {
            Uri img = data.getData();
//            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), img);
//            Bitmap bitmap = ImageDecoder.decodeBitmap(source);
            // to do. bitmap might be null, because image too large.
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), img);
            } catch (Exception e) { e.printStackTrace(); }

            this.cell.addImage(new Image(null, ImageProcessor.getInstance().encodeBitmapByte(bitmap), null));
            this.imageAdapter.setImages(this.cell.getImages());
            this.recyclerView.setAdapter(imageAdapter);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Fail to upload, try again", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

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

    private void openMain() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    private File createFolder(File parent, String name) {
        File folder = new File(parent, name);
        if (folder.mkdir()) {
            return folder;
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Don't have permission to write.", Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }
    }

    private void saveToLocal(Cell cell) {
        int number = UserCache.getInstance().getCells().size();

        File parent = getFilesDir();
        File child = createFolder(parent, Integer.toString(number));
        if (child == null) {
            return ;
        }

        writeFile(child, "title", cell.getTitle());
        writeFile(child, "date", cell.getDate());
        writeFile(child, "desc", cell.getDesc());

        // write images
        // now assume only 1 resolution will be used for the entire session
        File lowImageFolder = createFolder(child, "low_image");
        File mediumImageFolder = createFolder(child, "medium_image");
        File highImageFolder = createFolder(child, "high_image");
        for (int i=0; i<cell.getImages().size(); i++) {
            Image image = cell.getImages().get(i);
            String id = Integer.toString(i);
//            writeFile(lowImageFolder, id, image.getLowImageByte());
            writeFile(mediumImageFolder, id, image.getMediumImageByte());
//            writeFile(highImageFolder, id, image.getHighImageByte());
        }
    }

    private void writeFile(File parent, String filename, String context) {
        File file = new File(parent, filename);

        if (context == null) {
            return ;
        }

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(context.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(File parent, String filename, byte[] context) {
        File file = new File(parent, filename);

        if (context == null) {
            return ;
        }

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(context);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
