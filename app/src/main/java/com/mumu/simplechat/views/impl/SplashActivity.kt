package com.mumu.simplechat.views.impl

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.mumu.simplechat.R
import com.mumu.simplechat.presenters.ISplashPresenter
import com.mumu.simplechat.presenters.impl.SplashPresenter
import com.mumu.simplechat.views.ISplashView

class SplashActivity : AppCompatActivity(), ISplashView {
    companion object {
        private val sPresenter: ISplashPresenter = SplashPresenter()
    }

    private val mSwitcher: Button by lazy { findViewById(R.id.splash_switcher) as Button }
    private var mLoginView: Fragment? = null
    private var mRegisterView: Fragment? = null

    override fun getContext(): Context = baseContext

    override fun showLoginView() {
        val transaction = fragmentManager.beginTransaction()
        if (!mLoginView!!.isAdded) {
            transaction.add(R.id.fragment_container, mLoginView, "LOGIN")
        } else {
            transaction.show(mLoginView)
        }
        if (mRegisterView!!.isAdded) {
            transaction.hide(mRegisterView)
        }
        transaction.commit()
        mSwitcher.text = "新用户注册"
    }

    override fun showRegisterView() {
        val transaction = fragmentManager.beginTransaction()
        if (!mRegisterView!!.isAdded) {
            transaction.add(R.id.fragment_container, mRegisterView, "REGISTER")
        } else {
            transaction.show(mRegisterView)
        }
        if (mLoginView!!.isAdded) {
            transaction.hide(mLoginView)
        }
        transaction.commit()
        mSwitcher.text = "登录"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mLoginView = fragmentManager.findFragmentByTag("LOGIN")
            mRegisterView = fragmentManager.findFragmentByTag("REGISTER")
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        if (mLoginView == null) mLoginView = LoginView()
        if (mRegisterView == null) mRegisterView = RegisterView()
        sPresenter.bind(this)
        mSwitcher.setOnClickListener { _ -> sPresenter?.onSwitchScreen() }
    }

    override fun onDestroy() {
        super.onDestroy()
        sPresenter.bind(null)
    }

    override fun exit() {
        finish()
    }
}