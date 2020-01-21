package com.example.cardapplication;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

public class CardViewHolder extends RecyclerView.ViewHolder {

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    private ImageView imageView;

    public CardViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_card);
    }
}
