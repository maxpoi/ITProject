package com.example.homesweethome;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.homesweethome.ArtifactDatabase.ArtifactDatabase;
import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;

import java.util.List;

public class ArtifactRepository {

    private static ArtifactRepository INSTANCE;

    private ArtifactDatabase artifactDatabase;
    private LiveData<List<Artifact>> artifacts;

    public static ArtifactRepository getInstance(final ArtifactDatabase artifactDatabase) {
        if (INSTANCE == null) {
            synchronized (ArtifactRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ArtifactRepository(artifactDatabase);
                }
            }
        }
        return INSTANCE;
    }

    private ArtifactRepository(final ArtifactDatabase artifactDatabase) {
        this.artifactDatabase = artifactDatabase;
        artifacts = artifactDatabase.artifactDAO().getAllArtifacts();
    }

    public LiveData<List<Artifact>> getAllArtifacts() { return artifacts; }
    public LiveData<Artifact> getArtifact(final int artifactId) { return artifactDatabase.artifactDAO().getArtifact(artifactId); }

    public LiveData<List<Image>> getArtifactImages(final int artifactId) { return artifactDatabase.imageDAO().getImages(artifactId); }
    public LiveData<List<String>> getHomeImages() { return  artifactDatabase.artifactDAO().getCoverImagesPath(); }

    public void addArtifact (Artifact artifact) { new insertArtifactAsyncTask(artifactDatabase).execute(artifact); }
    public void addImage(Image image) { new insertImageAsyncTask(artifactDatabase).execute(image); }

    public void deleteImage(Image image) { artifactDatabase.imageDAO().delete(image); }
    public void deleteArtifact(Artifact artifact) { artifactDatabase.artifactDAO().delete(artifact); }



    private static class insertArtifactAsyncTask extends AsyncTask<Artifact, Void, Void> {
        private ArtifactDatabase artifactDatabase;

        insertArtifactAsyncTask(ArtifactDatabase artifactDatabase) { this.artifactDatabase = artifactDatabase; }

        @Override
        protected Void doInBackground(final Artifact... params) {
            artifactDatabase.artifactDAO().insert(params[0]);
            return null;
        }
    }

    private static class insertImageAsyncTask extends AsyncTask<Image, Void, Void> {
        private ArtifactDatabase artifactDatabase;

        insertImageAsyncTask(ArtifactDatabase artifactDatabase) { this.artifactDatabase = artifactDatabase; }

        @Override
        protected Void doInBackground(final Image... params) {
            artifactDatabase.imageDAO().insert(params[0]);
            return null;
        }
    }
}
