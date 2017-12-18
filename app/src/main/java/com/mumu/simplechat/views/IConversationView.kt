package com.mumu.simplechat.views

import android.os.Bundle

interface IConversationView {
    fun getArguments():Bundle
    fun showTitle(title: String)
}