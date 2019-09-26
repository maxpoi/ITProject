package com.example.homesweethome;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImageProcessor {
    private static final ImageProcessor ourInstance = new ImageProcessor();

    static ImageProcessor getInstance() {
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
                    || (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // https://developer.android.com/topic/performance/graphics/load-bitmap
    public static Bitmap CustomDecodeResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // need to somehow keep track of this information.
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public byte[] encodeLowImageByte(Context context, int res) {
        Bitmap bitmap = CustomDecodeResource(context.getResources(), res, 100, 100);
        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arr);
        return arr.toByteArray();
    }

    public byte[] encodeMediumImageByte(Context context, int res) {
        Bitmap bitmap = CustomDecodeResource(context.getResources(), res, 500, 500);
        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arr);
        return arr.toByteArray();
    }

    public byte[] encodeHighImageByte(Context context, int res) {
        Bitmap bitmap = CustomDecodeResource(context.getResources(), res, 1440, 2560);
        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arr);
        return arr.toByteArray();
    }

    public String encodeLowImageString(Context context, int res) { return Base64.encodeToString(encodeLowImageByte(context, res),  Base64.DEFAULT); }

    public String encodeMediumImageString(Context context, int res) { return Base64.encodeToString(encodeMediumImageByte(context, res),  Base64.DEFAULT); }

    public String encodeLargeImageString(Context context, int res) { return Base64.encodeToString(encodeHighImageByte(context, res),  Base64.DEFAULT); }

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
}
