package com.mumu.simplechat.views.helper

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.mumu.simplechat.R


class DropdownHelper {
    companion object {
        fun makeDropdown(anchor: View): Dropdown {
            return Dropdown(anchor)
        }
    }

    private constructor()

    class Dropdown {
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

        fun setFirstAction(body:(View) -> Unit): Dropdown {
            mPopup.contentView.findViewById(R.id.make_audio_call).setOnClickListener(body)
            return this
        }

        fun setFirstAction(action: View.OnClickListener): Dropdown {
            mPopup.contentView.findViewById(R.id.make_audio_call).setOnClickListener(action)
            return this
        }

        fun setSecondAction(body:(View) -> Unit): Dropdown {
            mPopup.contentView.findViewById(R.id.make_video_call).setOnClickListener(body)
            return this
        }

        fun setSecondAction(action: View.OnClickListener): Dropdown {
            mPopup.contentView.findViewById(R.id.make_video_call).setOnClickListener(action)
            return this
        }

        fun show() {
            if (!isShowing() && mAnchor != null) {
                mPopup.showAsDropDown(mAnchor)
            }
        }

        fun dismiss() {
            if (isShowing()) {
                mPopup.dismiss()
            }
        }

        fun isShowing(): Boolean = mPopup.isShowing
    }
}