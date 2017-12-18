package com.mumu.simplechat.views.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.hyphenate.easeui.ui.EaseChatFragment
import com.mumu.simplechat.R
import com.mumu.simplechat.bean.TitleBar
import com.mumu.simplechat.presenters.IConversationPresenter
import com.mumu.simplechat.presenters.impl.ConversationPresenter
import com.mumu.simplechat.views.IConversationView

class EMConversationFragment : EaseChatFragment(), IConversationView {
    companion object {
        private val sHandler = Handler(Looper.getMainLooper())
        private val sPresenter: IConversationPresenter = ConversationPresenter()
    }

    private var mTitleBar: TitleBar? = null
    private val mPopup: PopupWindow = PopupWindow()
    private var title: String? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        sPresenter.bind(this)
    }

    override fun onDetach() {
        super.onDetach()
        sPresenter.bind(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideTitleBar()
        mPopup.contentView = LayoutInflater.from(context).inflate(R.layout.call_popup_layout, null, false)
        mPopup.width = ViewGroup.LayoutParams.WRAP_CONTENT
        mPopup.height = ViewGroup.LayoutParams.WRAP_CONTENT
        mPopup.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mPopup.contentView.findViewById(R.id.make_audio_call).setOnClickListener {
            sPresenter.onAudioCall()
            mPopup.dismiss()
        }
        mPopup.contentView.findViewById(R.id.make_video_call).setOnClickListener {
            sPresenter.onVideoCall()
            mPopup.dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.base_fragment_layout, container, false) as ViewGroup
        mTitleBar = root.findViewById(R.id.base_fragment_toolbar) as TitleBar
        mTitleBar?.showLeftIcon(false)
        mTitleBar?.showLeftText(false)
        mTitleBar?.showRightText(false)
        mTitleBar?.showRightIcon(true)
        mTitleBar?.setRightIcon(context.getDrawable(R.drawable.ic_call))
        mTitleBar?.setRightAction(View.OnClickListener {
            if (mPopup.isShowing) {
                mPopup.dismiss()
            } else {
                mPopup.isOutsideTouchable = true
                mPopup.isFocusable = true
                mPopup.showAsDropDown(mTitleBar!!.getRightAnchor())
            }
        })
        root.addView(v)
        return root
    }

    override fun showTitle(title: String) {
        sHandler.post {
            mTitleBar?.setTitle(title)
            this.title = title
        }
    }
}