package com.mumu.simplechat.views.fragments

import android.view.View

interface IBaseFragment {
    fun setTitle(title: String)
    fun setLeftAction(action: View.OnClickListener)
    fun setRightAction(action: View.OnClickListener)
}