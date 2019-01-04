package com.sw926.toolkitdemo

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.sw926.toolkit.R
import com.sw926.toolkit.ResUtils
import com.sw926.toolkit.findActivity

object StatsVisibility {

    fun showStats(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.findActivity()?.window?.statusBarColor = ResUtils.getAttrColor(view.context, R.attr.colorPrimaryDark)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.findActivity()?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }

        var uiOption = view.systemUiVisibility
        uiOption = uiOption and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()
        view.systemUiVisibility = uiOption
    }

    fun hideStats(view: View) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.findActivity()?.window?.statusBarColor = Color.TRANSPARENT
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.findActivity()?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }

        var uiOption = view.systemUiVisibility
        uiOption = uiOption or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        view.systemUiVisibility = uiOption
    }
}