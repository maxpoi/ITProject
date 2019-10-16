package com.example.homesweethome.UI.timeline;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class TimelineImage {
    private Bitmap image;
    private float x;
    private float y;

    public TimelineImage(Bitmap image){
        this.image = image;
    }

    public void setTimelineImage(Bitmap image){this.image = image;}

    public void setX(Float x){this.x = x;}

    public void setY(Float y){
        this.y = y;
    }

    public Bitmap getTimelineImage(){
        return this.image;
    }

    public Float getTimelineImageX(){
        return this.x;
    }

    public Float getTimelinImageY(){
        return this.y;
    }
}
