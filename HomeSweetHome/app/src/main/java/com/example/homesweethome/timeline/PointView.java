package com.example.homesweethome.timeline;

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

import com.example.homesweethome.UserCache;
import com.example.homesweethome.register.RegisterFailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.bumptech.glide.Glide.init;

public class PointView extends View implements View.OnClickListener {
    ArrayList<String> years = UserCache.getSortedDate();


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
        // testing functions
        ArrayList<String> testingArrayI = new ArrayList<String>();
        ArrayList<Float> testingArrayR = new ArrayList<Float>();
        testingArrayI.add("1982");
        testingArrayI.add("1983");
        testingArrayI.add("1983");
        testingArrayI.add("1983");
        testingArrayI.add("1987");
        testingArrayI.add("1987");
        testingArrayR = initPoints(((float)getHeight()), testingArrayI);
        System.out.println("testingArrayR is:" + testingArrayR.toString());


        // draw circle
        drawPoints(canvas,testingArrayR);


        // draw text
        // 创建画笔
        Paint textPaint = new Paint();
        // 设置颜色
        textPaint.setColor(Color.BLACK);
        // 设置样式
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(50);

        // 文本(要绘制的内容)
        String str = "TEXTTEXT";
        // 参数分别为 (文本 基线x 基线y 画笔)
        canvas.drawText(str,230,600,textPaint);


    }

    private void drawPoints(Canvas canvas, ArrayList<Float> heights)
    {
        canvas.drawColor(Color.RED);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        paint.setColor(Color.parseColor("#008577"));
        //canvas.drawCircle(getWidth()/2, getHeight()/2,100,paint);
        for (float height: heights){
            canvas.drawCircle(getWidth()/2, height,50,paint);

        }
    }

    public ArrayList<Float> initPoints(Float height, ArrayList<String> years){
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
        float startHeight = 180;
        float initHeight = startHeight + 25;
        float finalHeight = height - 25;
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
        System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");

    }
}
