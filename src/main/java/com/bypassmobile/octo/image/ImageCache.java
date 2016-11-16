package com.bypassmobile.octo.image;


import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Cache;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * An image cache.
 */
public class ImageCache implements Cache {

    private final int CACHE_EXPIRATION_TIME_MS = 60 * 60 * 1000; // 1 hour

    private class CacheEntry {
        Bitmap bitmap;
        Date expiration;

        public CacheEntry(Bitmap bitmap) {
            this.bitmap = bitmap;
            this.expiration = new Date(new Date().getTime() + CACHE_EXPIRATION_TIME_MS);
        }
    }

    private Map<String, CacheEntry> cacheMap = new LinkedHashMap<String, CacheEntry>();
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
        CacheEntry cacheEntry = cacheMap.get(stringResource);

        if (cacheEntry == null) {
            return null;
        }

        if (new Date().after(cacheEntry.expiration)) {
            invalidate(stringResource);
            return null;
        }

        return cacheEntry.bitmap;
    }

    private void invalidate(String stringResource) {
        cacheMap.remove(stringResource);
    }

    @Override
    public void set(String stringResource, Bitmap bitmap) {
        cacheMap.put(stringResource, new CacheEntry(bitmap));
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
