package com.rawee.app.utils.ui

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

class ImageViewSquare: AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}