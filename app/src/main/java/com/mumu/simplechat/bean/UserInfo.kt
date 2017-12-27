package com.mumu.simplechat.bean

import android.graphics.drawable.Drawable

class UserInfo(name: String, pwd: String, avatar: Drawable?) {
    val useName = name
    val password = pwd
    val avatar = avatar
    var id: Int = 0
    var nickname: String? = null
    var telphone: String? = null
    /*others*/
}