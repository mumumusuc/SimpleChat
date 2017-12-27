package com.mumu.simplechat.presenters.impl

import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.RegisterSuccessEvent
import com.mumu.simplechat.model.IUserModel
import com.mumu.simplechat.model.impl.EMUserManager
import com.mumu.simplechat.presenters.IRegisterPresenter
import com.mumu.simplechat.views.IRegisterView
import io.reactivex.android.schedulers.AndroidSchedulers

class RegisterPresenter : IRegisterPresenter {
    private val mUserModel: IUserModel = EMUserManager()
    private var mRegisterView: IRegisterView? = null

    override fun bind(view: IRegisterView?) {
        mRegisterView = view
        if (mRegisterView != null) {
            getVerifyCode()
        }
    }

    override fun onRegister() {
        if (mRegisterView?.getPassword() != mRegisterView?.getRepeatPassword()) {
            mRegisterView?.showMessage("两次密码不一致!")
            return
        }
/*        val user = UserInfo(
                mRegisterView?.getUserName() ?: "",
                mRegisterView?.getPassword() ?: "",
                null
        )*/
        mUserModel.register(
                mRegisterView?.getUserName() ?: "",
                mRegisterView?.getPassword() ?: "",
                mRegisterView?.getRepeatPassword() ?: "",
                mRegisterView?.getRegisterString() ?: ""
        ).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {},
                        { e -> onError(e.localizedMessage) },
                        { onSuccess() }
                )
        mRegisterView?.showRegisterWaiting(true)
        mRegisterView?.showMessage("注册中")
    }

    override fun getVerifyCode() {
        mUserModel.getRegisterCode()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bmp -> mRegisterView?.showRegisterCode(bmp) })
    }

    override fun onCancel() {
    }

    private fun onSuccess() {
        mRegisterView?.showRegisterWaiting(false)
        mRegisterView?.showMessage("注册成功")
        EventBus.post(RegisterSuccessEvent())
    }

    private fun onError(message: String?) {
        mRegisterView?.showRegisterWaiting(false)
        mRegisterView?.showMessage("$message")
    }
}