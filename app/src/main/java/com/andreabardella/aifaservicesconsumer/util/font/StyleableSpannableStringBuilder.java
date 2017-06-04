package com.andreabardella.aifaservicesconsumer.util.font;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Toast;

public class StyleableSpannableStringBuilder extends SpannableStringBuilder {

    public StyleableSpannableStringBuilder appendWithStyles(CharSequence text, CharacterStyle... charsStyles) {
        super.append(text); // append the text => now the overall length is length()
        int startPos = length() - text.length();
        for (CharacterStyle style : charsStyles) {
            this.setSpan(style, startPos, length(), 0);
        }
        return this;
    }

    public StyleableSpannableStringBuilder append(CharSequence text) {
        super.append(text);
        return this;
    }

    public StyleableSpannableStringBuilder append(SpannableString text) {
        super.append(text);
        return this;
    }

    public StyleableSpannableStringBuilder appendWithStyle(CharSequence text, CharacterStyle charsStyle) {
        return appendWithStyles(text, charsStyle);
    }

    public StyleableSpannableStringBuilder appendBold(CharSequence text) {
        return this.appendWithStyle(text, new StyleSpan(Typeface.BOLD));
    }

    public StyleableSpannableStringBuilder appendItalic(CharSequence text) {
        return this.appendWithStyle(text, new StyleSpan(Typeface.ITALIC));
    }

    public StyleableSpannableStringBuilder appendBoldItalic(CharSequence text) {
        return this.appendWithStyle(text, new StyleSpan(Typeface.BOLD_ITALIC));
    }

    public StyleableSpannableStringBuilder appendUnderlineSpan(CharSequence text) {
        return this.appendWithStyle(text, new UnderlineSpan());
    }

    public StyleableSpannableStringBuilder appendStrikethroughSpan(CharSequence text) {
        return this.appendWithStyle(text, new StrikethroughSpan());
    }

    public StyleableSpannableStringBuilder appendForegroundColorSpan(CharSequence text, int argb) {
        return this.appendWithStyle(text, new ForegroundColorSpan(argb));
    }

    public StyleableSpannableStringBuilder appendBackgroundColorSpan(CharSequence text, int argb) {
        return this.appendWithStyle(text, new BackgroundColorSpan(argb));
    }

    public StyleableSpannableStringBuilder appendRelativeSizeSpan(CharSequence text, float scaleFactor) {
        if (scaleFactor <= 0) {
            super.append(text);
            return this;
        }
        return this.appendWithStyle(text, new RelativeSizeSpan(scaleFactor));
    }

    public StyleableSpannableStringBuilder appendURLSpan(CharSequence text) {
        return this.appendWithStyle(text, new URLSpan(String.valueOf(text)));
    }

    public StyleableSpannableStringBuilder appendClickableSpan(CharSequence text, final CharSequence toastMessage, final Context context) {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
            }
        };
        return this.appendWithStyle(text, clickableSpan);
    }

    public StyleableSpannableStringBuilder appendClickableSpan(CharSequence text, final ClickableSpan clickableSpan) {
        return this.appendWithStyle(text, clickableSpan);
    }

    public StyleableSpannableStringBuilder appendClickableSpanStartActivity(CharSequence text, final Intent intent, final Activity activity, final boolean finishCurrentActivity) {
        SpannableString span = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                activity.startActivity(intent);
                if (finishCurrentActivity) {
                    activity.finish();
                }
            }
        };
        span.setSpan(clickableSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this.append(span);
//        return this.appendWithStyle(span, clickableSpan);
    }

    public StyleableSpannableStringBuilder appendSuperscriptSpan(CharSequence text, float scaleFactor) {
        if (scaleFactor > 0) {
            return this.appendWithStyles(text, new RelativeSizeSpan(scaleFactor), new SuperscriptSpan());
        }
        return this.appendWithStyle(text, new SuperscriptSpan());
    }

    public StyleableSpannableStringBuilder appendSubscriptSpan(CharSequence text, float scaleFactor) {
        if (scaleFactor > 0) {
            return this.appendWithStyles(text, new RelativeSizeSpan(scaleFactor), new SubscriptSpan());
        }
        return this.appendWithStyle(text, new SubscriptSpan());
    }
}
