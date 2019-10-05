package com.example.homesweethome.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.HelperClasses.ImageProcessor;
import com.example.homesweethome.R;
import com.example.homesweethome.ViewModels.ArtifactViewModel;

import java.util.List;

public class SingleImagePage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image_page);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");
        int artifactId = intent.getIntExtra("artifactId", 0);
        final int imageId = intent.getIntExtra("imageId", 0);
        final ImageView img = (ImageView) findViewById(R.id.single_image);

        ArtifactViewModel.ArtifactViewModelFactory artifactViewModelFactory = new ArtifactViewModel.ArtifactViewModelFactory(getApplication(), artifactId);
        ArtifactViewModel artifactViewModel = new ViewModelProvider(this, artifactViewModelFactory).get(ArtifactViewModel.class);
        artifactViewModel.getArtifactImages().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                Image image = images.get(imageId);
                if (image.getHighImageBitmap() == null) {
                    String imagePath = image.getHighResImagePath();
                    byte[] imageByte = ImageProcessor.getInstance().readFileByte(imagePath);
                    Glide.with(getApplicationContext()).asBitmap().load(ImageProcessor.getInstance().restoreImage(imageByte)).into(img);
                } else {
                    Glide.with(getApplicationContext()).asBitmap().load(image.getHighImageBitmap()).into(img);
                }
            }
        });


    }
}
