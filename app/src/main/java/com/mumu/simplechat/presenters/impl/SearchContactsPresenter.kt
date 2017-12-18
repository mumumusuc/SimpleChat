package com.mumu.simplechat.presenters.impl

import android.os.Handler
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.BackKeyEvent
import com.mumu.simplechat.model.IContactsModel
import com.mumu.simplechat.model.impl.EMContactsManager
import com.mumu.simplechat.presenters.ISearchContactsPresenter
import com.mumu.simplechat.views.ISearchContactView

class SearchContactsPresenter : ISearchContactsPresenter {

    companion object {
        private val sHandler = Handler()
    }

    private val SEARCH_DELAY = 500L
    private var mView: ISearchContactView? = null
    private val mSearchList = mutableListOf<String>()
    private val mContactsManager: IContactsModel<String> = EMContactsManager()

    override fun bind(view: ISearchContactView?) {
        mView = view
    }

    override fun onAdd(name: String) {
        mContactsManager.addContacts(name, "添加好友")
    }

    override fun getSearchResult(): List<String> = mSearchList

    override fun onSearch(str: String) {
        sHandler.removeCallbacksAndMessages(null)
        sHandler.postDelayed(
                {
                    mSearchList.clear()
                    mSearchList.add(str)
                    mView?.showResults(mSearchList)
                },
                SEARCH_DELAY
        )
    }

    override fun onLeftAction() {
        EventBus.post(BackKeyEvent())
    }
}