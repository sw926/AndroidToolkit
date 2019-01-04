package com.sw926.toolkit2

import android.app.Activity
import android.view.View
import androidx.appcompat.widget.TintContextWrapper

fun View.findActiviy(): Activity? {
    val context = context
    if (context is Activity) {
        return context
    }
    if (context is TintContextWrapper) {
        val baseContext = context.baseContext
        if (baseContext is Activity) {
            return baseContext
        }
    }
    return null
}