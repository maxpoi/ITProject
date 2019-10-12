package com.example.homesweethome.HelperClasses;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.ViewModels.ArtifactListViewModel;
import com.example.homesweethome.ViewModels.ArtifactViewModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.List;

public class SynchronizeHandler {
    //private StorageReference mStorageRef;
    private ArtifactListViewModel artifactListViewModel;
    private ArtifactViewModel artifactViewModel;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    SynchronizeHandler(ArtifactListViewModel artifactListViewModel,
                       ArtifactViewModel artifactViewModel){
        this.artifactListViewModel = artifactListViewModel;
        this.artifactViewModel = artifactViewModel;
    }

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

    public void createReference() {

    }
}
