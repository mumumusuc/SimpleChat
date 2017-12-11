package com.mumu.simplechat.bean

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView
import com.hyphenate.media.EMCallSurfaceView

class EMVideoView : IVideoView, EMCallSurfaceView {
    constructor(p0: Context?) : super(p0)
    constructor(p0: Context?, p1: AttributeSet?) : super(p0, p1)
    constructor(p0: Context?, p1: AttributeSet?, p2: Int) : super(p0, p1, p2)
    override fun <V : SurfaceView> asSurfaceView(): V = this as V
}