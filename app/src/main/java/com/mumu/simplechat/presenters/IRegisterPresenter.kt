package com.mumu.simplechat.presenters

import com.mumu.simplechat.bean.UserInfo
import com.mumu.simplechat.views.IRegisterView

interface IRegisterPresenter : IPresenter<IRegisterView> {
    fun getVerifyCode()
    fun onRegister()
    fun onCancel()
}