package com.example.homesweethome.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.HelperClasses.HomeSweetHome;
import com.example.homesweethome.HelperClasses.ImageAdapter;
import com.example.homesweethome.HelperClasses.ImageProcessor;
import com.example.homesweethome.R;
import com.example.homesweethome.ViewModels.ArtifactViewModel;

import java.util.List;

public class EditPage extends AppCompatActivity {

    private int REQUEST_LOAD_CODE = 1;
    private int REQUEST_LOAD_VIDEO_CODE = 2;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Artifact artifact;
    private ArtifactViewModel artifactViewModel;
    private List<Image> imageList;

    private ImageAdapter imageAdapter;
    private boolean saveButtonPressed = false;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit this artifact");
        getSupportActionBar().setSubtitle("Field with a '*' must be filled");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(lm);

        imageAdapter = new ImageAdapter(getApplicationContext());
        recyclerView.setAdapter(imageAdapter);

        final int artifactId = getIntent().getIntExtra("artifactId", 0);
        ArtifactViewModel.ArtifactViewModelFactory artifactViewModelFactory = new ArtifactViewModel.ArtifactViewModelFactory(getApplication(), artifactId);
        artifactViewModel = new ViewModelProvider(this, artifactViewModelFactory).get(ArtifactViewModel.class);
        artifactViewModel.getArtifactImages().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                imageAdapter.setImages(images);
            }
        });

        final EditText title = findViewById(R.id.edit_title);
        final EditText date = findViewById(R.id.edit_date);
        final EditText desc = findViewById(R.id.edit_desc);
        final VideoView videoView = findViewById(R.id.edit_page_video);
        videoView.setMediaController(new MediaController(this));

        artifactViewModel.getArtifact().observe(this, new Observer<Artifact>() {
            @Override
            public void onChanged(Artifact artifact) {
                title.setText(artifact.getTitle());
                date.setText(artifact.getDate());
                desc.setText(artifact.getDesc());

                if (artifact.getVideo() != null) {
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoPath(artifact.getVideo());
                    findViewById(R.id.edit_video_background).setVisibility(View.INVISIBLE);
                }
            }
        });

        // perform upload action
        Button uploadImageButton, uploadVideoButton;
        uploadImageButton = findViewById(R.id.upload_image_button);
        uploadVideoButton = findViewById(R.id.upload_video_button);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
//                String[] mimeTypes = {"image/jpeg", "image/png"}; // force to only upload those 2 types
                startActivityForResult(intent, REQUEST_LOAD_CODE);
            }
        });

        uploadVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
//                String[] mimeTypes = {"video/mp4, "video/mov"}; // force to only upload those 2 types
                startActivityForResult(intent, REQUEST_LOAD_VIDEO_CODE);
            }
        });


        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkField()) { return; }
                saveButtonPressed = true;
                saveArtifact(artifactId);
                openMain();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==  REQUEST_LOAD_CODE && resultCode == RESULT_OK && data != null) {
            uriImage = data.getData();
            if (checkPermissions()) {
                createImage();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 0);
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Fail to upload, try again", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean checkPermissions() {

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allAllowed = true;
        switch (requestCode) {
            case 0:
                for (int res : grantResults) {
                    allAllowed = allAllowed && (res == PackageManager.PERMISSION_GRANTED);
                }
        }

        if (allAllowed) {
            createImage();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please give permissions to access.", Toast.LENGTH_SHORT);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (!saveButtonPressed) {
//            System.out.println("+++++++++++++");
//            System.out.println("DELETEEEEEEE");
//            for(Image image : imageList) {
//                artifactViewModel.deleteImage(image);
//            }
//            artifactViewModel.deleteArtifact(artifact);
//        }
    }

    private void openMain() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    private void saveArtifact(int artifactId) {
        // set cell context
        String title, date, desc;
        title = ((EditText)findViewById(R.id.edit_title)).getText().toString();
        date = ((EditText)findViewById(R.id.edit_date)).getText().toString();
        desc = ((EditText)findViewById(R.id.edit_desc)).getText().toString();

        artifact.setId(artifactId);
        artifact.setTitle(title);
        artifact.setDate(date);
        artifact.setDesc(desc);
        artifactViewModel.addArtifact(artifact);
        for (Image image : imageList) artifactViewModel.addImage(image);
        ((HomeSweetHome)getApplication()).getImageProcessor().saveImageListToLocal(imageList);
    }

    private void createImage() {
        String folderPath = ImageProcessor.PARENT_FOLDER_PATH + artifact.getId();

        int imageId =  imageList.size();
        Image image = new Image(imageId);
        image.setArtifactId(artifact.getId());

        // https://androidclarified.com/pick-image-gallery-camera-android/
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        // Get the cursor
        Cursor cursor = getContentResolver().query(uriImage, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        //Get the column index of MediaStore.Images.Media.DATA
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //Gets the String value in the column
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        // Set the Image in ImageView after decoding the String

        image.setLowResImagePath(folderPath + ImageProcessor.LOW_RES_IMAGE_FOLDER_NAME + imageId + ImageProcessor.IMAGE_TYPE);
        image.setMediumResImagePath(folderPath + ImageProcessor.MEDIUM_RES_IMAGE_FOLDER_NAME + imageId + ImageProcessor.IMAGE_TYPE);
        image.setHighResImagePath(folderPath + ImageProcessor.HIGH_RES_IMAGE_FOLDER_NAME + imageId + ImageProcessor.IMAGE_TYPE);

        image.setLowImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToLowBitmap(filePath));
        image.setMediumImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToMediumBitmap(filePath));
        image.setHighImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToHighBitmap(filePath));

        imageList.add(image);
        imageAdapter.setImages(imageList);

        if (artifact.getCoverImagePath() == null) {
            artifact.setCoverImagePath(image.getLowResImagePath());
        }
    }

    private boolean checkField() {
        if (imageList.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Must upload at least 1 photo!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return true;
    }
}
