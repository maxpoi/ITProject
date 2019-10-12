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
    public LiveData<List<String>> getCoverImagesPath() { return  artifactDatabase.artifactDAO().getCoverImagesPath(); }

    public Artifact getStaticArtifact(final int artifactId) {
        try {
            return new getStaticArtifact(artifactDatabase.artifactDAO()).execute(artifactId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Artifact> getAllStaticArtifacts() {
        try {
            return new getAllStaticArtifacts(artifactDatabase.artifactDAO()).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Image>> getArtifactImages(final int artifactId) { return artifactDatabase.imageDAO().getImages(artifactId); }
    public List<Image> getArtifactStaticImages(final int artifactId) {
        try {
            return new getStaticImagesAsyncTask(artifactDatabase.imageDAO()).execute(artifactId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getLastArtifactId() {
        try {
            return new getLastId(artifactDatabase.artifactDAO()).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getLastImageId(int artifactId) {
        try {
            return new getLastId(artifactDatabase.imageDAO(), artifactId).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void addArtifact (Artifact artifact) { new insertArtifactAsyncTask(artifactDatabase).execute(artifact); }
    public void addImage(Image image) { new insertImageAsyncTask(artifactDatabase).execute(image); }

    public void deleteImage(Image image) { artifactDatabase.imageDAO().delete(image); }
    public void deleteArtifactImages(int artifactId) { new deleteArtifactImagesAsyncTask(artifactDatabase.imageDAO()).execute(artifactId); }
    public void deleteArtifact(Artifact artifact) { new deleteArtifactAsyncTask(artifactDatabase.artifactDAO()).execute(artifact); }

    public LiveData<List<Artifact>> searchAllArtifacts(String query) { return artifactDatabase.artifactDAO().searchAllArtifacts(query); }


    // private classes & functions
    private static class getLastId extends AsyncTask<Void, Void, Integer> {
        private ImageDAO imageDAO;
        private ArtifactDAO artifactDAO;
        private int artifactId;

        getLastId(ImageDAO imageDAO, int artifactId) {
            this.imageDAO = imageDAO;
            this.artifactId = artifactId;
            this.artifactDAO = null;
        }
        getLastId(ArtifactDAO artifactDAO) {
            this.artifactDAO = artifactDAO;
            this.imageDAO = null;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (imageDAO == null)
                return artifactDAO.getLastArtifactId();
            else
                return imageDAO.getLastImageId(artifactId);
        }
    }

    private static class deleteArtifactImagesAsyncTask extends AsyncTask<Integer, Void, Void> {
        private ImageDAO imageDAO;
        deleteArtifactImagesAsyncTask(ImageDAO imageDAO) { this.imageDAO = imageDAO; }

        @Override
        protected Void doInBackground(Integer... artifactIds) {
            imageDAO.deleteAllArtifactImages(artifactIds[0]);
            return null;
        }
    }

    private static class deleteArtifactAsyncTask extends  AsyncTask<Artifact, Void, Void> {
        private ArtifactDAO artifactDAO;
        deleteArtifactAsyncTask(ArtifactDAO artifactDAO) { this.artifactDAO = artifactDAO; }

        @Override
        protected Void doInBackground(Artifact... artifacts) {
            artifactDAO.delete(artifacts[0]);
            return null;
        }
    }

    private static class getStaticImagesAsyncTask extends AsyncTask<Integer, Void, List<Image>> {
        private ImageDAO imageDAO;
        getStaticImagesAsyncTask(ImageDAO imageDAO) { this.imageDAO = imageDAO; }

        @Override
        protected List<Image> doInBackground(Integer... artifactIds) {
            return imageDAO.getStaticImages(artifactIds[0]);
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

    private static class getAllStaticArtifacts extends AsyncTask<Void, Void, List<Artifact>> {
        private ArtifactDAO artifactDAO;
        getAllStaticArtifacts(ArtifactDAO artifactDAO) { this.artifactDAO = artifactDAO; }

        @Override
        protected List<Artifact> doInBackground(Void... params) {
            return artifactDAO.getAllStaticArtifacts();
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
