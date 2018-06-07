package com.developer.rohal.recreationcenteer

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.developer.rohal.recreationcenteer.Api.Session

class ActivitySplashScreen : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_splash_screen)
        Handler().postDelayed(Runnable
        {
            if(Session(this).loggedin()) {
                fragmentManager?.beginTransaction()
                     //   ?.replace(R.id.container,FragmentHome())
                        ?.commit()
            }
            else {
                fragmentManager?.beginTransaction()
                      //  ?.replace(R.id.container, FragmentLogin())
                        ?.addToBackStack("SignUp Page")
                        ?.commit()
            }
        }, 2000.toLong())
    }
}
