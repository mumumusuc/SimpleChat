package com.mumu.simplechat.bean

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.mumu.simplechat.R

class TitleBar : RelativeLayout, ITitleBar {

    private val leftDrawable: ImageView by lazy { findViewById(R.id.title_bar_left_icon) as ImageView }
    private val leftText: TextView by lazy { findViewById(R.id.title_bar_left_text) as TextView }
    private val leftAction: View by lazy { findViewById(R.id.title_bar_left_action) }
    private val titleText: TextView by lazy { findViewById(R.id.title_bar_title) as TextView }
    private val rightDrawable: ImageView by lazy { findViewById(R.id.title_bar_right_icon) as ImageView }
    private val rightText: TextView by lazy { findViewById(R.id.title_bar_right_text) as TextView }
    private val rightAction: View by lazy { findViewById(R.id.title_bar_right_action) }

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.title_bar_content, this, true)
        if (attrs != null) {
            val type = context.obtainStyledAttributes(attrs, R.styleable.TitleBarAttrs)

            val defSize = resources.getDimension(R.dimen.title_text_size)
            val titleTextSize = type.getDimension(R.styleable.TitleBarAttrs_titleTextSize, defSize)
            val boundTextSize = type.getDimension(R.styleable.TitleBarAttrs_boundTextSize, defSize)
            val textColor = type.getColor(R.styleable.TitleBarAttrs_textColor, titleText.currentTextColor)
            titleText.textSize = titleTextSize
            titleText.setTextColor(textColor)
            leftText.textSize = boundTextSize
            leftText.setTextColor(textColor)
            rightText.textSize = boundTextSize
            rightText.setTextColor(textColor)
            val leftTextVisibility = type.getBoolean(
                    R.styleable.TitleBarAttrs_showLeftText,
                    leftText.visibility == View.VISIBLE)
            leftText.visibility = if (leftTextVisibility) View.VISIBLE else View.GONE
            val rightTextVisibility = type.getBoolean(
                    R.styleable.TitleBarAttrs_showRightText,
                    rightText.visibility == View.VISIBLE)
            rightText.visibility = if (rightTextVisibility) View.VISIBLE else View.GONE


            val defLeftDrawable = type.getDrawable(R.styleable.TitleBarAttrs_leftDrawable)
            val leftDrawableVisibility = type.getBoolean(
                    R.styleable.TitleBarAttrs_showLeftDrawable,
                    leftDrawable.visibility == View.VISIBLE)
            leftDrawable.setImageDrawable(defLeftDrawable)
            leftDrawable.visibility = if (leftDrawableVisibility) View.VISIBLE else View.GONE
            val defRightDrawable = type.getDrawable(R.styleable.TitleBarAttrs_rightDrawable)
            val rightDrawableVisibility = type.getBoolean(
                    R.styleable.TitleBarAttrs_showRightDrawable,
                    rightDrawable.visibility == View.VISIBLE)
            rightDrawable.setImageDrawable(defRightDrawable)
            rightDrawable.visibility = if (rightDrawableVisibility) View.VISIBLE else View.GONE

            type.recycle()
        }
    }

    override fun setLeftAction(listener: OnClickListener) {
        post {
            leftAction?.setOnClickListener(listener)
        }
    }

    override fun setRightAction(listener: OnClickListener) {
        post {
            rightAction?.setOnClickListener(listener)
        }
    }

    override fun setTitle(title: String) {
        post {
            titleText.text = title
        }
    }

    override fun setLeftIcon(icon: Drawable) {
        post {
            leftDrawable.setImageDrawable(icon)
        }
    }

    override fun setLeftText(str: String) {
        post {
            leftText.text = str
        }
    }

    override fun setRightIcon(icon: Drawable) {
        post {
            rightDrawable.setImageDrawable(icon)
        }
    }

    override fun setRightText(str: String) {
        post {
            rightText.text = str
        }
    }

    override fun showLeftIcon(s: Boolean) {
        post {
            leftDrawable.visibility = if (s) View.VISIBLE else GONE
        }
    }

    override fun showRightIcon(s: Boolean) {
        post {
            rightDrawable.visibility = if (s) View.VISIBLE else GONE
        }
    }

    override fun showLeftText(s: Boolean) {
        post {
            leftText.visibility = if (s) View.VISIBLE else GONE
        }
    }

    override fun showRightText(s: Boolean) {
        post {
            rightText.visibility = if (s) View.VISIBLE else GONE
        }
    }

    override fun getLeftAnchor(): View = leftAction

    override fun getRightAnchor(): View = rightAction
}