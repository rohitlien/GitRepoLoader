package com.rohit.gitrepoloader.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by oust on 4/17/18.
 */

public class CustomTvBold extends TextView {
    public CustomTvBold(Context context) {
        super(context);
        init(context);
    }

    public CustomTvBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTvBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface customFont = FontCache.getTypeface("Crimson-Bold.otf", context);
        setTypeface(customFont);
    }


}
