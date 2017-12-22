package com.mumu.simplechat.views.impl

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.Toast
import com.mumu.simplechat.R
import com.mumu.simplechat.Router
import com.mumu.simplechat.Utils
import com.mumu.simplechat.bean.CallArgument
import com.mumu.simplechat.presenters.IMainPresenter
import com.mumu.simplechat.presenters.impl.MainPresenterImpl
import com.mumu.simplechat.views.IMainView
import com.mumu.simplechat.views.fragments.*

class MainActivity : AppCompatActivity(),
        IMainView{

    companion object {
        private val sMainPresenter: IMainPresenter = MainPresenterImpl()
        private val sHandler = Handler(Looper.getMainLooper())
    }

    private val mChatTab: View by lazy { findViewById(R.id.main_dock_chat) }
    private val mContactsTab: View by lazy { findViewById(R.id.main_dock_contacts) }
    private val mSettingTab: View by lazy { findViewById(R.id.main_dock_setting) }
    private val mTabClickListener = View.OnClickListener { v ->
        when (v?.id) {
            R.id.main_dock_chat -> sMainPresenter.onChatList()
            R.id.main_dock_contacts -> sMainPresenter.onContacts()
            R.id.main_dock_setting -> sMainPresenter.onSetting()
        }
    }
    private val chatListFragment = EMChatlistFragment()
    private val contactsFragment = EMContactsFragment()
    private val settingFragment = SettingFragment()
    private val searchFragment = SearchContactFragment()
    private var conversationFragment: Fragment? = null

    override fun selectChatListTab(s: Boolean) {
        sHandler.post {
            mChatTab?.isSelected = s
        }
    }

    override fun selectContactsTab(s: Boolean) {
        sHandler.post {
            mContactsTab?.isSelected = s
        }
    }

    override fun selectSettingTab(s: Boolean) {
        sHandler.post {
            mSettingTab?.isSelected = s
        }
    }

    override fun showChatList() {
        replaceWith(chatListFragment)
    }

    override fun showContacts() {
        replaceWith(contactsFragment)
    }

    override fun showSettings() {
        replaceWith(settingFragment)
    }

    override fun showSearchContact() {
        replaceWith(searchFragment)
    }

    override fun refresh() {
        sHandler.post {
            chatListFragment.refresh()
           // contactsFragment.refresh()
        }
    }

    private fun replaceWith(f: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        if (f.isAdded && !f.isHidden) {
            return
        }
        if (chatListFragment.isAdded && !chatListFragment.isHidden) {
            transaction.hide(chatListFragment)
        }
        if (contactsFragment.isAdded && !contactsFragment.isHidden) {
            transaction.hide(contactsFragment)
        }
        if (settingFragment.isAdded && !settingFragment.isHidden) {
            transaction.hide(settingFragment)
        }
        if (searchFragment.isAdded && !searchFragment.isHidden) {
            transaction.hide(searchFragment)
        }
        if (!f.isAdded) {
            transaction.add(R.id.main_container, f, f::class.java.simpleName).commit()
        } else {
            transaction.show(f).commit()
        }
    }

    /**/
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mChatTab.setOnClickListener(mTabClickListener)
        mContactsTab.setOnClickListener(mTabClickListener)
        mSettingTab.setOnClickListener(mTabClickListener)
        sMainPresenter.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        sMainPresenter.bind(null)
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
        }
    }


    override fun showConversation(arg: Bundle) {
        val t = supportFragmentManager.beginTransaction()
        if (conversationFragment != null && contactsFragment.isAdded) {
            t.remove(contactsFragment)
        }
        conversationFragment = EMConversationFragment()
        conversationFragment!!.arguments = arg
        t.add(R.id.main_conversation_container, conversationFragment).commit()
    }

    override fun showMessage(msg: String) {
        sHandler.post {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    /**/
    private var mContactsDialog: AlertDialog? = null

    private fun createContactDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setNegativeButton("拒绝") { _, _ ->
            sMainPresenter.onRefuse()
        };
        builder.setPositiveButton("接受", { _, _ ->
            sMainPresenter.onAgree()
        });
        mContactsDialog = builder.create()
    }

    override fun showInvitation(name: String, reason: String) {
        sHandler.post {
            if (mContactsDialog == null) {
                createContactDialog()
            }
            mContactsDialog!!.setTitle("好友请求")
            mContactsDialog!!.setMessage(String.format("来自%s的好友请求\n%s", name, reason))
            if (!mContactsDialog!!.isShowing) {
                mContactsDialog!!.show()
            }
        }
    }

    override fun onBackPressed() {
        if (!sMainPresenter.onBackKey()) {
            super.onBackPressed()
        }
    }
}

