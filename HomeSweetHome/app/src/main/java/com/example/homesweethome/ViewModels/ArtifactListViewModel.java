package com.example.homesweethome.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.homesweethome.AppDataBase.Entities.Artifact;
import com.example.homesweethome.AppRepository;
import com.example.homesweethome.HelperClasses.HomeSweetHome;

import java.util.List;

public class ArtifactListViewModel extends AndroidViewModel {

    private LiveData<List<Artifact>> artifacts;
    private LiveData<List<String>> coverImagesPathList;
    private AppRepository appRepository;

    public ArtifactListViewModel(@NonNull Application application) {
        super(application);
        this.appRepository = ((HomeSweetHome)application).getRepository();
        artifacts = appRepository.getAllArtifacts();
        coverImagesPathList = appRepository.getCoverImagesPath();
    }

    public LiveData<List<Artifact>> getArtifacts() { return artifacts; }
    public List<Artifact> getAllStaticArtifacts() { return appRepository.getAllStaticArtifacts(); }
    public LiveData<List<String>> getCoverImagesPath() { return coverImagesPathList; }
    public int getLastArtifactId() { return appRepository.getLastArtifactId(); }

    public LiveData<List<Artifact>> searchAllArtifacts(String query) { return appRepository.searchAllArtifacts(query); }
}
