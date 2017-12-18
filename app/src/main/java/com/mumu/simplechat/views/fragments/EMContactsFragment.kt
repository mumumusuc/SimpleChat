package com.mumu.simplechat.views.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hyphenate.easeui.domain.EaseUser
import com.hyphenate.easeui.ui.EaseContactListFragment
import com.mumu.simplechat.R
import com.mumu.simplechat.bean.TitleBar
import com.mumu.simplechat.presenters.IContactsPresenter
import com.mumu.simplechat.presenters.impl.EMContactsPresenter
import com.mumu.simplechat.views.IContactsView

class EMContactsFragment : IContactsView, EaseContactListFragment() {
    companion object {
        private val sPresenter: IContactsPresenter = EMContactsPresenter()
        private val sHandler = Handler(Looper.getMainLooper())
    }

    init {
        setContactListItemClickListener {
            user ->
            sPresenter.onItemClick(user.username)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.base_fragment_layout, container, false) as ViewGroup
        root.addView(view)
        val titleBar = root.findViewById(R.id.base_fragment_toolbar) as TitleBar
        titleBar.setTitle("通讯录")
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
        super.onActivityCreated(savedInstanceState)
        hideTitleBar()
    }

    override fun showContacts(contacts: Map<String,EaseUser>) {
        sHandler.post {
            this.setContactsMap(contacts)
        }
    }
}