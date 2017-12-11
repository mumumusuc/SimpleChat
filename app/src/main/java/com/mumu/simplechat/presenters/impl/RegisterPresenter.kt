package com.mumu.simplechat.presenters.impl

import com.mumu.simplechat.bean.UserInfo
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.RegisterSuccessEvent
import com.mumu.simplechat.model.IUserModel
import com.mumu.simplechat.model.impl.EMUserModel
import com.mumu.simplechat.presenters.IRegisterPresenter
import com.mumu.simplechat.views.IRegisterView

class RegisterPresenter : IRegisterPresenter, IUserModel.Callback {
    private val mUserModel: IUserModel = EMUserModel()
    private var mRegisterView: IRegisterView? = null

    override fun bind(view: IRegisterView?) {
        mRegisterView = view
    }

    override fun onRegister() {
        if (mRegisterView?.getPassword() != mRegisterView?.getRepeatPassword()) {
            mRegisterView?.showMessage("两次密码不一致!")
            return
        }
        val user = UserInfo(
                mRegisterView?.getUserName() ?: "",
                mRegisterView?.getPassword() ?: "",
                null
        )
        mUserModel.register(user, this)
        mRegisterView?.showRegisterWaiting(true)
        mRegisterView?.showMessage("注册中")
    }

    override fun onCancel() {
    }

    override fun onSuccess() {
        mRegisterView?.showRegisterWaiting(false)
        mRegisterView?.showMessage("注册成功")
        EventBus.post(RegisterSuccessEvent())
    }

    override fun onProgress(progress: Int, status: String?) {
    }

    override fun onError(state: IUserModel.State, message: String?) {
        mRegisterView?.showRegisterWaiting(false)
        mRegisterView?.showMessage("$state,$message")
    }
}