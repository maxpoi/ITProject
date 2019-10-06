package com.example.homesweethome;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.homesweethome.ArtifactDatabase.ArtifactDatabase;
import com.example.homesweethome.ArtifactDatabase.Dao.ArtifactDAO;
import com.example.homesweethome.ArtifactDatabase.Dao.ImageDAO;
import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    public Artifact getStaticArtifact(final int artifactId) {
        try {
            return new getStaticArtifact(artifactDatabase.artifactDAO()).execute(artifactId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public LiveData<List<String>> getHomeImages() { return  artifactDatabase.artifactDAO().getCoverImagesPath(); }

    public LiveData<List<Image>> getArtifactImages(final int artifactId) { return artifactDatabase.imageDAO().getImages(artifactId); }
    public int getArtifactImagesLen(final int artifactId) {
        try {
            return new getStaticImagesAsyncTask(artifactDatabase.imageDAO()).execute(artifactId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void addArtifact (Artifact artifact) { new insertArtifactAsyncTask(artifactDatabase).execute(artifact); }
    public void addImage(Image image) { new insertImageAsyncTask(artifactDatabase).execute(image); }

    public void deleteImage(Image image) { artifactDatabase.imageDAO().delete(image); }
    public void deleteArtifact(Artifact artifact) { artifactDatabase.artifactDAO().delete(artifact); }

    public LiveData<List<Artifact>> searchAllArtifacts(String query) { return artifactDatabase.artifactDAO().searchAllArtifacts(query); }


    // private classes & functions
    private static class getStaticImagesAsyncTask extends AsyncTask<Integer, Void, Integer> {

        private ImageDAO imageDAO;

        getStaticImagesAsyncTask(ImageDAO imageDAO) { this.imageDAO = imageDAO; }

        @Override
        protected Integer doInBackground(Integer... artifactIds) {
            return imageDAO.getStaticImages(artifactIds[0]).size();
        }
    }

    private static class getStaticArtifact extends AsyncTask<Integer, Void, Artifact> {

        private ArtifactDAO artifactDAO;

        getStaticArtifact(ArtifactDAO artifactDAO) { this.artifactDAO = artifactDAO; }

        @Override
        protected Artifact doInBackground(Integer... artifactIds) {
            return artifactDAO.getStaticArtifact(artifactIds[0]);
        }
    }

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
