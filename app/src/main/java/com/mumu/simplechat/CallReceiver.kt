package com.mumu.simplechat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mumu.simplechat.bean.CallArgument

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 拨打方username
        val from = intent?.getStringExtra("from")
        // call type
        val type = intent?.getStringExtra("type")
        if (from.isNullOrEmpty() || type.isNullOrEmpty()) {
            return
        }
        val _type = Utils.parseCallType(type)
        if (_type != null) {
            Router.goIncomingCallView(context, CallArgument(from, null, _type))
        }
    }
}