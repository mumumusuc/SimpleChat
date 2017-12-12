package com.mumu.simplechat.presenters.impl

import android.content.Intent
import android.util.Log
import com.mumu.simplechat.Router
import com.mumu.simplechat.bean.CallArgument
import com.mumu.simplechat.presenters.IIncomingCallPresenter
import com.mumu.simplechat.views.IIncomingCallView
import com.mumu.simplechat.model.ICallModel
import com.mumu.simplechat.model.impl.EMCallModel


class IncomingCallPresenter : IIncomingCallPresenter {
    private val TAG = IncomingCallPresenter::class.java.simpleName
    private var mIncomingCallView: IIncomingCallView? = null
    private val mCallModel: ICallModel<String> = EMCallModel
    private var mCallArgument: CallArgument? = null

    override fun bind(view: IIncomingCallView?) {
        mIncomingCallView = view
    }

    override fun onInvoke(intent: Intent) {
        val arg = Router.getCallExtra(intent)
        if (arg == null || !arg.isAvaliable()) return
        mCallArgument = arg
        val from = arg.getFrom()
        val type = arg.getType()
        if (!from.isNullOrEmpty() && !from.isNullOrBlank()) {
            val msg = "来自${from}的${type}电话"
            Log.i(TAG, "onInvoke -> ${msg}")
            //TODO 控制界面显示
            mIncomingCallView?.showIncomingCall(msg)
        }
    }

    override fun onReject() {
        mCallModel.rejectCall("")
        mIncomingCallView?.dismissIncomingCall()
    }

    override fun onAnswer() {
        if (mCallArgument == null) return
        Router.goCallView(mIncomingCallView!!.getContext(), mCallArgument!!)
        mIncomingCallView?.dismissIncomingCall()
        mCallModel.answerCall("", null)
    }

}