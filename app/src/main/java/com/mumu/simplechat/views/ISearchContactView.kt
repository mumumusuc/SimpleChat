package com.mumu.simplechat.views

interface ISearchContactView {
    fun showResults(r: List<String>)
    fun clearResults()
    fun showMessage(msg: String)
}