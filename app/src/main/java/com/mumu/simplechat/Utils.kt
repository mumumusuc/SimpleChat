package com.mumu.simplechat

import com.mumu.simplechat.model.ICallModel

object Utils {
    fun parseCallType(type: String?): ICallModel.CallType? {
        val _type: ICallModel.CallType
        try {
            _type = ICallModel.CallType.valueOf(type?.toUpperCase() ?: "")
        } catch (e: Exception) {
            return null
        }
        return _type
    }
}