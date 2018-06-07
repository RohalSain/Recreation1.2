package com.developer.rohal.recreationcenteer.Helper

import android.content.Context

class Counter private constructor() {
    init {
        println("This ($this) is a singleton")
    }

    private object Holder {
        val INSTANCE = Counter()
    }

    companion object {
        val instance:Counter by lazy { Holder.INSTANCE }
    }


    var context: Context?=null
    var count:Boolean=false
}