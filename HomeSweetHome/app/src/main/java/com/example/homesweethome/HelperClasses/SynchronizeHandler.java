package com.example.homesweethome.HelperClasses;

import android.app.ExpandableListActivity;
import android.net.Uri;
import android.provider.ContactsContract;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.ViewModels.ArtifactListViewModel;
import com.example.homesweethome.ViewModels.ArtifactViewModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;

public class SynchronizeHandler {
    private static ArtifactListViewModel artifactListViewModel;
    private static ArtifactViewModel artifactViewModel;
    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private static String email;

//    SynchronizeHandler(ArtifactListViewModel artifactListViewModel,
//                       ArtifactViewModel artifactViewModel){
//        this.artifactListViewModel = artifactListViewModel;
//        this.artifactViewModel = artifactViewModel;
//    }

    public void uploadAll() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray artifactjsonArray = new JSONArray();
        // Change all the artifact and their pictures to JSON one by one
        for(Artifact artifact : artifactListViewModel.getAllStaticArtifacts()) {
            JSONObject artifactJson = new JSONObject();
            artifactJson.put("artifact_id", artifact.getId());
            artifactJson.put("artifact_video", artifact.getVideo());
            artifactJson.put("artifact_audio", artifact.getAudio());
            artifactJson.put("artifact_date", artifact.getDate());
            artifactJson.put("artifact_desc", artifact.getDesc());
            artifactJson.put("artifact_title", artifact.getTitle());
            artifactJson.put("artifact_cover", artifact.getCoverImagePath());
            artifactjsonArray.put(artifactJson);

            // Change the pictures attached to this artifacts into JSON
            List<Image> images = artifactViewModel.getArtifactStaticImages(artifact.getId());
            for (int i = 0; i < images.size(); i++) {
                JSONObject imageJson = new JSONObject();
                imageJson.put("image_id", images.get(i).getId());
                imageJson.put("image_video", images.get(i).getArtifactId());
                artifactjsonArray.put(imageJson);
            }
        }
        jsonObject.put("forms", artifactjsonArray);
    }


    public static boolean uploadUserProfile(String email){
        setEmail("13171209963@163.com");
        boolean isSuccessful = uploadFile(SynchronizeHandler.email + "/" + "databases/",
                new File(ImageProcessor.DATABASE_PATH));
        isSuccessful = isSuccessful && uploadFile(SynchronizeHandler.email + "/" + "files",
                new File(ImageProcessor.PARENT_FOLDER_PATH));
        return isSuccessful;
    }

    private static void setEmail(String email){
        SynchronizeHandler.email = email;
    }



    private static boolean uploadFile(String curPath, File file) {
        File[] fileList = file.listFiles();
        boolean isSuccessful = true;
        try {
            if(fileList.length == 0){
                // TODO: create empty directory???
            }

            for (File subFile : fileList) {
                System.out.println(subFile.getName());
                if (subFile.isDirectory()) {
                    isSuccessful = isSuccessful && uploadFile(curPath + subFile.getName() + "/", subFile);
                }
                if (subFile.isFile()) {
                    StorageReference curRef = storageRef.child(curPath + subFile.getName());
                    System.out.println("current path is :" + curRef.getPath());
                    Uri FileUri = Uri.fromFile(subFile);
                    curRef.putFile(FileUri);
                }
            }
            return isSuccessful;

        }
        catch(Exception e){
            e.printStackTrace();
            isSuccessful = false;
        }
        return isSuccessful;
    }
}
