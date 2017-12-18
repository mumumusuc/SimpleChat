package com.mumu.simplechat

object Config {
    val autoLogin = false
    val saveLogin = true

    fun getOrientation(): Int = MainApplication.getContext()!!.resources.configuration.orientation
}