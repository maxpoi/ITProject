package com.example.homesweethome.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.homesweethome.AppDataBase.Entities.Artifact;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.R;
import com.example.homesweethome.ViewModels.ArtifactViewModel;

public class PopUpText extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_text);

        final TextView desc = findViewById(R.id.description);
        final int artifactId = getIntent().getIntExtra(DataTag.ARTIFACT_ID.toString(), 0);
        ArtifactViewModel.ArtifactViewModelFactory artifactViewModelFactory = new ArtifactViewModel.ArtifactViewModelFactory(getApplication(), artifactId);
        ArtifactViewModel artifactViewModel = new ViewModelProvider(this, artifactViewModelFactory).get(ArtifactViewModel.class);
        artifactViewModel.getArtifact().observe(this, new Observer<Artifact>() {
            @Override
            public void onChanged(Artifact artifact) {
                desc.setText(artifact.getDesc());
            }
        });

    }
}
