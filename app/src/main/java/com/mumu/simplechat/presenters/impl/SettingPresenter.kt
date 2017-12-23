package com.mumu.simplechat.presenters.impl

import com.mumu.simplechat.MainApplication
import com.mumu.simplechat.Router
import com.mumu.simplechat.model.IUserModel
import com.mumu.simplechat.model.impl.EMUserManager
import com.mumu.simplechat.presenters.ISettingPresenter
import com.mumu.simplechat.views.ISettingView

class SettingPresenter : ISettingPresenter {

    private val mUserManager: IUserModel = EMUserManager()
    private var mView: ISettingView? = null

    override fun bind(view: ISettingView?) {
        mView = view
    }

    override fun onLogout() {
        mView?.showWaitingMessage("退出登录中...")
        mUserManager.logout(object : IUserModel.Callback {
            override fun onSuccess() {
                mView?.dismissWaitMessage()
                Router.goSplashView(MainApplication.getContext()!!)
                mView?.close()
            }

            override fun onProgress(progress: Int, status: String?) {
            }

            override fun onError(state: IUserModel.State, message: String?) {
                mView?.showWaitingMessage("退出登录失败")
                mView?.dismissWaitMessage()
            }
        })
    }
}