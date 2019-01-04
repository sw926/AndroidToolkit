package com.sw926.toolkit

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.TintContextWrapper
import android.view.View

fun View.findActivity(): AppCompatActivity? {
    val context = context
    if (context is AppCompatActivity) {
        return context
    }
    if (context is TintContextWrapper) {
        val baseContext = context.baseContext
        if (baseContext is AppCompatActivity) {
            return baseContext
        }
    }
    return null
}