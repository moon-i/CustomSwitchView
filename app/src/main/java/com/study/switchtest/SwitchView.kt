package com.study.switchtest

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

class SwitchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var mView: View = View.inflate(context, R.layout.layout_switch, this)
    private var isOn = true
    private val leftTextView: TextView = mView.findViewById(R.id.leftText)
    private val rightTextView: TextView = mView.findViewById(R.id.rightText)
    private val switchBtn: View = mView.findViewById(R.id.switchBtn)
    private val switchBackground: View = mView.findViewById(R.id.switchBackground)

    private var btnSize: Float = resources.getDimension(R.dimen.switch_btn_width)
    private var switchViewSize: Float = resources.getDimension(R.dimen.switch_view_root_width)

    private val fadeIn by lazy {
        AlphaAnimation(0f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = ON_OFF_DURATION
            fillAfter = true
        }
    }
    private val fadeOut by lazy {
        AlphaAnimation(1f, 0f).apply {
            interpolator = AccelerateInterpolator()
            duration = ON_OFF_DURATION
            fillAfter = true
        }
    }

    private var onStateChangeListener: ((Boolean) -> Unit)? = null

    init {
        rightTextView.visibility = View.GONE
        mView.setOnClickListener {
            setState(!isOn)
        }
    }

    fun initState(isOn: Boolean) {
        this.isOn = isOn

        if (switchViewSize != 0.0f && btnSize != 0.0f) {
            val transitionXSize = if (this.isOn) 0f else -(switchViewSize - (btnSize))
            switchBtn.translationX = transitionXSize
        }

        if (this.isOn) {
            leftTextView.visibility = View.VISIBLE
            rightTextView.visibility = View.GONE
        } else {
            leftTextView.visibility = View.GONE
            rightTextView.visibility = View.VISIBLE
        }
    }

    fun getState(): Boolean = isOn

    fun setState(isOn: Boolean) {
        this.isOn = isOn
        val transitionXSize = if (this.isOn) 0f else -(switchViewSize - (btnSize))
        ObjectAnimator.ofFloat(switchBtn, "translationX", transitionXSize).apply {
            duration = ON_OFF_DURATION
            start()
        }

        if (this.isOn) {
            leftTextView.startAnimation(fadeIn)
            rightTextView.startAnimation(fadeOut)
            leftTextView.visibility = View.VISIBLE
            rightTextView.visibility = View.GONE
        } else {
            leftTextView.startAnimation(fadeOut)
            rightTextView.startAnimation(fadeIn)
            leftTextView.visibility = View.GONE
            rightTextView.visibility = View.VISIBLE
        }
        onStateChangeListener?.invoke(this.isOn)
    }

    fun setBtnText(left: String? = null, right: String? = null) {
        left?.let { leftTextView.text = it }
        right?.let { rightTextView.text = it }
    }

    fun setBackground(@ColorRes colorId: Int) {
        DrawableCompat.setTint(
            DrawableCompat.wrap(switchBackground.background),
            ContextCompat.getColor(context, colorId)
        )
    }

    fun setStateChangeListener(listenerFunction: ((Boolean) -> Unit)) {
        onStateChangeListener = listenerFunction
    }

    companion object {
        const val ON_OFF_DURATION = 150L
    }
}