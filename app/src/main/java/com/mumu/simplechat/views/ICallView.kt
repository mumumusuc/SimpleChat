package com.mumu.simplechat.views

import com.mumu.simplechat.bean.IVideoView

interface ICallView {
    fun showSelfVoiceEnable(enable: Boolean)
    fun showSelfVideoEnable(enable: Boolean)
    fun showVideoView(show: Boolean)
    fun showSelfVideoView(show: Boolean)
    fun showAudioView(show: Boolean)

    fun showMessage(msg: String)
    fun getOppositeVideoView(): IVideoView
    fun getLocalVideoView(): IVideoView
}