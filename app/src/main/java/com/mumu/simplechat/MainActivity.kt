package com.mumu.simplechat

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.easeui.ui.EaseConversationListFragment
import com.hyphenate.util.NetUtils
import com.hyphenate.easeui.ui.EaseChatFragment
import com.hyphenate.easeui.EaseConstant
import com.mumu.simplechat.bean.CallArgument
import com.mumu.simplechat.presenters.IIncomingCallPresenter
import com.mumu.simplechat.presenters.impl.IncomingCallPresenter
import com.mumu.simplechat.views.IIncomingCallView


class MainActivity : AppCompatActivity(), EMConnectionListener, IIncomingCallView {

    companion object {
        private val sIncomingCallPresenter: IIncomingCallPresenter = IncomingCallPresenter()
    }

    private val conversationListFragment = EaseConversationListFragment()
    private var conversationChatFragment = EaseChatFragment()
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EMClient.getInstance().chatManager().loadAllConversations()
        EMClient.getInstance().groupManager().loadAllGroups()
        conversationListFragment.setConversationListItemClickListener(object : EaseConversationListFragment.EaseConversationListItemClickListener {
            override fun onListItemClicked(conversation: EMConversation?) {
                val id = conversation?.conversationId()
                if (!id.isNullOrEmpty()) {
                    if (conversationChatFragment?.isAdded) {
                        supportFragmentManager.beginTransaction().remove(conversationChatFragment).commit()
                        conversationChatFragment = EaseChatFragment()
                    }
                    conversationChatFragment
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
        conversationChatFragment.showTitleBar()
        supportFragmentManager.beginTransaction()
                .add(R.id.conversation_list_container, conversationListFragment)
                .commit()
        //传入参数
/*        val args = Bundle()
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE)
        args.putString(EaseConstant.EXTRA_USER_ID, "111")
        conversationChatFragment.arguments = args
        supportFragmentManager.beginTransaction()
                .add(R.id.conversation_chat_container, conversationChatFragment)
                .commit()*/
        sIncomingCallPresenter.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        sIncomingCallPresenter.bind(null)
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

    /*incoming call dialog*/
    private var mIcomingViewRoot: View? = null
    private var mIcomingView: PopupWindow? = null
    private var mIcomingAvatar: ImageView? = null
    private var mIcomingTitle: TextView? = null
    private var mIcomingAnswer: View? = null
    private var mIcomingReject: View? = null
    private val mIcomingClicker = object : View.OnClickListener {
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.incoming_answer -> {
                    sIncomingCallPresenter.onAnswer()
                }
                R.id.incoming_reject -> {
                    sIncomingCallPresenter.onReject()
                }
            }
        }
    }

    private fun createIncomingView() {
        mIcomingView = PopupWindow(this)
        val root = LayoutInflater.from(this).inflate(R.layout.incoming_call_view, null, false);
        mIcomingViewRoot = root?.findViewById(R.id.incoming_root)
        mIcomingAnswer = root?.findViewById(R.id.incoming_answer)
        mIcomingReject = root?.findViewById(R.id.incoming_reject)
        mIcomingAvatar = root?.findViewById(R.id.incoming_avatar) as ImageView
        mIcomingTitle = root?.findViewById(R.id.incoming_title) as TextView
        mIcomingAnswer?.setOnClickListener(mIcomingClicker)
        mIcomingReject?.setOnClickListener(mIcomingClicker)

        mIcomingView?.contentView = root
        mIcomingView?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        mIcomingView?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        mIcomingView?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun showIncomingCall(from: String) {
        mHandler.post {
            if (mIcomingView == null) {
                createIncomingView()
            }
            if (!mIcomingView!!.isShowing) {
                mIcomingView?.showAtLocation(
                        window.decorView,
                        Gravity.CENTER_HORIZONTAL or Gravity.TOP,
                        0,
                        0)
            }
            mIcomingTitle?.text = from
        }
    }

    override fun dismissIncomingCall() {
        if (mIcomingView?.isShowing == true) {
            mHandler.post {
                mIcomingView?.dismiss()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            if (intent.action == "com.mumu.simplechat.TEST_MAKE_CALL") {
                val to = intent.getStringExtra("to")
                val type = Utils.parseCallType(intent.getStringExtra("type"))
                if (!to.isNullOrEmpty() && type != null) {
                    Router.goCallView(this, CallArgument(null, to, type))
                    return
                }
            }
            sIncomingCallPresenter.onInvoke(intent)
        }
    }

    override fun getContext(): Context = this
}
