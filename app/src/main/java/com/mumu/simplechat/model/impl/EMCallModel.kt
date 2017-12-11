package com.mumu.simplechat.model.impl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hyphenate.chat.EMCallStateChangeListener
import com.mumu.simplechat.model.ICallModel
import com.hyphenate.exceptions.EMServiceNotReadyException
import com.hyphenate.chat.EMClient
import com.hyphenate.media.EMCallSurfaceView
import com.mumu.simplechat.bean.IVideoView
import com.hyphenate.exceptions.EMNoActiveCallException

class EMCallModel : ICallModel<String>, EMCallStateChangeListener {
    private val TAG = EMCallModel::class.java.simpleName

    private var mStateListener: ICallModel.CallCallback? = null
    private var fromUser: String? = null
    private var toUser: String? = null
    private var callType: ICallModel.CallType? = null

    init {
        EMClient.getInstance().callManager().addCallStateChangeListener(this)
    }

    private fun setStateListener(listener: ICallModel.CallCallback) {
        if (listener == mStateListener) return
        mStateListener?.onCallStateChanged(
                ICallModel.CallState.IDLE,
                ICallModel.CallError.ERROR_UNAVAILABLE)
        mStateListener = listener
    }

    override fun makeAudioCall(user: String, stateListener: ICallModel.CallCallback) {
        setStateListener(stateListener)
        try {//单参数
            EMClient.getInstance().callManager().makeVoiceCall(user)
        } catch (e: EMServiceNotReadyException) {
            e.printStackTrace()
            stateListener?.onCallStateChanged(
                    ICallModel.CallState.IDLE,
                    ICallModel.CallError.ERROR_UNAVAILABLE)
        }
    }

    override fun makeVideoCall(user: String, stateListener: ICallModel.CallCallback) {
        setStateListener(stateListener)
        try {//单参数
            EMClient.getInstance().callManager().makeVideoCall(user)
        } catch (e: EMServiceNotReadyException) {
            e.printStackTrace()
            stateListener?.onCallStateChanged(
                    ICallModel.CallState.IDLE,
                    ICallModel.CallError.ERROR_UNAVAILABLE)
        }
    }

    override fun answerCall(user: String, stateListener: ICallModel.CallCallback) {
        setStateListener(stateListener)
        try {
            EMClient.getInstance().callManager().answerCall()
        } catch (e: Exception) {
            e.printStackTrace()
            stateListener?.onCallStateChanged(
                    ICallModel.CallState.IDLE,
                    ICallModel.CallError.ERROR_UNAVAILABLE)
        }
    }

    override fun endCall(user: String) {
        EMClient.getInstance().callManager().endCall();
    }

    override fun rejectCall(user: String) {
        try {
            EMClient.getInstance().callManager().rejectCall()
        } catch (e: EMNoActiveCallException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    override fun setVideoCallSurface(opposite: IVideoView, local: IVideoView) {
        EMClient.getInstance().callManager().setSurfaceView(
                opposite.asSurfaceView<EMCallSurfaceView>(),
                local.asSurfaceView<EMCallSurfaceView>()
        )
    }

    /**/
    override fun onCallStateChanged(
            callState: EMCallStateChangeListener.CallState?,
            error: EMCallStateChangeListener.CallError?) {
        val r = convertToCallCallback(callState, error)
        mStateListener?.onCallStateChanged(r.first, r.second)
    }

    private fun convertToCallCallback(
            callState: EMCallStateChangeListener.CallState?,
            error: EMCallStateChangeListener.CallError?):
            Pair<ICallModel.CallState, ICallModel.CallError> = Pair(
            ICallModel.CallState.valueOf(callState?.name ?: "unknown"),
            ICallModel.CallError.valueOf(error?.name ?: "error_unknown")
    )

    /**/
    private inner class IncomingCallLisenter : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // 拨打方username
            val from = intent?.getStringExtra("from") ?: ""
            // call type
            val type = intent?.getStringExtra("type") ?: ""
            if (from.isNullOrEmpty() || type.isNullOrEmpty()) {
                return
            }
            fromUser = from
            callType = convertToCallType(type)
            Log.d(TAG, "onReceive -> from:$fromUser, type:$callType")
            //TODO:跳转到通话页面
        }
    }

    private fun convertToCallType(type: String): ICallModel.CallType {
        return ICallModel.CallType.valueOf(type.toLowerCase())
    }
}