package com.codequark.imagemanager;

import android.content.Context;
import android.util.AttributeSet;

public class TouchImageView extends com.ortiz.touchview.TouchImageView {
    public TouchImageView(Context context) {
        super(context);

        setMaxZoom(5.0f);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setMaxZoom(5.0f);
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setMaxZoom(5.0f);
    }
}