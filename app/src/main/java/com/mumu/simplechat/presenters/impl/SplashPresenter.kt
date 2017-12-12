package com.mumu.simplechat.presenters.impl

import com.google.common.eventbus.Subscribe
import com.mumu.simplechat.Config
import com.mumu.simplechat.Router
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.LoginSuccessEvent
import com.mumu.simplechat.eventbus.events.RegisterSuccessEvent
import com.mumu.simplechat.model.IUserModel
import com.mumu.simplechat.model.impl.EMUserModel
import com.mumu.simplechat.presenters.ISplashPresenter
import com.mumu.simplechat.views.ISplashView


class SplashPresenter : ISplashPresenter {
    private val LOGIN_VIEW = 1
    private val REGISTER_VIEW = 2

    private var mSplashView: ISplashView? = null
    private var mCurrentView: Int = 0
    private val mUserModel: IUserModel = EMUserModel()

    init {
        EventBus.register(this)
    }

    override fun bind(view: ISplashView?) {
        mSplashView = view
        if (mUserModel.checkLogin() == IUserModel.State.USER_ALREADY_LOGIN
                && mUserModel.isAutoLogin()
                && Config.autoLogin) {
            goNext()
        } else {
            showLoginView()
        }
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
        goNext()
    }

    @Subscribe
    fun onRegisterSuccess(event: RegisterSuccessEvent) {
        mSplashView?.showLoginView()
    }

    private fun goNext() {
        val context = mSplashView?.getContext()
        if (context != null) {
            Router.goRecentChatView(context)
        }
        mSplashView?.close()
    }
}
