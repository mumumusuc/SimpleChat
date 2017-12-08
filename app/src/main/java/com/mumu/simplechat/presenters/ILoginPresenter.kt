package com.mumu.simplechat.presenters

import com.mumu.simplechat.bean.UserInfo
import com.mumu.simplechat.views.ILoginView

interface ILoginPresenter : IPresenter<ILoginView> {
    fun onLogin()
    fun onCancel()
}