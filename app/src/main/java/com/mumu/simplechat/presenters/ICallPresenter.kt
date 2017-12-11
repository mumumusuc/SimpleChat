package com.mumu.simplechat.presenters

import com.mumu.simplechat.bean.IVideoView
import com.mumu.simplechat.views.ICallView

interface ICallPresenter : IPresenter<ICallView> {
    fun onCall()
    fun onEndCall()
}