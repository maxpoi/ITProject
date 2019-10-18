package com.example.homesweethome;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.homesweethome.AppDataBase.AppDatabase;
import com.example.homesweethome.AppDataBase.Dao.ArtifactDAO;
import com.example.homesweethome.AppDataBase.Dao.ImageDAO;
import com.example.homesweethome.AppDataBase.Entities.Artifact;
import com.example.homesweethome.AppDataBase.Entities.Image;
import com.example.homesweethome.AppDataBase.Entities.User;

import java.util.List;

public class AppRepository {

    private static AppRepository INSTANCE;

    private AppDatabase appDatabase;
    private LiveData<List<Artifact>> artifacts;

    public static AppRepository getInstance(final AppDatabase appDatabase) {
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppRepository(appDatabase);
                }
            }
        }
        return INSTANCE;
    }

    private AppRepository(final AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        artifacts = appDatabase.artifactDAO().getAllArtifacts();
    }

    /*******************************************************
     *                   Getter for User                   *
     *******************************************************/
    public LiveData<User> getUser(final String userEmail) { return appDatabase.userDAO().getUser(userEmail); }
    public LiveData<String> getUserPortraitImagePath(final String userEmail) { return appDatabase.userDAO().getPortraitImagePath(userEmail); }

    /*******************************************************
     *                   Add/Delete User                   *
     *******************************************************/
    public void addUser(User user) { appDatabase.userDAO().insert(user); }
    public void deleteUser(User user) { appDatabase.userDAO().delete(user); }


    /*******************************************************
     *                Getter for Artifact                  *
     *******************************************************/
    public LiveData<List<Artifact>> getAllArtifacts() { return artifacts; }
    public LiveData<Artifact> getArtifact(final int artifactId) { return appDatabase.artifactDAO().getArtifact(artifactId); }
    public LiveData<List<String>> getCoverImagesPath() { return  appDatabase.artifactDAO().getCoverImagesPath(); }

    public Artifact getStaticArtifact(final int artifactId) {
        try {
            return new getStaticArtifact(appDatabase.artifactDAO()).execute(artifactId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Artifact> getAllStaticArtifacts() {
        try {
            return new getAllStaticArtifacts(appDatabase.artifactDAO()).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getLastArtifactId() {
        try {
            return new getLastId(appDatabase.artifactDAO()).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    /*******************************************************
     *                   Getter for Image                  *
     *******************************************************/
    public LiveData<List<Image>> getArtifactImages(final int artifactId) { return appDatabase.imageDAO().getImages(artifactId); }
    public List<Image> getArtifactStaticImages(final int artifactId) {
        try {
            return new getStaticImagesAsyncTask(appDatabase.imageDAO()).execute(artifactId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getLastImageId(int artifactId) {
        try {
            return new getLastId(appDatabase.imageDAO(), artifactId).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*******************************************************
     *           Add/Delete Artifact/Image                 *
     *******************************************************/
    public void addArtifact (Artifact artifact) { new insertArtifactAsyncTask(appDatabase).execute(artifact); }
    public void addImage(Image image) { new insertImageAsyncTask(appDatabase).execute(image); }

    public void deleteImage(Image image) { appDatabase.imageDAO().delete(image); }
    public void deleteArtifactImages(int artifactId) { new deleteArtifactImagesAsyncTask(appDatabase.imageDAO()).execute(artifactId); }
    public void deleteArtifact(Artifact artifact) { new deleteArtifactAsyncTask(appDatabase.artifactDAO()).execute(artifact); }

    public LiveData<List<Artifact>> searchAllArtifacts(String query) { return appDatabase.artifactDAO().searchAllArtifacts(query); }


    /*******************************************************
     *            Private classes & functions              *
     *******************************************************/
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
        private AppDatabase appDatabase;
        insertArtifactAsyncTask(AppDatabase appDatabase) { this.appDatabase = appDatabase; }

        @Override
        protected Void doInBackground(final Artifact... params) {
            appDatabase.artifactDAO().insert(params[0]);
            return null;
        }
    }

    private static class insertImageAsyncTask extends AsyncTask<Image, Void, Void> {
        private AppDatabase appDatabase;
        insertImageAsyncTask(AppDatabase appDatabase) { this.appDatabase = appDatabase; }

        @Override
        protected Void doInBackground(final Image... params) {
            appDatabase.imageDAO().insert(params[0]);
            return null;
        }
    }
}
