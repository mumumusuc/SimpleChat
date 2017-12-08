package com.mumu.simplechat.views.impl

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import com.mumu.simplechat.R
import com.mumu.simplechat.presenters.ILoginPresenter
import com.mumu.simplechat.presenters.impl.LoginPresenter
import com.mumu.simplechat.views.ILoginView


class LoginView : Fragment(), ILoginView {
    companion object {
        private val sPresenter: ILoginPresenter = LoginPresenter()
        private val sHandler = Handler(Looper.getMainLooper())
    }

    private var loginAvatar: ImageView? = null
    private var loginName: EditText? = null
    private var loginPassword: EditText? = null
    private var loginProvision: CheckBox? = null
    private var loginConfirm: Button? = null
    private var loginMsg: TextView? = null
    private var loginWait: ProgressBar? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        sPresenter.bind(this)
    }

    override fun onDetach() {
        super.onDetach()
        sPresenter.bind(null)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var _inflater = inflater
        if (_inflater == null) {
            _inflater = LayoutInflater.from(context)
        }
        val root = _inflater!!.inflate(R.layout.login_layout, container, false)
        loginAvatar = root.findViewById(R.id.login_avatar) as ImageView
        loginName = root.findViewById(R.id.login_input_username) as EditText
        loginPassword = root.findViewById(R.id.login_input_password) as EditText
        loginProvision = root.findViewById(R.id.login_provision) as CheckBox
        loginConfirm = root.findViewById(R.id.login_confirm) as Button
        loginMsg = root.findViewById(R.id.login_msg) as TextView
        loginWait = root.findViewById(R.id.login_waiting) as ProgressBar

        loginProvision?.setOnCheckedChangeListener { compoundButton, checked ->
            run {
                loginConfirm?.isEnabled = checked
            }
        }
        loginConfirm?.setOnClickListener { view -> sPresenter?.onLogin() }
        return root
    }

    override fun showUserName(name: String) {
        sHandler.post {
            loginName?.setText(name)
        }
    }

    override fun getUserName(): String = loginName?.text?.toString() ?: ""

    override fun showPassword(pwd: String) {
        sHandler.post {
            loginPassword?.setText(pwd)
        }
    }

    override fun getPassword(): String = loginPassword?.text?.toString() ?: ""

    override fun showLoginWaiting(show: Boolean) {
        sHandler.post {
            loginWait?.visibility = if (show) VISIBLE else INVISIBLE
        }
    }

    override fun showMessage(msg: String) {
        sHandler.post {
            loginMsg?.text = msg
        }
    }
}