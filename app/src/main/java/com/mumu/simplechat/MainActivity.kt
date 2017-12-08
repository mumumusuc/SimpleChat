package com.mumu.simplechat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.easeui.ui.EaseConversationListFragment
import com.hyphenate.util.NetUtils
import com.hyphenate.easeui.ui.EaseChatFragment
import com.hyphenate.easeui.EaseConstant
import com.hyphenate.easeui.widget.EaseConversationList


class MainActivity : AppCompatActivity(), EMConnectionListener {

    private val conversationListFragment = EaseConversationListFragment()
    private var conversationChatFragment = EaseChatFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EMClient.getInstance().chatManager().loadAllConversations()
        EMClient.getInstance().groupManager().loadAllGroups()
        conversationListFragment.setConversationListItemClickListener(object : EaseConversationListFragment.EaseConversationListItemClickListener {
            override fun onListItemClicked(conversation: EMConversation?) {
                val id = conversation?.conversationId()
                if(!id.isNullOrEmpty()){
                    if(conversationChatFragment?.isAdded){
                        supportFragmentManager.beginTransaction().remove(conversationChatFragment).commit()
                        conversationChatFragment = EaseChatFragment()
                    }
                    val args = Bundle()
                    args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE)
                    args.putString(EaseConstant.EXTRA_USER_ID, id)
                    conversationChatFragment.arguments = args
                    supportFragmentManager.beginTransaction()
                            .add(R.id.conversation_chat_container, conversationChatFragment)
                            .commit()
                }
            }
        })
        supportFragmentManager.beginTransaction()
                .add(R.id.conversation_list_container, conversationListFragment)
                .commit()
        //传入参数
        val args = Bundle()
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE)
        args.putString(EaseConstant.EXTRA_USER_ID, "111")
        conversationChatFragment.arguments = args
        supportFragmentManager.beginTransaction()
                .add(R.id.conversation_chat_container, conversationChatFragment)
                .commit()
    }

    override fun onConnected() {
    }

    override fun onDisconnected(error: Int) {
        if (error == EMError.USER_REMOVED) {
            // 显示帐号已经被移除
        } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
            // 显示帐号在其他设备登录
        } else {
            if (NetUtils.hasNetwork(this)) {
                //连接不到聊天服务器
            } else {
                //当前网络不可用，请检查网络设置
            }
        }
    }
}
