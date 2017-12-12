package com.mumu.simplechat.presenters

import android.content.Intent
import com.mumu.simplechat.bean.CallArgument
import com.mumu.simplechat.views.IIncomingCallView


interface IIncomingCallPresenter : IPresenter<IIncomingCallView> {
    fun onInvoke(arg: Intent)
    fun onReject()
    fun onAnswer()
}