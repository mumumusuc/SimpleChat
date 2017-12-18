package com.mumu.simplechat.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mumu.simplechat.R
import com.mumu.simplechat.bean.TitleBar
import com.mumu.simplechat.views.ISettingView

class SettingFragment : ISettingView, Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.setting_layout, container, false)
        val titleBar = root.findViewById(R.id.base_fragment_toolbar) as TitleBar
        titleBar.setTitle("设置")
        titleBar.showLeftIcon(false)
        titleBar.showLeftText(false)
        titleBar.showRightIcon(false)
        titleBar.showRightText(false)
        return root
    }
}