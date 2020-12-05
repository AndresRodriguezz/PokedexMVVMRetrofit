package com.codequark.imagemanager.interfaces;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public interface ImageListener {
    default void onDrawableLoaded(@NonNull Drawable drawable) {

    }

    default void onBitmapLoaded(@NonNull Bitmap bitmap) {

    }
}