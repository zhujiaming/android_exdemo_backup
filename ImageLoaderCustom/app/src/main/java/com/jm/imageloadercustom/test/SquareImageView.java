package com.jm.imageloadercustom.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by zhujiaming on 17/8/6.
 */

public class SquareImageView extends AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
