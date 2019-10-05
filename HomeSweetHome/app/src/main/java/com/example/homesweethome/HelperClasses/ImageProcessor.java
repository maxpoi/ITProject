package com.example.homesweethome.HelperClasses;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.example.homesweethome.ArtifactDatabase.Entities.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ImageProcessor {
    private static final ImageProcessor ourInstance = new ImageProcessor();

    public static final int lowImageWidth = 100;
    public static final int lowImageHeight = 100;
    public static final int mediumImageWidth = 500;
    public static final int mediumImageHeight = 500;
    public static final int highImageWidth = 2560;
    public static final int highImageHeight = 1440;


    public static ImageProcessor getInstance() {
        return ourInstance;
    }

    private ImageProcessor() {}

    // https://developer.android.com/topic/performance/graphics/load-bitmap
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

    public Bitmap decodeFileToLow(String file) { return CustomDecodeFile(file, lowImageWidth, lowImageHeight); }
    public Bitmap decodeFileToMedium(String file) { return CustomDecodeFile(file, mediumImageWidth, mediumImageHeight); }
    public Bitmap decodeFileToHigh(String file) { return CustomDecodeFile(file, highImageWidth, highImageHeight); }

    private static byte[] encodeBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arr);
        return arr.toByteArray();
    }

    public String encodeBitmapString(Bitmap bitmap) { return Base64.encodeToString(encodeBitmapByte(bitmap), Base64.DEFAULT); }

    public Bitmap restoreImage(String image) {
        //https://stackoverflow.com/questions/4837110/how-to-convert-a-base64-string-into-a-bitmap-image-to-show-it-in-a-imageview
        byte[] decodedImg = Base64.decode(image, Base64.DEFAULT);
        InputStream inputStream = new ByteArrayInputStream(decodedImg);
        return BitmapFactory.decodeStream(inputStream);
    }

    public Bitmap restoreImage(byte[] image) {
        InputStream inputStream = new ByteArrayInputStream(image);
        return BitmapFactory.decodeStream(inputStream);
    }

    public String readFileString(File folder, String filename) {
        File file = new File(folder, filename);

        try {
            byte[] output = new byte[(int)file.length()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(output);
            inputStream.close();
            return new String(output);
        } catch (IOException e) {
            return null;
        }
    }

    public byte[] readFileByte(File folder, String filename) {
        File file = new File(folder, filename);

        try {
            byte[] output = new byte[(int)file.length()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(output);
            inputStream.close();
            return output;
        } catch (IOException e) {
            return null;
        }
    }

    public byte[] readFileByte(String filePath) {
        File file = new File(filePath);

        try {
            byte[] output = new byte[(int)file.length()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(output);
            inputStream.close();
            return output;
        } catch (IOException e) {
            return null;
        }
    }

    public void saveToLocal(List<Image> images) { new saveToLocalAsyncTask(images); }

    private static class saveToLocalAsyncTask extends AsyncTask<Void, Void, Void> {
        private List<Image> images;

        saveToLocalAsyncTask(List<Image> images) { this.images = images; }

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

            checkFileExistence(file);
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
                outputStream = new FileOutputStream(file);
                outputStream.write(contextByte);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void checkFileExistence(File file) {
            int lastFolderIndex = file.getAbsolutePath().lastIndexOf('/');
            String parentFolder = file.getAbsolutePath().substring(0, lastFolderIndex);
            File parent = new File(parentFolder);
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }
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
//    public String encodeLowImageString(Context context, int res) { return Base64.encodeToString(encodeLowImageByte(context, res),  Base64.DEFAULT); }
//
//    public String encodeMediumImageString(Context context, int res) { return Base64.encodeToString(encodeMediumImageByte(context, res),  Base64.DEFAULT); }
//
//    public String encodeLargeImageString(Context context, int res) { return Base64.encodeToString(encodeHighImageByte(context, res),  Base64.DEFAULT); }
}
