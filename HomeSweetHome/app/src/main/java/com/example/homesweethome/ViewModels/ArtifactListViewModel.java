package com.example.homesweethome.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactRepository;
import com.example.homesweethome.HelperClasses.HomeSweetHome;

import java.util.List;

public class ArtifactListViewModel extends AndroidViewModel {

    private LiveData<List<Artifact>> artifacts;
    private LiveData<List<String>> images;
    private ArtifactRepository artifactRepository;

    public ArtifactListViewModel(@NonNull Application application) {
        super(application);
        this.artifactRepository = ((HomeSweetHome)application).getRepository();
        artifacts = artifactRepository.getAllArtifacts();
        images = artifactRepository.getHomeImages();
    }

    public LiveData<List<Artifact>> getArtifacts() { return artifacts; }
    public LiveData<List<String>> getHomeImages() { return images; }

    public LiveData<List<Artifact>> searchAllArtifacts(String query) { return artifactRepository.searchAllArtifacts(query); }

}
