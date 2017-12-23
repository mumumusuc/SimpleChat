package com.mumu.simplechat

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.mumu.simplechat.model.ICallModel

object Utils {
    fun parseCallType(type: String?): ICallModel.CallType? {
        val _type: ICallModel.CallType
        try {
            if (type == "voice") return ICallModel.CallType.AUDIO
            _type = ICallModel.CallType.valueOf(type?.toUpperCase() ?: "")
        } catch (e: Exception) {
            return null
        }
        return _type
    }

    fun checkAndAskPermissions(activity: Activity): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return -1
        val pl = listOf(
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_PHONE_STATE
        )
        val pr = mutableListOf<String>()
        pl.forEach {
            val granted = activity.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
            Log.i("checkPermission", "$it -> $granted")
            if (!granted) {
                pr.add(it)
            }
        }
        if (pr.size > 0) {
            activity.requestPermissions(pr.toTypedArray(), 123)
            return 123
        }
        return -1
    }
}