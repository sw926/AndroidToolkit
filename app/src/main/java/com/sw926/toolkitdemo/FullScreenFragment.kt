package com.sw926.toolkitdemo


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.sw926.toolkit2.FullScreen
import com.sw926.toolkit2.findActiviy
import com.sw926.toolkitdemo.databinding.FragmentFullScreenBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class FullScreenFragment : Fragment() {

    class FullScreenModel : ViewModel() {

        fun onClickSwitchOrientation(view: View) {
           view.findActiviy()?.let { FullScreen.toggleScreenOrientation(it) }
        }

        fun onClickFullScreen(view: View) {
           view.findActiviy()?.let { FullScreen.enterFullScreen(it) }
        }

        fun onClickExistFullScreen(view: View) {
           view.findActiviy()?.let { FullScreen.existFullScreen(it) }
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
