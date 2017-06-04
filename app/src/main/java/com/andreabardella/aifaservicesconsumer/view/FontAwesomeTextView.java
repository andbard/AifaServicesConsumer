package com.andreabardella.aifaservicesconsumer.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import com.andreabardella.aifaservicesconsumer.util.font.FontManager;

/**
 * TextView that can use <a href=http://fontawesome.io/>Font Awesome</a> fonts
 */
public class FontAwesomeTextView extends android.support.v7.widget.AppCompatTextView {

    public FontAwesomeTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public FontAwesomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public FontAwesomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    // uncomment if extending TextView
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public FontAwesomeTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        applyCustomFont(context);
//    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontManager.getTypefaceFromAssets(context, FontManager.FONT_AWESOME_TTF);
        setTypeface(customFont);
    }

}
