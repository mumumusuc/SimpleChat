package com.mumu.simplechat.presenters.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.hardware.camera2.CameraManager
import android.util.Log
import com.mumu.simplechat.MainApplication
import com.mumu.simplechat.Router
import com.mumu.simplechat.bean.UserInfo
import com.mumu.simplechat.model.ICallModel
import com.mumu.simplechat.model.impl.EMCallManager
import com.mumu.simplechat.presenters.ICallPresenter
import com.mumu.simplechat.views.ICallView
import android.hardware.camera2.CameraCharacteristics
import com.mumu.simplechat.Config


class CallPresenter : ICallPresenter, ICallModel.CallCallback {
    private val TAG = CallPresenter::class.java.simpleName

    private val mCallModel: ICallModel<String> = EMCallManager
    private var mCallView: ICallView? = null
    private var mLastState: ICallModel.CallState? = null

    override fun bind(view: ICallView?) {
        mCallView = view
        if (mCallView != null) {
            mCallModel.setVideoCallSurface(
                    mCallView!!.getLocalVideoView(),
                    mCallView!!.getOppositeVideoView()
            )
        } else {
            onEndCall()
        }
    }

    override fun onInvoke(intent: Intent) {
        val arg = Router.getCallExtra(intent) ?: return
        val from = arg.getFrom()
        val to = arg.getTo()
        val type = arg.getType()
        Log.d(TAG, "onInvoke -> from = $from, to = $to, type = $type")
        if (!from.isNullOrEmpty() && !from.isNullOrBlank()) {
            Log.i(TAG, "onInvoke -> 接听${type}电话")
            //TODO 控制界面显示
            when (type) {
                ICallModel.CallType.AUDIO -> {
                    showAudioCallView()
                }
                ICallModel.CallType.VIDEO -> {
                    showVideoCallView()
                }
            }
        } else if (!to.isNullOrEmpty() && !to.isNullOrBlank()) {
            Log.i(TAG, "onInvoke -> 拨打${type}电话")
            makeCall(to!!, type)
        }
    }

    override fun onCall() {
        mCallModel.answerCall("", this)
    }

    override fun onEndCall() {
        mCallModel.endCall("")
        mCallView?.close()
        mLastState = null
    }

    override fun onSwitchCamera() {
        mCallModel.switchCamera()
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
                if (mLastState == ICallModel.CallState.ACCEPTED ||
                        mLastState == ICallModel.CallState.CONNECTED) {
                    //TODO:关闭通话界面,释放相关资源
                    mCallView?.showMessage("电话挂断")
                    onEndCall()
                } else if (mLastState == ICallModel.CallState.CONNECTING) {
                    //TODO:对方未在线
                    mCallView?.showMessage("对方不在线")
                }
                //onEndCall()
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
        mLastState = callState
    }

    private fun makeCall(name: String, type: ICallModel.CallType) {
        when (type) {
            ICallModel.CallType.AUDIO -> {
                mCallModel.makeAudioCall(name, this)
                showAudioCallView()
            }
            ICallModel.CallType.VIDEO -> {
                mCallModel.makeVideoCall(name, this)
                showVideoCallView()
            }
        }
    }

    private fun showUserInfo(user: UserInfo) {

    }

    private fun showAudioCallView() {
        mCallView?.showAudioView(true)
        mCallView?.showOppositeVideoView(false)
        mCallView?.showSelfVideoView(false)
    }

    private fun showVideoCallView() {
        mCallView?.showAudioView(false)
        mCallView?.showOppositeVideoView(true)
        mCallView?.showSelfVideoView(true)
    }

    private fun switchCamera(){
        val cameraManager = MainApplication.getContext()!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameras = cameraManager.cameraIdList
        cameras.forEach { Log.i(TAG, "get camera $it") }
        if(cameras.size <= 1){
            val characteristics = cameraManager.getCameraCharacteristics(cameras[0])
            if(CameraCharacteristics.LENS_FACING_FRONT != characteristics.get(CameraCharacteristics.LENS_FACING)){
                Log.d(TAG,"got 1 camera and itsnt LENS_FACING_FRONT,switch camera")
                mCallModel.switchCamera()
            }
        }
    }
}