package com.sw926.toolkitdemo

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.sw926.toolkit.addFlag

class FitSystemWindowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        var uiOption = window.decorView.systemUiVisibility
        uiOption = uiOption.addFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = 0x00000000
        }
        window.decorView.systemUiVisibility = uiOption


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fit_system_window)
    }
}
