package com.example.micah.rxRecyclerViewArrayListAdaper

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText

/**
 * Created by Micah on 27/08/2017.
 */
class MinHeightEnforcedEditText: EditText {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):  super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        println("minHeight height is: $minimumHeight")

        if (minimumHeight > heightMeasureSpec)

            super.onMeasure(widthMeasureSpec, 5000)

        else

            super.onMeasure(widthMeasureSpec, 5000)
    }
}