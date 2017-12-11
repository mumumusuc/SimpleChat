package com.mumu.simplechat.presenters

import com.mumu.simplechat.views.IIncomingCallView


interface IIncomingCallPresenter : IPresenter<IIncomingCallView> {
    fun onReject()
    fun onAnswer()
}