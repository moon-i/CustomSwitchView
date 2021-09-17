package com.shinhan.switchtest

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
    private val leftTextView: TextView
    private val rightTextView: TextView
    private val switchBtn: View
    private val switchBackground: View

    private var bgSize: Float = 0f
    private var btnSize: Float = 0f

    private val fadeIn = AlphaAnimation(0f, 1f)
    private val fadeOut = AlphaAnimation(1f, 0f)

    private var onStateChangeListener: ((Boolean) -> Unit)? = null

    init {
        leftTextView = mView.findViewById<TextView>(R.id.leftText)
        rightTextView = mView.findViewById<TextView>(R.id.rightText)
        switchBtn = mView.findViewById(R.id.switchBtn)
        switchBackground = mView.findViewById(R.id.switchBackground)
        switchBackground.post {
            bgSize = switchBackground.measuredWidth.toFloat()
        }
        switchBtn.post {
            btnSize = switchBtn.measuredWidth.toFloat()
        }

        fadeIn.apply {
            interpolator = DecelerateInterpolator() //add this
            duration = ON_OFF_DURATION
            fillAfter = true
        }

        fadeOut.apply {
            interpolator = AccelerateInterpolator() //and this
            fillAfter = true
            duration = ON_OFF_DURATION
        }

        rightTextView.visibility = View.GONE
        mView.setOnClickListener {
            isOn = !isOn

            val transitionXSize = if (isOn) 0f else -(bgSize - btnSize)
            ObjectAnimator.ofFloat(switchBtn, "translationX", transitionXSize).apply {
                duration = ON_OFF_DURATION
                start()
            }

            if (isOn) {
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
            onStateChangeListener?.let { func -> func(isOn) }
        }
    }

    fun getState(): Boolean = isOn

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
        val ON_OFF_DURATION = 300L
    }
}