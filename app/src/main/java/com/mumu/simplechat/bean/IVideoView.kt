package com.mumu.simplechat.bean

import android.view.SurfaceView

interface IVideoView {
    fun <V : SurfaceView> asSurfaceView(): V
}