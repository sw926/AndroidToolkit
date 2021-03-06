package com.sw926.toolkitdemo


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFullScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_fullScreenFragment)
        }

        btnStatusVisibility.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_statsVisibilityActivity)
        }

        btnFitSystemWindowActivity.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_fitSystemWindowActivity)
        }

    }


}
