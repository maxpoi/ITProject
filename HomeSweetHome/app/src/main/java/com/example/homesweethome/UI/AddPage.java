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
import android.widget.MediaController;
import android.widget.TextView;
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
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.HomeSweetHome;
import com.example.homesweethome.HelperClasses.ImageAdapter;
import com.example.homesweethome.HelperClasses.ImageProcessor;
import com.example.homesweethome.R;
import com.example.homesweethome.ViewModels.ArtifactViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddPage extends AppCompatActivity {

    private final int REQUEST_LOAD_IMAGE_CODE = 1;
    private final int REQUEST_LOAD_VIDEO_CODE = 2;
    private final int REQUEST_INPUT_CODE = 3;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Artifact artifact;
    private ArtifactViewModel artifactViewModel;
    private List<Image> newImages;

    private ImageAdapter imageAdapter;
    private boolean saveButtonPressed = false;
    private Uri uriImage, uriVideo;

    private VideoView videoView;
    private TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add a new Artifact");
        getSupportActionBar().setSubtitle("Fields with a '*' must be filled");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(lm);

        imageAdapter = new ImageAdapter(getApplicationContext());
        recyclerView.setAdapter(imageAdapter);

        Intent intent = getIntent();
        final String tag = intent.getStringExtra(DataTag.TAG.toString());
        artifact = new Artifact(intent.getIntExtra(DataTag.ARTIFACT_ID.toString(), 0));
        newImages = new ArrayList<>();

        ArtifactViewModel.ArtifactViewModelFactory artifactViewModelFactory = new ArtifactViewModel.ArtifactViewModelFactory(getApplication(), artifact.getId());
        artifactViewModel = new ViewModelProvider(this, artifactViewModelFactory).get(ArtifactViewModel.class);
        artifactViewModel.getArtifactImages().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                imageAdapter.setImages(images);
            }
        });

        if (artifactViewModel.getStaticArtifact() != null) {
            artifact = new Artifact(artifactViewModel.getStaticArtifact());
        }

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
                startActivityForResult(intent, REQUEST_LOAD_IMAGE_CODE);
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

        final EditText title = findViewById(R.id.edit_title);
        final EditText date = findViewById(R.id.edit_date);
        desc = findViewById(R.id.edit_desc);
        videoView = findViewById(R.id.add_page_video);

        artifactViewModel.getArtifact().observe(this, new Observer<Artifact>() {
            @Override
            public void onChanged(Artifact artifact) {
                if (artifact != null) {
                    title.setText(artifact.getTitle());
                    date.setText(artifact.getDate());
                    desc.setText(artifact.getDesc());

                    if (artifact.getVideo() != null) {
                        videoView.setVisibility(View.VISIBLE);
                        videoView.setVideoPath(artifact.getVideo());
                        videoView.seekTo(1);
                        findViewById(R.id.add_page_video_background).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PopUpEditText.class);
                intent.putExtra(DataTag.INPUT_TEXT.toString(), desc.getText().toString());
                startActivityForResult(intent, REQUEST_INPUT_CODE);
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoPlayPage.class);
                intent.putExtra(DataTag.ARTIFACT_VIDEO.toString(), artifact.getVideo());
                startActivity(intent);
            }
        });

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkField()) { return; }
                saveButtonPressed = true;
                saveArtifact();

                if (tag.equals(DataTag.ADD.toString())) {
                    openMain();
                } else {
                    openSingleArtifactPage();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_LOAD_IMAGE_CODE:
                    uriImage = data.getData();
                    if (checkPermissions()) {
                        createImage();
                    } else {
                        ActivityCompat.requestPermissions(this, permissions, REQUEST_LOAD_IMAGE_CODE);
                    }
                    break;

                case REQUEST_LOAD_VIDEO_CODE:
                    uriVideo = data.getData();
                    if (checkPermissions()) {
                        uploadVideo();
                    } else {
                        ActivityCompat.requestPermissions(this, permissions, REQUEST_LOAD_VIDEO_CODE);
                    }
                    break;

                case REQUEST_INPUT_CODE:
                    String input = data.getStringExtra(DataTag.INPUT_TEXT.toString());
                    desc.setText(input);
                    break;
                default:
                    Toast toast = Toast.makeText(getApplicationContext(), "Fail to upload, try again", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
            }
        }
    }

    private boolean checkPermissions() {

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allAllowed = true;
        switch (requestCode) {
            case REQUEST_LOAD_IMAGE_CODE:
                for (int res : grantResults)
                    allAllowed = allAllowed && (res == PackageManager.PERMISSION_GRANTED);

                if (allAllowed)
                    createImage();

                break;
            case  REQUEST_LOAD_VIDEO_CODE:
                for (int res : grantResults)
                    allAllowed = allAllowed && (res == PackageManager.PERMISSION_GRANTED);

                if (allAllowed)
                    uploadVideo();

                break;
            default:
                break;
        }

        if (!allAllowed) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please give permissions to access.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openMain();
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
//            for(Image image : newImages) {
//                artifactViewModel.deleteImage(image);
//            }
//            artifactViewModel.deleteArtifact(artifact);
//        }
    }

    private void openMain() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    private void openSingleArtifactPage() {
        Intent intent;
        intent = new Intent(this, SingleArtifactPage.class);
        intent.putExtra(DataTag.ARTIFACT_ID.toString(), artifact.getId());
        startActivity(intent);
    }

    private void saveArtifact() {
        // set cell context
        String title, date, desc;
        title = ((EditText)findViewById(R.id.edit_title)).getText().toString();
        date = ((EditText)findViewById(R.id.edit_date)).getText().toString();
        desc = ((TextView)findViewById(R.id.edit_desc)).getText().toString();

        artifact.setTitle(title);
        artifact.setDate(date);
        artifact.setDesc(desc);
        artifactViewModel.addArtifact(artifact);

        for (Image image : newImages) artifactViewModel.addImage(image);
        ((HomeSweetHome)getApplication()).getImageProcessor().saveImageListToLocal(newImages);
        ((HomeSweetHome)getApplication()).getImageProcessor().saveVideoToLocal(artifact.getId(), artifact.getVideo());
    }

    private void createImage() {
        final String folderPath = ImageProcessor.PARENT_FOLDER_PATH + artifact.getId();

        int imageId=  artifactViewModel.getImagesLen() + newImages.size();
        final Image image = new Image(imageId);
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

//        String filePath = uriImage.getPath();
        image.setLowResImagePath(folderPath + ImageProcessor.LOW_RES_IMAGE_FOLDER_NAME + imageId + ImageProcessor.IMAGE_TYPE);
        image.setMediumResImagePath(folderPath + ImageProcessor.MEDIUM_RES_IMAGE_FOLDER_NAME + imageId + ImageProcessor.IMAGE_TYPE);
        image.setHighResImagePath(folderPath + ImageProcessor.HIGH_RES_IMAGE_FOLDER_NAME + imageId + ImageProcessor.IMAGE_TYPE);

        image.setLowImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToLowBitmap(filePath));
        image.setMediumImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToMediumBitmap(filePath));
        image.setHighImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToHighBitmap(filePath));

        image.setOriginalPath(filePath);

        if (artifact.getCoverImagePath() == null) {
            artifact.setCoverImagePath(image.getLowResImagePath());
        }

        newImages.add(image);
        imageAdapter.addImage(image);
    }

    private void uploadVideo() {
        // https://androidclarified.com/pick-image-gallery-camera-android/
        String[] filePathColumn = { MediaStore.Video.Media.DATA };
        // Get the cursor
        Cursor cursor = getContentResolver().query(uriVideo, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        //Get the column index of MediaStore.Images.Media.DATA
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //Gets the String value in the column
        String sourcePath = cursor.getString(columnIndex);
        cursor.close();

        if(sourcePath == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_SHORT);
            toast.show();
            return ;
        }
        artifact.setVideo(sourcePath);
        videoView.setVideoURI(uriVideo);
        videoView.setVisibility(View.VISIBLE);
        findViewById(R.id.add_page_video_background).setVisibility(View.INVISIBLE);
    }

    private boolean checkField() {
        if (newImages.size() + artifactViewModel.getImagesLen() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Must upload at least 1 photo!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return true;
    }
}
