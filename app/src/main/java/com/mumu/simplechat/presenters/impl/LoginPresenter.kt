package com.mumu.simplechat.presenters.impl

import android.content.Intent
import com.mumu.simplechat.bean.UserInfo
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.LoginSuccessEvent
import com.mumu.simplechat.model.IUserModel
import com.mumu.simplechat.model.impl.EMUserModel
import com.mumu.simplechat.presenters.ILoginPresenter
import com.mumu.simplechat.views.ILoginView

class LoginPresenter : ILoginPresenter, IUserModel.Callback {
    private val mUserModel: IUserModel = EMUserModel()
    private var mLoginView: ILoginView? = null
    private var mUser: UserInfo? = null

    override fun bind(view: ILoginView?) {
        mLoginView = view
        if (mLoginView != null) {
            val default = mUserModel.getDefaultSavedUser(mLoginView!!.getContext())
            if (default != null) {
                mLoginView!!.showUserName(default.useName)
                mLoginView!!.showPassword(default.password)
            }
        }
    }

    override fun onLogin() {
        mUser = UserInfo(
                mLoginView?.getUserName() ?: "",
                mLoginView?.getPassword() ?: "",
                null
        )
        mUserModel.login(mUser!!, this)
        mLoginView?.showLoginWaiting(true)
        mLoginView?.showMessage("登录中")
    }

    override fun onCancel() {
    }

    override fun onSuccess() {
        mLoginView?.showLoginWaiting(false)
        mLoginView?.showMessage("登录成功")
        mUserModel.saveAsDefaultUser(mLoginView!!.getContext(), mUser!!)
        goMainActivity()
    }

    override fun onProgress(progress: Int, status: String?) {
    }

    override fun onError(state: IUserModel.State, message: String?) {
        mLoginView?.showLoginWaiting(false)
        mLoginView?.showMessage("$state , $message")
        mUser = null
    }

    private fun goMainActivity() {
        //TODO: tell splash_screen to go to main_interface and finish himself
        EventBus.post(LoginSuccessEvent())
    }
}