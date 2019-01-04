package com.sw926.toolkit

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager

fun Int.addFlag(flag: Int): Int {
    return this or flag
}

fun Int.removeFlag(flag: Int): Int {
    return this and flag.inv()
}

fun Int.hasFlag(flag: Int): Boolean {
    return this and flag == flag
}

/*
 * 添加某个 Flag，使用 "|"，Kotlin 为 or，例如 Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
 * 判断某个 Flag 是否存在，使用 "&"，例如 (intent.getFlags()&Intent.FLAG_ACTIVITY_NEW_TASK) == 0
 * 清除某个 Flag，使用 "&= ~", 例如 mFlags &= ~FLAG_START_TRACKING;
 */
object FullScreen {

    fun toggleScreenOrientation(activity: Activity) {
        if (getCurrentOrientation(activity) == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            switchScreenOrientation(activity, true)
        } else {
            switchScreenOrientation(activity, false)
        }
    }

    fun isLandscape(activity: Activity): Boolean {
        return activity.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    fun getCurrentOrientation(activity: Activity): Int {
        return activity.resources.configuration.orientation
    }

    fun switchScreenOrientation(activity: Activity, isLandscape: Boolean) {
        if (isLandscape) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    fun enterFullScreen(activity: Activity) {

        if (activity is AppCompatActivity) {
            activity.supportActionBar?.hide()
        } else {
            activity.actionBar?.hide()
        }

        val attrs = activity.window.attributes
        attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        activity.window.attributes = attrs

        var uiOptions = activity.window.decorView.systemUiVisibility

        uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
        uiOptions = uiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN
        uiOptions = uiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        activity.window.decorView.systemUiVisibility = uiOptions
    }

    fun existFullScreen(activity: Activity) {
        if (activity is AppCompatActivity) {
            activity.supportActionBar?.show()
        } else {
            activity.actionBar?.show()
        }

        val attrs = activity.window.attributes
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON.inv()
        activity.window.attributes = attrs

        var uiOptions = activity.window.decorView.systemUiVisibility

        uiOptions = uiOptions and View.SYSTEM_UI_FLAG_LOW_PROFILE.inv()
        uiOptions = uiOptions and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
        uiOptions = uiOptions and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE.inv()
            uiOptions = uiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
        }

        activity.window.decorView.systemUiVisibility = uiOptions
    }

}