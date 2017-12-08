package com.mumu.simplechat.presenters

interface IPresenter<T> {
    fun bind(view: T?)
}