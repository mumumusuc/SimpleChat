package com.mumu.simplechat.views

import android.content.Context

interface ILoginView {
    fun showUserName(name: String)
    fun getUserName(): String
    fun showPassword(pwd: String)
    fun getPassword(): String
    fun showLoginWaiting(show: Boolean)
    fun showMessage(msg: String)
    fun getContext(): Context
}