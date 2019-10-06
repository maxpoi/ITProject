package com.example.homesweethome.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.ImageAdapter;
import com.example.homesweethome.R;
import com.example.homesweethome.ViewModels.ArtifactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SingleArtifactPage extends AppCompatActivity{

    private ImageView img;
    private boolean hasAudio = true;

    private ArtifactViewModel artifactViewModel;
    private int artifactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_artifact_page);

        final ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Single artifact");
            bar.setSubtitle("Have a good day");
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.gallery);
        rv.setHasFixedSize(true);
        rv.setItemViewCacheSize(100);
        rv.setDrawingCacheEnabled(true);
        rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(lm);

        final ImageAdapter ia = new ImageAdapter(getApplicationContext());
        rv.setAdapter(ia);

        Intent intent = getIntent();
        artifactId = intent.getIntExtra(DataTag.ARTIFACT_ID.toString(), 0);

        ArtifactViewModel.ArtifactViewModelFactory artifactViewModelFactory = new ArtifactViewModel.ArtifactViewModelFactory(getApplication(), artifactId);
        artifactViewModel = new ViewModelProvider(this, artifactViewModelFactory).get(ArtifactViewModel.class);
        artifactViewModel.getArtifactImages().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                ia.setImages(images);
            }
        });

        final TextView title_content = findViewById(R.id.title);
        final TextView date_content = findViewById(R.id.date);
        final TextView desc_content = findViewById(R.id.description);
        final VideoView videoView = findViewById(R.id.single_artifact_video);
        videoView.setMediaController(new MediaController(this));

        artifactViewModel.getArtifact().observe(this, new Observer<Artifact>() {
            @Override
            public void onChanged(Artifact artifact) {
                title_content.setText(artifact.getTitle());
                date_content.setText(artifact.getDate());
                desc_content.setText(artifact.getDesc());
                if (bar != null) {
                    bar.setTitle(artifact.getTitle());
                    bar.setSubtitle(artifact.getDate());
                }

                if (artifact.getVideo() != null) {
                    videoView.setVideoPath(artifact.getVideo());
                    videoView.setVisibility(View.VISIBLE);
                }
            }
        });

        final FloatingActionButton edit_button = (FloatingActionButton) findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditPage();
            }
        });

        final FloatingActionButton audio_play = (FloatingActionButton) findViewById(R.id.audio_play);

        if (this.hasAudio)
            audio_play.show();
        else
            audio_play.hide();
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

    public void openEditPage() {
        Intent i = new Intent(getApplicationContext(), AddPage.class);
        i.putExtra(DataTag.TAG.toString(), DataTag.EDIT.toString());
        i.putExtra(DataTag.ARTIFACT_ID.toString(), artifactId);
        startActivity(i);
    }
}
