package com.mumu.simplechat.presenters.impl

import android.content.Intent
import com.google.common.eventbus.Subscribe
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.LoginSuccessEvent
import com.mumu.simplechat.presenters.ISplashPresenter
import com.mumu.simplechat.views.ISplashView


class SplashPresenter : ISplashPresenter {
    private val LOGIN_VIEW = 1
    private val REGISTER_VIEW = 2

    private var mSplashView: ISplashView? = null
    private var mCurrentView: Int = 0

    init {
        EventBus.register(this)
    }

    override fun bind(view: ISplashView?) {
        mSplashView = view
        showLoginView()
    }

    override fun onSwitchScreen() {
        when (mCurrentView) {
            LOGIN_VIEW -> {
                showRegisterView()
            }
            REGISTER_VIEW -> {
                showLoginView()
            }
        }
    }

    private fun showLoginView() {
        mSplashView?.showLoginView()
        mCurrentView = LOGIN_VIEW
    }

    private fun showRegisterView() {
        mSplashView?.showRegisterView()
        mCurrentView = REGISTER_VIEW
    }

    @Subscribe
    fun onLoginSuccess(event: LoginSuccessEvent) {
        val context = mSplashView?.getContext()
        if (context != null) {
            try {
                val intent = Intent("android.intent.action.MAIN_ACTIVITY")
                intent.`package` = context.packageName
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                mSplashView!!.exit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
