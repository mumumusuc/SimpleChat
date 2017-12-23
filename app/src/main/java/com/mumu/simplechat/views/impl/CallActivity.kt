package com.mumu.simplechat.views.impl

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.TextView
import com.mumu.simplechat.Config
import com.mumu.simplechat.R
import com.mumu.simplechat.bean.IVideoView
import com.mumu.simplechat.presenters.ICallPresenter
import com.mumu.simplechat.presenters.impl.CallPresenter
import com.mumu.simplechat.views.ICallView

class CallActivity : AppCompatActivity(), ICallView {
    companion object {
        private val sPresenter: ICallPresenter = CallPresenter()
        private val sHandler = Handler(Looper.getMainLooper())
    }

    private val TAG = CallActivity::class.java.simpleName

    private val mUserRoot: View by lazy { findViewById(R.id.call_user_root) }
    private val mUserAvatar: ImageView by lazy { findViewById(R.id.call_user_avatar) as ImageView }
    private val mUserName: TextView by lazy { findViewById(R.id.call_user_name) as TextView }
    private val mStateMsg: TextView by lazy { findViewById(R.id.call_state) as TextView }
    private val mCallEnd: View by lazy { findViewById(R.id.call_end) }
    private val mCallOppositeView: IVideoView
            by lazy { findViewById(R.id.call_opposite_surface) as IVideoView }
    private val mCallLocalView: IVideoView
            by lazy { findViewById(R.id.call_local_surface) as IVideoView }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = if (Config.isPhone()) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call_dialog_layout)
        mCallEnd.setOnClickListener { _ -> sPresenter.onEndCall() }
        mCallEnd.setOnLongClickListener { _ ->
            sPresenter.onSwitchCamera(); true
        }
        sPresenter.bind(this)
        onNewIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        sPresenter.bind(null)
    }

    override fun getOppositeVideoView(): IVideoView = mCallOppositeView

    override fun getLocalVideoView(): IVideoView = mCallLocalView

    override fun showOppositeVideoView(show: Boolean) {
        sHandler.post {
            val view = mCallOppositeView as View
            view.visibility = if (show) VISIBLE else INVISIBLE
        }
    }

    override fun showSelfVideoView(show: Boolean) {
        sHandler.post {
            val view = mCallLocalView as View
            view.visibility = if (show) VISIBLE else INVISIBLE
        }
    }

    override fun showAudioView(show: Boolean) {
        sHandler.post {
            mUserRoot.visibility = if (show) VISIBLE else GONE
        }
    }

    override fun showUserAvatar(avatar: Drawable?) {
        if (avatar != null) {
            mUserAvatar.setImageDrawable(avatar)
        }
    }

    override fun showUserName(name: String) {
        mUserName.text = name
    }

    override fun showMessage(msg: String) {
        sHandler.post {
            mStateMsg?.text = msg
        }
    }

    override fun close() {
        finish()
    }

    /*test only*/
    override fun onNewIntent(intent: Intent?) {
        Log.i(TAG, "onNewIntent")
        if (intent != null) {
            sPresenter.onInvoke(intent)
        }
    }
}
