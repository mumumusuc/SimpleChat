package com.mumu.simplechat.model

import android.content.Context
import com.mumu.simplechat.bean.UserInfo

interface IUserModel {
    fun saveAsDefaultUser(context: Context, user: UserInfo): Boolean
    fun getDefaultSavedUser(context: Context): UserInfo?
    fun queryUsers(): Array<UserInfo>?
    fun checkLogin(): State
    fun checkUser(user: UserInfo): State
    fun login(user: UserInfo, callback: Callback?)
    fun logout(callback: Callback?)
    fun register(user: UserInfo, callback: Callback?)

    fun isAutoLogin(): Boolean
    fun isSaveUser(): Boolean
    fun enableAutoLogin(enable: Boolean)
    fun enableSaveUser(enable: Boolean)

    interface Callback {
        fun onSuccess()
        fun onProgress(progress: Int, status: String?)
        fun onError(state: State, message: String?)
    }

    enum class State {
        NO_ERROR,
        USER_ALREADY_LOGIN,
        NO_USER_LOGIN,

        ERROR_BAD_USER_NAME,
        ERROR_BAD_PASSWORD,
        ERROR_USER_NAME_EXIST,
        ERROR_VERIFY_FAILED,
        ERROR_NO_NETWORK,
        ERROR_TIME_OUT,
        ERROR_UNKNOWN
    }
}