package com.example.homesweethome.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homesweethome.Cell;
import com.example.homesweethome.R;
import com.example.homesweethome.UserCache;
import com.example.homesweethome.register.RegisterFailActivity;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {
    //UserCache cache =

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        PointView pointView = new PointView(this);
    }

    public void jumpPage(){
        Toast.makeText(this, "jump to individual artifact page", Toast.LENGTH_SHORT).show();
    }


}
