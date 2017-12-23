package com.mumu.simplechat.views.fragments

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mumu.simplechat.R
import com.mumu.simplechat.bean.TitleBar
import com.mumu.simplechat.presenters.ISettingPresenter
import com.mumu.simplechat.presenters.impl.SettingPresenter
import com.mumu.simplechat.views.ISettingView

class SettingFragment : ISettingView, Fragment() {

    companion object {
        private val sPresenter: ISettingPresenter = SettingPresenter()
        private val sHandler = Handler(Looper.getMainLooper())
    }

    private var mWaitingDialog: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.setting_layout, container, false)
        val titleBar = root.findViewById(R.id.base_fragment_toolbar) as TitleBar
        titleBar.setTitle("设置")
        titleBar.showLeftIcon(false)
        titleBar.showLeftText(false)
        titleBar.showRightIcon(false)
        titleBar.showRightText(false)
        root.findViewById(R.id.setting_logout)?.setOnClickListener { sPresenter.onLogout() }
        return root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        sPresenter.bind(this)
    }

    override fun onDetach() {
        super.onDetach()
        sPresenter.bind(null)
    }

    override fun showWaitingMessage(msg: String) {
        sHandler.post {
            if (mWaitingDialog == null) {
                mWaitingDialog = ProgressDialog(context)
                mWaitingDialog?.isIndeterminate = true;
                mWaitingDialog?.setCancelable(false)
            }
            mWaitingDialog?.setMessage(msg)
            if (mWaitingDialog?.isShowing == false) {
                mWaitingDialog?.show();
            }
        }
    }

    override fun dismissWaitMessage() {
        if (mWaitingDialog?.isShowing == true) {
            mWaitingDialog?.dismiss()
        }
    }

    override fun close() {
        activity?.finish()
    }
}