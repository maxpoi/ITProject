package com.example.homesweethome.HelperClasses;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public byte[] encodeBitmapByte(Bitmap bitmap) {
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
