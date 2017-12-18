package com.mumu.simplechat.presenters

import com.mumu.simplechat.views.IChatlistView

interface IChatlistPresenter : IPresenter<IChatlistView> {
    fun onItemClick(id: String)
    fun onRightAction()
}