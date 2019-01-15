package com.sw926.toolkitdemo

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import android.view.WindowManager
import com.sw926.toolkit.addFlag
import kotlinx.android.synthetic.main.activity_stats_visibility.*
import me.yokeyword.fragmentation.SupportActivity

class StatsVisibilityActivity : SupportActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // 4.4 状态栏透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0 及以上，修改状态栏颜色
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0x00000000
        }

        // 设置 DecorView 可以在状态栏和导航栏下面进行绘制
        var uiOption = window.decorView.systemUiVisibility
        uiOption = uiOption.addFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.decorView.systemUiVisibility = uiOption

        setContentView(R.layout.activity_stats_visibility)

        viewPager.adapter = MyPagerAdapter()

    }

    inner class MyPagerAdapter : FragmentPagerAdapter(supportFragmentManager) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(p0: Int): Fragment {
            return if (p0 == 0) {
                NonTransStatsFragment()
            } else {
                TransStatsFragment()
            }
        }

    }
}
