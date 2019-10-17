package com.example.homesweethome.UI.timeline;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.HelperClasses.ImageProcessor;
import com.example.homesweethome.UI.HomePage;
import com.example.homesweethome.UI.LoginPage;
import com.example.homesweethome.R;
import com.example.homesweethome.ViewModels.ArtifactListViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineActivity extends AppCompatActivity {

    float axis = 7;
    final float pointSize = 100;
    final float width = 1440;
    final float height = 7650;
    private ArtifactListViewModel artifactListViewModel;
    private List<Artifact> martifacts;
    private FrameLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_old);

        // return button on top left
        final ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Timeline");
        bar.setDisplayHomeAsUpEnabled(true);

        artifactListViewModel = new ViewModelProvider(this).get(ArtifactListViewModel.class);
        artifactListViewModel.getArtifacts().observe(this, new Observer<List<Artifact>>() {
            @Override
            public void onChanged(List<Artifact> artifacts) {
                if (artifacts!=null){
                    generateTimeline(artifacts);
                }
                else{}
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openHomePage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openHomePage() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }
    public void generateTimeline(List<Artifact> artifacts){
        createImageView(artifacts);
        createButtonView(artifacts);
        createTextView(artifacts);
        generateStartingYear(artifacts);
    }

    public String getStartingYear(List<Artifact> martifacts){
        if (martifacts == null){
            return null;
        }
        String year = martifacts.get(0).getDate().substring(0,4);
        return year;
    }

    // Dynamically generate the startin year to tell the user when the story started
    public void generateStartingYear(List<Artifact> martifacts){
        TextView start_year = (TextView)this.findViewById(R.id.starting_year);
        String year = getStartingYear(martifacts);
        start_year.setText(year);

    }

    // get the year from date of artifact
    public ArrayList<String> getYearFromDate(List<Artifact> martifacts){
        ArrayList<String> years = new ArrayList<String>();
        if (martifacts == null){
            return null;
        }
        for (Artifact a: martifacts){
            years.add(a.getDate().substring(0,4));
        }
        return years;
    }

    // turn image path into bitmap
    public ArrayList<Bitmap> getBitmaps(List<Artifact> martifacts){
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        for (Artifact a: martifacts){
            assert(a!=null);
            Bitmap bitmap = ImageProcessor.getInstance(a.getCoverImagePath()).decodeFileToLowBitmap(a.getCoverImagePath());
            if (bitmap == null){
                bitmaps.add(null);
            }else{
                bitmaps.add(bitmap);
            }

        }
        return bitmaps;
    }

    // Generate images
    public void createImageView(List<Artifact> artifacts){
        if (artifacts == null){
            return ;
        }
        linearLayout = (FrameLayout)this.findViewById(R.id.timeline_f_layout);

        // testing algorithm of images
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        ArrayList<String> years = new ArrayList<String>();
        Map<Integer,Integer> map = new HashMap<Integer, Integer>();
        ArrayList<TimelineImage> timelineImages = new ArrayList<TimelineImage>();

        years = getYearFromDate(artifacts);
        bitmaps = getBitmaps(artifacts);

        map = mappingYearInterval(years);
        float margin = 5;

        ArrayList<Float> pointLocation = new ArrayList<Float>();
        pointLocation = initPoints(height, years, pointSize);
        if (pointLocation!=null){
        }
        timelineImages = createImageLocs(pointLocation, bitmaps, map, years, width, height, pointSize, margin);

        for (TimelineImage t: timelineImages){
            initButton(t, getImageSize(height, pointSize,years, margin), getImageSize(height, pointSize,years, margin));
        }
    }

    // calculate buttons' x and y
    public ArrayList<TimelineImage> createImageLocs(ArrayList<Float> pointLocation,ArrayList<Bitmap> bitmaps, Map<Integer, Integer> map, ArrayList<String> years, float width, float height, float pointSize, float margin){
        ArrayList<TimelineImage> timelineImages = new ArrayList<TimelineImage>();
        for (Bitmap b: bitmaps){
            timelineImages.add(new TimelineImage(b));
        }
        int bitmapNum = 0;
        int buttonNum = 0;
        ArrayList<String> nubYears = new ArrayList<String>();
        nubYears = removeDuplicate(years);

        float distanceFromCenter = 130;

        int pointIndex = 0;
        for (String year_: nubYears){

            int year = Integer.parseInt(year_);
            assert(map.containsKey(year));
            int imgNum = map.get(year);
            buttonNum++;

            float base = pointLocation.get(pointIndex);
            pointIndex++;
            for (int i = 0; i < imgNum; i++){
                //TODO: magic number
                float x = calculateX(bitmapNum, width, distanceFromCenter, getImageSize(height, pointSize,years, margin));
                float y = calculateY(getImageSize(height, pointSize,years, margin), base, pointSize, buttonNum, i, 100);
                timelineImages.get(bitmapNum).setX(x);
                timelineImages.get(bitmapNum).setY(y);
                bitmapNum++;
            }

        }
        return timelineImages;
    }

    // remove duplicate years from a list of years
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
            return width/2 - axis - imageWidth - distanceFromCenter; }
        return width/2 + distanceFromCenter;
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

    // Generate buttons
    public void createButtonView(List<Artifact> artifacts){
        if (artifacts == null){
            return ;
        }
        linearLayout = (FrameLayout)this.findViewById(R.id.timeline_f_layout);

        ArrayList<String> years = new ArrayList<String>();
        years = getYearFromDate(artifacts);
        ArrayList<String> nubYears = removeDuplicate(years);

        ArrayList<Float> pointHeights = new ArrayList<Float>();
        pointHeights = initPoints(height, years, pointSize);
        if (pointHeights == null){
            return ;
        }
        int num = 0;
        for (float f: pointHeights){
            addButton(f,nubYears.get(num));
            num++;
        }

    }

    // draw button
    public void addButton(float height, String year){
        Button btnShow = new Button(this);
        btnShow.setText(year);
        btnShow.setBackgroundColor(45);
        btnShow.setBackgroundResource(R.drawable.ic_launcher_background);
        FrameLayout.LayoutParams rel_btn = new FrameLayout.LayoutParams(30,30);
        rel_btn.setMargins((int)width/2 + (int)axis/2 - (int)pointSize/2 ,(int)height,0,0);

        btnShow.setLayoutParams(rel_btn);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TimelineActivity.this, "ttt", Toast.LENGTH_LONG).show();
            }
        });

        // Add Button to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(btnShow);
        }
    }

    // generate points on the time line
    public ArrayList<Float> initPoints(Float height, ArrayList<String> years, float pointSize){
        //assert(years != null);
        if (years.size() == 0){
            return null;
        }
        Collections.sort(years);
        String minYear_ = years.get(0);
        String maxYear_ = years.get(years.size() - 1);
        ArrayList<Float> points = new ArrayList<Float>();
        if (minYear_.equals(maxYear_)){
            points.add(height/2);
            return points;
        }
        int minYear = Integer.parseInt(minYear_);
        int maxYear = Integer.parseInt(maxYear_);
        float initHeight = 100 + pointSize/2;
        float finalHeight = height - initHeight;
        //System.out.println("Final Height is -- " + finalHeight);
        //System.out.println("DuplicatedYears is -- " + getDuplicateYears(years));
        float unit = (finalHeight - initHeight) / (maxYear - minYear + getDuplicateYears(years));
        Map<Integer,Integer> checkMap = new HashMap<Integer, Integer>();
        float accumulation = 0;
        int last = 0;
        for (String year: years){
            int yearNum = Integer.parseInt(year);
            if (checkMap == null || checkMap.isEmpty()){
                points.add(initHeight);
                checkMap.put(yearNum, 1);
                last = yearNum;
            }
            else if (checkMap.containsKey(yearNum)){
                //points.add(initHeight + accumulation);
                accumulation += unit;
                //checkMap.put(yearNum, checkMap.get(yearNum) + 1);
                last = yearNum;
            }
            else if (!checkMap.containsKey(yearNum)){
                points.add(initHeight + accumulation + (yearNum - last) * unit);
                initHeight = initHeight + accumulation + (yearNum - last) * unit;
                //accumulation = 0;
                checkMap.put(yearNum, 1);
                last = yearNum;
            }
            else{
                // ignore
            }
        }
        return points;
    }

    // get years corresponding to the artifacts
    public int getDuplicateYears(ArrayList<String> years){
        int num = 0;
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String year: years){
            if (map.containsKey(year)){
                num++;
            }
            else{
                map.put(year, 1);
            }
        }
        return num;
    }

    // calculate what the image size should be
    public float getImageSize(float height, float pointSize, ArrayList<String> years, float margin){
        assert(years != null);
        Collections.sort(years);
        String minYear_ = years.get(0);
        String maxYear_ = years.get(years.size() - 1);
        int minYear = Integer.parseInt(minYear_);
        int maxYear = Integer.parseInt(maxYear_);
        if (maxYear - minYear == 0){
            //magic number
            return 300;
        }

        float initHeight = 100 + pointSize/2;
        float finalHeight = height - initHeight;
        float unit = (finalHeight - initHeight) / (maxYear - minYear + getDuplicateYears(years));
        if (unit - margin < 70){
            return 70;
        }
        return unit - margin;
    }

    // Generate year texts
    public void createTextView(List<Artifact> artifacts){
        if (artifacts == null){
            return ;
        }
        linearLayout = (FrameLayout)this.findViewById(R.id.timeline_f_layout);

        ArrayList<String> years = new ArrayList<String>();
        years = getYearFromDate(artifacts);
        ArrayList<String> nubYears = removeDuplicate(years);

        ArrayList<Float> pointHeights = new ArrayList<Float>();
        pointHeights = initPoints(height, years, pointSize);
        if (pointHeights == null){
            return ;
        }
        int num = 0;
        for (float f: pointHeights){
            addText(f,nubYears.get(num));
            num++;
        }

    }

    // write text
    public void addText(float height, String year){
        TextView textView = new TextView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(300,190);
        textView.setLayoutParams(params);
        textView.setTextSize(30);
        textView.setText(year);
        textView.setTextColor(Color.BLUE);
        params.setMargins(40 ,(int)height,0,0);
        textView.setLayoutParams(params);
        if (linearLayout != null) {
            System.out.println("kkkkkkkkkkkkkkkkkkkkk");
            linearLayout.addView(textView);
        }
    }

}
