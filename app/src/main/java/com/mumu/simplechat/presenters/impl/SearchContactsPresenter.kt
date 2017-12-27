package com.mumu.simplechat.presenters.impl

import android.os.Handler
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.BackKeyEvent
import com.mumu.simplechat.model.IContactsModel
import com.mumu.simplechat.model.impl.EMContactsManager
import com.mumu.simplechat.presenters.ISearchContactsPresenter
import com.mumu.simplechat.views.ISearchContactView
import io.reactivex.android.schedulers.AndroidSchedulers

class SearchContactsPresenter : ISearchContactsPresenter {

    companion object {
        private val sHandler = Handler()
    }

    private val SEARCH_DELAY = 500L
    private var mView: ISearchContactView? = null
    private val mSearchList = mutableListOf<String>()
    private val mContactsManager: IContactsModel<String> = EMContactsManager

    override fun bind(view: ISearchContactView?) {
        mView = view
        mView?.clearResults()
    }

    override fun onAdd(name: String) {
        mContactsManager.addContacts(name, "添加好友")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (result == "NO_ERROR") mView?.showMessage("已发送好友请求")
                        },
                        { e ->
                            mView?.showMessage("发生错误:${e.localizedMessage}")
                        })
    }

    override fun getSearchResult(): List<String> = mSearchList

    override fun onSearch(str: String) {
        sHandler.removeCallbacksAndMessages(null)
        sHandler.postDelayed(
                {
                    mSearchList.clear()
                    if (str.isNullOrEmpty() || str.isNullOrBlank()) {
                        mView?.showResults(mSearchList)
                    } else {
                        mView?.showWaiting()
                        mContactsManager.findContacts(str)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        { list ->
                                            if (list.isNotEmpty()) {
                                                mSearchList.addAll(list)
                                            } else {
                                                mView?.showMessage("未找到用户$str")
                                            }
                                        },
                                        {
                                            mView?.dismissWaiting()
                                        },
                                        {
                                            mView?.showResults(mSearchList)
                                            mView?.dismissWaiting()
                                        }
                                )
                    }
                },
                SEARCH_DELAY
        )
    }

    override fun onLeftAction() {
        EventBus.post(BackKeyEvent())
    }
}