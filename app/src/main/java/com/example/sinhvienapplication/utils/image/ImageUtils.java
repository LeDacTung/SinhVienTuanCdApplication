package com.example.sinhvienapplication.utils.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageUtils {
    public static void loadImage(Context context, CircleImageView imageView, String url){
        Glide.with(context).load(url).into(imageView);
    }

    public static void loadImage(Context context, ImageView imageView, String url){
        Glide.with(context).load(url).into(imageView);
    }
}
