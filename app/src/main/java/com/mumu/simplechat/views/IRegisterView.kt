package com.mumu.simplechat.views

import android.content.Context
import android.graphics.Bitmap

interface IRegisterView {
    fun getUserName(): String
    fun clearUserName()

    fun getPassword(): String
    fun clearPassword()

    fun getRepeatPassword():String
    fun clearRepeatPassword()

    fun showRegisterCode(code:Bitmap)
    fun getRegisterString():String

    fun showRegisterWaiting(show:Boolean)

    fun showMessage(msg:String)

    fun getContext(): Context
}