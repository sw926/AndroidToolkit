package com.sw926.toolkitdemo


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sw926.toolkit.FullScreen
import com.sw926.toolkit.findActivity
import com.sw926.toolkitdemo.databinding.FragmentFullScreenBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class FullScreenFragment : Fragment() {

    class FullScreenModel : ViewModel() {


        val SYSTEM_UI_FLAG_LOW_PROFILE = MutableLiveData<Boolean>()
        val SYSTEM_UI_FLAG_HIDE_NAVIGATION = MutableLiveData<Boolean>()
        val SYSTEM_UI_FLAG_FULLSCREEN = MutableLiveData<Boolean>()
        val SYSTEM_UI_FLAG_IMMERSIVE = MutableLiveData<Boolean>()
        val SYSTEM_UI_FLAG_IMMERSIVE_STICKY = MutableLiveData<Boolean>()

        fun onClickSwitchOrientation(view: View) {
            view.findActivity()?.let { FullScreen.toggleScreenOrientation(it) }
        }

        fun onClickFullScreen(view: View) {
            view.findActivity()?.let { FullScreen.enterFullScreen(it) }
        }

        fun onClickExistFullScreen(view: View) {
            view.findActivity()?.let { FullScreen.existFullScreen(it) }
        }
    }

    private var binding: FragmentFullScreenBinding? = null
    private var model: FullScreenModel? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_full_screen, container, false)
        binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProviders.of(this).get(FullScreenModel::class.java)
        binding?.model = model
        binding?.setLifecycleOwner(this)

        model?.SYSTEM_UI_FLAG_FULLSCREEN?.observe(this, Observer {
            update()
        })
        model?.SYSTEM_UI_FLAG_HIDE_NAVIGATION?.observe(this, Observer {
            update()
        })
        model?.SYSTEM_UI_FLAG_FULLSCREEN?.observe(this, Observer {
            update()
        })
        model?.SYSTEM_UI_FLAG_IMMERSIVE?.observe(this, Observer {
            update()
        })
        model?.SYSTEM_UI_FLAG_IMMERSIVE_STICKY?.observe(this, Observer {
            update()
        })
    }

    private fun update() {
        val activity = activity ?: return
        val model = model ?: return

        var uiOptions = activity.window.decorView.systemUiVisibility

        uiOptions = if (model.SYSTEM_UI_FLAG_LOW_PROFILE.value == true) uiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE else uiOptions and View.SYSTEM_UI_FLAG_LOW_PROFILE.inv()
        uiOptions = if (model.SYSTEM_UI_FLAG_FULLSCREEN.value == true) uiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN else uiOptions and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
        uiOptions = if (model.SYSTEM_UI_FLAG_HIDE_NAVIGATION.value == true) uiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION else uiOptions and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = if (model.SYSTEM_UI_FLAG_IMMERSIVE.value == true) uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE else uiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE.inv()
            uiOptions = if (model.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.value == true) uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY else uiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
        }

        activity.window.decorView.systemUiVisibility = uiOptions
    }


}
