package com.mumu.simplechat

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.mumu.simplechat.bean.CallArgument

object Router {
    val MAIN_VIEW_INTENT = "com.mumu.simplechat.MAIN_ACTIVITY"
    val INCOMING_CALL_VIEW_INTENT = "com.mumu.simplechat.INCOMING_CALL_ACTIVITY"
    val CALL_VIEW_INTENT = "com.mumu.simplechat.CALL_ACTIVITY"
    val CONVERSATION_VIEW_INTENT = "com.mumu.simplechat.CONVERSATION_ACTIVITY"
    /**/
    val CALL_EXTRA_KEY = "call_extra_argument"
    val CONVERSATION_EXTRA_KEY = "conversation_extra_argument"
    /**/

    fun goMainView(context: Context) {
        goWithParcelable(context, MAIN_VIEW_INTENT, null, null)
    }

    fun goIncomingCallView(context: Context, arg: CallArgument) {
        goWithParcelable(context, INCOMING_CALL_VIEW_INTENT, CALL_EXTRA_KEY, arg)
    }

    fun goCallView(context: Context, arg: CallArgument) {
        goWithParcelable(context, CALL_VIEW_INTENT, CALL_EXTRA_KEY, arg)
    }

    fun goConversationView(context: Context, p: Bundle) {
        goWithParcelable(context, CONVERSATION_VIEW_INTENT, CONVERSATION_EXTRA_KEY, p)
    }

    fun goWithParcelable(context: Context, action: String, key: String?, p: Parcelable?) {
        val intent = Intent(action)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (!key.isNullOrEmpty() && p != null) {
            intent.putExtra(key, p)
        }
        context.startActivity(intent)
    }

    fun getCallExtra(intent: Intent): CallArgument? = getParcelableExtra(intent, CALL_EXTRA_KEY)

    fun getConversationExtra(intent: Intent): Bundle? = getParcelableExtra(intent, CONVERSATION_EXTRA_KEY)

    fun <T : Parcelable> getParcelableExtra(intent: Intent, key: String): T? {
        val extra = intent.getParcelableExtra<T>(key)
        return if (extra == null) null else extra as T
    }
}