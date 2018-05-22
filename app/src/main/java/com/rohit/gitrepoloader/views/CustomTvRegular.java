package com.rohit.gitrepoloader.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by oust on 4/17/18.
 */

public class CustomTvRegular extends TextView {
    public CustomTvRegular(Context context) {
        super(context);
    }

    public CustomTvRegular(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTvRegular(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        Typeface customFont = FontCache.getTypeface("Crimson-Roman.otf", context);
        setTypeface(customFont);
    }


}
