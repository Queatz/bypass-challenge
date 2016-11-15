package com.bypassmobile.octo.image;


import android.content.Context;

import com.squareup.picasso.Picasso;

public final class ImageLoader {

    private static Picasso singleton;

    public static Picasso getImageLoader(Context context) {
        if(singleton == null) {
            singleton = new Picasso.Builder(context).memoryCache(new ImageCache(context)).build();
        }

        return singleton;
    }
}
