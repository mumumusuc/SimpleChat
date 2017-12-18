package com.mumu.simplechat.model

interface IContactsModel<T> {
    fun getAllContacts(): List<T>
    fun findContacts(name: String)
    fun addContacts(name: String, reason: String)
    fun deleteContacts(name: String)

    fun agreeInvitation(name:String)
    fun refuseInvitation(name:String)

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