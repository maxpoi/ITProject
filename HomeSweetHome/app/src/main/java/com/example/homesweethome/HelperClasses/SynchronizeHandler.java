package com.example.homesweethome.HelperClasses;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.homesweethome.ViewModels.ArtifactListViewModel;
import com.example.homesweethome.ViewModels.ArtifactViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class SynchronizeHandler {
    private static SynchronizeHandler instance = null;

    private ArtifactListViewModel artifactListViewModel;
    private ArtifactViewModel artifactViewModel;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private String email;

    private SynchronizeHandler(){}

    public static SynchronizeHandler getInstance(){
        if(instance == null){
            instance = new SynchronizeHandler();
        }
        return instance;
    }

    public boolean uploadUser(String email){

        setEmail(email);
        boolean isSuccessful = uploadFile(this.email + "/" + "databases/",
                new File(ImageProcessor.DATABASE_PATH));
        isSuccessful = isSuccessful && uploadFile(this.email + "/" + "files/",
                new File(ImageProcessor.PARENT_FOLDER_PATH));
        return isSuccessful;
    }

    public boolean downloadUser(String email){

        setEmail(email);
        return downloadFile(ImageProcessor.SHARED_PATH, storageRef.child(this.email));
    }

    private void setEmail(String email){
        this.email = email;
    }



    private boolean uploadFile(String curPath, File file) {
        File[] fileList = file.listFiles();
        boolean isSuccessful = true;
        try {
            if(fileList.length == 0){
                StorageReference curRef = storageRef.child(curPath);
                // TODO: create empty directory???
            }

            for (File subFile : fileList) {
                if (subFile.isDirectory()) {
                    isSuccessful = isSuccessful && uploadFile(curPath + subFile.getName() + "/", subFile);
                }
                if (subFile.isFile()) {
                    StorageReference curRef = storageRef.child(curPath + subFile.getName());
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


    private boolean downloadFile(final String curPath, StorageReference ref){
        boolean isSuccessful = true;
        ref.listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                ArrayList<StorageReference> prefixesRefs = (ArrayList<StorageReference>) task.getResult().getPrefixes();
                for (StorageReference curRef : prefixesRefs) {
                    downloadFile(curPath + curRef.getName() + "/", curRef);
                }
                ArrayList<StorageReference> itemsRefs = (ArrayList<StorageReference>) task.getResult().getItems();
                for (StorageReference curRef : itemsRefs) {
                    try {
                        File directory = new File(curPath);
                        checkDirectory(directory);
                        File tempFile = new File(curPath + curRef.getName());
                        checkFile(tempFile);
                        curRef.getFile(tempFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });


        return false;
    }

    private static void checkDirectory(File tempFile) throws Exception {
        if(!tempFile.exists()){
            if(!tempFile.mkdirs()){
                throw new Exception("create directory fail");
            }
        }
    }

    private static void checkFile(File tempFile) throws Exception {
        if(tempFile.exists()){
            if(!tempFile.delete()){
                throw new Exception("delete old file fail");
            }
        }
        if(!tempFile.createNewFile()){
            throw new Exception("create file fail");
        }
    }



}

