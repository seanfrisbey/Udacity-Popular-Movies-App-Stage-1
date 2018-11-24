package com.persistentdevelopment.watchit.views;

import android.content.Context;
import android.util.AttributeSet;

public class MoviePosterView extends android.support.v7.widget.AppCompatImageView {

    public MoviePosterView(Context context) {
        super(context);
    }

    public MoviePosterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoviePosterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = (int)(width * 1.5);
        setMeasuredDimension(width, height);
    }
}
