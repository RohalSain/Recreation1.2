package com.developer.rohal.recreationcenteer
import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.developer.rohal.recreationcenteer.Api.Session
import com.developer.rohal.recreationcenteer.fragment.FragmentHome
import com.developer.rohal.recreationcenteer.fragment.SplashScreen
import com.facebook.drawee.backends.pipeline.Fresco

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Fresco.initialize(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
                ?.replace(R.id.container, SplashScreen())
                ?.commit()
    }
}