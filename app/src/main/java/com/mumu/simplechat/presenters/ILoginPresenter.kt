package com.mumu.simplechat.presenters

import com.mumu.simplechat.views.ILoginView

interface ILoginPresenter : IPresenter<ILoginView> {
    fun onSaveUser(save: Boolean)
    fun onAutoLogin(auto: Boolean)
    fun onLogin()
    fun onCancel()
}