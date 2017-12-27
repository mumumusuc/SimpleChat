package com.mumu.simplechat.presenters.impl

import android.util.Log
import com.mumu.simplechat.bean.UserInfo
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.LoginSuccessEvent
import com.mumu.simplechat.model.IUserModel
import com.mumu.simplechat.model.impl.EMUserManager
import com.mumu.simplechat.presenters.ILoginPresenter
import com.mumu.simplechat.views.ILoginView
import io.reactivex.android.schedulers.AndroidSchedulers

class LoginPresenter : ILoginPresenter {
    private val TAG = LoginPresenter::class.java.simpleName
    private val mUserModel: IUserModel = EMUserManager()
    private var mLoginView: ILoginView? = null
    private var mUser: UserInfo? = null

    override fun bind(view: ILoginView?) {
        mLoginView = view
        if (mLoginView != null) {
            mLoginView?.enableAutoLogin(mUserModel.isAutoLogin())
            mLoginView?.enableSaveUser(mUserModel.isSaveUser())
            val default = mUserModel.getDefaultSavedUser(mLoginView!!.getContext())

            if (default != null) {
                mLoginView!!.showUserName(default.useName)
                mLoginView!!.showPassword(default.password)
            }
        }
    }

    override fun onLogin() {
        Log.d(TAG, "onLogin")
        mUser = UserInfo(
                mLoginView?.getUserName() ?: "",
                mLoginView?.getPassword() ?: "",
                null
        )
        mLoginView?.showLoginWaiting(true)
        mLoginView?.showMessage("登录中")
        mUserModel.login(mUser!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {},
                        { e -> onError(e.localizedMessage) },
                        { onSuccess() }
                )
    }

    override fun onCancel() {
    }

    override fun onSaveUser(save: Boolean) {
        if (mUserModel.isAutoLogin()) {
            mLoginView?.enableSaveUser(true)
            return
        }
        mUserModel.enableSaveUser(save)
        mLoginView?.enableSaveUser(save)
    }

    override fun onAutoLogin(auto: Boolean) {
        mUserModel.enableAutoLogin(auto)
        mLoginView?.enableAutoLogin(auto)
        if (auto)
            mLoginView?.enableSaveUser(true)
    }

    private fun onSuccess() {
        val context = mLoginView?.getContext()
        mLoginView?.showLoginWaiting(false)
        mLoginView?.showMessage("登录成功")
        if (context != null) {
            mUserModel.saveAsDefaultUser(context, mUser!!)
        }
        goMainActivity()
    }

    private fun onError(message: String?) {
        Log.d(TAG, "onError -> state = ${message}")
        mLoginView?.showLoginWaiting(false)
        mLoginView?.showMessage("$message")
        mUser = null
    }

    private fun goMainActivity() {
        //TODO: tell splash_screen to go to main_interface and finish himself
        EventBus.post(LoginSuccessEvent())
    }
}