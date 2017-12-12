package com.mumu.simplechat.views

import android.graphics.drawable.Drawable
import com.mumu.simplechat.bean.IVideoView

interface ICallView {
    fun showOppositeVideoView(show: Boolean)
    fun showSelfVideoView(show: Boolean)
    fun showAudioView(show: Boolean)

    fun getOppositeVideoView(): IVideoView
    fun getLocalVideoView(): IVideoView

    fun showUserAvatar(avatar: Drawable?)
    fun showUserName(name: String)
    fun showMessage(msg: String)

    fun close()
}