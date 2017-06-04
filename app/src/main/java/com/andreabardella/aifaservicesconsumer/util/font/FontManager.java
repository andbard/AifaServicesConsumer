package com.andreabardella.aifaservicesconsumer.util.font;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;

/**
 * A typeface, also known as font family, is a set of one or more fonts
 * each composed of glyphs (an elemental symbol within an agreed set of symbols,
 * intended to represent a readable character for the purposes of writing)
 * that share common design features.
 * <br>
 * Inspired by
 * <a href=https://code.tutsplus.com/tutorials/how-to-use-fontawesome-in-an-android-app--cms-24167>this tutorial</a>,
 * <a href=http://stackoverflow.com/questions/16901930/memory-leaks-with-custom-font-for-set-custom-font/16902532#16902532>this observation/tip</a> and
 * <a href=https://futurestud.io/tutorials/custom-fonts-on-android-extending-textview>this tutorial</a>
 */
public class FontManager {

    public static final String FONT_AWESOME_TTF = "fontawesome-webfont.ttf";

    /**
     * Get the font family represented by the specified file in the <code>assets</code> folder
     *
     * @param context the application context
     * @param typefaceFileName the font family filename
     * @return the {@link Typeface} related to the provided resource or null
     */
    public static Typeface getTypefaceFromAssets(Context context, String typefaceFileName) {
        return FontCache.getFromAssets(context, typefaceFileName);
    }

    /**
     * Get the font family represented by the specified file
     *
     * @param typefaceFilePath the font family filepath
     * @return the {@link Typeface} related to the provided resource or null
     */
    public static Typeface getTypefaceFromFile(String typefaceFilePath) {
        return FontCache.getFromFile(typefaceFilePath);
    }

    /**
     * Get the font family represented by the specified file
     *
     * @param typefaceFile the font family file
     * @return the {@link Typeface} related to the provided resource or null
     */
    public static Typeface getTypefaceFromFile(File typefaceFile) {
        return FontCache.getFromFile(typefaceFile);
    }

    /**
     * Icons are often contained in a single ViewGroup, such as a RelativeLayout or a LinearLayout.
     * This method climbs the tree of a given XML parent
     * and recursively overrides the typeface of each TextView it finds
     */
    public static void setTypefaceForEachContainedTextView(@NonNull View view, Typeface typeface) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i=0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setTypefaceForEachContainedTextView(child, typeface);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }
    }

    /**
     * Act as a cache in order to do not load the same font family
     * more than once from assets or file
     */
    private static class FontCache {

        private enum TypefaceSource {
            ASSETS, FILE;
        }

        private static HashMap<String, Typeface> fontCache = new HashMap<>();

        static Typeface getFromAssets(Context context, String path) {
            if (path != null) {
                return get(TypefaceSource.ASSETS, context, path);
            }
            return null;
        }

        static Typeface getFromFile(File file) {
            if (file != null) {
                return getFromFile(file.getAbsolutePath());
            }
            return null;
        }

        static Typeface getFromFile(String path) {
            return get(TypefaceSource.FILE, null, path);
        }

        private static Typeface get(TypefaceSource source, Context context, String name) {
            Typeface typeface = fontCache.get(name);
            if (typeface == null) {
                try {
                    switch (source) {
                        case ASSETS:
                            typeface = Typeface.createFromAsset(context.getAssets(), name);
                            break;
                        case FILE:
                            typeface = Typeface.createFromFile(name);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    return null;
                }
                fontCache.put(name, typeface);
            }
            return typeface;
        }

    }

}
