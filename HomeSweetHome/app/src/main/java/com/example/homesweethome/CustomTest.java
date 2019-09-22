package com.example.homesweethome;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CustomTest{

    private Context context;
    public CustomTest(Context context) { this.context = context; }

    public String createImg(int res) {
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
        Bitmap bitmap = ((BitmapDrawable) context.getDrawable(res)).getBitmap();
        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, arr);
        String encodedImg = Base64.encodeToString(arr.toByteArray(),  Base64.DEFAULT);
        return encodedImg;
    }

    public ArrayList<String> createCell() {
        ArrayList<String> imgs = new ArrayList<>();

//        Bitmap bitmap = ((BitmapDrawable) context.getDrawable(R.drawable.img_1)).getBitmap();
        imgs.add(createImg(R.drawable.img_1));
        imgs.add(createImg(R.drawable.img_2));
        imgs.add(createImg(R.drawable.img_3));
        imgs.add(createImg(R.drawable.img_4));
        imgs.add(createImg(R.drawable.img_5));

        return imgs;
    }

    public ArrayList<String> createCells() {
        int i=0;
        ArrayList<String> imgs = new ArrayList<>();

        while(i<4) {
            ArrayList<String> temp = createCell();
            imgs.addAll(temp);
            i++;
        }

        return imgs;
    }
}
