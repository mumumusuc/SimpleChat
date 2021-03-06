package com.mumu.simplechat.model.impl

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import com.hyphenate.EMCallBack
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.mumu.simplechat.bean.UserInfo
import com.mumu.simplechat.model.IUserModel
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Log
import com.mumu.simplechat.Config
import com.mumu.simplechat.MainApplication
import com.mumu.simplechat.Service.ServiceAction
import com.mumu.simplechat.Untils.HttpUntil
import io.reactivex.Observable
import io.reactivex.exceptions.Exceptions
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.nio.charset.Charset


class EMUserManager : IUserModel {
    private val TAG = EMUserManager::class.java.simpleName
    private val CipherMode = "AES/CFB/NoPadding"
    private val CipherKey = "EMUserModel000000000000000000000"
    private val MIN_USER_NAME_LENGTH = 1
    private val MAX_USER_NAME_LENGTH = 10
    private val MIN_PASSWORD_LENGTH = 6
    private val MAX_PASSWORD_LENGTH = 10
    private val DEFAULT_USER = "default_user"
    private val AUTO_LOGIN = "auto_login"
    private var autoLogin: Boolean? = null
    private var saveUser: Boolean? = null

    override fun saveAsDefaultUser(context: Context, user: UserInfo): Boolean {
        val state = checkUser(user)
        if (state != IUserModel.State.NO_ERROR) {
            return false
        }
        val name = user.useName
        val pwd = encrypt(user.password)
        val sp = context.getSharedPreferences(DEFAULT_USER, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        editor.putString("name", name)
        editor.putString("pwd", pwd)
        return editor.commit()
    }

    override fun getDefaultSavedUser(context: Context): UserInfo? {
        val sp = context.getSharedPreferences(DEFAULT_USER, Context.MODE_PRIVATE)
        val name = sp.getString("name", "")
        val pwd = sp.getString("pwd", "")
        Log.d(TAG, "getDefaultSavedUser -> name=$name, pwd=$pwd")
        if (name.isNullOrEmpty() || pwd.isNullOrEmpty()) {
            return null
        }
        return UserInfo(name, decrypt(pwd), null)
    }

    override fun queryUsers(): Array<UserInfo>? {
        return null
    }

    override fun checkLogin(): IUserModel.State {
        if (EMClient.getInstance().isLoggedInBefore) {
            return IUserModel.State.USER_ALREADY_LOGIN
        } else {
            return IUserModel.State.NO_USER_LOGIN
        }
    }

    override fun checkUser(user: UserInfo): IUserModel.State {
        val name = user.useName
        val pwd = user.password
        if (name.isNullOrBlank() || name.length < MIN_USER_NAME_LENGTH || name.length > MAX_USER_NAME_LENGTH) {
            return IUserModel.State.ERROR_BAD_USER_NAME
        }
        if (pwd.isNullOrBlank() || pwd.length < MIN_PASSWORD_LENGTH || pwd.length > MAX_PASSWORD_LENGTH) {
            return IUserModel.State.ERROR_BAD_PASSWORD
        }
        return IUserModel.State.NO_ERROR
    }

    override fun login(user: UserInfo): Observable<IUserModel.State> {
        return Observable.create<IUserModel.State>(
                { subscriber ->
                    val state = checkUser(user)
                    Log.i(TAG, "login -> checkUser = $state")
                    if (state != IUserModel.State.NO_ERROR) {
                        subscriber.onError(Throwable(state.toString()))
                        return@create
                    }
                    val lastUser = EMClient.getInstance().currentUser
                    if (!lastUser.isNullOrEmpty() && lastUser != user.useName) {
                        logout()
                    }
                    val serviceAction = ServiceAction()
                    try {
                        val ad = serviceAction.login(user)
                        if (ad == "FAIL") {
                            subscriber.onError(Throwable("登录失败"))
                            return@create
                        }
                        Log.i(TAG, "login -> $ad")
                        val json = JSONObject(ad)
                        val aa = json.getInt("id")
                        user.id = aa
                        if (aa <= 0) {
                            throw Exceptions.propagate(Throwable("id=$aa"))
                        }
                        //HttpUntil.name = user.useName
                    } catch (e: HyphenateException) {
                        subscriber.onError(e)
                        return@create
                    }
                    EMClient.getInstance().login(user.useName, user.password, object : EMCallBack {
                        override fun onSuccess() {
                            subscriber.onNext(state)
                            subscriber.onComplete()
                        }

                        override fun onProgress(progress: Int, status: String?) {
                        }

                        override fun onError(code: Int, error: String?) {
                            subscriber.onError(Throwable("${convertEMErrorCode(code)} : $error"))
                        }
                    })
                }).subscribeOn(Schedulers.io())
    }

    override fun logout(): Observable<IUserModel.State> {
        return Observable.create<IUserModel.State>(
                { subscriber ->
                    EMClient.getInstance().logout(true, object : EMCallBack {
                        override fun onSuccess() {
                            enableAutoLogin(false)
                            subscriber.onNext(IUserModel.State.NO_ERROR)
                            subscriber.onComplete()
                        }

                        override fun onProgress(progress: Int, status: String?) {
                        }

                        override fun onError(code: Int, error: String?) {
                            subscriber.onError(Throwable(convertEMErrorCode(code).toString() + error))
                        }
                    })
                })
    }

    private fun getSavedInfo() {
        val sp = MainApplication?.getContext()?.getSharedPreferences(AUTO_LOGIN, Context.MODE_PRIVATE)
        autoLogin = sp?.getBoolean("login", Config.autoLogin) ?: Config.autoLogin
        saveUser = sp?.getBoolean("save", Config.saveLogin) ?: Config.saveLogin
    }

    private fun saveInfo() {
        val sp = MainApplication?.getContext()?.getSharedPreferences(AUTO_LOGIN, Context.MODE_PRIVATE)
        val editor = sp?.edit()
        editor?.clear()
        editor?.putBoolean("login", if (autoLogin == true) true else Config.autoLogin)
        editor?.putBoolean("save", if (saveUser == true) true else Config.saveLogin)
        editor?.commit()
    }

    override fun isAutoLogin(): Boolean {
        if (autoLogin == null) {
            getSavedInfo()
        }
        return autoLogin!!
    }

    override fun isSaveUser(): Boolean {
        if (saveUser == null) {
            getSavedInfo()
        }
        return saveUser!!
    }

    override fun enableAutoLogin(enable: Boolean) {
        autoLogin = enable
        saveInfo()
    }

    override fun enableSaveUser(enable: Boolean) {
        saveUser = enable
        saveInfo()
    }

    override fun register(
            name: String,
            pwd: String,
            pwdrp: String,
            verifyCode: String): Observable<IUserModel.State> {
        return Observable.create<IUserModel.State>({ subscriber ->
            val user = UserInfo(name, pwd, null)
            val state = checkUser(user)
            if (state != IUserModel.State.NO_ERROR) {
                subscriber.onError(Throwable(state.toString()))
                return@create
            }
            try {
                //注册失败会抛出HyphenateException
                val re = ServiceAction().register(user, pwdrp, verifyCode)
                if (re.startsWith("FAIL")) {
                    subscriber.onError(Throwable("注册失败"))
                    return@create
                }
                //EMClient.getInstance().createAccount(user.useName, user.password);//同步方法
            } catch (e: HyphenateException) {
                subscriber.onError(e)
            }
            subscriber.onNext(IUserModel.State.NO_ERROR)
            subscriber.onComplete()
        }).subscribeOn(Schedulers.io())
    }

    override fun getRegisterCode(): Observable<Bitmap> {
        return Observable.create<Bitmap>({ subscriber ->
            try {
                val bmp = ServiceAction().getImagCode()
                subscriber.onNext(bmp)
                subscriber.onComplete()
            } catch (e: Exception) {
                subscriber.onError(Exceptions.propagate(e))
            }
        }).subscribeOn(Schedulers.io())
    }

    private fun convertEMErrorCode(code: Int): IUserModel.State {
        when (code) {
            EMError.EM_NO_ERROR -> {
                return IUserModel.State.NO_ERROR
            }
            EMError.USER_ALREADY_LOGIN -> {
                return IUserModel.State.USER_ALREADY_LOGIN
            }
            else -> {
                return IUserModel.State.ERROR_UNKNOWN
            }
        }
    }

    private fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(CipherMode)
        val keyspec = SecretKeySpec(CipherKey.toByteArray(), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, IvParameterSpec(
                ByteArray(cipher.blockSize)))
        val encrypted = cipher.doFinal(value.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    private fun decrypt(value: String): String {
        val encrypted1 = Base64.decode(value.toByteArray(), Base64.DEFAULT)
        val cipher = Cipher.getInstance(CipherMode)
        val keyspec = SecretKeySpec(CipherKey.toByteArray(), "AES")
        cipher.init(Cipher.DECRYPT_MODE, keyspec, IvParameterSpec(
                ByteArray(cipher.blockSize)))
        val original = cipher.doFinal(encrypted1)
        return String(original, Charset.forName("UTF-8"))
    }
}