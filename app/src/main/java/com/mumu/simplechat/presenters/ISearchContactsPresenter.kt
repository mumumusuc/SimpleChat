package com.mumu.simplechat.presenters

import com.mumu.simplechat.views.ISearchContactView

interface ISearchContactsPresenter : IPresenter<ISearchContactView> {
    fun onSearch(str: String)
    fun onAdd(name: String)
    fun onLeftAction()
    fun getSearchResult(): List<String>
}