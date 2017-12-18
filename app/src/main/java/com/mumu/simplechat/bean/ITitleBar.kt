package com.mumu.simplechat.bean

import android.graphics.drawable.Drawable
import android.view.View


interface ITitleBar {
    fun setLeftAction(listener: View.OnClickListener)

    fun setRightAction(listener: View.OnClickListener)

    fun setTitle(title: String)

    fun setLeftIcon(icon: Drawable)

    fun setLeftText(str: String)

    fun setRightIcon(icon: Drawable)

    fun setRightText(str: String)

    fun showLeftIcon(s:Boolean)

    fun showRightIcon(s:Boolean)

    fun showLeftText(s:Boolean)

    fun showRightText(s:Boolean)

    fun getLeftAnchor(): View

    fun getRightAnchor(): View
}