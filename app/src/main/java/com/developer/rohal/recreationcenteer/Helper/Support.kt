package com.developer.rohal.recreationcenteer.Helper

import okhttp3.RequestBody
import retrofit2.http.HeaderMap
import java.util.HashMap

class Support {
    init {
        println("This is a Support Class")
    }
    private object Holder {
        val INSTANCE = Support()
    }
    companion object {
        val instance:Support by lazy { Support.Holder.INSTANCE }
    }

    /*
     Sign up Location and Court Selection
    */
    var SignUpLocation = "Location"
    var SignUpLocationlat = " "
    var SignUpLocationlang = " "
    var SignUpCourt = " "
    var headerMap = HashMap<String, RequestBody>()

    /*
    *
    * Sign up Court Selected
     */
    var HomeCourt:String = " "
}