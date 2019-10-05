package com.example.homesweethome.UI.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.homesweethome.R;

import java.util.Collections;

public class MultipleImageView extends View implements View.OnClickListener{
    public MultipleImageView(Context context) {
        super(context);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.img_1);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getWidth(),getHeight());
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        //        400);
        imageView.setLayoutParams(params);
        imageView.setBackgroundColor(Color.BLUE);
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(imageView.getLayoutParams());
        margin.leftMargin=40;
        margin.topMargin=40;



        //textView testing
        TextView textView = new TextView(context);
        textView.setText("asdfkjasdl");
        FrameLayout.LayoutParams params__ = new FrameLayout.LayoutParams(getWidth(),getHeight());
        textView.setLayoutParams(params__);
        ViewGroup.MarginLayoutParams margin__ = new ViewGroup.MarginLayoutParams(textView.getLayoutParams());
        margin__.leftMargin=40;
        margin__.topMargin=40;


        //margin.rightMargin=screenWidth-400-margin.leftMargin-num;
        //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        //imageView.setLayoutParams(layoutParams);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //relativeLayout_add_imageview.addView(imageView);
    }

    public MultipleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultipleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MultipleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onDraw(Canvas canvas){


    }

}
