package com.example.homesweethome.timeline;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.homesweethome.Cell;
import com.example.homesweethome.ImageProcessor;
import com.example.homesweethome.LoginPage;
import com.example.homesweethome.R;
import com.example.homesweethome.UserCache;
import com.example.homesweethome.register.RegisterActivity;
import com.example.homesweethome.register.RegisterFailActivity;
import com.example.homesweethome.register.RetrievePasswordActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TimelineActivity extends AppCompatActivity {
    UserCache userCache = UserCache.getInstance();
    float axis = 7;
    float pointSize = 50;

    //private LinearLayout linearLayout;
    private FrameLayout linearLayout;
    //private LinkedList<ImageButton> imageButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        createImageView();
        PointView pointView = new PointView(this);
    }

    public void createImageView(){
        //linearLayout = (LinearLayout) this.findViewById(R.id.timeline_layout);
        linearLayout = (FrameLayout)this.findViewById(R.id.timeline_f_layout);


        float width = 1440;
        float height = 2450;
        float pointSize = 30;
        addButton();


        // testing algorithm of images
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        ArrayList<String> years = new ArrayList<String>();
        Map<Integer,Integer> map = new HashMap<Integer, Integer>();
        ArrayList<TimelineImage> timelineImages = new ArrayList<TimelineImage>();

        Bitmap img1, img2, img3, img4, img5, img6;
        img1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        img2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        img3 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        img4 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        img5 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        img6 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        bitmaps.add(img1);
        bitmaps.add(img2);
        bitmaps.add(img3);
        bitmaps.add(img4);
        bitmaps.add(img5);
        bitmaps.add(img6);

        years.add("1982");
        years.add("1983");
        years.add("1983");
        years.add("1983");
        years.add("1987");
        years.add("1987");



        map = mappingYearInterval(years);
        float margin = 10;

        ArrayList<Float> pointLocation = new ArrayList<Float>();
        pointLocation = PointView.initPoints(height, years, pointSize);
        timelineImages = createButtons(pointLocation, bitmaps, map, years, width, height, pointSize, margin);

        for (TimelineImage t: timelineImages){
            initButton(t, PointView.getImageSize(height, pointSize,years, margin), PointView.getImageSize(height, pointSize,years, margin));
        }
    }

    // calculate buttons' x and y
    public ArrayList<TimelineImage> createButtons(ArrayList<Float> pointLocation,ArrayList<Bitmap> bitmaps, Map<Integer, Integer> map, ArrayList<String> years, float width, float height, float pointSize, float margin){
        ArrayList<TimelineImage> timelineImages = new ArrayList<TimelineImage>();
        for (Bitmap b: bitmaps){
            timelineImages.add(new TimelineImage(b));
        }
        int bitmapNum = 0;
        int buttonNum = 0;
        ArrayList<String> nubYears = new ArrayList<String>();
        nubYears = removeDuplicate(years);

        float distanceFromCenter = 130;

        System.out.println("There are "+nubYears.size()+"years");
        System.out.println("There are "+bitmaps.size()+"images");
        System.out.println("Width is "+width);
        System.out.println("Height is"+height);
        System.out.println("pointLocation is "+pointLocation.toString());
        int pointIndex = 0;
        for (String year_: nubYears){

            int year = Integer.parseInt(year_);
            assert(map.containsKey(year));
            int imgNum = map.get(year);
            buttonNum++;

            float base = pointLocation.get(pointIndex);
            System.out.println("base is "+ base);
            pointIndex++;
            for (int i = 0; i < imgNum; i++){
                //TODO: magic number
                float x = calculateX(bitmapNum, width, distanceFromCenter, PointView.getImageSize(height, pointSize,years, margin));
                float y = calculateY(PointView.getImageSize(height, pointSize,years, margin), base, pointSize, buttonNum, i, 100);
                timelineImages.get(bitmapNum).setX(x);
                timelineImages.get(bitmapNum).setY(y);
                System.out.println("bitmapNum is "+bitmapNum);
                bitmapNum++;
            }

        }
        return timelineImages;
    }

    public static ArrayList<String> removeDuplicate(ArrayList<String> years){
        ArrayList<String> arr = new ArrayList<String>();
        for (String year: years){
            if (!arr.contains(year)){
                arr.add(year);
            }
        }
        return arr;
    }


    // calculate the x of image
    // image's x should be at left and right sepereately
    public float calculateX(int bitmapNum, float width, float distanceFromCenter, float imageWidth){
        if (bitmapNum%2 == 0){
            return width/2 - axis - imageWidth - distanceFromCenter;
            //return width/2 - distanceFromCenter - imageWidth/2;
        }
        return width/2 + distanceFromCenter;
        //return width/2 + distanceFromCenter;
    }

    // calculate the y of image
    public float calculateY(float imageHeight, float base, float pointSize, int pointNum, int imageOrder, float margin){
        return base + imageOrder * imageHeight;
    }

    // add the buttons to the view and draw the buttons
    public void initButton(TimelineImage timelineImage, float imageHeight, float imageWidth){
        Bitmap bitmap = timelineImage.getTimelineImage();
        float x = timelineImage.getTimelineImageX();
        float y = timelineImage.getTimelinImageY();

        ImageButton button = new ImageButton(TimelineActivity.this);
        button.setImageBitmap(bitmap);
        FrameLayout.LayoutParams param1 = new FrameLayout.LayoutParams((int)imageWidth, (int)imageHeight);
        param1.setMargins((int)x,(int)y,0,0);
        button.setLayoutParams(param1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent registerIntent = new Intent(TimelineActivity.this, LoginPage.class);
                startActivity(registerIntent);
            }

        });
        linearLayout.addView(button);
    }


    // a small function testing initButton()
    public void addButton(){
        ImageButton button = new ImageButton(TimelineActivity.this);
        button.setBackgroundResource(R.drawable.ic_launcher_background);
        FrameLayout.LayoutParams param1 = new FrameLayout.LayoutParams(
                100,100);
        param1.setMargins(100,100,0,0);
        button.setLayoutParams(param1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent registerIntent = new Intent(TimelineActivity.this, LoginPage.class);
                startActivity(registerIntent);
            }

        });

        // UserCache Connection of images
        /*byte[] image;
        if (UserCache.getInstance() != null && UserCache.getInstance().getCells()!=null){
            System.out.println("SHOULD DO THE WORK");
            image = UserCache.getInstance().getImagesByCell(0).get(0).getMediumImageByte();
            Bitmap bitmap = ImageProcessor.getInstance().restoreImage(image);

            button.setImageBitmap(bitmap);
        }*/
        linearLayout.addView(button);
    }

    // return a map storing for each year how many artifacts are there
    public Map<Integer, Integer> mappingYearInterval(ArrayList<String> years){
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (String year: years){
            if (map.containsKey(Integer.parseInt(year))){
                map.put(Integer.parseInt(year), map.get(Integer.parseInt(year))+1);
            }else{
                map.put(Integer.parseInt(year),1);
            }
        }
        return map;
    }
}
