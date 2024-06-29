package com.example.whatflower.ui.picture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatflower.R;
import com.bumptech.glide.Glide;


import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {

    private List<PictureBean> pictureBeans;
    private Context context;

    public PictureAdapter(Context context, List<PictureBean> pictureBeans) {
        this.pictureBeans = pictureBeans;
        this.context = context;
    }

    @NonNull
    @Override
    public PictureAdapter.PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
        return new PictureAdapter.PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureAdapter.PictureViewHolder holder, int position) {
        PictureBean pictureBean = pictureBeans.get(position);
        String imageUrl = pictureBean.getImgUrl();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_bg2)
                .error(R.drawable.ic_bg2)
                .into(holder.pictureImage);
        holder.pictureResult.setText("FlowerName："+pictureBean.result);
        holder.pictureScore.setText("Similarity："+pictureBean.similarity+ "%");
        holder.Detail.setText("Details："+  pictureBean.detail);
    }

    @Override
    public int getItemCount() {
        return pictureBeans.size();
    }

    static class PictureViewHolder extends RecyclerView.ViewHolder {

        ImageView pictureImage;
        TextView pictureResult;
        TextView pictureScore;
        TextView Detail;
        PictureViewHolder(View itemView) {
            super(itemView);
            pictureImage = itemView.findViewById(R.id.item_picture_image);
            pictureResult = itemView.findViewById(R.id.tv_picture_result);
            pictureScore = itemView.findViewById(R.id.tv_picture_score);
            Detail = itemView.findViewById(R.id.tv_details);
        }
    }
}
