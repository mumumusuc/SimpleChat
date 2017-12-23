package com.mumu.simplechat.views.helper

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.mumu.simplechat.R


class PopupHelper {
    companion object {
        fun makeDropdown(anchor: View): Popup {
            //return Dropdown(anchor)
            return CenterPopup(anchor)
        }
    }

    private constructor()

    interface Popup {
        fun setFirstAction(body: (View) -> Unit): Popup

        fun setFirstAction(action: View.OnClickListener): Popup

        fun setSecondAction(body: (View) -> Unit): Popup

        fun setSecondAction(action: View.OnClickListener): Popup

        fun show()

        fun dismiss()

        fun isShowing(): Boolean
    }

    private class Dropdown : Popup {
        private val mPopup: PopupWindow = PopupWindow()
        private var mAnchor: View? = null

        constructor(anchor: View) {
            mAnchor = anchor
            mPopup.contentView = LayoutInflater.from(anchor.context).inflate(R.layout.call_popup_layout, null, false)
            mPopup.width = ViewGroup.LayoutParams.WRAP_CONTENT
            mPopup.height = ViewGroup.LayoutParams.WRAP_CONTENT
            mPopup.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mPopup.isOutsideTouchable = true
            mPopup.isFocusable = true
        }

        override fun setFirstAction(body: (View) -> Unit): Dropdown {
            mPopup.contentView.findViewById(R.id.make_audio_call).setOnClickListener(body)
            return this
        }

        override fun setFirstAction(action: View.OnClickListener): Dropdown {
            mPopup.contentView.findViewById(R.id.make_audio_call).setOnClickListener(action)
            return this
        }

        override fun setSecondAction(body: (View) -> Unit): Dropdown {
            mPopup.contentView.findViewById(R.id.make_video_call).setOnClickListener(body)
            return this
        }

        override fun setSecondAction(action: View.OnClickListener): Dropdown {
            mPopup.contentView.findViewById(R.id.make_video_call).setOnClickListener(action)
            return this
        }

        override fun show() {
            if (!isShowing() && mAnchor != null) {
                mPopup.showAsDropDown(mAnchor)
            }
        }

        override fun dismiss() {
            if (isShowing()) {
                mPopup.dismiss()
            }
        }

        override fun isShowing(): Boolean = mPopup.isShowing
    }

    private class CenterPopup : Popup {
        private var mDialog: Dialog
        private var mRootView: View

        constructor(anchor: View) {
            val builder = AlertDialog.Builder(anchor.context,R.style.PopupDialog)
            mRootView = LayoutInflater.from(anchor.context).inflate(R.layout.call_popup_center_layout, null, false)
            builder.setView(mRootView)
            mDialog = builder.create()
        }

        override fun setFirstAction(body: (View) -> Unit): Popup {
            mRootView.findViewById(R.id.make_audio_call).setOnClickListener(body)
            return this
        }

        override fun setFirstAction(action: View.OnClickListener): Popup {
            mRootView.findViewById(R.id.make_audio_call).setOnClickListener(action)
            return this
        }

        override fun setSecondAction(body: (View) -> Unit): Popup {
            mRootView.findViewById(R.id.make_video_call).setOnClickListener(body)
            return this
        }

        override fun setSecondAction(action: View.OnClickListener): Popup {
            mRootView.findViewById(R.id.make_video_call).setOnClickListener(action)
            return this
        }

        override fun show() {
            mDialog.show()
        }

        override fun dismiss() {
            mDialog.dismiss()
        }

        override fun isShowing(): Boolean {
            return mDialog.isShowing
        }
    }
}