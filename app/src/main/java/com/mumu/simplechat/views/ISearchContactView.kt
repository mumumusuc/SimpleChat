package com.mumu.simplechat.views

interface ISearchContactView {
    fun showWaiting()
    fun dismissWaiting()
    fun showResults(r: List<String>)
    fun clearResults()
    fun showMessage(msg: String)
}