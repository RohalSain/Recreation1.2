package com.developer.rohal.recreationcenteer.fragment


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developer.rohal.recreationcenteer.Api.Session

import com.developer.rohal.recreationcenteer.R

class SplashScreen : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Handler().postDelayed(Runnable
        {
            if(Session(context).loggedin()) {
                fragmentManager?.beginTransaction()
                        ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        ?.replace(R.id.container,FragmentHome())
                        ?.commit()
            }
            else {
                fragmentManager?.beginTransaction()
                        ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        ?.replace(R.id.container,FragmentLogin())
                        ?.commit()
            }
        }, 2000.toLong())
    }

}
