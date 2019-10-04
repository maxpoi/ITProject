package com.example.homesweethome.UI.timeline;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homesweethome.R;

public class TimelineActivity extends AppCompatActivity {
    //UserCache cache =

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        PointView pointView = new PointView(this);
    }




}
