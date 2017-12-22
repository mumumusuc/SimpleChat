package com.mumu.simplechat.model.impl

import android.util.Log
import com.hyphenate.EMContactListener
import com.hyphenate.chat.EMClient
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.ContactsAcceptedEvent
import com.mumu.simplechat.eventbus.events.ContactsChangedEvent
import com.mumu.simplechat.eventbus.events.InvitationEvent
import com.mumu.simplechat.model.IContactsModel
import io.reactivex.Observable
import io.reactivex.exceptions.Exceptions
import io.reactivex.schedulers.Schedulers

class EMContactsManager : IContactsModel<String>, EMContactListener {
    private val TAG = EMContactsManager::class.java.simpleName

    init {
        EMClient.getInstance().contactManager().setContactListener(this)
    }

    override fun getAllContacts(): Observable<List<String>> {
        return Observable.create<List<String>>(
                { subscriber ->
                    try {
                        val list = EMClient.getInstance().contactManager().allContactsFromServer
                        subscriber.onNext(list)
                        subscriber.onComplete()
                    } catch (e: Exception) {
                        subscriber.onError(Exceptions.propagate(e))
                    }
                }).subscribeOn(Schedulers.io())
    }

    override fun findContacts(name: String): Observable<List<String>> {
        return Observable.create<List<String>>(
                { subscriber ->
                    try {
                        //subscriber.onNext(list)
                        subscriber.onComplete()
                    } catch (e: Exception) {
                        subscriber.onError(Exceptions.propagate(e))
                    }
                }).subscribeOn(Schedulers.io())
    }

    override fun addContacts(name: String, reason: String) {
        EMClient.getInstance().contactManager().addContact(name, reason);
    }

    override fun deleteContacts(name: String) {
        EMClient.getInstance().contactManager().deleteContact(name);
    }

    override fun agreeInvitation(name: String) {
        EMClient.getInstance().contactManager().acceptInvitation(name);
    }

    override fun refuseInvitation(name: String) {
        EMClient.getInstance().contactManager().declineInvitation(name);
    }

    /**/
    override fun onContactInvited(p0: String, p1: String) {
        Log.i(TAG, "收到好友邀请 $p0,$p1")
        EventBus.post(InvitationEvent(p0, p1))
    }

    override fun onContactDeleted(p0: String?) {
        Log.i(TAG, "被删除 $p0")
    }

    override fun onFriendRequestAccepted(p0: String) {
        Log.i(TAG, "好友请求被同意 $p0")
        //TODO:postevent
        EventBus.post(ContactsAcceptedEvent(p0))
    }

    override fun onContactAdded(p0: String) {
        Log.i(TAG, "增加了联系人 $p0")
        EventBus.post(ContactsChangedEvent(p0))
    }

    override fun onFriendRequestDeclined(p0: String?) {
        Log.i(TAG, "好友请求被拒绝 $p0")
    }
}

