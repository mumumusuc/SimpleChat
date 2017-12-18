package com.mumu.simplechat.views

import android.os.Bundle

/**
 * 显示会话界面/最近会话/联系人
 * 根据设备不同会有不同的布局
 */
interface IMainView {
    fun selectChatListTab(s: Boolean)
    fun selectContactsTab(s: Boolean)
    fun selectSettingTab(s: Boolean)

    fun showChatList()
    fun showContacts()
    fun showSettings()
    fun showSearchContact()
    fun showInvitation(name: String, reason: String)
    fun showConversation(arg: Bundle)

    fun refresh()
    fun showMessage(msg: String)
}