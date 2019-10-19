package com.example.homesweethome.DeprecatedClasses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.homesweethome.AppDataBase.Entities.Artifact;
import com.example.homesweethome.AppDataBase.Entities.Image;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.HomeSweetHome;
import com.example.homesweethome.HelperClasses.ImageProcessor;
import com.example.homesweethome.R;
import com.example.homesweethome.UI.PopUpImage;
import com.example.homesweethome.ViewModels.ArtifactViewModel;

import java.util.ArrayList;
import java.util.List;

public class ScreenshotGenerator extends AppCompatActivity {

    TextView title, date, desc;
    ImageView image;

    private final int MAX_TEXT_CHAR = 1500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screenshot_generate_page);

        Intent intent = getIntent();
        int artifactId = intent.getIntExtra(DataTag.ARTIFACT_ID.toString(), 0);
        int imageId = intent.getIntExtra(DataTag.IMAGE_ID.toString(), 0);

        ArtifactViewModel.ArtifactViewModelFactory artifactViewModelFactory = new ArtifactViewModel.ArtifactViewModelFactory(getApplication(), artifactId);
        ArtifactViewModel artifactViewModel = new ViewModelProvider(this, artifactViewModelFactory).get(ArtifactViewModel.class);
        Artifact artifact = artifactViewModel.getStaticArtifact(artifactId);

        title = findViewById(R.id.screenshot_title);
        date = findViewById(R.id.screenshot_date);
        desc = findViewById(R.id.screenshot_text);
        image = findViewById(R.id.screenshot_image);

        title.setText(artifact.getTitle());
        date.setText(artifact.getDate());
        desc.setText(artifact.getDesc());

        String imagePath = artifactViewModel.getArtifactStaticImages(artifactId).get(imageId).getMediumResImagePath();
        Bitmap imageBitmap = ((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToMediumBitmap(imagePath);
        Glide.with(this).load(imageBitmap).into(image);

        ScrollView scrollView = findViewById(R.id.screenshot_scroll_view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                generateLongScreenShot();
            }
        });
    }

    private void generateLongScreenShot() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Generating screen shot...");
        dialog.show();

        ScrollView scrollView = findViewById(R.id.screenshot_scroll_view);
        Bitmap screenShotBitmap = Bitmap.createBitmap(scrollView.getHeight(), scrollView.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenShotBitmap);
        Drawable bgDrawable = scrollView.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.GRAY);
        scrollView.draw(canvas);

        String path = ImageProcessor.PARENT_FOLDER_PATH + ImageProcessor.SCREENSHOT_TEMP_FILE;
        Image screenshot = new Image(path, path, path);
        screenshot.setHighImageBitmap(screenShotBitmap);

        List<Image> tempList = new ArrayList<>();
        tempList.add(screenshot);
        ((HomeSweetHome) getApplication()).getImageProcessor().saveImageListToLocal(tempList);

        dialog.dismiss();
        Toast.makeText(this, "Successfully generate screen shot!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), PopUpImage.class);
        intent.putExtra(DataTag.SCREEN_SHOT_PATH.toString(), path);
        startActivity(intent);
    }
}
