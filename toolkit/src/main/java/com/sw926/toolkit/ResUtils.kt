package com.sw926.toolkit

import android.content.Context
import android.support.annotation.ColorInt
import android.util.TypedValue

object ResUtils {

    @ColorInt
    fun getAttrColor(context: Context, colorId: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(colorId, typedValue, true)
        return typedValue.data
    }

}