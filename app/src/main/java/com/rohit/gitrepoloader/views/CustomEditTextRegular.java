package com.rohit.gitrepoloader.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by oust on 4/22/18.
 */

public class CustomEditTextRegular extends EditText {
    public CustomEditTextRegular(Context context) {
        super(context);
        init(context);
    }

    public CustomEditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public CustomEditTextRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }
    private void init(Context context) {
        Typeface customFont = FontCache.getTypeface("Crimson-Roman.otf", context);
        setTypeface(customFont);
    }

}
