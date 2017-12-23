package com.mumu.simplechat.model.impl

import android.util.Log
import com.hyphenate.chat.EMCallStateChangeListener
import com.mumu.simplechat.model.ICallModel
import com.hyphenate.exceptions.EMServiceNotReadyException
import com.hyphenate.chat.EMClient
import com.hyphenate.media.EMCallSurfaceView
import com.mumu.simplechat.bean.IVideoView
import com.hyphenate.exceptions.EMNoActiveCallException
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.CallStateChangedEvent

object EMCallManager : ICallModel<String>, EMCallStateChangeListener {
    private val TAG = EMCallManager::class.java.simpleName

    private var mStateListener: ICallModel.CallCallback? = null

    init {
        EMClient.getInstance().callManager().addCallStateChangeListener(this)
    }

    private fun setStateListener(listener: ICallModel.CallCallback?) {
        if (listener == null || listener == mStateListener) return
        mStateListener?.onCallStateChanged(
                ICallModel.CallState.IDLE,
                ICallModel.CallError.ERROR_UNAVAILABLE)
        mStateListener = listener
    }

    override fun makeAudioCall(user: String, stateListener: ICallModel.CallCallback?) {
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

    override fun makeVideoCall(user: String, stateListener: ICallModel.CallCallback?) {
        setStateListener(stateListener)
        try {//单参数
            EMClient.getInstance().callManager().makeVideoCall(user)
            EMClient.getInstance().callManager().cameraFacing = 1
           // switchCamera()
        } catch (e: EMServiceNotReadyException) {
            e.printStackTrace()
            stateListener?.onCallStateChanged(
                    ICallModel.CallState.IDLE,
                    ICallModel.CallError.ERROR_UNAVAILABLE)
        }
    }

    override fun answerCall(user: String, stateListener: ICallModel.CallCallback?) {
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
        try {
            EMClient.getInstance().callManager().endCall();
        } catch (e: EMNoActiveCallException) {
            e.printStackTrace()
        }
    }

    override fun rejectCall(user: String) {
        try {
            EMClient.getInstance().callManager().rejectCall()
        } catch (e: EMNoActiveCallException) {
            e.printStackTrace()
        }
    }

    override fun switchCamera() {
        EMClient.getInstance().callManager().switchCamera();
    }

    override fun setVideoCallSurface(opposite: IVideoView, local: IVideoView) {
        EMClient.getInstance().callManager().setSurfaceView(
                opposite.asSurfaceView<EMCallSurfaceView>(),
                local.asSurfaceView<EMCallSurfaceView>()
        )
        //EMClient.getInstance().callManager().getCallOptions().setVideoResolution(1080, 720);
    }

    override fun onCallStateChanged(
            callState: EMCallStateChangeListener.CallState?,
            error: EMCallStateChangeListener.CallError?) {
        val r = convertToCallCallback(callState, error)
        mStateListener?.onCallStateChanged(r.first, r.second)
        EventBus.post(CallStateChangedEvent(r.first))
    }

    private fun convertToCallCallback(
            callState: EMCallStateChangeListener.CallState?,
            error: EMCallStateChangeListener.CallError?):
            Pair<ICallModel.CallState, ICallModel.CallError> = Pair(
            ICallModel.CallState.valueOf(callState?.name ?: "unknown"),
            ICallModel.CallError.valueOf(error?.name ?: "error_unknown")
    )

}