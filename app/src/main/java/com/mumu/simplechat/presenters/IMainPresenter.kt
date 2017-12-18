package com.mumu.simplechat.presenters

import com.mumu.simplechat.views.IMainView

interface IMainPresenter : IPresenter<IMainView> {
    fun onChatList()
    fun onContacts()
    fun onSetting()
    fun onAgree()
    fun onRefuse()
    fun onBackKey():Boolean
}