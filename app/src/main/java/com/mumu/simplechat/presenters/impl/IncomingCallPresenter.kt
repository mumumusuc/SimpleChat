package com.mumu.simplechat.presenters.impl

import android.content.Context
import android.content.Intent
import android.util.Log
import com.mumu.simplechat.presenters.IIncomingCallPresenter
import com.mumu.simplechat.views.IIncomingCallView
import com.mumu.simplechat.bean.UserInfo
import com.mumu.simplechat.model.ICallModel
import com.mumu.simplechat.model.impl.EMCallModel


class IncomingCallPresenter : IIncomingCallPresenter {
    private val TAG = IncomingCallPresenter::class.java.simpleName
    private var mIncomingCallView: IIncomingCallView? = null
    private val mCallModel: ICallModel<String> = EMCallModel()

    override fun bind(view: IIncomingCallView?) {
        mIncomingCallView = view
    }

    override fun onReject() {
        mCallModel.rejectCall("")
        mIncomingCallView?.dismissIncomingCall("")
    }

    override fun onAnswer() {
        val intent = Intent("com.mumu.simplechat.CALL_ACTIVITY")
        mIncomingCallView?.go(intent)
        mIncomingCallView?.dismissIncomingCall("")
    }

    private fun startIncomingCallView(context: Context?) {
        val intent = Intent("com.mumu.simplechat.INCOMING_CALL_ACTIVITY")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.`package` = context?.packageName
        context?.startActivity(intent)
    }
}