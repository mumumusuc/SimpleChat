package com.mumu.simplechat.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hyphenate.easeui.ui.EaseConversationListFragment
import com.mumu.simplechat.R
import com.mumu.simplechat.bean.TitleBar
import com.mumu.simplechat.presenters.IChatlistPresenter
import com.mumu.simplechat.presenters.impl.ChatlistPresenter
import com.mumu.simplechat.views.IChatlistView

class EMChatlistFragment : IChatlistView, EaseConversationListFragment() {

    companion object {
        private val sPresenter: IChatlistPresenter = ChatlistPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.base_fragment_layout, container, false) as ViewGroup
        root.addView(view)
        val titleBar = root.findViewById(R.id.base_fragment_toolbar) as TitleBar
        titleBar.setTitle("会话")
        titleBar.showLeftIcon(false)
        titleBar.showLeftText(false)
        titleBar.showRightIcon(true)
        titleBar.showRightText(false)
        titleBar.setRightIcon(context.getDrawable(R.drawable.ic_person_add))
        titleBar.setRightAction(View.OnClickListener { sPresenter.onRightAction() })
        return root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        sPresenter.bind(this)
    }

    override fun onDetach() {
        super.onDetach()
        sPresenter.bind(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setConversationListItemClickListener { conversation ->
            val id = conversation?.conversationId()
            if (!id.isNullOrEmpty()) {
                sPresenter.onItemClick(id!!)
            }
        }
        super.onActivityCreated(savedInstanceState)
        hideTitleBar()
    }
}