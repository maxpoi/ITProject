package com.example.homesweethome;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<Cell> images;
    private Context context;
    private String class_name;      // this is used to check where the adapter is used, so that it can jump to next activity accordingly
    private int viewID;
    private int layoutID;

    public ImageAdapter(Context context, String class_name, ArrayList<Cell> cells, int viewID, int layoutID) {
        this.context = context;
        this.images = cells;
        this.viewID = viewID;
        this.layoutID = layoutID;
        this.class_name = class_name;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false);
        return new ImageAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImage(position, holder.img);
        // to do
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (class_name.equals(SingleArtifactPage.class.getName())) {
                    intent = new Intent(context, SingleImagePage.class);
                } else {
                    intent = new Intent(context, SingleArtifactPage.class);
                }

                sendInfo(position, intent);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public ViewHolder(View view) {
            super (view);
            img = (ImageView) view.findViewById(viewID);
        }
    }

    private void setImage(int position, ImageView img) {
        if (img != null) {
            Glide.with(this.context).load(this.images.get(position).getTest_src()).into(img);
        }
    }

    private Intent sendInfo(int position, Intent intent) {
        // to do

        // test
        intent.putExtra("id", images.get(position).getTest_src());
        intent.putExtra("title", "Homemade Coffee");
        intent.putExtra("desc", "This is a test description. Let's see how long it can be. " +
                "Let's see how long it can be.Let's see how long it can be.Let's see how long it can be." +
                "Let's see how long it can be.Let's see how long it can be.Let's see how long it can be." +
                "Let's see how long it can be.Let's see how long it can be.Let's see how long it can be." +
                "Let's see how long it can be.Let's see how long it can be.Let's see how long it can be." +
                "Let's see how long it can be.Let's see how long it can be.");
        return intent;
    }
}
