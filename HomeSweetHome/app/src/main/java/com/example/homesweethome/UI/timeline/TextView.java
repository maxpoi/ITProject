package com.example.homesweethome.UI.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.homesweethome.UI.timeline.PointView;
import com.example.homesweethome.UI.timeline.TimelineText;

import java.util.ArrayList;

public class TextView extends View implements View.OnClickListener {
    float pointSize = 30;
    float textSize = 100;
    float width = 1440;
    float height = 2450;
    float axis = 7;

    public TextView(Context context) {
        super(context);
    }

    public TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onClick(View view) {

    }

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
        testingArrayR = PointView.initPoints(((float)getHeight()), testingArrayI, pointSize);
        System.out.println("testingArrayR is:" + testingArrayR.toString());
        System.out.println("Width should be"+getWidth());
        System.out.println("Height should be"+getHeight());

        ArrayList<TimelineText> timelineTexts = new ArrayList<TimelineText>();
        ArrayList<Float> pointLocations = PointView.initPoints((float)getHeight(), testingArrayI, pointSize);
        timelineTexts = initText(pointLocations, TimelineActivity.removeDuplicate(testingArrayI), textSize);

        drawText(canvas, timelineTexts, textSize);

    }

    // draw text
    public void drawText(Canvas canvas, ArrayList<TimelineText> timelineTexts, float textSize){
        for (TimelineText t: timelineTexts){
            System.out.println("DRAWING TEXT" + t.getYear());
            Paint textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setFakeBoldText(true);
            textPaint.setTextSize(textSize);

            String str = t.getYear();
            canvas.drawText(str,t.getX(),t.getY(),textPaint);
        }
    }

    public ArrayList<TimelineText> initText(ArrayList<Float> pointLocations, ArrayList<String> nubYears, float textSize){
        ArrayList<TimelineText> timelineTexts = new ArrayList<TimelineText>();
        int nub = 0;
        System.out.println("WIDTH is "+ width);
        for (String text_: nubYears){
            TimelineText text = new TimelineText(text_);
            text.setX(width/2 - textSize - axis);
            text.setY(pointLocations.get(nub));
            timelineTexts.add(text);
            nub++;
        }
        return timelineTexts;
    }
}
