package com.mumu.simplechat

import android.content.res.Configuration

object Config {
    val autoLogin = true
    val saveLogin = true

    fun getOrientation(): Int = MainApplication.getContext()!!.resources.configuration.orientation

    fun isPad(): Boolean =
            MainApplication.getContext()!!.getResources().getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE

    fun isPhone(): Boolean = !isPad()
}