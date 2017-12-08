package com.mumu.simplechat.presenters

import com.mumu.simplechat.views.ISplashView

interface ISplashPresenter : IPresenter<ISplashView> {
    fun onSwitchScreen()
}