package com.developer.rohal.recreationcenteer.Helper

import android.content.Context
import android.net.Uri

class LocationClass private constructor() {
    init {
        println("This ($this) is a singleton")
    }

    private object Holder {
        val INSTANCE = LocationClass()
    }

    companion object {
        val instance:LocationClass by lazy { Holder.INSTANCE }
    }


    var context: Context?=null
    var loc:String = ""
    var uri:Uri?=null
}