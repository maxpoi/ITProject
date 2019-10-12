package com.example.homesweethome.HelperClasses;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.ViewModels.ArtifactListViewModel;
import com.example.homesweethome.ViewModels.ArtifactViewModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.List;

public class SynchronizeHandler {
    //private StorageReference mStorageRef;
    private ArtifactListViewModel artifactListViewModel;
    private ArtifactViewModel artifactViewModel;

    SynchronizeHandler(ArtifactListViewModel artifactListViewModel,
                       ArtifactViewModel artifactViewModel){
        this.artifactListViewModel = artifactListViewModel;
        this.artifactViewModel = artifactViewModel;
    }

    public void uploadAll() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray artifactjsonArray = new JSONArray();
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

            List<Image> images = artifactViewModel.getArtifactStaticImages(artifact.getId());

        }
        jsonObject.put("forms", artifactjsonArray);
    }

    public void createReference() {

    }
}
