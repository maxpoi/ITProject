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
    private LiveData<List<String>> coverImagesPathList;
    private ArtifactRepository artifactRepository;

    public ArtifactListViewModel(@NonNull Application application) {
        super(application);
        this.artifactRepository = ((HomeSweetHome)application).getRepository();
        artifacts = artifactRepository.getAllArtifacts();
        coverImagesPathList = artifactRepository.getCoverImagesPath();
    }

    public LiveData<List<Artifact>> getArtifacts() { return artifacts; }
    public List<Artifact> getAllStaticArtifacts() { return artifactRepository.getAllStaticArtifacts(); }
    public LiveData<List<String>> getCoverImagesPath() { return coverImagesPathList; }
    public int getLastArtifactId() { return artifactRepository.getLastArtifactId(); }

    public LiveData<List<Artifact>> searchAllArtifacts(String query) { return artifactRepository.searchAllArtifacts(query); }
}
