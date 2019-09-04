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

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<Cell> images;
    private Context context;

    public ImageAdapter(Context context, ArrayList<Cell> cells) {
        this.context = context;
        this.images = cells;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_image, parent, false);
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
                Intent intent = new Intent(context, SingleImage.class);
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
            img = (ImageView) view.findViewById(R.id.recycleview_image);
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
