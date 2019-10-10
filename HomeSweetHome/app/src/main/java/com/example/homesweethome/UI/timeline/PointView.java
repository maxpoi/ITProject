package com.example.homesweethome.UI.timeline;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

//import com.example.homesweethome.UserCache;
//import com.example.homesweethome.register.RegisterFailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.bumptech.glide.Glide.init;

public class PointView extends View implements View.OnClickListener {
    //ArrayList<String> years = UserCache.getSortedYear();


    public PointView(Context context) {

        super(context);

    }


    public PointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

/*
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //Check if the x and y position of the touch is inside the bitmap
                if( x > bitmapXPosition && x < bitmapXPosition + bitmapWidth && y > bitmapYPosition && y < bitmapYPosition + bitmapHeight )
                {
                    //Bitmap touched
                }
                return true;
        }
        return false;
    }*/

    @Override
    protected void onDraw(Canvas canvas){
        float pointSize = 30;

        // testing functions
        ArrayList<String> testingArrayI = new ArrayList<String>();
        ArrayList<Float> testingArrayR = new ArrayList<Float>();
        testingArrayI.add("1982");
        testingArrayI.add("1983");
        testingArrayI.add("1983");
        testingArrayI.add("1983");
        testingArrayI.add("1987");
        testingArrayI.add("1987");
        testingArrayR = initPoints(((float)getHeight()), testingArrayI, pointSize);
        System.out.println("testingArrayR is:" + testingArrayR.toString());
        System.out.println("Width should be"+getWidth());
        System.out.println("Height should be"+getHeight());


        // draw circle
        drawPoints(canvas,testingArrayR, pointSize);


        // draw text
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(50);

        String str = "TEXTTEXT";
        canvas.drawText(str,230,600,textPaint);


    }

    private void drawPoints(Canvas canvas, ArrayList<Float> heights, float pointSize)
    {
        canvas.drawColor(Color.RED);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        paint.setColor(Color.parseColor("#008577"));
        //canvas.drawCircle(getWidth()/2, getHeight()/2,100,paint);
        for (float height: heights){
            canvas.drawCircle(getWidth()/2, height,pointSize,paint);

        }
    }

    public static ArrayList<Float> initPoints(Float height, ArrayList<String> years, float pointSize){
        assert(years != null);
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
        //TODO: magic number
        //float startHeight = 200;
        float initHeight = 100 + pointSize/2;
        float finalHeight = height - initHeight;
        System.out.println("Final Height is -- " + finalHeight);
        System.out.println("DuplicatedYears is -- " + getDuplicateYears(years));
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
                checkMap.put(yearNum, 1);
                last = yearNum;
            }
            else{
                // ignore
            }
        }
        return points;
    }

    public static float getImageSize(float height, float pointSize, ArrayList<String> years, float margin){
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
        float unit = (finalHeight - initHeight) / (maxYear - minYear + getDuplicateYears(years)) - pointSize;
        return unit - margin;
    }

    public static int getDuplicateYears(ArrayList<String> years){
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


    @Override
    public void onClick(View view) {

    }
}
