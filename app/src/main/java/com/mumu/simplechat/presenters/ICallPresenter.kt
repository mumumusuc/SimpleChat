package com.mumu.simplechat.presenters

import android.content.Intent
import com.mumu.simplechat.bean.CallArgument
import com.mumu.simplechat.bean.IVideoView
import com.mumu.simplechat.views.ICallView

interface ICallPresenter : IPresenter<ICallView> {
    fun onInvoke(arg: Intent)
    fun onCall()
    fun onEndCall()
}