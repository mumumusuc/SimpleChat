package com.mumu.simplechat.views

import com.hyphenate.easeui.domain.EaseUser

interface IContactsView {
    fun showContacts(contacts: Map<String,EaseUser>)
}