@file:Suppress("unused")

package com.sw926.toolkit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.TintContextWrapper
import android.util.Base64
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import timber.log.Timber
import java.io.File
import java.lang.reflect.Field

fun View.showKeyboard() {

    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.forceShowKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun <T : Activity> Context.startActivity(clazz: Class<T>) {
    startActivity(Intent(this, clazz))
}

fun Context.dp2px(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics)
fun Context.sp2px(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, this.resources.displayMetrics)

/**
 * 创建一个AlertDialog.Builder
 */
fun Context.nashAlertDialog(): AlertDialog.Builder = AlertDialog.Builder(this)

fun Context.nashAlertDialog(message: String? = null): AlertDialog.Builder {
    val builder = nashAlertDialog()
    message?.let {
        builder.setMessage(it)
    }
    return builder
}

fun Context.getDeviceUniqueId(): String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

fun Context.getScreenSize(): IntArray {
    val size = IntArray(2)
    val dm = this.resources.displayMetrics
    size[0] = dm.widthPixels
    size[1] = dm.heightPixels
    return size
}

fun Context.getWindowToken(): IBinder? {
    if (this !is Activity)
        return null
    return this.window?.decorView?.windowToken
}

fun <T : View> Array<T>.goneBy(action: () -> Boolean) {
    forEach {
        it.goneBy { action.invoke() }
    }
}

fun <T : View> Array<T>.selectedBy(action: () -> Boolean) {
    forEach {
        it.selectedBy { action.invoke() }
    }
}

fun View.selectedBy(action: () -> Boolean) {
    this.isSelected = action.invoke()
}

fun View.goneBy(action: () -> Boolean) {
    this.visibility = if (action.invoke()) View.GONE else View.VISIBLE
}

fun View.visibleBy(action: () -> Boolean) {
    this.visibility = if (action.invoke()) View.VISIBLE else View.GONE
}


fun View.invisibleBy(action: () -> Boolean) {
    this.visibility = if (action.invoke()) View.INVISIBLE else View.VISIBLE
}

fun View.onGlobalLayout(callback: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            } else {
                viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
            callback.invoke()
        }
    })
}

fun Float?.orZero(): Float {
    if (this == null) {
        return 0f
    }
    return this
}


fun <T> List<T>?.sizeLessThan(size: Int): Boolean {
    val realSize = this?.size ?: 0
    return realSize < size
}

fun Context.showMessage(messageId: Int) {
    Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()
}

fun Context.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// 获取手机状态栏高度
fun Context.getStatusBarHeight(): Int {
    val c: Class<*>?
    val obj: Any?
    val field: Field?
    val x: Int
    var statusBarHeight = 0
    try {
        c = Class.forName("com.android.internal.R\$dimen")
        obj = c!!.newInstance()
        field = c.getField("status_bar_height")
        x = Integer.parseInt(field!!.get(obj).toString())
        statusBarHeight = this.resources.getDimensionPixelSize(x)
    } catch (e1: Exception) {
        e1.printStackTrace()
    }

    return statusBarHeight
}

fun File.toUriString() = Uri.fromFile(this).toString()


fun Context.getTintDrawable(@DrawableRes resId: Int, @ColorRes color: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)?.let { origin ->
        val drawable = DrawableCompat.wrap(origin)
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, color))
        drawable
    }
}


fun View.locationOnScreen(): IntArray {
    val array: IntArray = intArrayOf(0, 0)
    getLocationOnScreen(array)

    return array
}

fun catchError(action: () -> Unit) {
    try {
        action.invoke()
    } catch (e: Throwable) {
        Timber.e(e)
    }
}


fun <T> safeGetValue(action: () -> T): T? {
    var value: T? = null
    try {
        value = action.invoke()
    } catch (e: Throwable) {
        Timber.e(e)
        e.printStackTrace()
    }
    return value
}

fun String.decodeBase64(): String? {
    return safeGetValue {
        val data = Base64.decode(this, Base64.DEFAULT)
        String(data, charset("UTF-8"))
    }
}

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

fun showViews(vararg views: View?) {
    views.forEach {
        it?.visibility = View.VISIBLE
    }
}

fun goneViews(vararg views: View?) {
    views.forEach {
        it?.visibility = View.GONE
    }
}