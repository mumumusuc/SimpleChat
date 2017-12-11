package com.mumu.simplechat.views.impl

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.mumu.simplechat.R
import com.mumu.simplechat.bean.IVideoView
import com.mumu.simplechat.presenters.ICallPresenter
import com.mumu.simplechat.presenters.impl.CallPresenter
import com.mumu.simplechat.views.ICallView

class CallDialogActivity : AppCompatActivity(), ICallView {
    companion object {
        private val sPresenter: ICallPresenter = CallPresenter()
    }

    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private val mStateMsg: TextView by lazy { findViewById(R.id.call_state) as TextView }
    private val mCallCall: View by lazy { findViewById(R.id.call_call) }
    private val mCallEnd: View by lazy { findViewById(R.id.call_end) }
    private val mCallOppositeView: IVideoView
            by lazy { findViewById(R.id.call_opposite_surface) as IVideoView }
    private val mCallLocalView: IVideoView
            by lazy { findViewById(R.id.call_local_surface) as IVideoView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_dialog_layout)
        mCallCall.setOnClickListener { _ -> sPresenter.onCall() }
        mCallEnd.setOnClickListener { _ -> sPresenter.onEndCall() }
        sPresenter.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        sPresenter.bind(null)
    }

    override fun getOppositeVideoView(): IVideoView = mCallOppositeView

    override fun getLocalVideoView(): IVideoView = mCallLocalView

    override fun showSelfVoiceEnable(enable: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSelfVideoEnable(enable: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showVideoView(show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSelfVideoView(show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAudioView(show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**/
    override fun showMessage(msg: String) {
        mHandler.post {
            mStateMsg?.text = msg
        }
    }
}
