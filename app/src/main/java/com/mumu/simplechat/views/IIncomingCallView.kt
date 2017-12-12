package com.mumu.simplechat.views

import android.content.Context

interface IIncomingCallView {
    fun getContext(): Context
    fun showIncomingCall(msg: String)
    fun dismissIncomingCall()
}