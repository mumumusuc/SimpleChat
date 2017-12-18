package com.mumu.simplechat.presenters.impl

import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.SearchContactEvent
import com.mumu.simplechat.eventbus.events.StartConversationEvent
import com.mumu.simplechat.presenters.IChatlistPresenter
import com.mumu.simplechat.views.IChatlistView

class ChatlistPresenter : IChatlistPresenter {
    private var mView: IChatlistView? = null

    override fun bind(view: IChatlistView?) {
        mView = view
    }

    override fun onItemClick(id: String) {
        EventBus.post(StartConversationEvent(id))
    }

    override fun onRightAction() {
        EventBus.post(SearchContactEvent())
    }
}