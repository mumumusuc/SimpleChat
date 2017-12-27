package com.mumu.simplechat.presenters.impl

import com.mumu.simplechat.MainApplication
import com.mumu.simplechat.Router
import com.mumu.simplechat.model.IUserModel
import com.mumu.simplechat.model.impl.EMUserManager
import com.mumu.simplechat.presenters.ISettingPresenter
import com.mumu.simplechat.views.ISettingView
import io.reactivex.android.schedulers.AndroidSchedulers

class SettingPresenter : ISettingPresenter {

    private val mUserManager: IUserModel = EMUserManager()
    private var mView: ISettingView? = null

    override fun bind(view: ISettingView?) {
        mView = view
    }

    override fun onLogout() {
        mView?.showWaitingMessage("退出登录中...")
        mUserManager.logout()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {},
                        { _ ->
                            mView?.showWaitingMessage("退出登录失败")
                            mView?.dismissWaitMessage()
                        },
                        {
                            mView?.dismissWaitMessage()
                            Router.goSplashView(MainApplication.getContext()!!)
                            mView?.close()
                        }
                )

    }
}