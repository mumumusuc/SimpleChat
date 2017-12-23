package com.mumu.simplechat.views

interface ISettingView {
    fun showWaitingMessage(msg: String)
    fun dismissWaitMessage()
    fun close()
}