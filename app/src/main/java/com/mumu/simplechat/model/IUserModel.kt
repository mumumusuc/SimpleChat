package com.mumu.simplechat.model

import android.content.Context
import android.graphics.Bitmap
import com.mumu.simplechat.bean.UserInfo
import io.reactivex.Observable

interface IUserModel {
    fun saveAsDefaultUser(context: Context, user: UserInfo): Boolean
    fun getDefaultSavedUser(context: Context): UserInfo?
    fun queryUsers(): Array<UserInfo>?
    fun checkLogin(): State
    fun checkUser(user: UserInfo): State

    fun login(user: UserInfo): Observable<State>
    fun logout(): Observable<State>
    fun register(name: String, pwd: String, pwdrp: String, verifyCode: String): Observable<State>
    fun getRegisterCode(): Observable<Bitmap>

    fun isAutoLogin(): Boolean
    fun isSaveUser(): Boolean
    fun enableAutoLogin(enable: Boolean)
    fun enableSaveUser(enable: Boolean)

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