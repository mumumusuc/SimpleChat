package com.mumu.simplechat.presenters

import com.mumu.simplechat.views.IContactsView

interface IContactsPresenter : IPresenter<IContactsView> {
    fun onItemClick(name: String)
    fun onRightAction()
}