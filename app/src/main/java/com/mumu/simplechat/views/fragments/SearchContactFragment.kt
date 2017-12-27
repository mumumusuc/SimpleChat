package com.mumu.simplechat.views.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import com.mumu.simplechat.R
import com.mumu.simplechat.bean.TitleBar
import com.mumu.simplechat.eventbus.EventBus
import com.mumu.simplechat.eventbus.events.BackKeyEvent
import com.mumu.simplechat.presenters.ISearchContactsPresenter
import com.mumu.simplechat.presenters.impl.SearchContactsPresenter
import com.mumu.simplechat.views.ISearchContactView

class SearchContactFragment : Fragment(), ISearchContactView {
    companion object {
        private val sPresenter: ISearchContactsPresenter = SearchContactsPresenter()
        private val sHandler = Handler(Looper.getMainLooper())
    }

    private val RESULT_MASK = "添加%s为联系人"
    private val mSearchEditor: EditText by lazy { view?.findViewById(R.id.search_contacts_search) as EditText }
    private val mListView: ListView by lazy { view?.findViewById(R.id.search_contacts_list) as ListView }
    private val mWaiting: ProgressBar by lazy { view?.findViewById(R.id.search_contacts_wait) as ProgressBar }
    private val mAdapter = object : BaseAdapter() {

        override fun getView(index: Int, convertView: View?, parent: ViewGroup?): View {
            val view: TextView
            if (convertView is TextView) {
                view = convertView
            } else {
                view = layoutInflater.inflate(R.layout.search_list_item, parent, false) as TextView
            }
            view.tag = index
            view.setOnClickListener { v ->
                val p = v.tag as Int
                sPresenter.onAdd(sPresenter.getSearchResult()[p])
            }
            view.text = String.format(RESULT_MASK, sPresenter.getSearchResult()[index])
            return view
        }

        override fun getItem(p0: Int): Any = sPresenter.getSearchResult()[p0]

        override fun getItemId(p0: Int): Long = 0L

        override fun getCount(): Int = sPresenter.getSearchResult().size

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        sPresenter.bind(this)
    }

    override fun onDetach() {
        super.onDetach()
        sPresenter.bind(null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.change_contacts_layout, container, false)
        val titleBar = root.findViewById(R.id.base_fragment_toolbar) as TitleBar
        titleBar.setTitle("联系人")
        titleBar.showLeftText(false)
        titleBar.showLeftIcon(true)
        titleBar.showRightText(false)
        titleBar.showRightIcon(false)
        titleBar.setLeftIcon(context.getDrawable(R.drawable.ic_left))
        titleBar.setLeftAction(View.OnClickListener { EventBus.post(BackKeyEvent()) })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mListView.adapter = mAdapter
        mSearchEditor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                sPresenter.onSearch(p0?.toString() ?: "")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    override fun showResults(r: List<String>) {
        sHandler.post {
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun clearResults() {
        sHandler.post {
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun showMessage(msg: String) {
        sHandler.post {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showWaiting() {
        sHandler.post {
            mWaiting?.visibility = VISIBLE
        }
    }

    override fun dismissWaiting() {
        sHandler.post {
            mWaiting?.visibility = GONE
        }
    }
}