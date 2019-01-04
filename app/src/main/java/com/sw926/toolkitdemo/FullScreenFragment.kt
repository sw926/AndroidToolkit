package com.sw926.toolkitdemo


import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_full_screen, container, false)
        binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProviders.of(this).get(FullScreenModel::class.java)
        binding?.model = model
        binding?.setLifecycleOwner(this)
    }


}
