package com.mumu.simplechat.views.impl

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mumu.simplechat.R
import com.mumu.simplechat.presenters.IRegisterPresenter
import com.mumu.simplechat.presenters.impl.RegisterPresenter
import com.mumu.simplechat.views.IRegisterView

class RegisterView : Fragment(), IRegisterView {
    companion object {
        private val sPresenter: IRegisterPresenter = RegisterPresenter()
        private val sHandler = Handler(Looper.getMainLooper())
    }

    private var registerAvatar: ImageView? = null
    private var registerName: EditText? = null
    private var registerPassword: EditText? = null
    private var registerRepeatPassword: EditText? = null
    private var registerProvision: CheckBox? = null
    private var registerConfirm: Button? = null
    private var registerMsg: TextView? = null
    private var registerWait: ProgressBar? = null

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
        val root = _inflater!!.inflate(R.layout.register_layout, container, false)
        registerAvatar = root.findViewById(R.id.register_avatar) as ImageView
        registerName = root.findViewById(R.id.register_input_username) as EditText
        registerPassword = root.findViewById(R.id.register_input_password) as EditText
        registerRepeatPassword = root.findViewById(R.id.register_input_confirm_password) as EditText
        registerProvision = root.findViewById(R.id.register_provision) as CheckBox
        registerConfirm = root.findViewById(R.id.register_confirm) as Button
        registerMsg = root.findViewById(R.id.register_msg) as TextView
        registerWait = root.findViewById(R.id.register_waiting) as ProgressBar

        registerProvision?.setOnCheckedChangeListener { compoundButton, checked ->
            run {
                registerConfirm?.isEnabled = checked
            }
        }
        registerConfirm?.setOnClickListener { view -> sPresenter?.onRegister() }
        return root
    }

    override fun getUserName(): String = registerName?.text?.toString() ?: ""

    override fun getPassword(): String = registerPassword?.text?.toString() ?: ""

    override fun getRepeatPassword(): String = registerRepeatPassword?.text?.toString() ?: ""

    override fun clearUserName() {
        sHandler.post {
            registerName?.setText("")
        }
    }

    override fun clearPassword() {
        sHandler.post {
            registerPassword?.setText("")
        }
    }

    override fun clearRepeatPassword() {
        sHandler.post {
            registerRepeatPassword?.setText("")
        }
    }

    override fun showRegisterWaiting(show: Boolean) {
        sHandler.post {
            registerWait?.visibility = if (show) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun showMessage(msg: String) {
        sHandler.post {
            registerMsg?.text = msg
        }
    }
}