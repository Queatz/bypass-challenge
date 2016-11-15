package com.bypassmobile.octo.image;


import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * An image cache.
 */
public class ImageCache implements Cache {

    private Map<String,Bitmap> cacheMap = new LinkedHashMap<String, Bitmap>();
    private int maxCacheSize;

    public ImageCache(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        // Use up to half of available memory
        maxCacheSize = (int) memoryInfo.availMem / 2;
    }

    @Override
    public Bitmap get(String stringResource) {
        return cacheMap.get(stringResource);
    }

    @Override
    public void set(String stringResource, Bitmap bitmap) {
        cacheMap.put(stringResource, bitmap);
    }

    @Override
    public int size() {
        return cacheMap.size();
    }

    @Override
    public int maxSize() {
        return maxCacheSize;
    }

    @Override
    public void clear() {
        cacheMap.clear();
    }
}
