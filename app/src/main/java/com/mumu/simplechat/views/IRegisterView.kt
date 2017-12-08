package com.mumu.simplechat.views

import android.content.Context

interface IRegisterView {
    fun getUserName(): String
    fun clearUserName()
    fun getPassword(): String
    fun clearPassword()
    fun getRepeatPassword():String
    fun clearRepeatPassword()
    fun showRegisterWaiting(show:Boolean)
    fun showMessage(msg:String)
    fun getContext(): Context
}