package com.mumu.simplechat.views

import android.content.Intent

interface IIncomingCallView {
    fun go(intent: Intent)
    fun showIncomingCall(from: String)
    fun dismissIncomingCall(from: String)
}