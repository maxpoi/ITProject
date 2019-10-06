package com.example.homesweethome.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.HomeSweetHome;
import com.example.homesweethome.R;
import com.example.homesweethome.ViewModels.ArtifactViewModel;

import java.util.List;

public class SingleImagePage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image_page);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra(DataTag.IMAGE_PATH.toString());
        int artifactId = intent.getIntExtra(DataTag.ARTIFACT_ID.toString(), 0);
        final int imageId = intent.getIntExtra(DataTag.IMAGE_ID.toString(), 0);
        final ImageView img = (ImageView) findViewById(R.id.single_image);

        if (imagePath != null) {
            Bitmap imageBitmap = ((HomeSweetHome) getApplication()).getImageProcessor().decodeFileToHighBitmap(imagePath);
            Glide.with(getApplicationContext()).asBitmap().load(imageBitmap).into(img);
        } else {
            ArtifactViewModel.ArtifactViewModelFactory artifactViewModelFactory = new ArtifactViewModel.ArtifactViewModelFactory(getApplication(), artifactId);
            ArtifactViewModel artifactViewModel = new ViewModelProvider(this, artifactViewModelFactory).get(ArtifactViewModel.class);
            artifactViewModel.getArtifactImages().observe(this, new Observer<List<Image>>() {
                @Override
                public void onChanged(List<Image> images) {
                    Image image = images.get(imageId);
                    if (image.getHighImageBitmap() == null) {
                        String imagePath = image.getHighResImagePath();
                        Bitmap imageBitmap = ((HomeSweetHome) getApplication()).getImageProcessor().decodeFileToHighBitmap(imagePath);
                        Glide.with(getApplicationContext()).asBitmap().load(imageBitmap).into(img);
                    } else {
                        Glide.with(getApplicationContext()).asBitmap().load(image.getHighImageBitmap()).into(img);
                    }
                }
            });
        }


    }
}
