package com.mumu.simplechat.presenters.impl

import android.content.res.Configuration
import android.os.Bundle
import com.google.common.eventbus.Subscribe
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.easeui.EaseConstant
import com.mumu.simplechat.Config
import com.mumu.simplechat.MainApplication
import com.mumu.simplechat.Router
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.*
import com.mumu.simplechat.model.IContactsModel
import com.mumu.simplechat.model.impl.EMContactsManager
import com.mumu.simplechat.presenters.IMainPresenter
import com.mumu.simplechat.views.IMainView

class MainPresenterImpl : IMainPresenter, EMConnectionListener {
    private val TAG = MainPresenterImpl::class.java.simpleName

    private val mContactsManager: IContactsModel<String> = EMContactsManager()
    private var mMainView: IMainView? = null
    private var mInvitationUserName: String? = null

    private enum class Tab { CHAT, CONTACTS, SETTING, SEARCH }

    private var mTab: Tab? = null
    private var mLastTab: Tab? = null

    private fun setTab(tab: Tab) {
        mLastTab = mTab
        mTab = tab
    }

    init {
        EventBus.register(this)
    }

    override fun bind(view: IMainView?) {
        mMainView = view
        if (mMainView != null) {
            EMClient.getInstance().chatManager().loadAllConversations()
            EMClient.getInstance().groupManager().loadAllGroups()
            onChatList()
            EMClient.getInstance().chatManager().addMessageListener(msgListener)
        } else {
            EMClient.getInstance().chatManager().removeMessageListener(msgListener)
        }
    }

    override fun onChatList() {
        setTab(Tab.CHAT)
        mMainView?.selectChatListTab(true)
        mMainView?.selectContactsTab(false)
        mMainView?.selectSettingTab(false)
        mMainView?.showChatList()
    }

    override fun onContacts() {
        setTab(Tab.CONTACTS)
        mMainView?.selectChatListTab(false)
        mMainView?.selectContactsTab(true)
        mMainView?.selectSettingTab(false)
        mMainView?.showContacts()
    }

    override fun onSetting() {
        setTab(Tab.SETTING)
        mMainView?.selectChatListTab(false)
        mMainView?.selectContactsTab(false)
        mMainView?.selectSettingTab(true)
        mMainView?.showSettings()
    }

    private val msgListener = object : EMMessageListener {
        //收到消息
        override fun onMessageReceived(messages: List<EMMessage>) {
            mMainView?.refresh()
        }

        //收到透传消息
        override fun onCmdMessageReceived(messages: List<EMMessage>) {
            mMainView?.refresh()
        }

        //收到已读回执
        override fun onMessageRead(messages: List<EMMessage>) {
            mMainView?.refresh()
        }

        //收到已送达回执
        override fun onMessageDelivered(message: List<EMMessage>) {
            mMainView?.refresh()
        }

        //消息被撤回
        override fun onMessageRecalled(messages: List<EMMessage>) {
            mMainView?.refresh()
        }

        //消息状态变动
        override fun onMessageChanged(message: EMMessage, change: Any) {
            mMainView?.refresh()
        }
    }

    /**/
    override fun onConnected() {
    }

    override fun onDisconnected(error: Int) {
        if (error == EMError.USER_REMOVED) {
            // 显示帐号已经被移除
        } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
            // 显示帐号在其他设备登录
        } else {
            /*  if (NetUtils.hasNetwork()) {
                  //连接不到聊天服务器
              } else {
                  //当前网络不可用，请检查网络设置
              }*/
        }
    }

    override fun onAgree() {
        mContactsManager.agreeInvitation(mInvitationUserName!!)
    }

    override fun onRefuse() {
        mContactsManager.refuseInvitation(mInvitationUserName!!)
    }

    override fun onBackKey(): Boolean {
        if (mTab == Tab.SEARCH) {
            when (mLastTab) {
                Tab.CHAT -> {
                    onChatList()
                    return true
                }
                Tab.CONTACTS -> {
                    onContacts()
                    return true
                }
            }
        }
        return false
    }

    @Subscribe
    fun onInvitation(event: InvitationEvent) {
        mInvitationUserName = event.userName
        mMainView?.showInvitation(mInvitationUserName!!, event.reason)
    }

    @Subscribe
    fun onContactsAccepted(event: ContactsAcceptedEvent) {
        mMainView?.showMessage("${event.userName}已同意了好友请求")
    }

    @Subscribe
    fun onSearchContact(event: SearchContactEvent) {
        setTab(Tab.SEARCH)
        mMainView?.showSearchContact()
        mMainView?.selectChatListTab(false)
        mMainView?.selectContactsTab(false)
        mMainView?.selectSettingTab(false)
    }

    @Subscribe
    fun onStartConversation(event: StartConversationEvent) {
        val args = Bundle()
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE)
        args.putString(EaseConstant.EXTRA_USER_ID, event.userName)
        if (Config.getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
            mMainView?.showConversation(args)
        } else {
            Router.goConversationView(MainApplication.getContext()!!, args)
        }
    }

    @Subscribe
    fun onBackKey(event: BackKeyEvent) {
        onBackKey()
    }
}