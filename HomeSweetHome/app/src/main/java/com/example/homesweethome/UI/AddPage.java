package com.example.homesweethome.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.HomeSweetHome;
import com.example.homesweethome.HelperClasses.ImageAdapter;
import com.example.homesweethome.HelperClasses.ImageProcessor;
import com.example.homesweethome.R;
import com.example.homesweethome.ViewModels.ArtifactViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddPage extends AppCompatActivity {

    private final int REQUEST_LOAD_IMAGE_CODE = 1;
    private final int REQUEST_LOAD_VIDEO_CODE = 2;
    private final int REQUEST_INPUT_CODE = 3;
    private final int FIRST_ARTIFACT_ID = 0;

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean enableDeletion = true;
    private String tag;

    private Artifact artifact;
    private int artifactId;
    private List<Image> newImages;
    private int nextImageId;

    private ArtifactViewModel artifactViewModel;
    private ImageAdapter imageAdapter;
    private boolean saveButtonPressed = false;
    private Uri uriImage, uriVideo;

    private ImageView videoCover;
    private TextView desc;
    private EditText title;
    private EditText dateYear;
    private EditText dateMonth;
    private EditText dateDay;

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

        imageAdapter = new ImageAdapter(getApplicationContext(), enableDeletion);
        recyclerView.setAdapter(imageAdapter);

        Intent intent = getIntent();
        tag = intent.getStringExtra(DataTag.TAG.toString());
        artifactId = intent.getIntExtra(DataTag.ARTIFACT_ID.toString(), FIRST_ARTIFACT_ID);

        artifact = new Artifact(artifactId);
        newImages = new ArrayList<>();
        nextImageId = 0;

        ArtifactViewModel.ArtifactViewModelFactory artifactViewModelFactory = new ArtifactViewModel.ArtifactViewModelFactory(getApplication(), artifactId);
        artifactViewModel = new ViewModelProvider(this, artifactViewModelFactory).get(ArtifactViewModel.class);

        if (tag.equals(DataTag.ADD.toString())) {
            imageAdapter.setImages(null);
        } else {
            nextImageId += artifactViewModel.getLastImageId(artifactId) + 1;
            artifactViewModel.getArtifactImages().observe(this, new Observer<List<Image>>() {
                @Override
                public void onChanged(List<Image> images) {
                    imageAdapter.setImages(images);
                }
            });
        }

//        ImageButton deleteImageButton = findViewById(R.id.delete_image_button);
//        deleteImageButton.setVisibility(View.VISIBLE);
//        deleteImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast toast = Toast.makeText(getApplicationContext(), "Please give permissions to access.", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        });

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

        title = findViewById(R.id.edit_title);
        desc = findViewById(R.id.edit_desc);
        dateYear = findViewById(R.id.edit_date_year);
        dateMonth = findViewById(R.id.edit_date_month);
        dateDay = findViewById(R.id.edit_date_day);
        videoCover = findViewById(R.id.add_page_video);

        if (tag.equals(DataTag.EDIT.toString())) {
            artifactViewModel.getArtifact().observe(this, new Observer<Artifact>() {
                @Override
                public void onChanged(Artifact artifact) {
                    if (artifact != null) {
                        title.setText(artifact.getTitle());
                        desc.setText(artifact.getDesc());

                        String date = artifact.getDate();
                        if (date != null) {
                            String[] dates = date.split("/");
                            dateYear.setText(dates[0]);
                            dateMonth.setText(dates[1]);
                            dateDay.setText(dates[2]);
                        }

                        if (artifact.getVideo() != null) {
                            videoCover.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(artifact.getVideo()).into(videoCover);
                            findViewById(R.id.add_page_video_background).setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PopUpEditText.class);
                intent.putExtra(DataTag.INPUT_TEXT.toString(), desc.getText().toString());
                startActivityForResult(intent, REQUEST_INPUT_CODE);
            }
        });

        videoCover.setOnClickListener(new View.OnClickListener() {
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
        intent.putExtra(DataTag.ARTIFACT_ID.toString(), artifactId);
        startActivity(intent);
    }

    private void saveArtifact() {
        // set cell context
        String date = dateYear.getText().toString() + "/" + dateMonth.getText().toString() + "/" + dateDay.getText().toString();
        newImages = imageAdapter.getImages();

        artifact.setTitle(title.getText().toString());
        artifact.setDesc(desc.getText().toString());
        artifact.setDate(date);
        artifact.setCoverImagePath(newImages.get(0).getLowResImagePath());

        artifactViewModel.addArtifact(artifact);

        artifactViewModel.deleteArtifactImages(artifactId);
        ((HomeSweetHome)getApplication()).getImageProcessor().deleteImageListFromLocal(artifactId);
        for(Image image : newImages) artifactViewModel.addImage(image);
//        for (Image image : newImages) artifactViewModel.addImage(image);

        ((HomeSweetHome)getApplication()).getImageProcessor().saveImageListToLocal(newImages);
        ((HomeSweetHome)getApplication()).getImageProcessor().saveVideoToLocal(artifact.getId(), artifact.getVideo());
    }

    private void createImage() {
        String folderPath = ImageProcessor.PARENT_FOLDER_PATH + artifactId;

        int imageId = nextImageId;
//        if (tag.equals(DataTag.ADD.toString())) {
//            imageId = artifactViewModel.getLastImageId(artifactId);
//        }
        final Image image = new Image(imageId);
        image.setArtifactId(artifactId);

        image.setLowResImagePath(folderPath + ImageProcessor.LOW_RES_IMAGE_FOLDER_NAME + imageId + ImageProcessor.IMAGE_TYPE);
        image.setMediumResImagePath(folderPath + ImageProcessor.MEDIUM_RES_IMAGE_FOLDER_NAME + imageId + ImageProcessor.IMAGE_TYPE);
        image.setHighResImagePath(folderPath + ImageProcessor.HIGH_RES_IMAGE_FOLDER_NAME + imageId + ImageProcessor.IMAGE_TYPE);

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

        image.setLowImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToLowBitmap(filePath));
        image.setMediumImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToMediumBitmap(filePath));
        image.setHighImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToHighBitmap(filePath));

        image.setOriginalPath(filePath);

        newImages.add(image);
        imageAdapter.addImage(image);
        nextImageId += 1;
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

        Glide.with(this).load(uriVideo).into(videoCover);
        videoCover.setVisibility(View.VISIBLE);
        findViewById(R.id.add_page_video_background).setVisibility(View.INVISIBLE);
    }

    private boolean checkField() {
        boolean allSatisfied;
        boolean requestedFocus = false;

        allSatisfied = checkTitle() && checkDate() && checkDescription() && checkPhotos()  && checkVideo();

        return allSatisfied;
    }

    private boolean checkTitle() {
        if (title.getText() == null || title.getText().toString().trim().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Must enter a Title!", Toast.LENGTH_SHORT);
            toast.show();

            final EditText editTitleFrame = findViewById(R.id.edit_title);
            editTitleFrame.setActivated(true);

            final TextView titleTitle = findViewById(R.id.add_page_title_title);
            titleTitle.setFocusableInTouchMode(true);
            titleTitle.requestFocus();
            titleTitle.setFocusableInTouchMode(false);
            return false;
        }
        return true;
    }

    private boolean checkDate() {
        boolean validDate = true;

        if (!isValidDate()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Date is wrong!", Toast.LENGTH_SHORT);
            toast.show();

            TextView dateTitle = findViewById(R.id.add_page_date_title);
            dateTitle.setFocusableInTouchMode(true);
            dateTitle.requestFocus();
            dateTitle.setFocusableInTouchMode(false);

            validDate = false;
        }

        // will be activated if not valid
        findViewById(R.id.edit_date_year).setActivated(!isValidYear());
        findViewById(R.id.edit_date_month).setActivated(!isValidMonth());
        findViewById(R.id.edit_date_day).setActivated(!isValidDay());
        findViewById(R.id.edit_date_day).setActivated(isFutureDate());
        return validDate;
    }

    private boolean isValidYear() {
        return dateYear.getText() != null
                && dateYear.getText().toString().matches(DataTag.DATE_PATTERN.toString())
                && Integer.parseInt(dateYear.getText().toString()) >= Calendar.YEAR
                && Integer.parseInt(dateYear.getText().toString()) <= Calendar.getInstance().get(Calendar.YEAR);
    }

    private boolean isValidMonth() {
        return dateMonth.getText() != null
                && dateMonth.getText().toString().matches(DataTag.DATE_PATTERN.toString())
                && Integer.parseInt(dateMonth.getText().toString()) <= Calendar.DECEMBER+1
                && Integer.parseInt(dateMonth.getText().toString()) > 0;
    }

    private boolean isValidDay() {
        return dateDay.getText() != null
                && dateDay.getText().toString().matches(DataTag.DATE_PATTERN.toString())
                && Integer.parseInt(dateDay.getText().toString()) <= Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
                &&  Integer.parseInt(dateDay.getText().toString()) > 0;
    }

    private boolean isFutureDate() {
        if (dateYear.getText() != null && dateMonth.getText() != null && dateDay.getText() != null) {
            if (Integer.parseInt(dateYear.getText().toString()) > Calendar.getInstance().get(Calendar.YEAR))
                return true;

            if (Integer.parseInt(dateYear.getText().toString()) == Calendar.getInstance().get(Calendar.YEAR)) {
                if (Integer.parseInt(dateMonth.getText().toString()) > (Calendar.getInstance().get(Calendar.MONTH) + 1))
                    return true;

                if (Integer.parseInt(dateMonth.getText().toString()) == (Calendar.getInstance().get(Calendar.MONTH) + 1)) {
                    return (Integer.parseInt(dateDay.getText().toString()) > Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                }
            }
        }
        return false;
    }

    private boolean isValidDate() {
        return isValidYear() && isValidMonth() && isValidDay() && !isFutureDate();
    }

    private boolean checkPhotos() {
        if (newImages.size() + artifactViewModel.getArtifactStaticImages(artifact.getId()).size() == 0) {
            final TextView recyclerViewFrame = findViewById(R.id.recyclerview_frame);
            recyclerViewFrame.setActivated(true);

            TextView photoTitle = findViewById(R.id.add_page_photo_title);
            photoTitle.setFocusableInTouchMode(true);
            photoTitle.requestFocus();
            photoTitle.setFocusableInTouchMode(false);

            Toast toast = Toast.makeText(getApplicationContext(), "Must upload at least 1 photo!", Toast.LENGTH_SHORT);
            toast.show();
//
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    recyclerViewFrame.setActivated(false);
//                }
//            }, 500);
            return false;
        }
        return true;
    }

    private boolean checkDescription() {
        return true;
    }

    private boolean checkVideo() {
        return true;
    }

    public void deleteImage(Image image, boolean fromArtifact) {
        boolean deleted = false;

        for(Image img : newImages) {
            if (img.equals(image)) {
                newImages.remove(img);
                deleted = true;
                break;
            }
        }

        if (!deleted)
            artifactViewModel.deleteImage(image);
    }

}
