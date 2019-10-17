package com.example.homesweethome.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
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
import com.example.homesweethome.R;
import com.example.homesweethome.ViewModels.ArtifactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SingleArtifactPage extends AppCompatActivity{
    private ArtifactViewModel artifactViewModel;
    private int artifactId;

    private boolean enableDeletion = false;

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

        final ImageAdapter ia = new ImageAdapter(getApplicationContext(), enableDeletion);
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

        final CardView text_card = findViewById(R.id.text_card);
        final TextView title_content = findViewById(R.id.title);
        final TextView date_content = findViewById(R.id.date);
        final TextView desc_content = findViewById(R.id.description);
        final ImageView videoCover = findViewById(R.id.single_artifact_video);

        artifactViewModel.getArtifact().observe(this, new Observer<Artifact>() {
            @Override
            public void onChanged(Artifact artifact) {
                if (artifact == null)
                    return ;

                if (bar != null) {
                    bar.setTitle(artifact.getTitle());
                    bar.setSubtitle(artifact.getDate());
                }

                title_content.setText(artifact.getTitle());
                date_content.setText(artifact.getDate());
                desc_content.setText(artifact.getDesc() == null ? "NO TEXT DESCRIPTION" : artifact.getDesc());

                if (artifact.getVideo() != null) {
                    Glide.with(getApplicationContext()).load(artifact.getVideo()).into(videoCover);
                    videoCover.setVisibility(View.VISIBLE);
                } else {
                    videoCover.setVisibility(View.GONE);
                }

                if(artifact.getDesc() != null)
                    text_card.setVisibility(View.VISIBLE);
            }
        });

        videoCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoPlayPage.class);
                intent.putExtra(DataTag.ARTIFACT_VIDEO.toString(), artifactViewModel.getStaticArtifact(artifactId).getVideo());
                startActivity(intent);
            }
        });

        text_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpText();
            }
        });

        final FloatingActionButton edit_button = (FloatingActionButton) findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditPage();
            }
        });

        final FloatingActionButton delete_button = (FloatingActionButton) findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artifactViewModel.deleteArtifact(artifactViewModel.getStaticArtifact(artifactId));
                artifactViewModel.deleteArtifactImages(artifactId);
                ((HomeSweetHome)getApplication()).getImageProcessor().deleteImageListFromLocal(artifactId);
                openHomePage();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openHomePage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openHomePage() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }

    public void openEditPage() {
        Intent i = new Intent(getApplicationContext(), AddPage.class);
        i.putExtra(DataTag.TAG.toString(), DataTag.EDIT.toString());
        i.putExtra(DataTag.ARTIFACT_ID.toString(), artifactId);
        startActivity(i);
    }

    private void openPopUpText() {
        Intent intent = new Intent(getApplicationContext(), PopUpText.class);
        intent.putExtra(DataTag.ARTIFACT_ID.toString(), artifactId);
        startActivity(intent);
    }
}
