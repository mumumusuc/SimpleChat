package com.mumu.simplechat.presenters

import com.mumu.simplechat.views.IConversationView

interface IConversationPresenter : IPresenter<IConversationView> {
    fun onAudioCall()
    fun onVideoCall()
}