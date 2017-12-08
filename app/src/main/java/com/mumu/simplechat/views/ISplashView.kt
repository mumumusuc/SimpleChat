package com.mumu.simplechat.views

import android.content.Context

interface ISplashView {
    fun getContext(): Context
    fun exit()
    fun showLoginView()
    fun showRegisterView()
}