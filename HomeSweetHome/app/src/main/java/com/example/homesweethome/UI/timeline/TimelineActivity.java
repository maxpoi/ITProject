package com.example.homesweethome.UI.timeline;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.homesweethome.AppDataBase.Entities.Artifact;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.ImageProcessor;
import com.example.homesweethome.UI.HomePage;
import com.example.homesweethome.R;
import com.example.homesweethome.UI.SingleArtifactPage;
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
    private FrameLayout frameLayout;


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
        generateWelcomeText(artifacts);
    }

    public String getStartingYear(List<Artifact> martifacts){
        if (martifacts == null){
            return null;
        }
        if (martifacts.size()<=0){
            return null;
        }
        String year = martifacts.get(0).getDate().substring(0,4);
        return year;
    }

    public void generateWelcomeText(List<Artifact> artifacts){

        if (artifacts.size()<=0){
            TextView start_year = (TextView)this.findViewById(R.id.welcome_text);
            start_year.setTextSize(20);
            start_year.setText("You have no artifacts added yet. Start your story by adding one.");
        }
        else{}
    }

    // Dynamically generate the startin year to tell the user when the story started
    public void generateStartingYear(List<Artifact> martifacts){
        TextView start_year = (TextView)this.findViewById(R.id.starting_year);
        String year = getStartingYear(martifacts);
        start_year.setText(year);

    }

    // get the year from date of artifact
    public ArrayList<String> getYearFromDate(List<Artifact> martifacts){
        //ArrayList<String> years = new ArrayList<String>();
        if (martifacts == null){
            return null;
        }
        //for (Artifact a: martifacts){
        //    years.add(a.getDate().substring(0,4));
        //}
        ArrayList<Integer> ints = new ArrayList<>();
        for (Artifact a: martifacts){
            ints.add(Integer.parseInt(a.getDate().substring(0,4)));
        }
        Collections.sort(ints);
        ArrayList<String> years = new ArrayList<String>();
        for (int b: ints){
            years.add(String.valueOf(b));
        }
        return years;
    }

    // turn image path into bitmap
    public ArrayList<Bitmap> getBitmaps(List<Artifact> martifacts){
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        List<Artifact> sortedArtifacts = new ArrayList<Artifact>();
        for (Artifact a: martifacts){
            sortedArtifacts.add(a);
        }
        Collections.sort(sortedArtifacts);
        for (Artifact a: sortedArtifacts){
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
    private void createImageView(List<Artifact> artifacts){
        if (artifacts == null){
            return ;
        }
        frameLayout = (FrameLayout)this.findViewById(R.id.timeline_f_layout);

        // testing algorithm of images
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        ArrayList<String> years = new ArrayList<String>();
        Map<Integer,Integer> map = new HashMap<Integer, Integer>();
        ArrayList<TimelineImage> timelineImages = new ArrayList<TimelineImage>();

        years = getYearFromDate(artifacts);
        bitmaps = getBitmaps(artifacts);
        ArrayList<Integer> ids = getIds(artifacts);

        map = mappingYearInterval(years);
        float margin = 5;

        ArrayList<Float> pointLocation = new ArrayList<Float>();
        pointLocation = initPoints(height, years, pointSize);
        if (pointLocation!=null){
        }
        timelineImages = createImageLocs(ids,pointLocation, bitmaps, map, years, width, height, pointSize, margin);

        for (TimelineImage t: timelineImages){
            initButton(t, getImageSize(height, pointSize,years, margin), getImageSize(height, pointSize,years, margin));
        }
    }

    private ArrayList<Integer> getIds(List<Artifact> artifacts) {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        if (artifacts==null){return null;}
        for (Artifact a: artifacts){
            ids.add(a.getId());
        }
        return ids;
    }

    // calculate buttons' x and y
    private ArrayList<TimelineImage> createImageLocs(ArrayList<Integer> ids, ArrayList<Float> pointLocation,ArrayList<Bitmap> bitmaps, Map<Integer, Integer> map, ArrayList<String> years, float width, float height, float pointSize, float margin){
        ArrayList<TimelineImage> timelineImages = new ArrayList<TimelineImage>();
        for (int i = 0; i < ids.size(); i++){
            timelineImages.add(new TimelineImage(bitmaps.get(i),ids.get(i)));
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
    private static ArrayList<String> removeDuplicate(ArrayList<String> years){
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
    private float calculateX(int bitmapNum, float width, float distanceFromCenter, float imageWidth){
        if (bitmapNum%2 == 0){
            return width/2 - axis - imageWidth - distanceFromCenter; }
        return width/2 + distanceFromCenter;
    }

    // calculate the y of image
    private float calculateY(float imageHeight, float base, float pointSize, int pointNum, int imageOrder, float margin){
        return base + imageOrder * imageHeight;
    }

    // add the buttons to the view and draw the buttons
    private void initButton(final TimelineImage timelineImage, float imageHeight, float imageWidth){
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
                //Intent registerIntent = new Intent(TimelineActivity.this, LoginPage.class);
                //startActivity(registerIntent);
                String action_return = "visit_single_artifact";
                Intent intent;
                intent = new Intent(getApplicationContext(), SingleArtifactPage.class);
                intent.putExtra(DataTag.ARTIFACT_ID.toString(), timelineImage.getId());
                intent.putExtra(DataTag.SINGLE_ARTIFACT.toString(), action_return);
                getApplicationContext().startActivity(intent);
            }

        });
        frameLayout.addView(button);
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
    private void createButtonView(List<Artifact> artifacts){
        if (artifacts == null){
            return ;
        }
        frameLayout = (FrameLayout)this.findViewById(R.id.timeline_f_layout);

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
    private void addButton(float height, String year){
        Button btnShow = new Button(this);
        btnShow.setText(year);
        btnShow.setBackgroundColor(943855);
        btnShow.setBackgroundResource(R.drawable.ic_launcher_background);
        FrameLayout.LayoutParams rel_btn = new FrameLayout.LayoutParams(30,30);
        rel_btn.setMargins((int)width/2 + (int)axis/2 - (int)pointSize/2 ,(int)height,0,0);

        btnShow.setLayoutParams(rel_btn);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO:
                Toast.makeText(TimelineActivity.this, "ttt", Toast.LENGTH_LONG).show();
            }
        });

        // Add Button to LinearLayout
        if (frameLayout != null) {
            frameLayout.addView(btnShow);
        }
    }

    // generate points on the time line
    private ArrayList<Float> initPoints(Float height, ArrayList<String> years, float pointSize){
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
    private int getDuplicateYears(ArrayList<String> years){
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
    private float getImageSize(float height, float pointSize, ArrayList<String> years, float margin){
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
        else if (unit - margin > 500){
            return 200;
        }
        return unit - margin;
    }

    // Generate year texts
    private void createTextView(List<Artifact> artifacts){
        if (artifacts == null){
            return ;
        }
        frameLayout = (FrameLayout)this.findViewById(R.id.timeline_f_layout);

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
    private void addText(float height, String year){
        TextView textView = new TextView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(300,190);
        textView.setLayoutParams(params);
        textView.setTextSize(30);
        textView.setText(year);
        textView.setTextColor(getResources().getColor(R.color.timelineIntro));
        params.setMargins(40 ,(int)height,0,0);
        textView.setLayoutParams(params);
        if (frameLayout != null) {
            frameLayout.addView(textView);
        }
    }

}
