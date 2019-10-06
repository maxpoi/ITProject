package com.example.homesweethome.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.ArtifactRepository;
import com.example.homesweethome.HelperClasses.HomeSweetHome;

import java.util.List;

public class ArtifactViewModel extends AndroidViewModel {

    private LiveData<Artifact> artifact;
    private LiveData<List<Image>> images;
    private ArtifactRepository artifactRepository;
    private int imagesLen;
    private Artifact staticArtifact;

    // use factory to create this view model as Room only call the no parameter constructor.
    public static class ArtifactViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;
        private final int artifactId;
        private final ArtifactRepository artifactRepository;

        public ArtifactViewModelFactory(@NonNull Application application, int artifactId) {
            this.application = application;
            this.artifactId = artifactId;
            this.artifactRepository = ((HomeSweetHome)application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ArtifactViewModel(application, artifactRepository, artifactId);
        }
    }

    private ArtifactViewModel(@NonNull Application application, ArtifactRepository artifactRepository, int artifactId) {
        super(application);
        this.artifactRepository = artifactRepository;
        artifact = artifactRepository.getArtifact(artifactId);
        images = artifactRepository.getArtifactImages(artifactId);
        imagesLen = artifactRepository.getArtifactImagesLen(artifactId);
        staticArtifact = artifactRepository.getStaticArtifact(artifactId);
    }

    public LiveData<Artifact> getArtifact() { return artifact; }
    public LiveData<List<Image>> getArtifactImages() { return images; }

    public void addImage(Image image) { artifactRepository.addImage(image); }
    public void addArtifact(Artifact artifact) { artifactRepository.addArtifact(artifact); }

    public void deleteImage(Image image) { artifactRepository.deleteImage(image); }
    public void deleteArtifact(Artifact artifact) { artifactRepository.deleteArtifact(artifact); }

    public int getImagesLen() { return imagesLen; }
    public Artifact getStaticArtifact() { return staticArtifact; }
}
