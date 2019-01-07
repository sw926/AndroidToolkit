@file:Suppress("unused")

package com.sw926.toolkit

import android.annotation.TargetApi
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.View
import android.view.ViewConfiguration
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object KitUtils {

    /**
     * 格式化字节大小
     */
    fun humanReadableByteCount(bytes: Long, si: Boolean = true): String {
        val unit = if (si) 1000 else 1024
        if (bytes < unit) return bytes.toString() + " B"
        val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
        val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
        return String.format("%.1f %sB", bytes / Math.pow(unit.toDouble(), exp.toDouble()), pre)
    }

    /**
     * App 是否是首次安装
     */
    fun isFirstInstall(context: Context) = getPackageFirstInstallTime(context) == getPackageLastUpdateTime(context)

    /**
     * 获取 App 首次安装时间
     */
    fun getPackageFirstInstallTime(context: Context): Long {
        val name = context.packageName
        var time: Long = 0
        try {
            time = context.packageManager.getPackageInfo(name, 0).firstInstallTime
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return time
    }

    /**
     * 获取 App 更新时间
     */
    fun getPackageLastUpdateTime(context: Context): Long {
        val name = context.packageName
        var time: Long = 0
        try {
            time = context.packageManager.getPackageInfo(name, 0).lastUpdateTime
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return time
    }

    /**
     * 创建一个 Color State List，用于选择效果
     */
    @JvmStatic
    fun createSelectStateList(context: Context, @ColorRes normal: Int, @ColorRes selected: Int): ColorStateList? {

        val colors = intArrayOf(ContextCompat.getColor(context, selected), ContextCompat.getColor(context, normal))
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_selected)
        states[1] = intArrayOf()
        return ColorStateList(states, colors)
    }

    /**
     * 保存 bitmap 到指定文件
     */
    @JvmStatic
    fun saveBitmap(bmp: Bitmap, filePath: String, format: Bitmap.CompressFormat, quality: Int) {
        var fo: FileOutputStream
        catchError {
            val f = File(filePath)
            if (f.exists()) {
                f.delete()
            }
            f.createNewFile()
            fo = FileOutputStream(f, true)
            bmp.compress(format, quality, fo)
            fo.flush()
            fo.close()
        }

    }


    @JvmStatic
    fun getDrawableByName(context: Context, name: String): Drawable? {
        if (name.isBlank()) {
            return null
        }
        val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
        return safeGetValue { ContextCompat.getDrawable(context, resId) }
    }


    /**
     * 获取丈量文本的矩形
     *
     * @param text
     * @param paint
     */
    @JvmStatic
    fun getTextBounds(text: String, paint: Paint): Rect {
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        return rect
    }

    @JvmStatic
    fun sp2px(context: Context, sp: Float) = context.sp2px(sp)

    @JvmStatic
    fun dp2px(context: Context, dp: Float) = context.dp2px(dp)

    @JvmStatic
    fun hideKeyBoard(view: View?) {
        view?.hideKeyboard()
    }

    @JvmStatic
    fun showKeyBoard(view: View?) {
        view?.showKeyboard()
    }


    @JvmStatic
    fun getWindowToken(activity: Context?) = activity?.let { (it as? Activity)?.window?.decorView?.windowToken }

    @JvmStatic
    fun getTintDrawable(context: Context, @DrawableRes resId: Int, @ColorRes color: Int): Drawable {
        val origin = ContextCompat.getDrawable(context, resId)
        val drawable = DrawableCompat.wrap(origin!!)
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, color))
        return drawable
    }

    @JvmStatic
    fun startAppSetting(activity: Activity?, request_code: Int) {
        if (activity == null)
            return
        val `in` = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.applicationContext.packageName, null)
        `in`.data = uri
        activity.startActivityForResult(`in`, request_code)
    }

    @JvmStatic
    fun startAppSetting(fragment: Fragment?, request_code: Int) {
        if (fragment == null)
            return
        val `in` = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", fragment.context!!.applicationContext.packageName, null)
        `in`.data = uri
        fragment.startActivityForResult(`in`, request_code)
    }

    @JvmStatic
    fun getNavigationBarHeight(context: Context): Int {
        val resources = context.resources

        val id = resources.getIdentifier(
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_height_landscape",
                "dimen", "android")
        return if (id > 0) {
            resources.getDimensionPixelSize(id)
        } else 0
    }

    @JvmStatic
    fun checkDeviceHasNavigationBar(activity: Context): Boolean {

        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        val hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK)

        return !hasMenuKey && !hasBackKey
    }

    @JvmStatic
    fun deleteAllFile(file: File?) {
        file?.let {
            if (!it.exists()) {
                return
            }
            if (it.isFile) {
                it.delete()
                return
            }
            if (it.isDirectory) {
                val files = it.listFiles()
                if (files == null || files.isEmpty()) {
                    it.delete()
                } else {
                    for (item in files) {
                        deleteAllFile(item)
                    }
                }
            }
        }
    }

    @JvmStatic
    fun getCacheFile(context: Context, url: String): File? {
        var file: File? = null
        try {
            val name = md5encode(url)
            if (name != null) {
                file = File(context.cacheDir, name)
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return file
    }

    @JvmStatic
    fun md5encode(password: String): String? {
        return try {
            val instance: MessageDigest = MessageDigest.getInstance("MD5")//获取md5加密对象
            val digest: ByteArray = instance.digest(password.toByteArray())//对字符串加密，返回字节数组
            val sb = StringBuffer()
            for (b in digest) {
                val i: Int = b.toInt() and 0xff//获取低八位有效值
                var hexString = Integer.toHexString(i)//将整数转化为16进制
                if (hexString.length < 2) {
                    hexString = "0$hexString"//如果是一位的话，补0
                }
                sb.append(hexString)
            }
            sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @JvmStatic
    fun sha1(text: String): String? {
        return try {
            val instance = MessageDigest.getInstance("SHA-1")
            instance.update(text.toByteArray())
            val digest = instance.digest()

            val sb = StringBuffer()
            for (b in digest) {
                val i: Int = b.toInt() and 0xff//获取低八位有效值
                var hexString = Integer.toHexString(i)//将整数转化为16进制
                if (hexString.length < 2) {
                    hexString = "0$hexString"//如果是一位的话，补0
                }
                sb.append(hexString)
            }
            sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }


    @JvmStatic
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun copyToClipboard(context: Context, content: String) {
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        manager.primaryClip = ClipData.newPlainText(content, content)
    }

    @JvmStatic
    fun isInstall(context: Context, packageName: String): Boolean {
        return try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            }
            packageInfo != null
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }

    }

    @JvmStatic
    fun openMarket(context: Context): Boolean {
        val packageName = context.packageName
        return openMarket(context, packageName)
    }

    @JvmStatic
    fun openMarket(context: Context, packageName: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=$packageName") //跳转到应用市场，非Google Play市场一般情况也实现了这个接口
        if (intent.resolveActivity(context.packageManager) != null) { //可以接收
            context.startActivity(intent)
        } else {
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            if (intent.resolveActivity(context.packageManager) != null) { //有浏览器
                context.startActivity(intent)
            } else {
                return false
            }
        }
        return true
    }

    fun getVersionCode(context: Context): Long {
        val packageManager = context.packageManager
        var packInfo: PackageInfo? = null
        try {
            packInfo = packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return if (packInfo != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packInfo.longVersionCode
            } else {
                packInfo.versionCode.toLong()
            }
        } else -1
    }

    fun getVersionName(context: Context): String? {
        val packageManager = context.packageManager
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return packageInfo?.versionName
    }

}