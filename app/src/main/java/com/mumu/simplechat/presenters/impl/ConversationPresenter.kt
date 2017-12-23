package com.mumu.simplechat.presenters.impl

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import com.hyphenate.easeui.EaseConstant
import com.mumu.simplechat.Config
import com.mumu.simplechat.MainApplication
import com.mumu.simplechat.Router
import com.mumu.simplechat.bean.CallArgument
import com.mumu.simplechat.model.ICallModel
import com.mumu.simplechat.presenters.IConversationPresenter
import com.mumu.simplechat.views.IConversationView

class ConversationPresenter : IConversationPresenter {
    private val TAG = ConversationPresenter::class.java.simpleName
    private var mConversationView: IConversationView? = null
    private var mUser: String? = null

    override fun bind(view: IConversationView?) {
        mConversationView = view
        if (mConversationView != null) {
            val bundle = mConversationView!!.getArguments()
            mUser = bundle.getString(EaseConstant.EXTRA_USER_ID)
            mConversationView!!.showTitle(mUser!!)
        }
    }

    override fun onAudioCall() {
        val context = MainApplication.getContext()
        if (context != null) {
            Log.d(TAG, "onAudioCall -> name = $mUser")
            Router.goCallView(context, CallArgument(null, mUser, ICallModel.CallType.AUDIO))
        }
    }

    override fun onVideoCall() {
        val context = MainApplication.getContext()
        if (context != null) {
            Log.d(TAG, "onVideoCall -> name = $mUser")
            Router.goCallView(context, CallArgument(null, mUser, ICallModel.CallType.VIDEO))
        }
    }
}