package com.mumu.simplechat.model

import com.mumu.simplechat.bean.IVideoView

interface ICallModel<in T> {
    fun makeAudioCall(user: T, stateListener: CallCallback)
    fun makeVideoCall(user: T, stateListener: CallCallback)
    fun answerCall(user: T, stateListener: CallCallback)
    fun endCall(user: T)
    fun rejectCall(user: T)
    fun setVideoCallSurface(opposite: IVideoView, local: IVideoView)

    interface CallCallback {
        fun onCallStateChanged(state: CallState, error: CallError)
    }

    enum class CallType(type: String) {
        AUDIO("audio"),
        VIDEO("video")
    }

    enum class CallState(msg: String) {
        UNKNOWN("unknown"),
        IDLE("idle"),
        RINGING("ringing"),
        ANSWERING("answering"),
        CONNECTING("connecting"),
        CONNECTED("connected"),
        ACCEPTED("accepted"),
        DISCONNECTED("disconnected"),
        VOICE_PAUSE("voice_pause"),
        VOICE_RESUME("voice_resume"),
        VIDEO_PAUSE("video_pause"),
        VIDEO_RESUME("video_resume"),
        NETWORK_UNSTABLE("network_unstable"),
        NETWORK_NORMAL("network_normal"),
        NETWORK_DISCONNECTED("network_disconnected")
    }

    enum class CallError(msg: String) {
        UNKNOWN("error_unknown"),
        ERROR_NONE("error_none"),
        ERROR_TRANSPORT("error_transport"),
        ERROR_UNAVAILABLE("error_unavailable"),
        REJECTED("rejected"),
        ERROR_NO_RESPONSE("error_noresponse"),
        ERROR_BUSY("busy"),
        ERROR_NO_DATA("error_no_data"),
        ERROR_LOCAL_SDK_VERSION_OUTDATED("error_local_sdk_version_outdated"),
        ERROR_REMOTE_SDK_VERSION_OUTDATED("error_remote_sdk_version_outdated")
    }
}