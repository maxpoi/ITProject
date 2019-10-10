package com.example.homesweethome.HelperClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class ImageProcessor {
    private static ImageProcessor ourInstance;

    private static final int lowImageWidth = 100;
    private static final int lowImageHeight = 100;
    private static final int mediumImageWidth = 500;
    private static final int mediumImageHeight = 500;
    private static final int highImageWidth = 2560;
    private static final int highImageHeight = 1440;

    public static String PARENT_FOLDER_PATH;
    public static String LOW_RES_IMAGE_FOLDER_NAME = "/low_image/";
    public static String MEDIUM_RES_IMAGE_FOLDER_NAME = "/medium_image/";
    public static String HIGH_RES_IMAGE_FOLDER_NAME = "/high_image/";
    public static String IMAGE_TYPE = ".jpeg";

    public static String VIDEO_FOLDER_NAME = "/video/";
    public static String VIDEO_NAME = "video.mp4";

    public static ImageProcessor getInstance(String path) {
        if (ourInstance == null) {
            PARENT_FOLDER_PATH = path;
            ourInstance = new ImageProcessor();
        }
        return ourInstance;
    }

    private ImageProcessor() {}

    public Bitmap decodeFileToLowBitmap(String file) { return CustomDecodeFile(file, lowImageWidth, lowImageHeight); }
    public Bitmap decodeFileToMediumBitmap(String file) { return CustomDecodeFile(file, mediumImageWidth, mediumImageHeight); }
    public Bitmap decodeFileToHighBitmap(String file) { return CustomDecodeFile(file, highImageWidth, highImageHeight); }

    public String encodeBitmapToString(Bitmap bitmap) { return Base64.encodeToString(encodeBitmapByte(bitmap), Base64.DEFAULT); }

    public Bitmap restoreImageFromString(String image) {
        //https://stackoverflow.com/questions/4837110/how-to-convert-a-base64-string-into-a-bitmap-image-to-show-it-in-a-imageview
        byte[] decodedImg = Base64.decode(image, Base64.DEFAULT);
        InputStream inputStream = new ByteArrayInputStream(decodedImg);
        return BitmapFactory.decodeStream(inputStream);
    }

    public Bitmap restoreImageFromByte(byte[] image) {
        InputStream inputStream = new ByteArrayInputStream(image);
        return BitmapFactory.decodeStream(inputStream);
    }

    public void saveImageListToLocal(List<Image> images) { new saveImageListToLocalAsyncTask(images).execute(); }
    public void saveVideoToLocal(int artifactId, String videoPath) { new saveVideoToLocalAsyncTask(artifactId, videoPath).execute(); }

    public void deleteImageListFromLocal(int artifactId) { new deleteImageListFromLocalAsyncTask().execute(artifactId); }


    /***
     * Result of the codes are private functions & classes
     */

    private static class deleteImageListFromLocalAsyncTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            String parent = PARENT_FOLDER_PATH + params[0];
            File parentFolder = new File(parent);
            deleteRecursive(parentFolder);
            return null;
        }

        void deleteRecursive(File fileOrDirectory) {
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    deleteRecursive(child);
            fileOrDirectory.delete();
        }
    }

    private static class saveImageListToLocalAsyncTask extends AsyncTask<Void, Void, Void> {
        private List<Image> images;

        saveImageListToLocalAsyncTask(List<Image> images) { this.images = images; }

        @Override
        protected Void doInBackground(Void... params) {
            for (Image image : images) {
                File low = new File(image.getLowResImagePath());
                File medium = new File(image.getMediumResImagePath());
                File high = new File(image.getHighResImagePath());

                writeFile(low, image.getLowImageBitmap());
                writeFile(medium, image.getMediumImageBitmap());
                writeFile(high, image.getHighImageBitmap());
            }

            return null;
        }

        private void writeFile(File file, Bitmap context) {
            if (context == null) {
                return ;
            }

            checkFolderExistence(file);
            if (!file.isFile()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream outputStream;
            byte[] contextByte = encodeBitmapByte(context);
            try {
                outputStream = new FileOutputStream(file, false); // set append to false thus it will overwrite the file!!!!
                outputStream.write(contextByte);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void checkFolderExistence(File file) {
            int lastFolderIndex = file.getAbsolutePath().lastIndexOf('/');
            String parentFolder = file.getAbsolutePath().substring(0, lastFolderIndex);
            File parent = new File(parentFolder);
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }
    }

    private static class saveVideoToLocalAsyncTask extends AsyncTask<Void, Void, Void> {
        private int artifactId;
        private String videoPath;

        saveVideoToLocalAsyncTask(int artifactId, String videoPath) {
            this.artifactId = artifactId;
            this.videoPath = videoPath;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (videoPath == null) {
                return null;
            }

            File sourceVideo = new File(videoPath);

            File destinationFolder = new File(PARENT_FOLDER_PATH + artifactId + VIDEO_FOLDER_NAME);
            destinationFolder.mkdirs();

            File destinationVideo = new File(PARENT_FOLDER_PATH + artifactId + VIDEO_FOLDER_NAME + VIDEO_NAME);
            try {
                if (destinationVideo.createNewFile()) {
                    // do nothing
                } else {
                    // delete the old file
                    destinationVideo.delete();
                    destinationVideo.createNewFile();
                }
                copyFile(sourceVideo, destinationVideo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void copyFile(File sourceFile, File destFile) throws IOException {
            if (!destFile.exists()) { return ; }

            FileChannel source = new FileInputStream(sourceFile).getChannel();
            FileChannel destination = new FileOutputStream(destFile).getChannel();
            if (source != null) {
                destination.transferFrom(source, 0, source.size());
                source.close();
            }

            destination.close();
        }
    }

    // https://developer.android.com/topic/performance/graphics/load-bitmap
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // https://developer.android.com/topic/performance/graphics/load-bitmap
    private static Bitmap CustomDecodeFile(String file, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        // need to somehow keep track of this information.
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bitmap = null;
        while(bitmap == null)
            bitmap = BitmapFactory.decodeFile(file, options);

        return bitmap;
    }

    private static byte[] encodeBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arr);
        return arr.toByteArray();
    }

//    // test functions only
//    public byte[] encodeLowImageByte(Context context, int res) {
//        Bitmap bitmap = CustomDecodeResource(context.getResources(), res, lowImageWidth, lowImageHeight);
//        ByteArrayOutputStream arr = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arr);
//        return arr.toByteArray();
//    }
//
//    public byte[] encodeMediumImageByte(Context context, int res) {
//        Bitmap bitmap = CustomDecodeResource(context.getResources(), res, mediumImageWidth, mediumImageHeight);
//        ByteArrayOutputStream arr = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arr);
//        return arr.toByteArray();
//    }
//
//    public byte[] encodeHighImageByte(Context context, int res) {
//        Bitmap bitmap = CustomDecodeResource(context.getResources(), res, highImageWidth, highImageHeight);
//        ByteArrayOutputStream arr = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arr);
//        return arr.toByteArray();
//    }
//
}
