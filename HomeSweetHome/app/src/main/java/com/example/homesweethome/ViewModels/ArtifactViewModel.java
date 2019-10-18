package com.example.homesweethome.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.homesweethome.AppDataBase.Entities.Artifact;
import com.example.homesweethome.AppDataBase.Entities.Image;
import com.example.homesweethome.AppRepository;
import com.example.homesweethome.HelperClasses.HomeSweetHome;

import java.util.List;

public class ArtifactViewModel extends AndroidViewModel {

    private LiveData<Artifact> artifact;
    private LiveData<List<Image>> images;
    private AppRepository appRepository;

    // use factory to create this view model as Room only call the no parameter constructor.
    public static class ArtifactViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;
        private final int artifactId;
        private final AppRepository appRepository;

        public ArtifactViewModelFactory(@NonNull Application application, int artifactId) {
            this.application = application;
            this.artifactId = artifactId;
            this.appRepository = ((HomeSweetHome)application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ArtifactViewModel(application, appRepository, artifactId);
        }
    }

    private ArtifactViewModel(@NonNull Application application, AppRepository AppRepository, int artifactId) {
        super(application);
        this.appRepository = AppRepository;
        artifact = AppRepository.getArtifact(artifactId);
        images = AppRepository.getArtifactImages(artifactId);
    }

    public LiveData<Artifact> getArtifact() { return artifact; }
    public LiveData<List<Image>> getArtifactImages() { return images; }

    public void addImage(Image image) { appRepository.addImage(image); }
    public void addArtifact(Artifact artifact) { appRepository.addArtifact(artifact); }

    public void deleteImage(Image image) { appRepository.deleteImage(image); }
    public void deleteArtifactImages(int artifactId) { appRepository.deleteArtifactImages(artifactId); }
    public void deleteArtifact(Artifact artifact) { appRepository.deleteArtifact(artifact); }

    public List<Image> getArtifactStaticImages(int artifactId) { return appRepository.getArtifactStaticImages(artifactId); }
    public Artifact getStaticArtifact(int artifactId) { return appRepository.getStaticArtifact(artifactId); }

    public int getLastArtifactId() { return appRepository.getLastArtifactId(); }
    public int getLastImageId(int artifactId) { return appRepository.getLastImageId(artifactId); }
}
