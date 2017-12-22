package com.mumu.simplechat.model

import io.reactivex.Observable

interface IContactsModel<T> {
    fun getAllContacts(): Observable<List<T>>
    fun findContacts(name: String): Observable<List<T>>
    fun addContacts(name: String, reason: String): Observable<String>
    fun deleteContacts(name: String):Observable<String>
    fun agreeInvitation(name: String):Observable<String>
    fun refuseInvitation(name: String):Observable<String>

    interface ContactListener {
        /**
         * 好友请求被同意
         */
        fun onContactAgreed(username: String)

        /**
         * 好友请求被拒绝
         */
        fun onContactRefused(username: String)

        /**
         * 收到好友邀请
         */
        fun onContactInvited(username: String, reason: String)

        /**
         * 被删除时回调此方法
         */
        fun onContactDeleted(username: String)

        /**
         * 增加了联系人时回调此方法
         */
        fun onContactAdded(username: String)
    }
}