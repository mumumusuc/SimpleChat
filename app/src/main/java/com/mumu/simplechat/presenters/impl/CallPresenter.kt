package com.mumu.simplechat.presenters.impl

import android.util.Log
import com.mumu.simplechat.bean.UserInfo
import com.mumu.simplechat.model.ICallModel
import com.mumu.simplechat.model.impl.EMCallModel
import com.mumu.simplechat.presenters.ICallPresenter
import com.mumu.simplechat.views.ICallView

class CallPresenter : ICallPresenter, ICallModel.CallCallback {
    private val TAG = CallPresenter::class.java.simpleName

    private val mCallModel: ICallModel<String> = EMCallModel()
    private var mCallView: ICallView? = null

    override fun bind(view: ICallView?) {
        mCallView = view
        if (mCallView != null) {
            mCallModel.setVideoCallSurface(
                    mCallView!!.getOppositeVideoView(),
                    mCallView!!.getLocalVideoView()
            )
        }
    }

    override fun onCall() {
        // mCallModel.makeAudioCall(null, this)
        mCallModel.answerCall("", this)
    }

    override fun onEndCall() {
        // mCallModel.endCall(null)
    }

    /**/
    override fun onCallStateChanged(
            callState: ICallModel.CallState,
            error: ICallModel.CallError) {
        when (callState) {
        // 正在连接对方
            ICallModel.CallState.CONNECTING -> {
                mCallView?.showMessage("连线中")
                Log.d(TAG, "onCallStateChanged -> CONNECTING")
            }
        // 双方已经建立连接
            ICallModel.CallState.CONNECTED -> {
                mCallView?.showMessage("连线成功")
                Log.d(TAG, "onCallStateChanged -> CONNECTED")
            }
        // 电话接通成功
            ICallModel.CallState.ACCEPTED -> {
                mCallView?.showMessage("接通成功")
                Log.d(TAG, "onCallStateChanged -> ACCEPTED")
            }
        // 电话断了
            ICallModel.CallState.DISCONNECTED -> {
                mCallView?.showMessage("电话断线")
                Log.d(TAG, "onCallStateChanged -> DISCONNECTED")
            }
        //网络不稳定
            ICallModel.CallState.NETWORK_UNSTABLE -> {
                mCallView?.showMessage("网络不稳定")
                Log.d(TAG, "onCallStateChanged -> NETWORK_UNSTABLE")
                //无通话数据
                if (error == ICallModel.CallError.ERROR_NO_DATA) {

                } else {

                }
            }
        //网络恢复正常
            ICallModel.CallState.NETWORK_NORMAL -> {
            }
        }
    }

    /**/

}