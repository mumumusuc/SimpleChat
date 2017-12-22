package com.mumu.simplechat.presenters.impl

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import com.mumu.simplechat.MainApplication
import com.mumu.simplechat.R
import com.mumu.simplechat.Router
import com.mumu.simplechat.bean.CallArgument
import com.mumu.simplechat.presenters.IIncomingCallPresenter
import com.mumu.simplechat.views.IIncomingCallView
import com.mumu.simplechat.model.ICallModel
import com.mumu.simplechat.model.impl.EMCallManager


class IncomingCallPresenter : IIncomingCallPresenter {
    private val TAG = IncomingCallPresenter::class.java.simpleName
    private val mCallManager: ICallModel<String> = EMCallManager
    private val ACTION_CONFIRM = "com.mumu.simplechat.CALL_CONFIRM"
    private val ACTION_CANCEL = "om.mumu.simplechat.CALL_CANCEL"
    private val CALL_ARGUMENTS = "arguments"
    private val mReceiver = NotificationReceiver()
    private val mFilter = IntentFilter()

    init {
        mFilter.addAction(ACTION_CONFIRM)
        mFilter.addAction(ACTION_CANCEL)
    }

    override fun bind(view: IIncomingCallView?) {}

    companion object {
        private val SELF_ID = 1

        fun makeNotification(context: Context, from: String, type: ICallModel.CallType) {
            val self = IncomingCallPresenter()
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(SELF_ID, self.makeNotification(context, from, type))
        }
    }

    private fun makeNotification(context: Context, from: String, type: ICallModel.CallType): Notification {
        val arg = CallArgument(from, null, type)
        val msg = "来自${from}的${type}电话"
        //val remoteView = RemoteViews(context.packageName, R.layout.notification_layout)
        //remoteView.setTextViewText(R.id.notification_msg, msg)

        val confirmIntent = Intent(ACTION_CONFIRM)
        confirmIntent.putExtra(CALL_ARGUMENTS, arg)
        val pendingIntentConfirm = PendingIntent.getBroadcast(context, 0, confirmIntent, PendingIntent.FLAG_ONE_SHOT)
        //remoteView.setOnClickPendingIntent(R.id.notification_confirm, pendingIntentConfirm)

        val cancelIntent = Intent(ACTION_CANCEL)
        val pendingIntentCancel = PendingIntent.getBroadcast(context, 0, cancelIntent, PendingIntent.FLAG_ONE_SHOT)
        //remoteView.setOnClickPendingIntent(R.id.notification_cancel, pendingIntentCancel)

        val builder = NotificationCompat.Builder(context)
                .setTicker(msg)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_call)
                .setPriority(Notification.PRIORITY_HIGH)
                //.setContentTitle(msg)
                .setContentText(msg)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                //.setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                .addAction(R.drawable.ic_call, "接听", pendingIntentConfirm)
                .addAction(R.drawable.ic_call_end, "挂断", pendingIntentCancel)

        context.registerReceiver(mReceiver, mFilter)
        return builder.build()
    }

    private fun getCallIntent(context: Context, flags: Int, arg: CallArgument): PendingIntent {
        val intent = Intent(Router.CALL_VIEW_INTENT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return PendingIntent.getActivity(context, 1, intent, flags)
    }

    override fun onReject() {
        mCallManager.rejectCall("")
    }

    override fun onAnswer() {
        mCallManager.answerCall("", null)
    }

    private inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            Log.d(TAG, "onReceive -> action = $action")
            when (action) {
                ACTION_CONFIRM -> {
                    val arg = intent.getParcelableExtra<CallArgument>(CALL_ARGUMENTS)
                    if (arg != null) {
                        onAnswer()
                        Router.goCallView(context, arg)
                    }
                }
                ACTION_CANCEL -> {
                    onReject()
                }
            }
            //TODO:dismiss notification
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(SELF_ID)
            context.unregisterReceiver(mReceiver)
        }
    }
}
