package com.example.homesweethome.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.R;
import com.example.homesweethome.UI.SingleArtifactPage;

import java.io.File;
import java.util.List;

public class ArtifactAdapter extends RecyclerView.Adapter<ArtifactAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView title;
        private TextView date;
        private TextView desc;

        public ViewHolder(View view) {
            super (view);
            img = (ImageView) view.findViewById(R.id.recycleview_artifact_image);
            title = view.findViewById(R.id.recycleview_artifact_title);
            date = view.findViewById(R.id.recycleview_artifact_date);
            desc = view.findViewById(R.id.recycleview_artifact_desc);
        }
    }

    private List<Artifact> artifacts;
    private Context context;

    public ArtifactAdapter(Context context) { this.context = context; }

    @NonNull
    @Override
    public ArtifactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleview_artifact, parent, false);
        return new ArtifactAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        setImage(holder.img, position);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(context, SingleArtifactPage.class);
                intent.putExtra(DataTag.ARTIFACT_ID.toString(), artifacts.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.title.setText(artifacts.get(position).getTitle());
        holder.date.setText(artifacts.get(position).getDate());
        holder.desc.setText(artifacts.get(position).getDesc());
    }

    @Override
    public int getItemCount() { return artifacts == null ? 0 : artifacts.size(); }

    private void setImage(ImageView img, int position) {
        String imagePath = artifacts.get(position).getCoverImagePath();
        if(imagePath == null || !(new File(imagePath)).exists())
            return ;

        Bitmap bitmap = ((HomeSweetHome)context).getImageProcessor().decodeFileToLowBitmap(imagePath);
        Glide.with(this.context).asBitmap().load(bitmap).into(img);
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
        notifyDataSetChanged();
    }
}
