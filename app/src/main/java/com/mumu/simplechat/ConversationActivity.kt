package com.mumu.simplechat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mumu.simplechat.views.fragments.EMConversationFragment

class ConversationActivity : AppCompatActivity() {
    private val TAG = ConversationActivity::class.java.simpleName

    private val conversationFragment = EMConversationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null) {
            val bundle = Router.getConversationExtra(intent)
            if (bundle != null) {
                conversationFragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                        .add(android.R.id.content, conversationFragment)
                        .commit()
            }
        }
    }
}