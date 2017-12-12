package com.mumu.simplechat

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.mumu.simplechat.bean.CallArgument

object Router {
    val INCOMING_CALL_VIEW_INTENT = "com.mumu.simplechat.INCOMING_CALL_ACTIVITY"
    val CALL_VIEW_INTENT = "com.mumu.simplechat.CALL_ACTIVITY"
    val CHAT_VIEW_INTENT = "com.mumu.simplechat.MAIN_ACTIVITY"
    val RECENT_CHAT_VIEW_INTENT = "com.mumu.simplechat.RECENT_CHAT_ACTIVITY"
    /**/
    val CALL_EXTRA_KEY = "call_extra_argument"

    /**/
    fun goIncomingCallView(context: Context, arg: CallArgument) {
        go(context, INCOMING_CALL_VIEW_INTENT, CALL_EXTRA_KEY, arg)
    }

    fun goCallView(context: Context, arg: CallArgument) {
        go(context, CALL_VIEW_INTENT, CALL_EXTRA_KEY, arg)
    }

    fun goConversationView(context: Context) {
        go(context, CHAT_VIEW_INTENT, null, null)
    }
    fun goRecentChatView(context: Context) {
        go(context, RECENT_CHAT_VIEW_INTENT, null, null)
    }

    fun goFriendsView(context: Context) {
        //go(context, RECENT_CHAT_VIEW_INTENT, null, null)
    }

    fun go(context: Context, action: String, key: String?, p: Parcelable?) {
        val intent = Intent(action)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (!key.isNullOrEmpty() && p != null) {
            intent.putExtra(key, p)
        }
        context.startActivity(intent)
    }

    fun getCallExtra(intent: Intent): CallArgument? = getParcelableExtra(intent, CALL_EXTRA_KEY)

    fun <T : Parcelable> getParcelableExtra(intent: Intent, key: String): T? {
        val extra = intent.getParcelableExtra<T>(key)
        return if (extra == null) null else extra as T
    }
}