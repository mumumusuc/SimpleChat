package com.mumu.simplechat.bean

import android.os.Parcel
import android.os.Parcelable
import com.mumu.simplechat.model.ICallModel

class CallArgument : Parcelable {

    private var from: String? = null
    private var to: String? = null
    private var type: ICallModel.CallType? = null

    private constructor()

    constructor(from: String?, to: String?, type: ICallModel.CallType) : this() {
        this.from = from
        this.to = to
        this.type = type
    }

    constructor(parcel: Parcel) : this() {
        this.from = parcel.readString()
        this.to = parcel.readString()
        this.type = ICallModel.CallType.valueOf(parcel.readString())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(from ?: "")
        parcel.writeString(to ?: "")
        parcel.writeString(type!!.name)
    }

    fun isAvaliable(): Boolean {
        return ((!from.isNullOrEmpty() && !from.isNullOrBlank())
                || (!to.isNullOrEmpty() && !from.isNullOrBlank()))
                && (type == ICallModel.CallType.AUDIO || type == ICallModel.CallType.VIDEO)
    }

    fun getFrom(): String? = from
    fun getTo(): String? = to
    fun getType(): ICallModel.CallType = type!!

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CallArgument> {
        override fun createFromParcel(parcel: Parcel): CallArgument {
            return CallArgument(parcel)
        }

        override fun newArray(size: Int): Array<CallArgument?> {
            return arrayOfNulls(size)
        }
    }
}