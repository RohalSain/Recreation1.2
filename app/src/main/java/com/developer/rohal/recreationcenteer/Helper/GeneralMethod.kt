package com.developer.rohal.recreationcenteer.Helper

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class GeneralMethod {
    init {
        println("this is a General Method class")
    }

    private object Holder {
        val INSTANCE = GeneralMethod()
    }

    companion object {
        val instance:GeneralMethod by lazy { Holder.INSTANCE }
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}