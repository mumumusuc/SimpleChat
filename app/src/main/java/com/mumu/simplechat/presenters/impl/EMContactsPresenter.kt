package com.mumu.simplechat.presenters.impl

import android.util.Log
import com.google.common.eventbus.Subscribe
import com.hyphenate.easeui.domain.EaseUser
import com.mumu.simplechat.MainApplication
import com.mumu.simplechat.Router
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.ContactsChangedEvent
import com.mumu.simplechat.eventbus.events.SearchContactEvent
import com.mumu.simplechat.eventbus.events.StartConversationEvent
import com.mumu.simplechat.model.IContactsModel
import com.mumu.simplechat.model.impl.EMContactsManager
import com.mumu.simplechat.presenters.IContactsPresenter
import com.mumu.simplechat.views.IContactsView

class EMContactsPresenter : IContactsPresenter {
    private val TAG = EMContactsPresenter::class.java.simpleName
    private var mView: IContactsView? = null
    private val mContactsManager: IContactsModel<String> = EMContactsManager()
    private val mContactsMap = mutableMapOf<String,EaseUser>()

    init {
        EventBus.register(this)
    }

    override fun bind(view: IContactsView?) {
        mView = view
        if (view != null) {
            if (mContactsMap.isEmpty()) {
                /*maybe cause ANR*/
                //mContactsList.addAll(mContactsManager.getAllContacts())
            }
            view.showContacts(mContactsMap)
        }
    }

    override fun onRightAction() {
        EventBus.post(SearchContactEvent())
    }

    @Subscribe
    fun onContactsChange(event: ContactsChangedEvent) {
        val name = event.userName
        if (!mContactsMap.keys.contains(name)) {
            mContactsMap.put(name, EaseUser(name))
            mView?.showContacts(mContactsMap)
        }
    }

    override fun onItemClick(name: String) {
        Log.i(TAG, "onItemClick -> $name")
        EventBus.post(StartConversationEvent(name))
    }
}