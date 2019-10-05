package com.example.homesweethome.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.R;
import com.example.homesweethome.UI.SingleImagePage;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public ViewHolder(View view) {
            super (view);
            img = (ImageView) view.findViewById(R.id.recycleview_image);
        }
    }

    private List<Image> images;
    private Context context;

    public ImageAdapter(Context context) { this.context = context; }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleview_image, parent, false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImage(holder.img, position);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(context, SingleImagePage.class);
                intent.putExtra("imagePath", images.get(position).getHighResImagePath());
                intent.putExtra("artifactId", images.get(position).getArtifactId());
                intent.putExtra("imageId", images.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return images == null ? 0 : images.size(); }

    private void setImage(ImageView img, int position) {
        Bitmap imageBitmap = images.get(position).getMediumImageBitmap();
        if (imageBitmap == null) {
            String imagePath = images.get(position).getMediumResImagePath();
            imageBitmap = ((HomeSweetHome)context).getImageProcessor().decodeFileToMedium(imagePath);
        }

        Glide.with(this.context).asBitmap().load(imageBitmap).into(img);
    }

    public void setImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }
}
