package com.rawee.app.utils.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.rawee.app.R;


/**
 * Created by akshaykale on 2017/08/19.
 */

public class FontTextView extends android.support.v7.widget.AppCompatTextView {


    public FontTextView(Context context) {
        super(context);
        setUp(null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp(attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(attrs);
    }

    private void setUp(AttributeSet set) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Nunito-Regular.ttf");
        if (set == null) {
            setTypeface(tf);
        }else {
            TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.TextViewFont, 0, 0);
            try {
                if (ta.hasValue(R.styleable.TextViewFont_fontStyle)) {
                    int value = ta.getInt(R.styleable.TextViewFont_fontStyle, 0);
                    if (value == 1){
                        setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                                "fonts/Nunito-Light.ttf"));
                    }else if(value == 2){
                        setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                                "fonts/Nunito-Bold.ttf"));
                    }else if(value == 3){
                        setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                                "fonts/caveat_bold.ttf"));
                    } else {
                        setTypeface(tf);
                    }
                }
            } finally {
                ta.recycle();
            }
        }

    }
}

