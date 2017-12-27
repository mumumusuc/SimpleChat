package com.mumu.simplechat.model.impl

import android.util.Log
import com.hyphenate.EMContactListener
import com.hyphenate.chat.EMClient
import com.hyphenate.easeui.R.id.list
import com.mumu.simplechat.Service.ServiceAction
import com.mumu.simplechat.Untils.HttpUntil
import com.mumu.simplechat.bean.UserInfo
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.ContactsAcceptedEvent
import com.mumu.simplechat.eventbus.events.ContactsChangedEvent
import com.mumu.simplechat.eventbus.events.InvitationEvent
import com.mumu.simplechat.model.IContactsModel
import io.reactivex.Observable
import io.reactivex.exceptions.Exceptions
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray

object EMContactsManager : IContactsModel<String>, EMContactListener {
    private val TAG = EMContactsManager::class.java.simpleName

    init {
        EMClient.getInstance().contactManager().setContactListener(this)
    }

    override fun getAllContacts(): Observable<List<String>> {
        return Observable.create<List<String>>(
                { subscriber ->
                    try {
                        //val list = EMClient.getInstance().contactManager().allContactsFromServer
                        val serverAction = ServiceAction()
                        var result = serverAction.getAllFriend(EMClient.getInstance().currentUser)
                        Log.d(TAG,"getAllContacts -> get $result")
                        val list1 = mutableListOf<String>()
                        val userList = mutableListOf<UserInfo>()
                        val jsonArray = JSONArray(result)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject2 = jsonArray.getJSONObject(i)
                            val id = jsonObject2.getInt("id")
                            val name = jsonObject2.getString("name")
                            val nickname = jsonObject2.getString("nickname")
                            val telphone = jsonObject2.getString("telphone")
                            val user = UserInfo(name, "", null) //这里看情况修改
                            user.nickname = nickname
                            user.id = id
                            user.telphone = telphone
                            userList.add(user)
                            list1.add(name)
                        }
                        subscriber.onNext(list1)
                        //subscriber.onNext(list)
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
                        val result = ServiceAction().search(name)
                        Log.d(TAG,"findContacts -> get $result")
                        val list = mutableListOf<String>()
                        val jsonArray = JSONArray(result)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject2 = jsonArray.getJSONObject(i)
                            //val id = jsonObject2.getInt("id")
                            val name = jsonObject2.getString("name")
                            //val nickname = jsonObject2.getString("nickname")
                            //val telphone = jsonObject2.getString("telphone")
                            //val user = UserInfo(name, "", null) //这里看情况修改
                            //user.nickname = nickname
                            //user.id = id
                            //user.telphone = telphone
                            //mSearchUserList.add(user)
                            list.add(name)
                        }
                        subscriber.onNext(list)
                        subscriber.onComplete()
                    } catch (e: Exception) {
                        subscriber.onError(Exceptions.propagate(e))
                    }
                }).subscribeOn(Schedulers.io())
    }

    override fun addContacts(name: String, reason: String): Observable<String> =
            Observable.just(Pair(name, reason)).subscribeOn(Schedulers.io()).map(
                    Function<Pair<String, String>, String> {
                        try {
                            EMClient.getInstance().contactManager().addContact(name, reason);
                            return@Function "NO_ERROR"
                        } catch (e: Exception) {
                            throw Exceptions.propagate(e)
                        }
                    }
            )

    override fun deleteContacts(name: String): Observable<String> =
            Observable.just(name).subscribeOn(Schedulers.io()).map(
                    Function<String, String> {
                        try {
//                            EMClient.getInstance().contactManager().deleteContact(name)
                            val serverAction = ServiceAction()
                            val result = serverAction.deleteFriend(EMClient.getInstance().currentUser, name)
                            if(result.contains("FAIL")){
                                Exceptions.propagate(Throwable(result))
                            }
                            return@Function "NO_ERROR"
                        } catch (e: Exception) {
                            throw Exceptions.propagate(e)
                        }
                    }
            )


    override fun agreeInvitation(name: String): Observable<String> =
            Observable.just(name).subscribeOn(Schedulers.io()).map(
                    Function<String, String> {
                        try {
                            val serverAction = ServiceAction()
                            val result = serverAction.addFriend(EMClient.getInstance().currentUser, name)
                            if(result.contains("FAIL")){
                                Exceptions.propagate(Throwable(result))
                            }
                            return@Function "NO_ERROR"
                            //EMClient.getInstance().contactManager().acceptInvitation(name);
                        } catch (e: Exception) {
                            throw Exceptions.propagate(e)
                        }
                    }
            )


    override fun refuseInvitation(name: String): Observable<String> =
            Observable.just(name).subscribeOn(Schedulers.io()).map(
                    Function<String, String> {
                        try {
                            EMClient.getInstance().contactManager().declineInvitation(name);
                            return@Function "NO_ERROR"
                        } catch (e: Exception) {
                            throw Exceptions.propagate(e)
                        }
                    }
            )


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
        //TODO:post-event
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

