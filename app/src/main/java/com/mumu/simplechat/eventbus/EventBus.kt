package com.mumu.simplechat.eventbus

import android.util.Log
import com.google.common.eventbus.SubscriberExceptionContext
import com.google.common.eventbus.SubscriberExceptionHandler


object EventBus : SubscriberExceptionHandler {
    private val TAG = EventBus::class.java.simpleName
    private val sEventBus = com.google.common.eventbus.EventBus(this)

    fun register(subscriber: Any) {
        sEventBus.register(subscriber)
    }

    fun post(event: Any) {
        sEventBus.post(event)
    }

    override fun handleException(exception: Throwable, context: SubscriberExceptionContext) {
        Log.e(TAG, Log.getStackTraceString(exception))
    }
}